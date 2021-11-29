package mamcoco.data;

import mamcoco.dao.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepo extends CrudRepository<TistoryInfo, String> {
    TistoryInfo findByTistoryBlogId(String tistory_blog_id);
}
