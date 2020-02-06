package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.Ingredient;
import models.NutritionalInformation;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import static play.libs.Json.toJson;

public class NutritionalController extends Controller {

    public Result findNutriotionalById(Http.Request request, Long id)
    {
        NutritionalInformation nut = NutritionalInformation.findById(id);
        if(nut == null)
        {
            return Results.status(404);
        }
        else if(request.accepts("application/json"))
        {
            JsonNode json = toJson(nut);
            return Results.ok(json);
        } else if(request.accepts("application/xml"))
        {
            return Results.ok(views.xml.nut.render(nut));
        } else
        {
            return Results.status(415);
        }
    }
}
