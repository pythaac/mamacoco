package mamcoco.database.dao;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
}
