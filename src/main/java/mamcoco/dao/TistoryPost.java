package mamcoco.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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

    @Column(name="post_id")
    @NotNull
    private Long postId;

    @Column(name="tistory_post_date")
    @NotNull
    private String tistoryPostDate;

    @Column(name="tistory_visibility")
    @NotNull
    private Integer tistoryVisibility;
}
