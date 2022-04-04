package mamcoco.database.repository;

import mamcoco.database.data.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public interface PostRepository extends CrudRepository<Post, Long>
{
    Post save(Post post);
    Long deleteByPostId(Long postId);
    ArrayList<Post> findAllByCatId(Long catId);
    Post findPostByPostId(Long postId);
}
