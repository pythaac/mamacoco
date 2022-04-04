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

    private final TistorySyncExecuter executer;

    @Autowired
    public SyncController(
            TistoryinfoRepository repo,
            TistoryCategoryRepository tCatRepo,
            TistoryPostRepository tPostRepo,
            CategoryRepository catRepo,
            PostRepository postRepo,
            TistoryAPI api,
            TistoryAPIMapper mapper,
            TistoryXMLParser parser,
            TistorySyncExecuter executer)
    {
        this.tCatRepo = tCatRepo;
        this.tPostRepo = tPostRepo;
        this.postRepo = postRepo;
        this.catRepo = catRepo;
        this.info = repo.findTistoryInfoByTistoryBlogName(blog_name);

        this.api = api;
        api.setTistory_info(info);
        this.mapper = mapper;
        mapper.setInfo(info);
        mapper.init();
        this.parser = parser;

        this.executer = executer;
        executer.setInfo(info);
    }

    @GetMapping
    public String home(){
        return "home";
    }

    @PostMapping
    @ResponseBody
    public String sync()
    {
        TistorySyncRetriever retriever = new TistorySyncRetriever(
                this.info,
                this.tCatRepo,
                this.tPostRepo,
                this.api,
                this.parser);
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

        executer.setData(data);
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
