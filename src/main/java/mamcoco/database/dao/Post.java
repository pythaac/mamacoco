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

    public Post(Long postId,
                Long catId,
                String postTitle,
                String postContent,
                String postTags,
                Integer postVisible)
    {
        this.postId = postId;
        this.catId = catId;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postTags = postTags;
        this.postVisible = postVisible;
    }

    public Post(Long catId,
                String postTitle,
                String postContent,
                String postTags,
                Integer postVisible)
    {
        this.catId = catId;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.postTags = postTags;
        this.postVisible = postVisible;
    }

    public Post() {};
}
