package mamcoco.sync;

import mamcoco.apis.TistoryAPI;
import mamcoco.apis.TistoryAPIMapper;
import mamcoco.dao.*;
import mamcoco.data.*;
import mamcoco.parser.TistoryXMLParser;
import mamcoco.parser.XMLParser;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class TistorySync {
    private ArrayList<TistoryCategorySync> catDB;
    private ArrayList<TistoryCategorySync> catBlog;
    private ArrayList<TistoryPostSync> postDB;
    private ArrayList<TistoryPostSync> postBlog;

    private final TistoryInfo info;
    private final TistoryCategoryRepository catRepo;
    private final TistoryPostRepository postRepo;

    private final TistoryAPI api;
    private final TistoryAPIMapper mapper;
    private final TistoryXMLParser xmlParser;

    public TistorySync(TistoryInfo info, TistoryCategoryRepository catRepo, TistoryPostRepository postRepo){
        this.info = info;
        this.catRepo = catRepo;
        this.postRepo = postRepo;
        this.api = new TistoryAPI(info);
        this.mapper = new TistoryAPIMapper(info, catRepo);
        this.xmlParser = new TistoryXMLParser(this.mapper);
    }

    public void getCatDB(){
        this.catDB = catRepo.findTistoryCategoriesWithCategoryForSync(this.info.getTistoryBlogName());
    }

    public void getPostDB(){
        this.postDB = postRepo.findTistoryPostsWithPostForSync(this.info.getTistoryBlogName());
    }

    public void getCatBlog(){
        String xml = this.api.categoryList();
        this.catBlog = this.xmlParser.getCategoryList(xml);
    }

    public void getPostBlog(){
        // 1. init ArrayList
        this.postBlog = new ArrayList<>();

        // 2. get the number of all pages
        String xmlPage = this.api.postList((long)1);
        Long numPages = this.xmlParser.getNumPages(xmlPage);

        // 3. foreach page
        for(long page = numPages; page>0; page--)
        {
            String xml = this.api.postList(page);
            this.postBlog.addAll(this.xmlParser.getPostList(xml));
        }
    }

    // for test
    public String test(){
        return "catDB : " + this.catDB.size() +
                "\ncatBlog : " + this.catBlog.size() +
                "\npostDB : " + this.postDB.size() +
                "\npostBlog : " + this.postBlog.size();
    }
}
