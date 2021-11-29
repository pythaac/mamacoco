package mamcoco.dao;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity
public class TistoryInfo implements Serializable {
    @Id
    @Column(name="tistory_blog_id")
    @NotNull
    private String tistoryBlogId;

    @Column(name="tistory_blog_name")
    @NotNull
    private String tistoryBlogName;

    @Column(name="tistory_access_token")
    @NotNull
    private String tistoryAccessToken;
}
