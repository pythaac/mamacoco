package mamcoco.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class TistoryCategoryAll extends TistoryCategorySync {
    @Column(name="cat_id")
    private Long catId;

    @Column(name="tistory_blog_name")
    @NotNull
    protected String tistoryBlogName;

    public TistoryCategoryAll(Long tistoryCatId,
                              String catName,
                              Long catParent,
                              Integer catVisible,
                              String tistoryBlogName,
                              Long catId)
    {
        this.tistoryCatId = tistoryCatId;
        this.catName = catName;
        this.catParent = catParent;
        this.catVisible = catVisible;
        this.tistoryBlogName = tistoryBlogName;
        this.catId = catId;
    }

    public TistoryCategoryAll() {

    }
}
