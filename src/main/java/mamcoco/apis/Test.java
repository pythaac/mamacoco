package mamcoco.apis;

import mamcoco.dao.*;
import mamcoco.data.*;
import mamcoco.parser.*;
import mamcoco.sync.TistorySync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

import static mamcoco.MamacocoApplication.blog_name;

@RestController
public class Test {
    private final TistoryinfoRepository repo;
    private final TistoryCategoryRepository catRepo;
    private final TistoryPostRepository postRepo;
    private final TistoryInfo info;
    private final TistoryAPI api;
    private final TistorySync sync;
    private final TistoryAPIMapper mapper;
    private final TistoryXMLParser parser;

    @Autowired
    public Test(TistoryinfoRepository repo,TistoryCategoryRepository catRepo, TistoryPostRepository postRepo)
    {
        this.repo = repo;
        this.catRepo = catRepo;
        this.postRepo = postRepo;
        this.info = repo.findTistoryInfoByTistoryBlogName(blog_name);
        this.api = new TistoryAPI(info);
        this.sync = new TistorySync(this.info, this.catRepo, this.postRepo);
        this.mapper = new TistoryAPIMapper(this.info, this.catRepo);
        this.parser = new TistoryXMLParser(this.mapper);
    }

    @GetMapping(value="/dbtest")
    public String dbtest(String s)
    {
        try
        {
            return "test : " + info.getTistoryAccessToken();
        }
        catch(NullPointerException e)
        {
            e.printStackTrace();
            return "test : " + e;
        }
        finally
        {
            System.out.println("test executed");
        }
    }

    @GetMapping(value="/apitest")
    public String apitest()
    {
        return api.postList((long)1);
    }

    @GetMapping(value="/parsingtest")
    public String parsingtest(String s)
    {
        ArrayList<TistoryPostSync> list = this.parser.getPostList(this.apitest());

        return list.get(0).toString();
    }

    @GetMapping(value="/jointest")
    public String jointest(String s)
    {
//        ArrayList<TistoryCategoryAll> list =
//                catRepo.findTistoryCategoriesWithCategory(info.getTistoryBlogName());
//
//        return list.get(0).getCatId().toString();
        ArrayList<TistoryPostAll> list =
                postRepo.findTistoryPostsWithPost(info.getTistoryBlogName());

        return list.get(0).getPostContent();
    }

    @GetMapping(value="/synctest")
    public String synctest(String s)
    {
        this.sync.getCatDB();
        this.sync.getPostDB();
        this.sync.getCatBlog();
        this.sync.getPostBlog();

        return this.sync.test();
    }
}
