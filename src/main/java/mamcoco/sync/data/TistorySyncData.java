package mamcoco.sync.data;

import lombok.Data;
import mamcoco.database.dao.TistoryCategorySync;
import mamcoco.database.dao.TistoryPostSync;

import java.util.ArrayList;

@Data
public class TistorySyncData
{
    public ArrayList<TistoryCategorySync> catDB;
    public ArrayList<TistoryCategorySync> catBlog;
    public ArrayList<TistoryPostSync> postDB;
    public ArrayList<TistoryPostSync> postBlog;

    public TistorySyncData(){
        this.catDB = new ArrayList<>();
        this.catBlog = new ArrayList<>();
        this.postDB = new ArrayList<>();
        this.postBlog = new ArrayList<>();
    }
}
