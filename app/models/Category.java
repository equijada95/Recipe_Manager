package models;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.List;

import io.ebean.*;
import io.ebean.annotation.EnumValue;

public enum Category
{
    @EnumValue("no category")
    NO_CATEGORY("no category"),

    @EnumValue("algae")
    ALGAE("algae"),

    @EnumValue("animal origin")
    ANIMAL_ORIGIN("animal origin"),

    @EnumValue("aromatized herbs")
    AROMATIZED_HERBS("aromatized herbs"),

    @EnumValue("bread")
    BREAD("bread"),

    @EnumValue("cereals")
    CEREALS("cereals"),

    @EnumValue("chocolate")
    CHOCOLATE("chocolate"),

    @EnumValue("cold cuts")
    COLD_CUTS("cold cuts"),

    @EnumValue("dried fruit")
    DRIED_FRUIT("dried fruit"),

    @EnumValue("drinks")
    DRINKS("drinks"),

    @EnumValue("egg")
    EGG("egg"),

    @EnumValue("fish")
    FISH("fish"),

    @EnumValue("fruit")
    FRUIT("fruit"),

    @EnumValue("legumes")
    LEGUMES("legumes"),

    @EnumValue("meats")
    MEATS("meats"),

    @EnumValue("milk or derivated")
    MILK("milk or derivated"),

    @EnumValue("molluscs")
    MOLLUSCS("molluscs"),

    @EnumValue("mushrooms")
    MUSHROOMS("mushrooms"),

    @EnumValue("offal")
    OFFAL("offal"),

    @EnumValue("oils")
    OILS("oils"),

    @EnumValue("pasta")
    PASTA("pasta"),

    @EnumValue("prepared")
    PREPARED("prepared"),

    @EnumValue("sauce")
    SAUCE("sauce"),

    @EnumValue("spice")
    SPICE("spice"),

    @EnumValue("shellfish")
    SHELLFISH("shellfish"),

    @EnumValue("vegetables")
    VEGETABLES("vegetables");

    private final String name;

    private Category(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /* @OneToMany(cascade= CascadeType.ALL, mappedBy="category")
    public List<Ingredient> ingredients;*/
}



