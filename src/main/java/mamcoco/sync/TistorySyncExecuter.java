package mamcoco.sync;

import mamcoco.database.dao.TistoryInfo;
import mamcoco.database.repository.TistoryCategoryRepository;
import mamcoco.database.repository.TistoryPostRepository;
import mamcoco.sync.data.TistorySyncUpdateData;

public class TistorySyncExecuter
{
    private final TistorySyncUpdateData data;
    private final TistoryInfo info;
    private final TistoryCategoryRepository catRepo;
    private final TistoryPostRepository postRepo;

    public TistorySyncExecuter(TistorySyncUpdateData data,
                               TistoryInfo info,
                               TistoryCategoryRepository catRepo,
                               TistoryPostRepository postRepo)
    {
        this.data = data;
        this.info = info;
        this.catRepo = catRepo;
        this.postRepo = postRepo;
    }

    private void createCat(){

    }
}
