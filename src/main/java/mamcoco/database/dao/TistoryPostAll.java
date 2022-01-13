package mamcoco.database.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class TistoryPostAll extends TistoryPostSync {
    @Id
    @Column(name="post_id")
    @NotNull
    private Long postId;

    @Column(name="post_title")
    @NotNull
    private String postTitle;

    @Column(name="post_content")
    @NotNull
    private String postContent;

    @Column(name="post_tags")
    @NotNull
    private String postTags;

    @Column(name="tistory_blog_name")
    @NotNull
    protected String tistoryBlogName;

    public TistoryPostAll(Long tistoryPostId,
                          Long postId,
                          Long catId,
                          String postTitle,
                          String postContent,
                          String postTags,
                          Integer postVisible,
                          String tistoryBlogName,
                          String tistoryPostDate){
        this.tistoryPostId =  tistoryPostId;
        this.postId = postId;
        this.catId = catId;
        this.postTitle =  postTitle;
        this.postContent =  postContent;
        this.postTags =  postTags;
        this.postVisible = postVisible;
        this.tistoryBlogName = tistoryBlogName;
        this.tistoryPostDate = tistoryPostDate;
    }

    public TistoryPostAll() {

    }
}
