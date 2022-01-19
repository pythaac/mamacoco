package mamcoco.database.repository;

import mamcoco.database.dao.Post;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface PostRepository extends CrudRepository<Post, Long>
{
    Post save(Post post);
    Post deleteByPostId(Long postId);
}
