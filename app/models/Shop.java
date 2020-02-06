package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import io.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

import java.util.*;
import javax.persistence.*;


@Entity
public class Shop extends Model {

    @Id
    private Long id;

    // @Constraints.Required
    private String noun;

    @OneToMany(cascade= CascadeType.ALL, mappedBy="shop")
    public List<Ingredient> ingredients;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNoun() {
        return noun;
    }

    public void setNoun(String noun) {
        this.noun = noun;
    }

    @JsonIgnore
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(Ingredient ingredient)
    {
        this.ingredients.add(ingredient);
        // ingredient.shop = this;
    }

    private static final Finder<Long, Shop> find = new Finder<>(Shop.class);

    public static Shop findByNoun(String name)
    {
        return find.query().where().eq("noun", name).findOne();
    }

    public static Shop findById(Long id)
    {
        // return find.byId(id).findOne();
        return find.query().where().eq("id", id).findOne();
    }


}
