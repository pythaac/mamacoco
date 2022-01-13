package mamcoco.database.repository;

import mamcoco.database.dao.TistoryCategory;
import mamcoco.database.dao.TistoryCategoryAll;
import mamcoco.database.dao.TistoryCategorySync;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface TistoryCategoryRepository extends CrudRepository<TistoryCategory, Long> {
    ArrayList<TistoryCategory> findTistoryCategoriesByTistoryBlogName(String tistory_blog_name);
    TistoryCategory findTistoryCategoryByTistoryCatId(Long tistory_cat_id);

    @Query("select NEW TistoryCategoryAll" +
            "(t.tistoryCatId, " +
            "c.catName, " +
            "c.catParent, " +
            "c.catVisible, " +
            "t.tistoryBlogName, " +
            "c.catId)" +
            "from TistoryCategory t join Category c on c.catId = t.catId where t.tistoryBlogName = :blog_name")
    ArrayList<TistoryCategoryAll> findTistoryCategoriesWithCategory(@Param("blog_name") String blog_name);

    // for compare
    @Query("select NEW mamcoco.database.dao.TistoryCategorySync" +
            "(t.tistoryCatId, " +
            "c.catName, " +
            "c.catParent, " +
            "c.catVisible)" +
            "from TistoryCategory t join Category c on c.catId = t.catId where t.tistoryBlogName = :blog_name")
    ArrayList<TistoryCategorySync> findTistoryCategoriesWithCategoryForSync(@Param("blog_name") String blog_name);
}
