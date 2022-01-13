package mamcoco.database.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Category {
    @Id
    @Column(name="cat_id")
    @NotNull
    private Long catId;

    @Column(name="cat_name")
    @NotNull
    private String catName;

    @Column(name="cat_parent")
    private Long catParent;

    @Column(name="cat_visible")
    @NotNull
    private Integer catVisible;
}
