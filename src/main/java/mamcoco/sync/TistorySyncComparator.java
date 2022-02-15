package mamcoco.sync;

import mamcoco.apis.TistoryAPIMapper;
import mamcoco.database.data.TistoryCategorySync;
import mamcoco.database.data.TistoryPostSync;
import mamcoco.sync.data.TistorySyncData;
import mamcoco.sync.data.TistorySyncUpdateData;

public class TistorySyncComparator
{
    private TistorySyncUpdateData result;
    private TistorySyncData data;
    private final TistoryAPIMapper mapper;
    private static final Long TISTORY_BUG_CATEGORY_ID = (long)0;

    private int i_db;
    private int i_blog;

    public TistorySyncComparator(TistorySyncData data, TistoryAPIMapper mapper){
        this.result = new TistorySyncUpdateData();
        this.data = data;
        this.mapper = mapper;
    }

    public TistorySyncUpdateData getResult(){
        return this.result;
    }

    public void checkCategory(){
        result.clearCat();

        i_db = 0;
        i_blog = 0;

        while(i_db < data.getSizeCatDB() || this.i_blog < data.getSizeCatBlog())
        {
            if (this.isCatCreate()){
                result.catCreateList.add(getCatBlog(i_blog));
                i_blog++;
            }
            else if (this.isCatDelete()){
                result.catDeleteList.add(getCatDB(i_db));
                i_db++;
            }
            else if (this.isCatUpdate()){
                result.catUpdateList.add(getCatBlog(i_blog));
                i_db++;
                i_blog++;
            }
            else{
                i_db++;
                i_blog++;
            }
        }
    }

    public void checkPost(){
        result.clearPost();

        i_db = 0;
        i_blog = 0;

        while(i_db < data.getSizePostDB() || i_blog < data.getSizePostBlog())
        {
            // skip if tistoryPostId == TISTORY_BUG_CATEGORY_ID
            if (i_blog < data.getSizePostBlog() && getPostBlog(i_blog).getCatId().equals(TISTORY_BUG_CATEGORY_ID)){
                i_blog++;
            }
            else if (this.isPostCreate()){
                result.postCreateList.add(getPostBlog(i_blog));
                i_blog++;
            }
            else if (this.isPostDelete()){
                result.postDeleteList.add(getPostDB(i_db));
                i_db++;
            }
            else if (this.isPostUpdate()){
                result.postUpdateList.add(getPostBlog(i_blog));
                i_db++;
                i_blog++;
            }
            else{
                i_db++;
                i_blog++;
            }
        }
    }

    private TistoryCategorySync getCatDB(int i){
        return this.data.catDB.get(i);
    }

    private TistoryCategorySync getCatBlog(int i){
        return this.data.catBlog.get(i);
    }

    private TistoryPostSync getPostDB(int i){
        return this.data.postDB.get(i);
    }

    private TistoryPostSync getPostBlog(int i){
        return this.data.postBlog.get(i);
    }

    private boolean isCatCreate(){
//        if (data.getSizeCatDB() > i_db && getCatDB(i_db).getTistoryCatId() == 0)
//            return false;
        if (data.getSizeCatDB() <= i_db)
            return true;
        if (i_blog < data.getSizeCatBlog())
            return getCatDB(i_db).getTistoryCatId() > getCatBlog(i_blog).getTistoryCatId();
        return false;
    }

    private boolean isCatDelete(){
//        if (data.getSizeCatDB() > i_db && getCatDB(i_db).getTistoryCatId() == 0)
//            return false;
        if (data.getSizeCatBlog() <= i_blog)
            return true;
        if (i_db < data.getSizeCatDB())
            return getCatDB(i_db).getTistoryCatId() < getCatBlog(i_blog).getTistoryCatId();
        return false;
    }

    private boolean isCatUpdate(){
//        if (data.getSizeCatDB() > i_db && getCatDB(i_db).getTistoryCatId() == 0)
//            return false;
        if (i_blog >= data.getSizeCatBlog() || i_db >= data.getSizeCatDB())
            return false;
        if (!getCatDB(i_db).getCatName().equals(getCatBlog(i_blog).getCatName()))
            return true;
        if (!getCatDB(i_db).getCatVisible().equals(getCatBlog(i_blog).getCatVisible()))
            return true;
        // 1) not null && 2) not equals mapped catId
        if (getCatBlog(i_blog).getCatParent() != null){
            Long cat_id = this.mapper.getMapByTistoryCatId(getCatBlog(i_blog).getCatParent());
            if (!getCatDB(i_db).getCatParent().equals(cat_id))
                return true;
        }
        return false;
    }

    private boolean isPostCreate(){
        if (data.getSizePostDB() <= i_db)
            return true;
        if (i_db < data.getSizePostDB())
            return getPostDB(i_db).getTistoryPostId() > getPostBlog(i_blog).getTistoryPostId();
        return false;
    }

    private boolean isPostDelete(){
        if (data.getSizePostBlog() <= i_blog)
            return true;
        if (i_db < data.getSizePostDB())
            return getPostDB(i_db).getTistoryPostId() < getPostBlog(i_blog).getTistoryPostId();
        return false;
    }

    private boolean isPostUpdate(){
        if (i_blog >= data.getSizePostBlog() || i_db >= data.getSizePostDB())
            return false;
        if (!getPostDB(i_db).getTistoryPostDate().equals(getPostBlog(i_blog).getTistoryPostDate()))
            return true;
        if (!getPostDB(i_db).getPostVisible().equals(getPostBlog(i_blog).getPostVisible()))
            return true;
        if (!getPostDB(i_db).getCatId().equals(mapper.getMapByTistoryCatId(getPostBlog(i_blog).getCatId())))
            return true;
        return false;
    }
}
