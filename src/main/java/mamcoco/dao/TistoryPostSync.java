package mamcoco.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
public class TistoryPostSync {
    @Id
    @Column(name="tistory_post_id")
    @NotNull
    protected Long tistoryPostId;

    @Column(name="cat_id")
    @NotNull
    protected Long catId;

    @Column(name="post_visible")
    @NotNull
    protected Integer postVisible;

    @Column(name="tistory_blog_name")
    @NotNull
    protected String tistoryBlogName;

    @Column(name="tistory_post_date")
    @NotNull
    protected String tistoryPostDate;

    public TistoryPostSync(Long tistoryPostId,
                          Long catId,
                          Integer postVisible,
                          String tistoryBlogName,
                          String tistoryPostDate){
        this.tistoryPostId =  tistoryPostId;
        this.catId = catId;
        this.postVisible = postVisible;
        this.tistoryBlogName = tistoryBlogName;
        this.tistoryPostDate = tistoryPostDate;
    }

    public TistoryPostSync() {

    }
}
