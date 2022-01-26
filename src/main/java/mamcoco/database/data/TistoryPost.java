package mamcoco.database.data;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class TistoryPost {
    @Id
    @Column(name="tistory_post_id")
    @NotNull
    private Long tistoryPostId;

    @Column(name="tistory_blog_name")
    @NotNull
    private String tistoryBlogName;

    @Column(name="tistory_post_date")
    @NotNull
    private String tistoryPostDate;

    @Column(name="post_id", insertable = false, updatable = false)
    @NotNull
    private Long postId;

    @OneToOne
    @JoinColumn(name="post_id")
    private Post post;

    public TistoryPost(Long tistoryPostId,
                       String tistoryBlogName,
                       String tistoryPostDate,
                       Long postId,
                       Post post){
        this.tistoryPostId = tistoryPostId;
        this.tistoryBlogName = tistoryBlogName;
        this.tistoryPostDate = tistoryPostDate;
        this.postId = postId;
        this.post = post;
    }

    public TistoryPost() {}
}
