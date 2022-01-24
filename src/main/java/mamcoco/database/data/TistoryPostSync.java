package mamcoco.database.data;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
public class TistoryPostSync {
    @Id
    @Column(name="tistory_post_id")
    @NotNull
    protected Long tistoryPostId;

    @Column(name="cat_id")
    protected Long catId;

    @Column(name="post_visible")
    @NotNull
    protected Integer postVisible;

    @Column(name="tistory_post_date")
    @NotNull
    protected String tistoryPostDate;

    public TistoryPostSync(Long tistoryPostId,
                          Long catId,
                          Integer postVisible,
                          String tistoryPostDate){
        this.tistoryPostId =  tistoryPostId;
        this.catId = catId;
        this.postVisible = postVisible;
        this.tistoryPostDate = tistoryPostDate;
    }

    public TistoryPostSync() {

    }
}
