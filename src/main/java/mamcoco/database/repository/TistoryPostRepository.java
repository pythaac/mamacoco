package mamcoco.database.repository;

import mamcoco.database.data.TistoryPost;
import mamcoco.database.data.TistoryPostAll;
import mamcoco.database.data.TistoryPostSync;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface TistoryPostRepository extends CrudRepository<TistoryPost, Long> {
    TistoryPost save(TistoryPost tPost);
    TistoryPost deleteByTistoryPostId(Long tistoryPostId);

    // all posts for user
    ArrayList<TistoryPost> findTistoryPostsByTistoryBlogName(String tistoryBlogName);

    // sync data for comparing
    @Query("select NEW mamcoco.database.data.TistoryPostSync" +
            "(t.tistoryPostId," +
            "p.catId," +
            "p.postVisible," +
            "t.tistoryPostDate)" +
            "from TistoryPost t join Post p on p.postId = t.postId where t.tistoryBlogName = :blog_name")
    ArrayList<TistoryPostSync> findTistoryPostsWithPostForSync(@Param("blog_name") String blog_name);

    // not used for now
    @Query("select NEW TistoryPostAll" +
            "(t.tistoryPostId," +
            "p.postId," +
            "p.catId," +
            "p.postTitle," +
            "p.postContent," +
            "p.postTags," +
            "p.postVisible," +
            "t.tistoryBlogName," +
            "t.tistoryPostDate)" +
            "from TistoryPost t join Post p on p.postId = t.postId where t.tistoryBlogName = :blog_name")
    ArrayList<TistoryPostAll> findTistoryPostsWithPost(@Param("blog_name") String blog_name);
}
