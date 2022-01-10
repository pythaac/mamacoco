package mamcoco.apis;

import mamcoco.dao.TistoryCategory;
import mamcoco.dao.TistoryCategoryAll;
import mamcoco.dao.TistoryInfo;
import mamcoco.parser.XMLparser;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class TistoryAPI {
    private final String tistory_domain;
    private final TistoryInfo tistory_info;
    private final RestTemplate restTemplate;

    public TistoryAPI(TistoryInfo tistory_info){
        this.tistory_domain = "https://www.tistory.com/apis/";

        RestTemplateBuilder builder = new RestTemplateBuilder();
        this.restTemplate = builder.build();

        this.tistory_info = tistory_info;
    }

    public String info(){
        String url = this.tistory_domain + "blog/info?" +
                "access_token=" + this.tistory_info.getTistoryAccessToken();

        return this.restTemplate.getForObject(url, String.class);
    }

    public ArrayList<TistoryCategoryAll> categoryList(){
        String url = this.tistory_domain + "category/list?" +
                "access_token=" + this.tistory_info.getTistoryAccessToken() +
                "&blogName=" + this.tistory_info.getTistoryBlogName();

        String xml = this.restTemplate.getForObject(url, String.class);
        XMLparser parser = new XMLparser(xml);

        ArrayList<TistoryCategoryAll> result = new ArrayList<>();
        NodeList list = parser.into("item").into("categories").getList();
        for(int i=0; i<list.getLength(); i++)
        {
            Element category = (Element)list.item(i);
            TistoryCategoryAll cat = new TistoryCategoryAll();

            Long id = Long.parseLong(parser.getValue(category, "id"));
            String name = parser.getValue(category, "name");
            Long parent = Long.parseLong(parser.getValue(category, "parent"));
            Integer visible = parser.getValue(category, "entries").equals("0")? 0 : 1;

            cat.setTistoryCatId(id);
            cat.setCatName(name);
            cat.setCatParent(parent);
            cat.setCatVisible(visible);

            result.add(cat);
        }

        return result;
    }

    public String postList(Integer page){
        String url = this.tistory_domain + "post/list?" +
                "access_token=" + this.tistory_info.getTistoryAccessToken() +
                "&blogName=" + this.tistory_info.getTistoryBlogName() +
                "&page=" + page.toString();

        return this.restTemplate.getForObject(url, String.class);
    }

    public String post(Long postId){
        String url = this.tistory_domain + "post/read?" +
                "access_token=" + this.tistory_info.getTistoryAccessToken() +
                "&blogName=" + this.tistory_info.getTistoryBlogName() +
                "&postId=" + postId.toString();

        return this.restTemplate.getForObject(url, String.class);
    }
}