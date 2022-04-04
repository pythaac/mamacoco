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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;

@Component
public class TistorySyncExecuter
{
    private TistorySyncUpdateData data;
    private TistoryInfo info;
    private final CategoryRepository catRepo;
    private final TistoryCategoryRepository tCatRepo;
    private final PostRepository postRepo;
    private final TistoryPostRepository tPostRepo;
    private final TistoryAPI api;
    private final TistoryXMLParser parser;
    private final TistoryAPIMapper mapper;

    public TistorySyncExecuter(CategoryRepository catRepo,
                               PostRepository postRepo,
                               TistoryCategoryRepository tCatRepo,
                               TistoryPostRepository tPostRepo,
                               TistoryAPI api,
                               TistoryXMLParser parser,
                               TistoryAPIMapper mapper)
    {
        this.tCatRepo = tCatRepo;
        this.tPostRepo = tPostRepo;
        this.catRepo = catRepo;
        this.postRepo = postRepo;
        this.api = api;
        this.parser = parser;
        this.mapper = mapper;
    }

    public void setData(TistorySyncUpdateData data) {
        this.data = data;
    }

    public void setInfo(TistoryInfo info) {
        this.info = info;
    }

    @Transactional(rollbackFor = {RuntimeException.class})
    public void execute(){
        System.out.println("createCat : " + this.data.getSizeCatCreateList());
        System.out.println("deleteCat : " + this.data.getSizeCatDeleteList());
        System.out.println("updateCat : " + this.data.getSizeCatUpdateList());
        System.out.println("createPost : " + this.data.getSizePostCreateList());
        System.out.println("deletePost : " + this.data.getSizePostDeleteList());
        System.out.println("updatePost : " + this.data.getSizePostUpdateList());
        this.createCats();
        this.createPosts();
        this.updatePosts();
        this.deletePosts();
        this.updateCats();
        this.deleteCats();
    }

    public void createCats()
    {
        ArrayList<TistoryCategorySync> catToCreate = this.data.catCreateList;

        for(int i=0; i<this.data.getSizeCatCreateList(); i++){
            TistoryCategorySync catSync = catToCreate.get(i);
            this.createCat(catSync);
        }
    }

    public void createCat(TistoryCategorySync catSync)
    {
        // 1. check parent
        this.checkParent(catSync);

        // 2. save Category to obtain catId
        Category cat = new Category(catSync.getCatName(), catSync.getCatParent(), catSync.getCatVisible());
        Category resCat = catRepo.save(cat);

        // 3. save TistoryCategory using catId
        TistoryCategory tCat = new TistoryCategory(catSync.getTistoryCatId(), info.getTistoryBlogName(), resCat.getCatId(), resCat);
        TistoryCategory resTistoryCat = tCatRepo.save(tCat);

        // 4. add TistoryCat-Category map
        this.mapper.addCatMapTable(tCat);
    }

    public void deleteCats()
    {
        ArrayList<TistoryCategorySync> catToDelete = this.data.catDeleteList;

        for(int i=0; i<this.data.getSizeCatDeleteList(); i++){
            TistoryCategorySync catSync = catToDelete.get(i);
            deleteCat(catSync);
        }
    }

    public void deleteCat(TistoryCategorySync catSync)
    {
        // 1. delete TistoryCategory first due to catId
        Long deletedTistoryCatCount = tCatRepo.deleteByTistoryCatId(catSync.getTistoryCatId());

        // 2. delete Category using catId
        Long deletedCatCount = catRepo.deleteByCatId(this.mapper.getMapByTistoryCatId(catSync.getTistoryCatId()));

        // 3. delete TistoryCat-Category map
        this.mapper.deleteCatMapTable(catSync.getTistoryCatId());
    }

    public void updateCats()
    {
        ArrayList<TistoryCategorySync> catToUpdate = this.data.catUpdateList;

        for(int i=0; i<this.data.getSizeCatUpdateList(); i++){
            TistoryCategorySync catSync = catToUpdate.get(i);
            updateCat(catSync);
        }
    }

    public void updateCat(TistoryCategorySync catSync)
    {
        // 1. get catId using TistoryAPIMapper
        Long cat_id = this.mapper.getMapByTistoryCatId(catSync.getTistoryCatId());

        // 2. convert from catParent as tistoryCatId into catId using TistoryAPIMapper
        Long catParent = catSync.getCatParent();
        if (catParent != null)
            catParent = this.mapper.getMapByTistoryCatId(catParent);

        // 2. update Category with catUpdateList
        Category cat = new Category(cat_id, catSync.getCatName(), catParent, catSync.getCatVisible());
        Category resCat = catRepo.save(cat);

        // 3. update TistoryCategory with catUpdateList
        TistoryCategory tCat = new TistoryCategory(catSync.getTistoryCatId(), info.getTistoryBlogName(), cat_id, resCat);
        TistoryCategory resTistoryCat = tCatRepo.save(tCat);
    }

