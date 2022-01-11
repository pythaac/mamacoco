package mamcoco.sync;

import mamcoco.apis.TistoryAPI;
import mamcoco.dao.*;
import mamcoco.data.*;
import mamcoco.parser.XMLparser;

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

    public TistorySync(TistoryInfo info, TistoryCategoryRepository catRepo, TistoryPostRepository postRepo){
        this.info = info;
        this.catRepo = catRepo;
        this.postRepo = postRepo;
        this.api = new TistoryAPI(info);
    }

    public void getCatDB(){
        this.catDB = catRepo.findTistoryCategoriesWithCategoryForSync(this.info.getTistoryBlogName());
    }

    public void getPostDB(){
        this.postDB = postRepo.findTistoryPostsWithPostForSync(this.info.getTistoryBlogName());
    }

    public void getCatBlog(){

    }
}
