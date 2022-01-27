package mamcoco.apis;

import mamcoco.database.data.TistoryInfo;
import mamcoco.database.data.TistoryPostAll;
import mamcoco.database.data.TistoryPostSync;
import mamcoco.database.repository.*;
import mamcoco.parser.*;
import mamcoco.sync.TistorySyncComparator;
import mamcoco.sync.TistorySyncExecuter;
import mamcoco.sync.TistorySyncRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

import static mamcoco.MamacocoApplication.blog_name;

@RestController
public class Test {
//    private final TistoryCategoryRepository tCatRepo;
//    private final TistoryPostRepository tPostRepo;
//    private final CategoryRepository catRepo;
//    private final PostRepository postRepo;
//    private final TistoryInfo info;
//
//    private final TistoryAPI api;
//    private final TistoryAPIMapper mapper;
//    private final TistoryXMLParser parser;
//
//    @Autowired
//    public Test(
//            TistoryinfoRepository repo,
//            TistoryCategoryRepository tCatRepo,
//            TistoryPostRepository tPostRepo,
//            CategoryRepository catRepo,
//            PostRepository postRepo)
//    {
//        this.tCatRepo = tCatRepo;
//        this.tPostRepo = tPostRepo;
//        this.postRepo = postRepo;
//        this.catRepo = catRepo;
//        this.info = repo.findTistoryInfoByTistoryBlogName(blog_name);
//
//        this.api = new TistoryAPI(info);
//        this.mapper = new TistoryAPIMapper(this.info, this.tCatRepo, this.tPostRepo);
//        this.parser = new TistoryXMLParser(this.mapper);
//    }
//
//    @GetMapping(value="/dbtest")
//    public String dbtest(String s)
//    {
//        try
//        {
//            return "test : " + info.getTistoryAccessToken();
//        }
//        catch(NullPointerException e)
//        {
//            e.printStackTrace();
//            return "test : " + e;
//        }
//        finally
//        {
//            System.out.println("test executed");
//        }
//    }
//
//    @GetMapping(value="/apitest")
//    public String apitest()
//    {
//        return api.postList((long)1);
//    }
//
//    @GetMapping(value="/parsingtest")
//    public String parsingtest(String s)
//    {
//        ArrayList<TistoryPostSync> list = this.parser.getPostListSync(this.apitest());
//
//        return list.get(0).toString();
//    }
//
//    @GetMapping(value="/jointest")
//    public String jointest(String s)
//    {
////        ArrayList<TistoryCategoryAll> list =
////                catRepo.findTistoryCategoriesWithCategory(info.getTistoryBlogName());
////
////        return list.get(0).getCatId().toString();
//        ArrayList<TistoryPostAll> list =
//                tPostRepo.findTistoryPostsWithPost(info.getTistoryBlogName());
//
//        return list.get(0).getPostContent();
//    }
//
//    @GetMapping(value="/retrievertest")
//    public String retrievertest(String s)
//    {
//        TistorySyncRetriever retriever = new TistorySyncRetriever(this.info, this.tCatRepo, this.tPostRepo);
//        retriever.retrieveAll();
//
//        TistorySyncComparator comparator = new TistorySyncComparator(retriever.getData(), this.mapper);
//        comparator.checkCategory();
//        comparator.checkPost();
//        return retriever.getData().printIds() + "\n" + comparator.getResult().printIds();
//    }
//
//    @GetMapping(value="/synctest")
//    public String synctest(String s)
//    {
//        TistorySyncRetriever retriever = new TistorySyncRetriever(this.info, this.tCatRepo, this.tPostRepo);
//        retriever.retrieveAll();
//
//        TistorySyncComparator comparator = new TistorySyncComparator(retriever.getData(), this.mapper);
//        comparator.checkCategory();
//        comparator.checkPost();
//
//        TistorySyncExecuter executer = new TistorySyncExecuter(
//                comparator.getResult(),
//                this.info,
//                this.catRepo,
//                this.postRepo,
//                this.tCatRepo,
//                this.tPostRepo,
//                this.api,
//                this.parser,
//                this.mapper);
//        executer.execute();
//
//        return "done";
//    }
}
