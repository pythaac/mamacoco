package mamcoco.sync;

import mamcoco.apis.TistoryAPI;
import mamcoco.apis.TistoryAPIMapper;
import mamcoco.database.data.*;
import mamcoco.database.repository.CategoryRepository;
import mamcoco.database.repository.PostRepository;
import mamcoco.database.repository.TistoryCategoryRepository;
import mamcoco.database.repository.TistoryPostRepository;
import mamcoco.parser.TistoryXMLParser;
import mamcoco.sync.data.TistorySyncUpdateData;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

public class TistorySyncExecuter
{
    private final TistorySyncUpdateData data;
    private final TistoryInfo info;
    private final CategoryRepository catRepo;
    private final TistoryCategoryRepository tCatRepo;
    private final PostRepository postRepo;
    private final TistoryPostRepository tPostRepo;
    private final TistoryAPI api;
    private final TistoryXMLParser parser;
    private final TistoryAPIMapper mapper;

    public TistorySyncExecuter(TistorySyncUpdateData data,
                               TistoryInfo info,
                               CategoryRepository catRepo,
                               PostRepository postRepo,
                               TistoryCategoryRepository tCatRepo,
                               TistoryPostRepository tPostRepo,
                               TistoryAPI api,
                               TistoryXMLParser parser,
                               TistoryAPIMapper mapper)
    {
        this.data = data;
        this.info = info;
        this.tCatRepo = tCatRepo;
        this.tPostRepo = tPostRepo;
        this.catRepo = catRepo;
        this.postRepo = postRepo;
        this.api = api;
        this.parser = parser;
        this.mapper = mapper;
    }

    public void execute(){
        this.createCat();
//        this.createPost();
//        this.updatePost();
//        this.deletePost();
        this.updateCat();
        this.deleteCat();
    }

    @Transactional
    void createCat()
    {
        ArrayList<TistoryCategorySync> catToCreate = this.data.catCreateList;

        for(int i=0; i<this.data.getSizeCatCreateList(); i++){
            TistoryCategorySync catSync = catToCreate.get(i);

            // 1. save Category first to obtain catId
            Category cat = new Category(catSync.getCatName(), catSync.getCatParent(), catSync.getCatVisible());
            Category resCat = catRepo.save(cat);

            // 2. save TistoryCategory using catId
            TistoryCategory tCat = new TistoryCategory(catSync.getTistoryCatId(), info.getTistoryBlogName(), resCat.getCatId(), resCat);
            System.out.println("catId : " + tCat.getCatId());
            TistoryCategory resTistoryCat = tCatRepo.save(tCat);

            // 3. add TistoryCat-Category map
            this.mapper.addCatMapTable(tCat);
        }
    }

    @Transactional
    void deleteCat()
    {
        ArrayList<TistoryCategorySync> catToDelete = this.data.catDeleteList;

        for(int i=0; i<this.data.getSizeCatDeleteList(); i++){
            TistoryCategorySync catSync = catToDelete.get(i);

            // 1. delete TistoryCategory first due to catId
            TistoryCategory resTistoryCat = tCatRepo.deleteByTistoryCatId(catSync.getTistoryCatId());

            // 2. delete Category using catId
            Category resCat = catRepo.deleteByCatId(resTistoryCat.getCatId());

            // 3. delete TistoryCat-Category map
            this.mapper.deleteCatMapTable(resTistoryCat.getTistoryCatId());
        }
    }

    @Transactional
    void updateCat()
    {
        ArrayList<TistoryCategorySync> catToUpdate = this.data.catUpdateList;

        for(int i=0; i<this.data.getSizeCatUpdateList(); i++){
            TistoryCategorySync catSync = catToUpdate.get(i);

            // 1. get catId using TistoryAPIMapper
            Long cat_id = this.mapper.getMapByTistoryCatId(catSync.getTistoryCatId());

            // 2. convert from catParent as tistoryCatId into catId using TistoryAPIMapper
            Long catParent = this.mapper.getMapByTistoryCatId(catSync.getCatParent());

            // 2. update Category with catUpdateList
            Category cat = new Category(cat_id, catSync.getCatName(), catParent, catSync.getCatVisible());
            Category resCat = catRepo.save(cat);

            // 3. update TistoryCategory with catUpdateList
            TistoryCategory tCat = new TistoryCategory(catSync.getTistoryCatId(), info.getTistoryBlogName(), cat_id, resCat);
            TistoryCategory resTistoryCat = tCatRepo.save(tCat);


        }
    }

    @Transactional
    void createPost()
    {
        ArrayList<TistoryPostSync> postToCreate = this.data.postCreateList;

        for(int i=0; i<this.data.getSizePostCreateList(); i++){
            TistoryPostSync postSync = postToCreate.get(i);

            // 1. get data using Tistory API and TistoryXmlParser
            String xml = this.api.post(postSync.getTistoryPostId());
            TistoryPostAll resAPI = this.parser.getPost(xml);

            // 2. create Post to obtain postId
            Post post = new Post(
                    resAPI.getCatId(),
                    resAPI.getPostTitle(),
                    resAPI.getPostContent(),
                    resAPI.getPostTags(),
                    resAPI.getPostVisible());
            Post resPost = postRepo.save(post);

            // 3. create TistoryPost
            TistoryPost tPost = new TistoryPost(
                    resAPI.getTistoryPostId(),
                    this.info.getTistoryBlogName(),
                    resAPI.getTistoryPostDate(),
                    resPost.getPostId());
            TistoryPost resTistoryPost = tPostRepo.save(tPost);

            // 4. add TistoryPost-Post table
            this.mapper.addPostMapTable(tPost);
        }
    }

    @Transactional
    void deletePost()
    {
        ArrayList<TistoryPostSync> postToDelete = this.data.postDeleteList;

        for(int i=0; i<this.data.getSizePostDeleteList(); i++){
            TistoryPostSync postSync = postToDelete.get(i);

            // 1. delete TistoryPost first due to postId
            TistoryPost resTistoryPost = tPostRepo.deleteByTistoryPostId(postSync.getTistoryPostId());

            // 2. delete Post using postId
            Post resPost = postRepo.deleteByPostId(resTistoryPost.getPostId());

            // 3. delete TistoryPost-Post table
            this.mapper.deletePostMapTable(resTistoryPost.getPostId());
        }
    }

    @Transactional
    void updatePost()
    {
        ArrayList<TistoryPostSync> postToUpdate = this.data.postUpdateList;

        for(int i=0; i<this.data.getSizePostUpdateList(); i++){
            TistoryPostSync postSync = postToUpdate.get(i);

            // 1. get data using Tistory API and TistoryXmlParser
            String xml = this.api.post(postSync.getTistoryPostId());
            TistoryPostAll resAPI = this.parser.getPost(xml);

            // 2. get postId using TistoryAPIMapper
            Long postId = this.mapper.getMapByTistoryPostId(postSync.getTistoryPostId());

            // 2. update TistoryPost
            TistoryPost tPost = new TistoryPost(
                    resAPI.getTistoryPostId(),
                    this.info.getTistoryBlogName(),
                    resAPI.getTistoryPostDate(),
                    postId);
            TistoryPost resTistoryPost = tPostRepo.save(tPost);

            // 2. update Post
            Post post = new Post(
                    resAPI.getCatId(),
                    resAPI.getPostTitle(),
                    resAPI.getPostContent(),
                    resAPI.getPostTags(),
                    resAPI.getPostVisible());
            Post resPost = postRepo.save(post);
        }
    }
}
