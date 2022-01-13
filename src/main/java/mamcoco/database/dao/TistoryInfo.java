package mamcoco.database.dao;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class TistoryInfo{
    @Id
    @Column(name="tistory_blog_name")
    @NotNull
    private String tistoryBlogName;

    @Column(name="tistory_blog_id")
    @NotNull
    private String tistoryBlogId;

    @Column(name="tistory_access_token")
    @NotNull
    private String tistoryAccessToken;
}
