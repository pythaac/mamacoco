package mamcoco.database.repository;

import mamcoco.database.data.TistoryCategory;
import mamcoco.database.data.TistoryCategoryAll;
import mamcoco.database.data.TistoryCategorySync;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public interface TistoryCategoryRepository extends CrudRepository<TistoryCategory, Long> {
    TistoryCategory save(TistoryCategory tCat);
    TistoryCategory deleteByTistoryCatId(Long tCatId);

    // all categories for user
    ArrayList<TistoryCategory> findTistoryCategoriesByTistoryBlogName(String tistory_blog_name);

    // sync data for comparing
    @Query("select NEW mamcoco.database.data.TistoryCategorySync" +
            "(t.tistoryCatId, " +
            "c.catName, " +
            "c.catParent, " +
            "c.catVisible)" +
            "from TistoryCategory t join Category c on c.catId = t.catId where t.tistoryBlogName = :blog_name")
    ArrayList<TistoryCategorySync> findTistoryCategoriesWithCategoryForSync(@Param("blog_name") String blog_name);

    // not used for now
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
}
