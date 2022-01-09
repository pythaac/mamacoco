package mamcoco.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Post {
    @Id
    @Column(name="post_id")
    @NotNull
    private Long postId;

    @Column(name="cat_id")
    @NotNull
    private Long catId;

    @Column(name="post_title")
    @NotNull
    private String postTitle;

    @Column(name="post_content")
    @NotNull
    private String postContent;

    @Column(name="post_tags")
    @NotNull
    private String postTags;

    @Column(name="post_visible")
    @NotNull
    private Integer postVisible;

    @Column(name="post_deleted")
    @NotNull
    private Integer postDeleted;
}
