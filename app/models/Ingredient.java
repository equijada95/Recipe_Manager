package models;


// import com.avaje.ebean.Query.findUnique;

// import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import io.ebean.Ebean;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.SqlUpdate;
import play.data.validation.Constraints;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import javax.persistence.*;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.*;




// POJO
// Bean

@Entity
public class Ingredient extends Model
{
    @Id
    private Long id;

    @Constraints.Required
    private String name;

    @Enumerated(EnumType.STRING)
    private Category category;

    private boolean withGluten;

    private boolean allergy;

    @ManyToMany(mappedBy = "ingredients")
    public Set<Recipe> recipes;

    @ManyToOne
    public Shop shop;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWithGluten() {
        return withGluten;
    }

    public void setWithGluten(boolean withGluten) {
        this.withGluten = withGluten;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    
    public boolean isAllergy() {
        return allergy;
    }

    public void setAllergy(boolean allergy) {
        this.allergy = allergy;
    }

    @JsonIgnore
    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
    }

    // @JsonIgnore
    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    private static final Finder<Long, Ingredient> find = new Finder<>(Ingredient.class);

    public static Ingredient findById(Long id)
    {
        // return find.byId(id).findOne();
        return find.query().where().eq("id", id).findOne();
    }

    public static Ingredient findByName(String name)
    {
        return find.query().where().eq("name", name).findOne();
    }

    public static List<Ingredient> findIngredientsByCategory(Category category)
    {
        return find.query().where().eq("category", category).findList();
    }

    public void addRecipe(Recipe recipe)
    {
        this.recipes.add(recipe);
    }


    public static List<Ingredient> findIngredientsByShop(Long id)
    {
        return find.query().where().eq("shop_id", id).findList();
    }


    public static void deleteIngWithDependencies(Ingredient ing)
    {
        Ebean.beginTransaction();
        try {
            SqlUpdate sql1 = Ebean.createSqlUpdate("delete from recipe_ingredient where ingredient_id = " + ing.getId());
            SqlUpdate sql2 = Ebean.createSqlUpdate("delete from ingredient where id = " + ing.getId());
            Ebean.execute(sql1);
            Ebean.execute(sql2);


            Ebean.commitTransaction();
        }
        finally {
            Ebean.endTransaction();
        }
    }

}