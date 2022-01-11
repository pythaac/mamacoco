package mamcoco.apis;

import mamcoco.dao.TistoryCategoryAll;
import mamcoco.dao.TistoryInfo;
import mamcoco.dao.TistoryPostAll;
import mamcoco.data.TistoryCategoryRepository;
import mamcoco.data.TistoryPostRepository;
import mamcoco.data.TistoryinfoRepository;
import mamcoco.parser.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
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

    @Autowired
    public Test(TistoryinfoRepository repo,TistoryCategoryRepository catRepo, TistoryPostRepository postRepo)
    {
        this.repo = repo;
        this.catRepo = catRepo;
        this.postRepo = postRepo;
        this.info = repo.findTistoryInfoByTistoryBlogName(blog_name);
        this.api = new TistoryAPI(info);
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
        return api.post((long) 187);
    }

    @GetMapping(value="/parsingtest")
    public String parsingtest(String s)
    {
        XMLparser parser = new XMLparser(this.apitest());

        return parser.into("item").get("content");
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
}
