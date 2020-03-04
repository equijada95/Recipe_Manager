package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.ebean.Finder;
import io.ebean.Model;
import play.data.validation.Constraints.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;


@Entity
public class Shop extends Model {

    @Id
    private Long id;

    @Required
    private String noun;

    @Email
    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
    }

    private static final Finder<Long, Shop> find = new Finder<>(Shop.class);

    public static Shop findByNoun(String name)
    {
        return find.query().where().eq("noun", name).findOne();
    }

    public static Shop findById(Long id)
    {
        return find.query().where().eq("id", id).findOne();
    }

    public static List<Shop> findAll()
    {
        return find.all();
    }


}
