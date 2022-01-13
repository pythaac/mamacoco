package mamcoco.database.repository;

import mamcoco.database.dao.TistoryInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TistoryinfoRepository extends CrudRepository<TistoryInfo, String> {
    TistoryInfo findTistoryInfoByTistoryBlogName(String blog_name);
}
