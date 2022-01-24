package mamcoco.apis;

import mamcoco.database.data.TistoryCategory;
import mamcoco.database.data.TistoryInfo;
import mamcoco.database.data.TistoryPost;
import mamcoco.database.repository.TistoryCategoryRepository;
import mamcoco.database.repository.TistoryPostRepository;

import java.util.ArrayList;
import java.util.Hashtable;

public class TistoryAPIMapper
{
    private final TistoryCategoryRepository tCatRepo;
    private final TistoryPostRepository tPostRepo;
    private final TistoryInfo info;
    private Hashtable<Long, Long> catMapTable;
    private Hashtable<Long, Long> postMapTable;

    public TistoryAPIMapper(TistoryInfo info, TistoryCategoryRepository tCatRepo, TistoryPostRepository tPostRepo){
        this.info = info;
        this.tCatRepo = tCatRepo;
        this.tPostRepo = tPostRepo;
        this.updateAllCatMapTable();
        this.updateAllPostMapTable();
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
    public Long getMapByTistoryCatId(Long tistory_cat_id){
        return this.catMapTable.getOrDefault(tistory_cat_id, null);
    }

    /*  =====================================================
        ==================== catMapTable ====================
        ===================================================== */

    public void updateAllCatMapTable()
    {
        ArrayList<TistoryCategory> catList = tCatRepo.findTistoryCategoriesByTistoryBlogName(this.info.getTistoryBlogName());

        Hashtable<Long, Long> result = new Hashtable<>();
        for(TistoryCategory cat : catList)
        {
            result.put(cat.getTistoryCatId(), cat.getCatId());
        }

        this.catMapTable = result;
    }

    public void addCatMapTable(TistoryCategory tCat)
    {
        this.catMapTable.put(tCat.getTistoryCatId(), tCat.getCatId());
    }

    public void deleteCatMapTable(Long tCatId) { this.catMapTable.remove(tCatId); }

    /*  =====================================================
        ==================== postMapTable ===================
        ===================================================== */

    public Long getMapByTistoryPostId(Long tistoryPostId){
        return this.postMapTable.getOrDefault(tistoryPostId, null);
    }

    public void updateAllPostMapTable()
    {
        ArrayList<TistoryPost> postList = tPostRepo.findTistoryPostsByTistoryBlogName(this.info.getTistoryBlogName());

        Hashtable<Long, Long> result = new Hashtable<>();
        for(TistoryPost post : postList)
        {
            result.put(post.getTistoryPostId(), post.getPostId());
        }

        this.postMapTable = result;
    }

    public void addPostMapTable(TistoryPost tPost)
    {
        this.postMapTable.put(tPost.getTistoryPostId(), tPost.getPostId());
    }

    public void deletePostMapTable(Long tPostId) { this.postMapTable.remove(tPostId); }
}
