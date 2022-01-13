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
}
