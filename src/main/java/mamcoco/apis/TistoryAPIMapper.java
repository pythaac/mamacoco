package mamcoco.apis;

import mamcoco.dao.TistoryCategory;
import mamcoco.dao.TistoryInfo;
import mamcoco.data.TistoryCategoryRepository;

import java.util.ArrayList;
import java.util.HashMap;

public class TistoryAPIMapper
{
    private final TistoryCategoryRepository catRepo;
    private final TistoryInfo info;
    private HashMap<Long, Long> catMapTable;

    public TistoryAPIMapper(TistoryInfo info, TistoryCategoryRepository catRepo){
        this.info = info;
        this.catRepo = catRepo;
    };

    /*
        <TistoryAPI data>
            $entries
                the number of posts in the category
        <Database>
            0   :   invisible
            1   :   visible
        <Mappings>
            if $entries == 0 then 0
            else then 1
        <Description>
            invisible(0) if category has no post
            visible(1) if category has posts at least 1
     */
    public Integer mapCategoryVisible(String entries){
        return entries.equals("0")? 0 : 1;
    }

    /*
        <TistoryAPI data>
            $visibility
            0   :   invisible
            10  :   locked
            20  :   visible
        <Database>
            0   :   invisible
            1   :   visible
            2   :   locked
        <Mappings>
            if $visibility == 0 then 0
            elif $visibility == 20 then 1
            elif $visibility == 10 then 2
            else then -1
        <Description>
            "locked" is 2 in database in that that might be Tistory-only
    */
    public Integer mapPostVisible(String visibility){
        int value = Integer.parseInt(visibility);
        switch (value)
        {
            case 0:
                return 0;

            case 20:
                return 1;

            case 10:
                return 2;

            default:
                return -1;
        }
    }

    /*
        <TistoryAPI data>
            $catId
        <Database>
            $tistoryCatId
        <Mappings>
            if $tistoryCatId corresponding $catID exists then $tistoryCatId
            else then null
        <Description>
            For checking updated posts, $tistoryCatId is needed
    */
    public Long mapTistoryCatId(Long cat_id){
        return this.catMapTable.getOrDefault(cat_id, null);
    }

    private void updateCatMapTable()
    {
        ArrayList<TistoryCategory> catList = catRepo.findTistoryCategoriesByTistoryBlogName(this.info.getTistoryBlogName());

        HashMap<Long, Long> result = new HashMap<>();
        for(TistoryCategory cat : catList)
        {
            result.put(cat.getTistoryCatId(), cat.getCatId());
        }

        this.catMapTable = result;
    }
}
