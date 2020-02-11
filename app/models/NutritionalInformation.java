package models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import io.ebean.Model;
import play.data.validation.Constraints.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;



@Entity
public class NutritionalInformation extends Model {

    @Id
    private Long Id;

    private int grams;

    @Max(message = "The maximum is 600 calories", value = 600)// no lo hace
    private int calories;

    @Max(message = "Since cholesterol is a percentage, can not be greater than one", value = 0)
    private Double cholesterol;

    private String protein;

    private String vitamins;

    @OneToOne(mappedBy="nutritionalInformation")
    public Recipe recipe;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public int getGrams() {
        return grams;
    }

    public void setGrams(int grams) {
        this.grams = grams;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public Double getCholesterol() {
        return cholesterol;
    }

    public void setCholesterol(Double cholesterol) {
        this.cholesterol = cholesterol;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getVitamins() {
        return vitamins;
    }

    public void setVitamins(String vitamins) {
        this.vitamins = vitamins;
    }

    @JsonIgnore
    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    private static final Finder<Long, NutritionalInformation> find = new Finder<>(NutritionalInformation.class);

    public static NutritionalInformation findById(Long id)
    {
        // return find.byId(id).findOne();
        return find.query().where().eq("id", id).findOne();
    }


}
