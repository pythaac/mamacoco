package mamcoco.service;

import mamcoco.apis.TistoryAPI;
import mamcoco.apis.TistoryAPIMapper;
import mamcoco.database.data.TistoryInfo;
import mamcoco.database.repository.*;
import mamcoco.parser.TistoryXMLParser;
import mamcoco.sync.TistorySyncComparator;
import mamcoco.sync.TistorySyncExecuter;
import mamcoco.sync.TistorySyncRetriever;
import mamcoco.sync.data.TistorySyncUpdateData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static mamcoco.MamacocoApplication.blog_name;

@Controller
@RequestMapping(value="/")
public class SyncController
{
    private final TistoryCategoryRepository tCatRepo;
    private final TistoryPostRepository tPostRepo;
    private final CategoryRepository catRepo;
    private final PostRepository postRepo;
    private final TistoryInfo info;

    private final TistoryAPI api;
    private final TistoryAPIMapper mapper;
    private final TistoryXMLParser parser;

    @Autowired
    public SyncController(
            TistoryinfoRepository repo,
            TistoryCategoryRepository tCatRepo,
            TistoryPostRepository tPostRepo,
            CategoryRepository catRepo,
            PostRepository postRepo)
    {
        this.tCatRepo = tCatRepo;
        this.tPostRepo = tPostRepo;
        this.postRepo = postRepo;
        this.catRepo = catRepo;
        this.info = repo.findTistoryInfoByTistoryBlogName(blog_name);

        this.api = new TistoryAPI(info);
        this.mapper = new TistoryAPIMapper(this.info, this.tCatRepo, this.tPostRepo);
        this.parser = new TistoryXMLParser(this.mapper);
    }

    @GetMapping
    public String home(){
        return "home";
    }

    @PostMapping
    @ResponseBody
    public String sync()
    {
        TistorySyncRetriever retriever = new TistorySyncRetriever(this.info, this.tCatRepo, this.tPostRepo);
        retriever.retrieveAll();

        TistorySyncComparator comparator = new TistorySyncComparator(retriever.getData(), this.mapper);
        comparator.checkCategory();
        comparator.checkPost();

        TistorySyncUpdateData data = comparator.getResult();
        Integer numCatCreate = data.getSizeCatCreateList();
        Integer numCatUpdate = data.getSizeCatUpdateList();
        Integer numCatDelete = data.getSizeCatDeleteList();
        Integer numPostCreate = data.getSizePostCreateList();
        Integer numPostUpdate = data.getSizePostUpdateList();
        Integer numPostDelete = data.getSizePostDeleteList();

        TistorySyncExecuter executer = new TistorySyncExecuter(
                data,
                this.info,
                this.catRepo,
                this.postRepo,
                this.tCatRepo,
                this.tPostRepo,
                this.api,
                this.parser,
                this.mapper);
        executer.execute();

        return "[Done]<br/>" +
                "Created Category : " + numCatCreate + "<br/>" +
                "Updated Category : " + numCatUpdate + "<br/>" +
                "Deleted Category : " + numCatDelete + "<br/>" +
                "Created Post : " + numPostCreate + "<br/>" +
                "Updated Post : " + numPostUpdate + "<br/>" +
                "Deleted Post : " + numPostDelete + "<br/>";
    }
}
