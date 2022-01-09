package mamcoco.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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

    @Column(name="cat_id")
    @NotNull
    private Long catId;

    @Column(name="tistory_entries")
    @NotNull
    private Long tistoryEntries;
}
