package mamcoco.sync;

import mamcoco.database.dao.TistoryCategorySync;
import mamcoco.database.dao.TistoryPostSync;
import mamcoco.sync.data.TistorySyncData;
import mamcoco.sync.data.TistorySyncUpdateData;

import java.util.ArrayList;

public class TistorySyncComparator
{
    private TistorySyncUpdateData result;
    private TistorySyncData data;

    private int i_db;
    private int i_blog;

    public TistorySyncComparator(TistorySyncData data){
        this.result = new TistorySyncUpdateData();
        this.data = data;
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

    private int getSizeCatDB(){
        return this.data.catDB.size();
    }

    private int getSizeCatBlog(){
        return this.data.catBlog.size();
    }

    private int getSizePostDB(){
        return this.data.postDB.size();
    }

    private int getSizePostBlog(){
        return this.data.postBlog.size();
    }

    public boolean isCatCreate(){
        if (getSizeCatDB() <= i_db)
            return true;
        if (i_blog < getSizeCatBlog())
            return getCatDB(i_db).getTistoryCatId() > getCatBlog(i_blog).getTistoryCatId();
        return false;
    }

    public boolean isCatDelete(){
        if (getSizeCatDB() <= i_blog)
            return true;
        if (i_db < getSizeCatDB())
            return getCatDB(i_db).getTistoryCatId() < getCatBlog(i_blog).getTistoryCatId();
        return false;
    }

    public boolean isCatUpdate(){
        if (i_blog >= getSizeCatBlog() || i_db >= getSizeCatDB())
            return false;
        if (!getCatDB(i_db).getCatName().equals(getCatBlog(i_blog).getCatName()))
            return true;
        if (!getCatDB(i_db).getCatParent().equals(getCatBlog(i_blog).getCatParent()))
            return true;
        if (!getCatDB(i_db).getCatVisible().equals(getCatBlog(i_blog).getCatVisible()))
            return true;
        return false;
    }

    public void checkCategory(){
        result.clearCat();

        i_db = 0;
        i_blog = 0;

        while(i_db < getSizeCatDB() || this.i_blog < getSizeCatBlog())
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
                result.catUpdateList.add(getCatDB(i_db));
                i_db++;
                i_blog++;
            }
            else{
                i_db++;
                i_blog++;
            }
        }
    }

    // for test
    public String printIds(){
        String catCreateId = "catCreateId: {\n\t";
        String catUpdateId = "catUpdateId: {\n\t";
        String catDeleteId = "catDeleteId: {\n\t";
        String postCreateId = "postCreateId: {\n\t";
        String postUpdateId = "postUpdateId: {\n\t";
        String postDeleteId = "postDeleteId: {\n\t";

        catCreateId = catCreateId + this.printCatIds(result.catCreateList) + "\n}\n";
        catUpdateId = catUpdateId + this.printCatIds(result.catUpdateList) + "\n}\n";
        catDeleteId = catDeleteId + this.printCatIds(result.catDeleteList) + "\n}\n";
        postCreateId = postCreateId + this.printPostIds(result.postCreateList) + "\n}\n";
        postUpdateId = postUpdateId + this.printPostIds(result.postUpdateList) + "\n}\n";
        postDeleteId = postDeleteId + this.printPostIds(result.postDeleteList) + "\n}\n";


        return catCreateId + catUpdateId + catDeleteId + postCreateId + postUpdateId + postDeleteId;
    }

    private String printPostIds(ArrayList<TistoryPostSync> list){
        String result = "";
        for(TistoryPostSync post : list){
            result = result.concat(post.getTistoryPostId().toString() + " ");
        }
        return result;
    }

    private String printCatIds(ArrayList<TistoryCategorySync> list){
        String result = "";
        for(TistoryCategorySync cat : list){
            result = result.concat(cat.getTistoryCatId().toString() + " ");
        }
        return result;
    }
}
