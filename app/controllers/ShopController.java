package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Recipe;
import models.Shop;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import static play.libs.Json.toJson;

public class ShopController extends Controller {

    public Result findShopById(Http.Request request, Long id)
    {
        Shop shop = Shop.findById(id);
        if(shop == null)
        {
            return Results.status(404);
        }
        else if(request.accepts("application/json"))
        {
            JsonNode json = toJson(shop);
            return Results.ok(json);
        } /*else if(request.accepts("application/xml"))
        {
            return Results.ok(views.xml.recipe.render(recipe));
        } */else
        {
            return Results.status(415);
        }
    }

    public Result findShopByName(Http.Request request, String name)
    {
        Shop shop = Shop.findByNoun(name);
        if(shop == null)
        {
            return Results.status(404);
        }
        else if(request.accepts("application/json"))
        {
            JsonNode json = toJson(shop);
            return Results.ok(json);
        } else if(request.accepts("application/xml"))
        {
            return Results.ok(views.xml.shop.render(shop));
        } else
        {
            return Results.status(415);
        }
    }
}
