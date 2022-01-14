package mamcoco.sync;

import mamcoco.apis.TistoryAPI;
import mamcoco.apis.TistoryAPIMapper;
import mamcoco.database.dao.TistoryCategorySync;
import mamcoco.database.dao.TistoryInfo;
import mamcoco.database.dao.TistoryPostSync;
import mamcoco.database.repository.TistoryCategoryRepository;
import mamcoco.database.repository.TistoryPostRepository;
import mamcoco.parser.TistoryXMLParser;
import mamcoco.sync.data.TistorySyncData;

import java.util.ArrayList;

public class TistorySyncRetriever
{
    private TistorySyncData data;

    private final TistoryInfo info;
    private final TistoryCategoryRepository catRepo;
    private final TistoryPostRepository postRepo;

    private final TistoryAPI api;
    private final TistoryAPIMapper mapper;
    private final TistoryXMLParser xmlParser;

    public TistorySyncRetriever(TistoryInfo info, TistoryCategoryRepository catRepo, TistoryPostRepository postRepo){
        this.info = info;
        this.catRepo = catRepo;
        this.postRepo = postRepo;
        this.api = new TistoryAPI(info);
        this.mapper = new TistoryAPIMapper(info, catRepo);
        this.xmlParser = new TistoryXMLParser(this.mapper);

        this.data = new TistorySyncData();
    }

    private void getCatDB(){
        this.data.catDB = catRepo.findTistoryCategoriesWithCategoryForSync(this.info.getTistoryBlogName());
    }

    private void getPostDB(){
        this.data.postDB = postRepo.findTistoryPostsWithPostForSync(this.info.getTistoryBlogName());
    }

    private void getCatBlog(){
        String xml = this.api.categoryList();
        this.data.catBlog = this.xmlParser.getCategoryListSync(xml);
    }

    private void getPostBlog(){
        // 1. init ArrayList
        this.data.postBlog.clear();

        // 2. get the number of all pages
        String xmlPage = this.api.postList((long)1);
        Long numPages = this.xmlParser.getNumPages(xmlPage);

        // 3. foreach page
        for(long page = numPages; page>0; page--)
        {
            // 4. add page list
            String xml = this.api.postList(page);
            this.data.postBlog.addAll(this.xmlParser.getPostListSync(xml));
        }
    }

    private void sortAll(){
        this.data.catDB.sort((a, b) -> a.getTistoryCatId().compareTo(b.getTistoryCatId()));
        this.data.postDB.sort((a, b) -> a.getTistoryPostId().compareTo(b.getTistoryPostId()));
        this.data.catBlog.sort((a, b) -> a.getTistoryCatId().compareTo(b.getTistoryCatId()));
        this.data.postBlog.sort((a, b) -> a.getTistoryPostId().compareTo(b.getTistoryPostId()));
    }

    public void retrieveAll(){
        this.getPostBlog();
        this.getPostDB();
        this.getCatBlog();
        this.getCatDB();
        this.sortAll();
    }

    public TistorySyncData getData(){
        return this.data;
    }
}
