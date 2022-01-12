package mamcoco.parser;

import mamcoco.apis.TistoryAPI;
import mamcoco.apis.TistoryAPIMapper;
import mamcoco.dao.TistoryCategorySync;
import mamcoco.dao.TistoryInfo;
import mamcoco.dao.TistoryPostSync;
import mamcoco.data.TistoryCategoryRepository;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class TistoryXMLParser
{
    private final TistoryAPIMapper mapper;

    public TistoryXMLParser(TistoryAPIMapper mapper){
        this.mapper = mapper;
    }

    public ArrayList<TistoryCategorySync> getCategoryList(String xml){
        // 1. get API data as XML
        XMLParser parser = new XMLParser(xml);

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

            // 1. get tags
            Long id = Long.parseLong(XMLParser.getValue(category, "id"));
            String name = XMLParser.getValue(category, "name");
            String tmpParent = XMLParser.getValue(category, "parent");
            String tmpVisible = XMLParser.getValue(category, "entries");

            // 2. get mapped data
            Integer visible = this.mapper.mapCategoryVisible(tmpVisible);
            Long parent = tmpParent.isEmpty()? null : Long.parseLong(tmpParent);;

            result.add(new TistoryCategorySync(id, name, parent, visible));
        }

        return result;
    }

    public ArrayList<TistoryPostSync> getPostList(String xml){
        // 1. get API data as XML
        XMLParser parser = new XMLParser(xml);

        // 2. get post list using XMLparser
        NodeList list = parser.into("item").into("posts").getList();

        // 3. convert each post using XMLparser into TistoryPostSync
        return this.postNodeListToArrayList(list);
    }

    private ArrayList<TistoryPostSync> postNodeListToArrayList(NodeList list){
        ArrayList<TistoryPostSync> result = new ArrayList<>();

        for(int i=0; i<list.getLength(); i++)
        {
            Element post = (Element)list.item(i);

            // 1. get tags
            Long tistory_post_id = Long.parseLong(XMLParser.getValue(post, "id"));
            Long cat_id = Long.parseLong(XMLParser.getValue(post, "categoryId"));
            String tmp_post_visible = XMLParser.getValue(post, "visibility");
            String tistory_post_date = XMLParser.getValue(post, "date");

            // 2. get mapped data
            Integer post_visible = this.mapper.mapPostVisible(tmp_post_visible);

            result.add(new TistoryPostSync(tistory_post_id, cat_id, post_visible, tistory_post_date));
        }

        return result;
    }

    public Long getNumPages(String xml)
    {
        XMLParser parser = new XMLParser(xml);
        parser.into("item");
        Double count = Double.parseDouble(parser.get("count"));
        Double totalCount = Double.parseDouble(parser.get("totalCount"));

        return (long) Math.ceil(totalCount / count);
    }
}
