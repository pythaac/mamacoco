package mamcoco.apis;

import mamcoco.dao.TistoryInfo;
import mamcoco.data.TistoryinfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import static mamcoco.MamacocoApplication.client_id;

@RestController
public class Test {
    private final TistoryinfoRepository repo;
    private final TistoryInfo info;
    private final RestTemplate restTemplate;


    @Autowired
    public Test(TistoryinfoRepository repo, RestTemplateBuilder builder)
    {
        this.repo = repo;
        this.info = repo.findByTistoryBlogId(client_id);
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
    public String apitest(String s)
    {
        final String url = "https://www.tistory.com/apis/blog/info?" +
            "access_token=" + info.getTistoryAccessToken() +
            "&output=statistics";

        return this.restTemplate.getForObject(url, String.class);
    }

    @GetMapping(value="/parsingtest")
    public String parsingtest(String s)
    {
        String result = "";
        try {
            String xml = this.apitest(s);
            System.out.println("after apitest : \n" + xml);

            DocumentBuilderFactory factory = DocumentBuilderFactory
                    .newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputStream is = new ByteArrayInputStream(xml.getBytes());
            Document doc = builder.parse(is);

            result = doc.getElementsByTagName("tistory").item(0)
                    .getChildNodes().item(0).getTextContent();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return "test :" + e;
        } catch (IOException e) {
            e.printStackTrace();
            return "test :" + e;
        } catch (SAXException e) {
            e.printStackTrace();
            return "test :" + e;
        }

        return "test : " + result;
    }
}

























