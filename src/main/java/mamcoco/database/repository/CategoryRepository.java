package mamcoco.database.repository;

import mamcoco.database.dao.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CategoryRepository extends CrudRepository<Category, Long>
{
    Category save(Category cat);
    Category deleteByCatId(Long catId);
}
