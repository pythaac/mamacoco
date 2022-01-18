package mamcoco.database.dao;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="cat_id")
    private Long catId;

    @Column(name="cat_name")
    @NotNull
    private String catName;

    @Column(name="cat_parent")
    private Long catParent;

    @Column(name="cat_visible")
    @NotNull
    private Integer catVisible;

    public Category(Long catId, String catName, Long catParent, Integer catVisible){
        this.catId = catId;
        this.catName = catName;
        this.catParent = catParent;
        this.catVisible = catVisible;
    }

    public Category(String catName, Long catParent, Integer catVisible){
        this.catName = catName;
        this.catParent = catParent;
        this.catVisible = catVisible;
    }

    public Category()
    {

    }
}
