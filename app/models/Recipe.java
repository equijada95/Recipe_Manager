package models;


import io.ebean.Ebean;
import io.ebean.Finder;
import io.ebean.Model;
import io.ebean.SqlUpdate;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Recipe extends Model {


    @Id
    private Long id;

    @Constraints.Required
    private String name;

    private boolean isVegetarian;

    @ManyToMany(cascade = CascadeType.ALL)
    public List<Ingredient> ingredients = new ArrayList<>();


    @OneToOne(cascade=CascadeType.ALL)
    public NutritionalInformation nutritionalInformation;


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

    public boolean isVegetarian() {
        return isVegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        isVegetarian = vegetarian;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }


    public void addIngredient(Ingredient ingredient)
    {
        this.ingredients.add(ingredient);
        //ingredient.addRecipe(this);
    }

    public NutritionalInformation getNutritionalInformation() {
        return nutritionalInformation;
    }

    public void setNutritionalInformation(NutritionalInformation nutritionalInformation) {
        this.nutritionalInformation = nutritionalInformation;
    }

    private static final Finder<Long, Recipe> find = new Finder<>(Recipe.class);

    public static Recipe findById(Long id)
    {
        // return find.byId(id).findOne();
        return find.query().where().eq("id", id).findOne();
    }


    public static Recipe findByName(String name)
    {
        return find.query().where().like("name", name).findOne();
    }

    public static List<Recipe> listVegetarian()
    {
        return find.query().where().eq("isVegetarian", true).findList();
    }

    public static void deleteRecipeWithDependencies(Recipe recipe)
    {
        Ebean.beginTransaction();
        try {

            SqlUpdate sql1 = Ebean.createSqlUpdate("delete from recipe_ingredient where recipe_id = " + recipe.getId());
            SqlUpdate sql2 = Ebean.createSqlUpdate("delete from recipe where id = " + recipe.getId());
            Ebean.execute(sql1);
            Ebean.execute(sql2);

            Ebean.commitTransaction();
        }
        finally {
            Ebean.endTransaction();
        }
    }
}
