package mamcoco.apis;

import mamcoco.dao.TistoryInfo;
import mamcoco.data.TistoryinfoRepository;
import mamcoco.parser.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static mamcoco.MamacocoApplication.blog_id;

@RestController
public class Test {
    private final TistoryinfoRepository repo;
    private final TistoryInfo info;
    private final RestTemplate restTemplate;


    @Autowired
    public Test(TistoryinfoRepository repo, RestTemplateBuilder builder)
    {
        this.repo = repo;
        this.info = repo.findByTistoryBlogName(blog_id);
        this.restTemplate = builder.build();
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
//        final String url = "https://www.tistory.com/apis/blog/info?" +
//            "access_token=" + info.getTistoryAccessToken() +
//            "&output=statistics";

        final String url = "https://www.tistory.com/apis/post/read?" +
                "access_token=" + info.getTistoryAccessToken() +
                "&blogName=" + info.getTistoryBlogName() +
                "&postId=187";

        return this.restTemplate.getForObject(url, String.class);
    }

    @GetMapping(value="/parsingtest")
    public String parsingtest(String s)
    {
        XMLparser parser = new XMLparser(this.apitest());
        parser.build();

        return parser.into("item").get("content");
    }
}
