package mamcoco.parser;

import mamcoco.apis.TistoryAPIMapper;
import mamcoco.database.data.TistoryCategorySync;
import mamcoco.database.data.TistoryPostAll;
import mamcoco.database.data.TistoryPostSync;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class TistoryXMLParser
{
    private final TistoryAPIMapper mapper;

    public TistoryXMLParser(TistoryAPIMapper mapper){
        this.mapper = mapper;
    }

    public ArrayList<TistoryCategorySync> getCategoryListSync(String xml){
        // 1. get API data as XML
        XMLParser parser = new XMLParser(xml);

        // 2. get category list using XMLparser
        NodeList list = parser.into("item").into("categories").getList();

        // 3. convert each category using XMLparser into TistoryCategorySync
        return this.categorySyncNodeListToArrayList(list);
    }

    public ArrayList<TistoryPostSync> getPostListSync(String xml){
        // 1. get API data as XML
        XMLParser parser = new XMLParser(xml);

        // 2. get post list using XMLparser
        NodeList list = parser.into("item").into("posts").getList();

        // 3. convert each post using XMLparser into TistoryPostSync
        return this.postSyncNodeListToArrayList(list);
    }

    public Long getNumPages(String xml)
    {
        XMLParser parser = new XMLParser(xml);
        parser.into("item");

        Double count = Double.parseDouble(parser.get("count"));
        Double totalCount = Double.parseDouble(parser.get("totalCount"));

        return (long) Math.ceil(totalCount / count);
    }

    public TistoryPostAll getPost(String xml)
    {
        XMLParser parser = new XMLParser(xml);
        parser.into("item");

        // 1. get tags
        Long id = Long.parseLong(parser.get("id"));
        String title = parser.get("title");
        String content = parser.get("content");
        Long catId = Long.parseLong(parser.get("categoryId"));
        String tmpVisible = parser.get("visibility");
        String date = parser.get("date");
        String tags = this.postTagsToString(parser.getListByTag("tags"));

        // 2. get mapped data
        Integer visible = this.mapper.mapPostVisible(tmpVisible);

        // 3. convert into TistoryPostAll
        TistoryPostAll result = new TistoryPostAll();
        result.setTistoryPostId(id);
        result.setPostTitle(title);
        result.setPostContent(content);
        result.setCatId(catId);
        result.setPostVisible(visible);
        result.setTistoryPostDate(date);
        result.setPostTags(tags);

        return result;
    }

    private ArrayList<TistoryCategorySync> categorySyncNodeListToArrayList(NodeList list)
    {
        ArrayList<TistoryCategorySync> result = new ArrayList<>();

        for(int i=0; i<list.getLength(); i++)
        {
            Element category = (Element)list.item(i);

            // 1. get tags
            Long id = Long.parseLong(XMLParser.getElementValueByTag(category, "id"));
            String name = XMLParser.getElementValueByTag(category, "name");
            String tmpParent = XMLParser.getElementValueByTag(category, "parent");
            String tmpVisible = XMLParser.getElementValueByTag(category, "entries");

            // 2. get mapped data
            Integer visible = this.mapper.mapCategoryVisible(tmpVisible);
            Long parent = tmpParent.isEmpty()? null : Long.parseLong(tmpParent);

            result.add(new TistoryCategorySync(id, name, parent, visible));
        }

        return result;
    }

    private ArrayList<TistoryPostSync> postSyncNodeListToArrayList(NodeList list){
        ArrayList<TistoryPostSync> result = new ArrayList<>();

        for(int i=0; i<list.getLength(); i++)
        {
            Element post = (Element)list.item(i);

            // 1. get tags
            Long tistory_post_id = Long.parseLong(XMLParser.getElementValueByTag(post, "id"));
            Long cat_id = Long.parseLong(XMLParser.getElementValueByTag(post, "categoryId"));
            String tmp_post_visible = XMLParser.getElementValueByTag(post, "visibility");
            String tistory_post_date = XMLParser.getElementValueByTag(post, "date");

            // 2. get mapped data
            Integer post_visible = this.mapper.mapPostVisible(tmp_post_visible);

            result.add(new TistoryPostSync(tistory_post_id, cat_id, post_visible, tistory_post_date));
        }

        return result;
    }

    private String postTagsToString(NodeList list){
        String result = XMLParser.getElementValue((Element)list.item(0));

        for(int i=1; i<list.getLength(); i++){
            result = result.concat("," + XMLParser.getElementValue((Element)list.item(i)));
        }

        return result;
    }
}
