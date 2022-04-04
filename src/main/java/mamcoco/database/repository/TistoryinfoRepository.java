package mamcoco.database.repository;

import mamcoco.database.data.TistoryInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public interface TistoryinfoRepository extends CrudRepository<TistoryInfo, String> {
    TistoryInfo findTistoryInfoByTistoryBlogName(String blog_name);
}
