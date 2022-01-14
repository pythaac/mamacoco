package mamcoco.database.dao;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class TistoryCategory {
    @Id
    @Column(name="tistory_cat_id")
    @NotNull
    private Long tistoryCatId;

    @Column(name="tistory_blog_name")
    @NotNull
    private String tistoryBlogName;

    @Column(name="cat_id", insertable = false, updatable = false)
    @NotNull
    private Long catId;

    @ManyToOne
    @JoinColumn(name="cat_id")
    private Category category;

    public TistoryCategory(Long tistoryCatId, String tistoryBlogName, Long catId){
        this.tistoryCatId = tistoryCatId;
        this.tistoryBlogName = tistoryBlogName;
        this.catId = catId;
    }
}
