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
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static mamcoco.MamacocoApplication.blog_name;

@Controller
@EnableScheduling
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

    @GetMapping(value="/")
    public String home(){
        return "home";
    }

    @PostMapping(value="/")
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

        String result = "[Done]<br/>" +
                "Created Category : " + numCatCreate + "<br/>" +
                "Updated Category : " + numCatUpdate + "<br/>" +
                "Deleted Category : " + numCatDelete + "<br/>" +
                "Created Post : " + numPostCreate + "<br/>" +
                "Updated Post : " + numPostUpdate + "<br/>" +
                "Deleted Post : " + numPostDelete + "<br/>";
        this.log(result);

        return result;
    }

    @Scheduled(cron = "0 0 6 * * *")
    public void autoSync(){
        this.sync();
    }

    private void log(String result){
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        String file = "/home/ubuntu/mamacoco/" + Timestamp.valueOf(now).toString();
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            writer.write(result);
        } catch(IOException | RuntimeException e){
            e.printStackTrace();
        }
    }
}
