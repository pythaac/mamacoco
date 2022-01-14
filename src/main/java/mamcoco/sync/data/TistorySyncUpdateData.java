package mamcoco.sync.data;

import lombok.Data;
import mamcoco.database.dao.TistoryCategorySync;
import mamcoco.database.dao.TistoryPostSync;

import java.util.ArrayList;

@Data
public class TistorySyncUpdateData
{
    public ArrayList<TistoryCategorySync> catCreateList;
    public ArrayList<TistoryCategorySync> catUpdateList;
    public ArrayList<TistoryCategorySync> catDeleteList;
    public ArrayList<TistoryPostSync> postCreateList;
    public ArrayList<TistoryPostSync> postUpdateList;
    public ArrayList<TistoryPostSync> postDeleteList;

    public TistorySyncUpdateData(){
        this.catCreateList = new ArrayList<>();
        this.catUpdateList = new ArrayList<>();
        this.catDeleteList = new ArrayList<>();
        this.postCreateList = new ArrayList<>();
        this.postUpdateList = new ArrayList<>();
        this.postDeleteList = new ArrayList<>();
    }

    public void clearCat(){
        this.catCreateList.clear();
        this.catUpdateList.clear();
        this.catDeleteList.clear();
    }

    public void clearPost(){
        this.postCreateList.clear();
        this.postUpdateList.clear();
        this.postDeleteList.clear();
    }

    public Integer getSizeCatCreateList(){
        return this.catCreateList.size();
    }

    public Integer getSizeCatDeleteList(){
        return this.catDeleteList.size();
    }

    public Integer getSizeCatUpdateList(){
        return this.catUpdateList.size();
    }

    public Integer getSizePostCreateList(){
        return this.postCreateList.size();
    }

    public Integer getSizePostDeleteList(){
        return this.postDeleteList.size();
    }

    public Integer getSizePostUpdateList(){
        return this.postUpdateList.size();
    }

    // for test
    public String printIds(){
        String catCreateId = "catCreateId: {\n\t";
        String catUpdateId = "catUpdateId: {\n\t";
        String catDeleteId = "catDeleteId: {\n\t";
        String postCreateId = "postCreateId: {\n\t";
        String postUpdateId = "postUpdateId: {\n\t";
        String postDeleteId = "postDeleteId: {\n\t";

        catCreateId = catCreateId + this.printCatIds(this.catCreateList) + "\n}\n";
        catUpdateId = catUpdateId + this.printCatIds(this.catUpdateList) + "\n}\n";
        catDeleteId = catDeleteId + this.printCatIds(this.catDeleteList) + "\n}\n";
        postCreateId = postCreateId + this.printPostIds(this.postCreateList) + "\n}\n";
        postUpdateId = postUpdateId + this.printPostIds(this.postUpdateList) + "\n}\n";
        postDeleteId = postDeleteId + this.printPostIds(this.postDeleteList) + "\n}\n";


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
