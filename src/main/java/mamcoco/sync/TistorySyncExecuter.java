package mamcoco.sync;

import mamcoco.apis.TistoryAPI;
import mamcoco.apis.TistoryAPIMapper;
import mamcoco.database.dao.*;
import mamcoco.database.repository.CategoryRepository;
import mamcoco.database.repository.PostRepository;
import mamcoco.database.repository.TistoryCategoryRepository;
import mamcoco.database.repository.TistoryPostRepository;
import mamcoco.sync.data.TistorySyncUpdateData;
import org.springframework.dao.DataAccessException;
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
    private final TistoryAPIMapper mapper;

    public TistorySyncExecuter(TistorySyncUpdateData data,
                               TistoryInfo info,
                               CategoryRepository catRepo,
                               PostRepository postRepo,
                               TistoryCategoryRepository tCatRepo,
                               TistoryPostRepository tPostRepo,
                               TistoryAPI api,
                               TistoryAPIMapper mapper)
    {
        this.data = data;
        this.info = info;
        this.tCatRepo = tCatRepo;
        this.tPostRepo = tPostRepo;
        this.catRepo = catRepo;
        this.postRepo = postRepo;
        this.api = api;
        this.mapper = mapper;
    }

    @Transactional
    void createCat() throws DataAccessException
    {
        ArrayList<TistoryCategorySync> catToCreate = this.data.catCreateList;

        for(int i=0; i<this.data.getSizeCatCreateList(); i++){
            TistoryCategorySync catSync = catToCreate.get(i);

            // 1. save Category first to obtain catId
            try
            {
                Category cat = new Category(catSync.getCatName(), catSync.getCatParent(), catSync.getCatVisible());
                Category resCat = catRepo.save(cat);
            } catch(Exception e){

            }

            // 2. save TistoryCategory using catId
            TistoryCategory tCat = new TistoryCategory(catSync.getTistoryCatId(), info.getTistoryBlogName(), resCat.getCatId());
            TistoryCategory resTistoryCat = tCatRepo.save(tCat);

            // 3. update TistoryCat-Category map
            this.mapper.
        }
    }

    @Transactional
    void deleteCat() throws DataAccessException
    {
        ArrayList<TistoryCategorySync> catToDelete = this.data.catDeleteList;

        for(int i=0; i<this.data.getSizeCatDeleteList(); i++){
            TistoryCategorySync catSync = catToDelete.get(i);

            // 1. delete TistoryCategory first due to catId
            TistoryCategory resTistoryCat = tCatRepo.deleteByTistoryCatId(catSync.getTistoryCatId());

            // 2. delete Category using catId
            Category resCat = catRepo.deleteByCatId(resTistoryCat.getCatId());
        }
    }

    @Transactional
    void updateCat() throws DataAccessException
    {
        ArrayList<TistoryCategorySync> catToUpdate = this.data.catUpdateList;

        for(int i=0; i<this.data.getSizeCatUpdateList(); i++){
            TistoryCategorySync catSync = catToUpdate.get(i);

            // 1. get catId using TistoryAPIMapper
            Long cat_id = this.mapper.mapTistoryCatId(catSync.getTistoryCatId());

            // 2. update TistoryCategory with catUpdateList
            TistoryCategory tCat = new TistoryCategory(catSync.getTistoryCatId(), info.getTistoryBlogName(), cat_id);
            TistoryCategory resTistoryCat = tCatRepo.save(tCat);

            // 3. update Category with catUpdateList
            Category cat = new Category(cat_id, catSync.getCatName(), catSync.getCatParent(), catSync.getCatVisible());
            Category resCat = catRepo.save(cat);
        }
    }

    @Transactional
    void createPost() throws DataAccessException
    {
        ArrayList<TistoryPostSync> postToCreate = this.data.postCreateList;

        // 1. get data using Tistory API


    }
}
