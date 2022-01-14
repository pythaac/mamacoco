package mamcoco.database.repository;

import mamcoco.database.dao.TistoryCategory;
import mamcoco.database.dao.TistoryPost;
import mamcoco.database.dao.TistoryPostAll;
import mamcoco.database.dao.TistoryPostSync;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface TistoryPostRepository extends CrudRepository<TistoryPost, Long> {
    ArrayList<TistoryPost> findTistoryPostsByTistoryBlogName(String tistory_blog_name);
    TistoryPost findTistoryPostByTistoryPostId(Long tistory_post_id);
    ArrayList<TistoryPost> saveAll(ArrayList<TistoryPost> list);

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

    // for compare
    @Query("select NEW mamcoco.database.dao.TistoryPostSync" +
            "(t.tistoryPostId," +
            "p.catId," +
            "p.postVisible," +
            "t.tistoryPostDate)" +
            "from TistoryPost t join Post p on p.postId = t.postId where t.tistoryBlogName = :blog_name")
    ArrayList<TistoryPostSync> findTistoryPostsWithPostForSync(@Param("blog_name") String blog_name);
}
