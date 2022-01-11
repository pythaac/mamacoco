package mamcoco.apis;

import mamcoco.dao.*;
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

    public ArrayList<TistoryCategorySync> categoryList(){
        String url = this.tistory_domain + "category/list?" +
                "access_token=" + this.tistory_info.getTistoryAccessToken() +
                "&blogName=" + this.tistory_info.getTistoryBlogName();

        // 1. get API data as XML
        String xml = this.restTemplate.getForObject(url, String.class);
        XMLparser parser = new XMLparser(xml);

        // 2. get category list using XMLparser
        NodeList list = parser.into("item").into("categories").getList();

        // 3. convert each category using XMLparser into TistoryCategorySync
        return this.categoryNodeListToArrayList(list);
    }

    private ArrayList<TistoryCategorySync> categoryNodeListToArrayList(NodeList list)
    {
        ArrayList<TistoryCategorySync> result = new ArrayList<>();

        for(int i=0; i<list.getLength(); i++)
        {
            Element category = (Element)list.item(i);

            Long id = Long.parseLong(XMLparser.getValue(category, "id"));
            String name = XMLparser.getValue(category, "name");
            Long parent = Long.parseLong(XMLparser.getValue(category, "parent"));
            Integer visible = XMLparser.getValue(category, "entries").equals("0")? 0 : 1;

            result.add(new TistoryCategorySync(id, name, parent, visible));
        }

        return result;
    }

//    public String postList(){
//        String url = this.tistory_domain + "post/list?" +
//                "access_token=" + this.tistory_info.getTistoryAccessToken() +
//                "&blogName=" + this.tistory_info.getTistoryBlogName() +
//                "&page=" + "1";
//
//        // 1. get API data as XML
//        String xml = this.restTemplate.getForObject(url, String.class);
//        XMLparser parser = new XMLparser(xml);
//
//        // 2. get the number of pages
//        Long numPages = this.getNumPages(parser);
//
//
//
//
//        return
//    }

    private ArrayList<TistoryPostSync> postNodeListToArrayList(NodeList list){
        ArrayList<TistoryPostSync> result = new ArrayList<>();

        for(int i=0; i<list.getLength(); i++)
        {
            TistoryPostSync post = new TistoryPostSync();
        }

        return result;
    }

    private Long getNumPages(XMLparser parser)
    {
        parser.saveCurrent();
        parser.init();
        parser.into("item");
        Double count = Double.parseDouble(parser.get("count"));
        Double totalCount = Double.parseDouble(parser.get("totalCount"));
        parser.restoreCurrent();

        return (long) Math.ceil(totalCount / count);
    }

    public String page(Long page){
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