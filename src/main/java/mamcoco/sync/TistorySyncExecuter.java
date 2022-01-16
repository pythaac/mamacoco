package mamcoco.sync;

import mamcoco.database.dao.*;
import mamcoco.database.repository.CategoryRepository;
import mamcoco.database.repository.PostRepository;
import mamcoco.database.repository.TistoryCategoryRepository;
import mamcoco.database.repository.TistoryPostRepository;
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

    public TistorySyncExecuter(TistorySyncUpdateData data,
                               TistoryInfo info,
                               CategoryRepository catRepo,
                               PostRepository postRepo,
                               TistoryCategoryRepository tCatRepo,
                               TistoryPostRepository tPostRepo)
    {
        this.data = data;
        this.info = info;
        this.tCatRepo = tCatRepo;
        this.tPostRepo = tPostRepo;
        this.catRepo = catRepo;
        this.postRepo = postRepo;
    }

    @Transactional
    void createCat(){
        ArrayList<TistoryCategorySync> catToCreate = this.data.catCreateList;

        for(int i=0; i<this.data.getSizeCatCreateList(); i++){
            TistoryCategorySync catSync = catToCreate.get(i);

            // 1. save Category first to obtain catId
            Category cat = (new Category(catSync.getCatName(), catSync.getCatParent(), catSync.getCatVisible()));
            Category resCat = catRepo.save(cat);

            // 2. save TistoryCategory using catId
            TistoryCategory tCat = new TistoryCategory(catSync.getTistoryCatId(), catSync.getCatName(), resCat.getCatId());
            TistoryCategory resTistoryCat = tCatRepo.save(tCat);
        }
    }

    @Transactional
    void deleteCat(){
        ArrayList<TistoryCategorySync> catToDelete = this.data.catDeleteList;

        for(int i=0; i<this.data.getSizeCatDeleteList(); i++){
            TistoryCategorySync catSync = catToDelete.get(i);

            // 1. delete TistoryCategory first due to catId
            TistoryCategory resTistoryCat = tCatRepo.deleteByTistoryCatId(catSync.getTistoryCatId());

            // 2. delete Category using catId
            Category resCat = catRepo.deleteByCatId(resTistoryCat.getCatId());
        }
    }

//    @Transactional
//    void updateCat(){
//        ArrayList<TistoryCategorySync> catToUpdate = this.data.catUpdateList;
//
//        for(int i=0; i<this.data.getSizeCatUpdateList(); i++){
//            TistoryCategorySync catSync = catToUpdate.get(i);
//
//            // 1. update TistoryCategory first due to catId
//
//        }
//    }
}
