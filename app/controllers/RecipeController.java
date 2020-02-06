package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.data.Form;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;
import play.mvc.*;
import scala.util.parsing.json.JSONArray;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static play.libs.Json.toJson;

public class RecipeController extends Controller {


    @Inject
    FormFactory formFactory;


    public Result createRecipe(Http.Request request){
        /*JsonNode node = request.body().asJson();

        Recipe recipe = new Recipe();
        recipe.setName(node.get("name").asText());

        recipe.setVegetarian(node.get("isVegetarian").asBoolean());
        */

        Form<Recipe> form = formFactory.form(Recipe.class).bindFromRequest(request);

        if(form.hasErrors()){
            return Results.badRequest(form.errorsAsJson());
        }

        Recipe recipe = form.get();

        NutritionalInformation nutritionalInformation = recipe.getNutritionalInformation();

        if (Recipe.findByName(recipe.getName()) != null) {
            return Results.status(403, "Error: Duplicated Recipe");
        }

        nutritionalInformation.setRecipe(recipe);

        nutritionalInformation.save();

        recipe.save();


        if(request.accepts("application/json"))
        {
            JsonNode json = Json.toJson(recipe);
            return Results.ok(json);
        } else if(request.accepts("application/xml"))
        {
           return Results.ok(views.xml.recipe.render(recipe));
        } else
        {
            return Results.status(415);
        }
    }

    public Result addIngredientToRecipe(Http.Request request, String nameIngredient, String nameRecipe)
    {
        Ingredient ingredient = Ingredient.findByName(nameIngredient);
        Recipe recipe = Recipe.findByName(nameRecipe);

        if(ingredient == null)
        {
            return Results.status(404, "Error: Ingredient not found");
            // intentar que se cree el ingrediente
            // en plan introduciendo en el body del json el ingrediente
            // sino lanzar mensaje que no existe ingrediente
        }
        else if(recipe == null)
        {
            return Results.status(404, "Error: Recipe not found");
        }
        recipe.addIngredient(ingredient);
        ingredient.addRecipe(recipe);

        recipe.update();
        ingredient.update();

        if(request.accepts("application/json"))
        {
            JsonNode json = Json.toJson(recipe);
            return Results.ok(json);
        } else if(request.accepts("application/xml"))
        {
            return Results.ok(views.xml.recipe.render(recipe));
        } else
        {
            return Results.status(415);
        }

    }

    public Result findRecipeByName(Http.Request request, String name)
    {
        Recipe recipe = Recipe.findByName(name);
        if(recipe == null)
        {
            return Results.status(404, "Error: Recipe Not Found");
        }
        else if(request.accepts("application/json"))
        {
            JsonNode json = toJson(recipe);
            return Results.ok(json);
        } else if(request.accepts("application/xml"))
        {
            return Results.ok(views.xml.recipe.render(recipe));
        } else
        {
            return Results.status(415);
        }
    }

    public Result listVegetarianRecipes(Http.Request request)
    {
        List<Recipe> recipes = Recipe.listVegetarian();

        if(recipes == null)
        {
            // por aqui no entra ver porqué sucede
            return Results.status(404, "Error: Vegetarian Recipes Not Found");
        } else if(request.accepts("application/json"))
        {
            JsonNode json = toJson(recipes);
            return Results.ok(json);
        } else if(request.accepts("application/xml"))
        {
            return Results.ok(views.xml.recipes.render(recipes));
        } else
        {
            return Results.status(415);
        }
    }

    public Result updateRecipe(Http.Request request, String name) {
        // TODO Use Form
        JsonNode node = request.body().asJson();

        Recipe recipe = Recipe.findByName(name);
        if (recipe == null)
        {
            return Results.status(404, "Error: Recipe Not Found");

        } else {
            // if((node.get("name").asText()) != null) {
                recipe.setName(node.get("name").asText());

            /*if(Recipe.findByName(recipe.getName()) != null ){
                return Results.status(403, "Error: Duplicated Recipe");
            }*/
            // }
            recipe.setVegetarian(node.get("vegetarian").asBoolean());

            JsonNode node2 = node.path("nutritionalInformation");

            NutritionalInformation nut = new NutritionalInformation();

            nut.setGrams(node2.get("grams").asInt());

            nut.setCalories(node2.get("calories").asInt());

            nut.setCholesterol(node2.get("cholesterol").asText());

            nut.setProtein(node2.get("protein").asText());

            nut.setVitamins(node2.get("vitamins").asText());

            recipe.setNutritionalInformation(nut);

            nut.save();
            recipe.update();

            if (request.accepts("application/json")) {
                JsonNode json = toJson(recipe);
                return Results.ok(json);
            } else if (request.accepts("application/xml")) {
                return Results.ok(views.xml.recipe.render(recipe));
            } else {
                return Results.status(415);
            }
        }
    }



    @play.db.ebean.Transactional
    public Result deleteRecipe(String name)
    {
        Recipe recipe = Recipe.findByName(name);
        if (recipe == null)
        {
            return Results.status(404, "Error: Recipe Not Found");

        } else{
            recipe.refresh();
            Recipe.deleteRecipeWithDependencies(recipe);
            return Results.ok("The recipe "+ name + " was deleted");
        }

    }



}
