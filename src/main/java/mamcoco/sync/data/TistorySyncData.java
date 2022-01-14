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

    // for test
    public String printIds(){
        String catDBId = "catDBId: {\n\t";
        String catBlogId = "catBlogId: {\n\t";
        String postDBId = "postDBId: {\n\t";
        String postBlogId = "postBlogId: {\n\t";

        catDBId = catDBId + this.printCatIds(this.catDB) + "\n}\n";
        catBlogId = catBlogId + this.printCatIds(this.catBlog) + "\n}\n";
        postDBId = postDBId + this.printPostIds(this.postDB) + "\n}\n";
        postBlogId = postBlogId + this.printPostIds(this.postBlog) + "\n}\n";

        return catDBId + catBlogId + postDBId + postBlogId;
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
