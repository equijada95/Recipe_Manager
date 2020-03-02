package controllers;

import actions.TimerAction;
import com.fasterxml.jackson.databind.JsonNode;
import models.Ingredient;
import models.NutritionalInformation;
import models.Recipe;
import play.data.Form;
import play.data.FormFactory;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.libs.Json;
import play.mvc.*;

import javax.inject.Inject;
import java.util.List;

import static play.libs.Json.toJson;

public class RecipeController extends Controller {


    @Inject
    FormFactory formFactory;

    @Inject
    MessagesApi messagesApi;



    @With(TimerAction.class)
    public Result createRecipe(Http.Request request){


        Messages messages = this.messagesApi.preferred(request);

        Form<Recipe> form = formFactory.form(Recipe.class).bindFromRequest(request);

        if(form.hasErrors()){
            return Results.badRequest(form.errorsAsJson());
        }

        Recipe recipe = form.get();

        NutritionalInformation nutritionalInformation = recipe.getNutritionalInformation();

        if (Recipe.findByName(recipe.getName()) != null) {
            String error = messages.at("DUPLICATED_RECIPE");
            return Results.status(403, error);
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


    @With(TimerAction.class)
    public Result addIngredientToRecipe(Http.Request request, String nameIngredient, String nameRecipe)
    {
        Messages messages = this.messagesApi.preferred(request);

        Ingredient ingredient = Ingredient.findByName(nameIngredient);
        Recipe recipe = Recipe.findByName(nameRecipe);

        if(ingredient == null)
        {
            String error = messages.at("INGNOTFOUND");
            return Results.status(404, error);
        }
        else if(recipe == null)
        {
            String error = messages.at("RECIPENOTFOUND");
            return Results.status(404, error);
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
        Messages messages = this.messagesApi.preferred(request);

        Recipe recipe = Recipe.findByName(name);
        if(recipe == null)
        {
            String error = messages.at("RECIPENOTFOUND");
            return Results.status(404, error);
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


    @With(TimerAction.class)
    public Result listVegetarianRecipes(Http.Request request)
    {
        Messages messages = this.messagesApi.preferred(request);

        List<Recipe> recipes = Recipe.listVegetarian();

        if(recipes == null)
        {
            String error = messages.at("VEGNOTFOUND");
            return Results.status(404, error);
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


    @With(TimerAction.class)
    public Result updateRecipe(Http.Request request, String name) {
        // TODO Use Form
        JsonNode node = request.body().asJson();

        Messages messages = this.messagesApi.preferred(request);

        Form<Recipe> form = formFactory.form(Recipe.class).bindFromRequest(request);

        if(form.hasErrors()){
            return Results.badRequest(form.errorsAsJson());
        }

        Recipe recipe = Recipe.findByName(name);
        if (recipe == null)
        {
            String error = messages.at("RECIPENOTFOUND");
            return Results.status(404, error);

        } else {
            if(node.has("name"))
                recipe.setName(node.get("name").asText());

            /*if(Recipe.findByName(recipe.getName()) != null ){
                return Results.status(403, "Error: Duplicated Recipe");
            }*/

            if(node.has("vegetarian"))
                recipe.setVegetarian(node.get("vegetarian").asBoolean());
            if(node.has("nutritionalInformation")) {
                JsonNode node2 = node.path("nutritionalInformation");

                NutritionalInformation nut = new NutritionalInformation();
                if(node2.has("grams"))
                    nut.setGrams(node2.get("grams").asInt());

                if(node2.has("calories"))
                    nut.setCalories(node2.get("calories").asInt());
                if(node2.has("cholesterol"))
                    nut.setCholesterol(node2.get("cholesterol").asDouble());

                if(node2.has("protein"))
                    nut.setProtein(node2.get("protein").asDouble());
                if(node2.has("vitamins"))
                    nut.setVitamins(node2.get("vitamins").asText());

                recipe.setNutritionalInformation(nut);

                nut.save();
            }
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
    public Result deleteRecipe(String name, Http.Request request)
    {
        Messages messages = this.messagesApi.preferred(request);

        Recipe recipe = Recipe.findByName(name);
        if (recipe == null)
        {
            String error = messages.at("RECIPENOTFOUND");
            return Results.status(404, error);

        } else{
            recipe.refresh();
            Recipe.deleteRecipeWithDependencies(recipe);
            return Results.ok("The recipe "+ name + " was deleted");
        }

    }



}
