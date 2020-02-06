package controllers;

import actions.TimerAction;
import com.fasterxml.jackson.databind.JsonNode;
import models.Category;
import models.Ingredient;
import models.Shop;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.*;

import javax.inject.Inject;
import java.util.List;

import static play.libs.Json.toJson;


public class IngredientController extends Controller
{

    @Inject
    FormFactory formFactory;


    @With(TimerAction.class)
    public Result createIngredient(Http.Request request) {



        Form<Ingredient> form = formFactory.form(Ingredient.class).bindFromRequest(request);



        if (form.hasErrors()) {
            return Results.badRequest(form.errorsAsJson());
        }


        Ingredient ing = form.get();

        Shop shop = ing.getShop();

        if (Ingredient.findByName(ing.getName()) != null) {
            return Results.status(403, "Error: Duplicated Ingredient");
        } else if (Shop.findByNoun(shop.getNoun()) != null) {
            shop = Shop.findByNoun(ing.getShop().getNoun());
            ing.setShop(shop);
        }


        shop.addIngredient(ing);
        shop.save();
        ing.save();

        if (request.accepts("application/json")) {
            JsonNode json = toJson(ing);
            return Results.ok(json);
        } else if (request.accepts("application/xml")) {
            return Results.ok(views.xml.ing.render(ing));
        } else {
            return Results.status(415);
        }

    }



    public Result updateIngredient(Http.Request request, String name) {
        // TODO Use Form
        JsonNode node = request.body().asJson();

        Ingredient ing = Ingredient.findByName(name);
        if (ing == null)
        {
            return Results.status(404, "Error: Ingredient Not Found");



        } else {
            // if(node.get("name").asText() != null) {
                ing.setName(node.get("name").asText());

                /*if (Ingredient.findByName(ing.getName()) != null) {
                    return Results.status(403, "Error: Duplicated Ingredient");
                }*/
           //  }
            String nameCat = node.get("category").asText();
            // if(nameCat != null) {
                ing.setCategory(Category.valueOf(nameCat));
            // }
            //if(node.get("withGluten").asBoolean() != null)
            ing.setWithGluten(node.get("withGluten").asBoolean());

            ing.setAllergy(node.get("allergy").asBoolean());

            JsonNode node2 = node.path("shop");

            String noun = node2.get("noun").asText();


            if (Shop.findByNoun(noun) != null) {
                Shop shop = Shop.findByNoun(noun);
                ing.setShop(shop);
                shop.addIngredient(ing);
                shop.save();
                ing.update();
            } else {

                Shop shop = new Shop();
                shop.setNoun(noun);
                shop.addIngredient(ing);
                ing.setShop(shop);

                shop.save();
                ing.update();
            }


            if (request.accepts("application/json")) {
                JsonNode json = toJson(ing);
                return Results.ok(json);
            } else if (request.accepts("application/xml")) {
                return Results.ok(views.xml.ing.render(ing));
            } else {
                return Results.status(415);
            }
        }
    }

    public Result listIngredientsByCategory(Http.Request request, String cat)
    {
        Category category = Category.valueOf(cat);
        List<Ingredient> ingredients = Ingredient.findIngredientsByCategory(category);
        if(ingredients == null)
        {
            return Results.status(404, "Error, Ingredients are null");
        } else if(request.accepts("application/json"))
        {
            JsonNode json = toJson(ingredients);
            return Results.ok(json);
        } else if(request.accepts("application/xml"))
        {
            return Results.ok(views.xml.ingredients.render(ingredients));
        } else
        {
            return Results.status(415);
        }



    }

    public Result findIngredientByName(Http.Request request, String name)
    {
        Ingredient ing = Ingredient.findByName(name);
        if(ing == null)
        {
            return Results.status(404, "Error: Ingredient Not Found");
        }
        else if(request.accepts("application/json"))
        {
            JsonNode json = toJson(ing);
            return Results.ok(json);
        } else if(request.accepts("application/xml"))
        {
            return Results.ok(views.xml.ing.render(ing));
        } else
        {
            return Results.status(415);
        }
    }

    public Result findIngredientById(Http.Request request, Long id)
    {
        Ingredient ing = Ingredient.findById(id);
        if(ing == null)
        {
            return Results.status(404, "Error: Ingredient Not Found");
        }
        else if(request.accepts("application/json"))
        {
            JsonNode json = toJson(ing);
            return Results.ok(json);
        } else if(request.accepts("application/xml"))
        {
            return Results.ok(views.xml.ing.render(ing));
        } else
        {
            return Results.status(415);
        }
    }

    public Result listIngredientsByShop(Http.Request request, String noun)
    {
        Shop shop = Shop.findByNoun(noun);
        List<Ingredient> ingredients = Ingredient.findIngredientsByShop(shop.getId());
        if(ingredients == null)
        {
            return Results.status(404);
        } else if(request.accepts("application/json"))
        {
            JsonNode json = toJson(ingredients);
            return Results.ok(json);
        } else if(request.accepts("application/xml"))
        {
            return Results.ok(views.xml.ingredients.render(ingredients));
        } else
        {
            return Results.status(415);
        }

    }



    @play.db.ebean.Transactional
    public Result deleteIngredient(String name)
    {

        Ingredient ing = Ingredient.findByName(name);
        if (ing == null)
        {
            return Results.status(404, "Error: Ingredient Not Found");

        } else{
            ing.refresh();

            Ingredient.deleteIngWithDependencies(ing);

            return Results.ok("The ingredient "+ name + " was deleted");
        }

    }


}