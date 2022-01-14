package mamcoco.parser;

import mamcoco.apis.TistoryAPIMapper;
import mamcoco.database.dao.TistoryCategoryAll;
import mamcoco.database.dao.TistoryCategorySync;
import mamcoco.database.dao.TistoryPostAll;
import mamcoco.database.dao.TistoryPostSync;
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

//    public ArrayList<TistoryCategoryAll> getCategoryListAll(String xml){
//        // 1. get API data as XML
//        XMLParser parser = new XMLParser(xml);
//
//        // 2. get category list using XMLparser
//        NodeList list = parser.into("item").into("categories").getList();
//
//        // 3. convert each category using XMLparser into TistoryCategorySync
//        return this.categoryNodeListToArrayList(list);
//    }
//
//    public TistoryPostAll getPostAll(String xml){
//        // 1. get API data as XML
//        XMLParser parser = new XMLParser(xml);
//
//        // 2. get post list using XMLparser
//        NodeList list = parser.into("item").into("posts").getList();
//
//        // 3. convert each post using XMLparser into TistoryPostSync
//        return this.postNodeListToArrayList(list);
//    }

    public Long getNumPages(String xml)
    {
        XMLParser parser = new XMLParser(xml);
        parser.into("item");
        Double count = Double.parseDouble(parser.get("count"));
        Double totalCount = Double.parseDouble(parser.get("totalCount"));

        return (long) Math.ceil(totalCount / count);
    }

    private ArrayList<TistoryCategorySync> categorySyncNodeListToArrayList(NodeList list)
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

    private ArrayList<TistoryPostSync> postSyncNodeListToArrayList(NodeList list){
        ArrayList<TistoryPostSync> result = new ArrayList<>();

        for(int i=0; i<list.getLength(); i++)
        {
            Element post = (Element)list.item(i);

            // 1. get tags
            Long tistory_post_id = Long.parseLong(XMLParser.getValue(post, "id"));
            Long tmp_cat_id = Long.parseLong(XMLParser.getValue(post, "categoryId"));
            String tmp_post_visible = XMLParser.getValue(post, "visibility");
            String tistory_post_date = XMLParser.getValue(post, "date");

            // 2. get mapped data
            Integer post_visible = this.mapper.mapPostVisible(tmp_post_visible);
            this.mapper.updateCatMapTable();
            Long cat_id = this.mapper.mapTistoryCatId(tmp_cat_id);

            result.add(new TistoryPostSync(tistory_post_id, cat_id, post_visible, tistory_post_date));
        }

        return result;
    }

//    private ArrayList<TistoryCategoryAll> categoryAllNodeListToArrayList(NodeList list)
//    {
//        ArrayList<TistoryCategoryAll> result = new ArrayList<>();
//
//        for(int i=0; i<list.getLength(); i++)
//        {
//            Element category = (Element)list.item(i);
//
//            // 1. get tags
//            Long id = Long.parseLong(XMLParser.getValue(category, "id"));
//            String name = XMLParser.getValue(category, "name");
//            String tmpParent = XMLParser.getValue(category, "parent");
//            String tmpVisible = XMLParser.getValue(category, "entries");
//
//            // 2. get mapped data
//            Integer visible = this.mapper.mapCategoryVisible(tmpVisible);
//            Long parent = tmpParent.isEmpty()? null : Long.parseLong(tmpParent);;
//
//            result.add(new TistoryCategoryAll(id, name, parent, visible));
//        }
//
//        return result;
//    }

//    private ArrayList<TistoryPostAll> postAllNodeListToArrayList(NodeList list){
//        ArrayList<TistoryPostAll> result = new ArrayList<>();
//
//        for(int i=0; i<list.getLength(); i++)
//        {
//            Element post = (Element)list.item(i);
//
//            // 1. get tags
//            Long tistory_post_id = Long.parseLong(XMLParser.getValue(post, "id"));
//            Long tmp_cat_id = Long.parseLong(XMLParser.getValue(post, "categoryId"));
//            String tmp_post_visible = XMLParser.getValue(post, "visibility");
//            String tistory_post_date = XMLParser.getValue(post, "date");
//
//            // 2. get mapped data
//            Integer post_visible = this.mapper.mapPostVisible(tmp_post_visible);
//            this.mapper.updateCatMapTable();
//            Long cat_id = this.mapper.mapTistoryCatId(tmp_cat_id);
//
//            result.add(new TistoryPostAll(tistory_post_id, cat_id, post_visible, tistory_post_date));
//        }
//
//        return result;
//    }
}