    public void createPosts()
    {
        ArrayList<TistoryPostSync> postToCreate = this.data.postCreateList;

        for(int i=0; i<this.data.getSizePostCreateList(); i++){
            TistoryPostSync postSync = postToCreate.get(i);
            createPost(postSync);
        }
    }

    public void createPost(TistoryPostSync postSync)
    {
        // 1. get data using Tistory API and TistoryXmlParser
        String xml = this.api.post(postSync.getTistoryPostId());
        TistoryPostAll resAPI = this.parser.getPost(xml);

        // 2. convert tistoryCatId into catId
        Long catId = mapper.getMapByTistoryCatId(resAPI.getCatId());

        // 3. create Post to obtain postId
        Post post = new Post(
                catId,
                resAPI.getPostTitle(),
                resAPI.getPostContent(),
                resAPI.getPostTags(),
                resAPI.getPostVisible());
        Post resPost = postRepo.save(post);

        // 4. create TistoryPost
        TistoryPost tPost = new TistoryPost(
                resAPI.getTistoryPostId(),
                this.info.getTistoryBlogName(),
                resAPI.getTistoryPostDate(),
                resPost.getPostId(),
                resPost);
        TistoryPost resTistoryPost = tPostRepo.save(tPost);

        // 5. add TistoryPost-Post table
        this.mapper.addPostMapTable(tPost);
    }

    public void deletePosts()
    {
        ArrayList<TistoryPostSync> postToDelete = this.data.postDeleteList;

        for(int i=0; i<this.data.getSizePostDeleteList(); i++){
            TistoryPostSync postSync = postToDelete.get(i);
            deletePost(postSync);
        }
    }

    public void deletePost(TistoryPostSync postSync)
    {
        // 1. delete TistoryPost first due to postId
        Long deletedTistoryPostCount = tPostRepo.deleteByTistoryPostId(postSync.getTistoryPostId());

        // 2. delete Post using postId
        Long deletedPostCount = postRepo.deleteByPostId(this.mapper.getMapByTistoryPostId(postSync.getTistoryPostId()));

        // 3. delete TistoryPost-Post table
        this.mapper.deletePostMapTable(postSync.getTistoryPostId());
    }

    public void updatePosts()
    {
        ArrayList<TistoryPostSync> postToUpdate = this.data.postUpdateList;

        for(int i=0; i<this.data.getSizePostUpdateList(); i++){
            TistoryPostSync postSync = postToUpdate.get(i);
            updatePost(postSync);
        }
    }

    public void updatePost(TistoryPostSync postSync)
    {
        // 1. get data using Tistory API and TistoryXmlParser
        String xml = this.api.post(postSync.getTistoryPostId());
        TistoryPostAll resAPI = this.parser.getPost(xml);

        // 2. convert tistoryCatId into catId
        Long catId = mapper.getMapByTistoryCatId(resAPI.getCatId());

        // 3. get postId using TistoryAPIMapper
        Long postId = this.mapper.getMapByTistoryPostId(postSync.getTistoryPostId());

        // 4. update Post
        Post post = new Post(
                catId,
                resAPI.getPostTitle(),
                resAPI.getPostContent(),
                resAPI.getPostTags(),
                resAPI.getPostVisible());
        Post resPost = postRepo.save(post);

        // 5. update TistoryPost
        TistoryPost tPost = new TistoryPost(
                resAPI.getTistoryPostId(),
                this.info.getTistoryBlogName(),
                resAPI.getTistoryPostDate(),
                postId,
                resPost);
        TistoryPost resTistoryPost = tPostRepo.save(tPost);
    }

    public void checkParent(TistoryCategorySync catSync){
        // 1. no parent -> parent=null
        if (catSync.getCatParent() == null){
            catSync.setCatParent(null);
        }
        // 2. has parent but no matched catId -> add to updateList
        else if (this.mapper.getMapByTistoryCatId(catSync.getCatParent()) == null){
            this.data.catUpdateList.add(catSync);
        }
        // 3. has parent and matched catId -> parent=catId
        else{
            catSync.setCatParent(this.mapper.getMapByTistoryCatId(catSync.getCatParent()));
        }
    }
}
