package mamcoco.dao;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
public class TistoryCategorySync {
    @Id
    @Column(name="tistory_cat_id")
    @NotNull
    protected Long tistoryCatId;

    @Column(name="cat_name")
    @NotNull
    protected String catName;

    @Column(name="cat_parent")
    @NotNull
    protected Long catParent;

    @Column(name="cat_visible")
    @NotNull
    protected Integer catVisible;

    public TistoryCategorySync(Long tistoryCatId,
                              String catName,
                              Long catParent,
                              Integer catVisible)
    {
        this.tistoryCatId = tistoryCatId;
        this.catName = catName;
        this.catParent = catParent;
        this.catVisible = catVisible;
    }

    public TistoryCategorySync() {

    }
}
