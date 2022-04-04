package mamcoco.database.repository;

import mamcoco.database.data.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Repository
@Transactional(propagation = Propagation.REQUIRED)
public interface CategoryRepository extends CrudRepository<Category, Long>
{
    Category save(Category cat);
    Category deleteByCatId(Long catId);
    ArrayList<Category> findAll();
}
