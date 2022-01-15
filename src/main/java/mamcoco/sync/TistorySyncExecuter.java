package mamcoco.sync;

import mamcoco.database.dao.*;
import mamcoco.database.repository.CategoryRepository;
import mamcoco.database.repository.PostRepository;
import mamcoco.database.repository.TistoryCategoryRepository;
import mamcoco.database.repository.TistoryPostRepository;
import mamcoco.sync.data.TistorySyncUpdateData;

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

    private void createCat(){
        ArrayList<Category> catList = new ArrayList<>();
        ArrayList<TistoryCategory> tistoryCatList = new ArrayList<>();

        // 1. insert Category first to generate catId
        ArrayList<TistoryCategorySync> catToCreate = this.data.catCreateList;
        for(int i=0; i<this.data.getSizeCatCreateList(); i++){
            TistoryCategorySync cat = catToCreate.get(i);
            catList.add(new Category(cat.getCatName(), cat.getCatParent(), cat.getCatVisible()));
        }
        //ArrayList<Category> catResult = catRepo.saveAll(catList);

//        // 2. insert TistoryCategory with catId
//        for(int i=0; i<catResult.size(); i++){
//            TistoryCategorySync tCat = catToCreate.get(i);
//            Category cat = catResult.stream().filter(tmpCat -> tCat.equals(tmpCat.));
//            tistoryCatList.add(new TistoryCategory(cat.))
//        }

    }
}
