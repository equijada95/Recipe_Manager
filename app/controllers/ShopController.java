package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Shop;
import play.i18n.Messages;
import play.i18n.MessagesApi;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;

import static play.libs.Json.toJson;

public class ShopController extends Controller {

    @Inject
    MessagesApi messagesApi;

    public Result findShopById(Http.Request request, Long id)
    {
        Messages messages = this.messagesApi.preferred(request);

        Shop shop = Shop.findById(id);
        if(shop == null)
        {
            String error = messages.at("SHOPNOTFOUND");
            return Results.status(404, error);
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

    public Result findShopByName(Http.Request request, String name)
    {
        Messages messages = this.messagesApi.preferred(request);

        Shop shop = Shop.findByNoun(name);
        if(shop == null)
        {
            String error = messages.at("SHOPNOTFOUND");
            return Results.status(404, error);
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
