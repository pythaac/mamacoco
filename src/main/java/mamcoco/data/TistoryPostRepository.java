package mamcoco.data;

import mamcoco.dao.TistoryCategoryAll;
import mamcoco.dao.TistoryPost;
import mamcoco.dao.TistoryPostAll;
import mamcoco.dao.TistoryPostSync;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface TistoryPostRepository extends CrudRepository<TistoryPost, Long> {
    ArrayList<TistoryPost> findTistoryPostsByTistoryBlogName(String tistory_blog_name);
    TistoryPost findTistoryPostByTistoryPostId(Long tistory_post_id);

    @Query("select NEW TistoryPostAll" +
            "(t.tistoryPostId," +
            "p.postId," +
            "p.catId," +
            "p.postTitle," +
            "p.postContent," +
            "p.postTags," +
            "p.postVisible," +
            "p.postDeleted," +
            "t.tistoryBlogName," +
            "t.tistoryPostDate)" +
            "from TistoryPost t join Post p on p.postId = t.postId where t.tistoryBlogName = :blog_name")
    ArrayList<TistoryPostAll> findTistoryPostsWithPost(@Param("blog_name") String blog_name);

    // for compare
    @Query("select NEW mamcoco.dao.TistoryPostSync" +
            "(t.tistoryPostId," +
            "p.catId," +
            "p.postVisible," +
            "t.tistoryPostDate)" +
            "from TistoryPost t join Post p on p.postId = t.postId where t.tistoryBlogName = :blog_name")
    ArrayList<TistoryPostSync> findTistoryPostsWithPostForSync(@Param("blog_name") String blog_name);
}
