package mamcoco.database.repository;

import mamcoco.database.data.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long>
{
    Post save(Post post);
    Post deleteByPostId(Long postId);
}
