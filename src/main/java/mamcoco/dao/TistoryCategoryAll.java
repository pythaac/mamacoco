package mamcoco.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class TistoryCategoryAll {
    @Id
    @Column(name="tistory_cat_id")
    @NotNull
    private Long tistoryCatId;

    @Column(name="cat_name")
    @NotNull
    private String catName;

    @Column(name="cat_parent")
    @NotNull
    private Long catParent;

    @Column(name="cat_visible")
    @NotNull
    private Integer catVisible;

    @Column(name="tistory_blog_name")
    @NotNull
    private String tistoryBlogName;

    @Column(name="cat_id")
    private Long catId;

    public TistoryCategoryAll() {};

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
}
