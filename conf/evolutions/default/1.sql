# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table ingredient (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  category                      varchar(17),
  with_gluten                   boolean default false not null,
  allergy                       boolean default false not null,
  shop_id                       bigint,
  constraint ck_ingredient_category check ( category in ('no category','algae','animal origin','aromatized herbs','bread','cereals','chocolate','cold cuts','dried fruit','drinks','egg','fish','fruit','legumes','meats','milk or derivated','molluscs','mushrooms','offal','oils','pasta','prepared','sauce','spice','shellfish','vegetables')),
  constraint pk_ingredient primary key (id)
);

create table nutritional_information (
  id                            bigint auto_increment not null,
  grams                         integer not null,
  calories                      integer not null,
  cholesterol                   double,
  protein                       varchar(255),
  vitamins                      varchar(255),
  constraint pk_nutritional_information primary key (id)
);

create table recipe (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  is_vegetarian                 boolean default false not null,
  nutritional_information_id    bigint,
  constraint uq_recipe_nutritional_information_id unique (nutritional_information_id),
  constraint pk_recipe primary key (id)
);

create table recipe_ingredient (
  recipe_id                     bigint not null,
  ingredient_id                 bigint not null,
  constraint pk_recipe_ingredient primary key (recipe_id,ingredient_id)
);

create table shop (
  id                            bigint auto_increment not null,
  noun                          varchar(255),
  email                         varchar(255),
  constraint pk_shop primary key (id)
);

create index ix_ingredient_shop_id on ingredient (shop_id);
alter table ingredient add constraint fk_ingredient_shop_id foreign key (shop_id) references shop (id) on delete restrict on update restrict;

alter table recipe add constraint fk_recipe_nutritional_information_id foreign key (nutritional_information_id) references nutritional_information (id) on delete restrict on update restrict;

create index ix_recipe_ingredient_recipe on recipe_ingredient (recipe_id);
alter table recipe_ingredient add constraint fk_recipe_ingredient_recipe foreign key (recipe_id) references recipe (id) on delete restrict on update restrict;

create index ix_recipe_ingredient_ingredient on recipe_ingredient (ingredient_id);
alter table recipe_ingredient add constraint fk_recipe_ingredient_ingredient foreign key (ingredient_id) references ingredient (id) on delete restrict on update restrict;


# --- !Downs

alter table ingredient drop constraint if exists fk_ingredient_shop_id;
drop index if exists ix_ingredient_shop_id;

alter table recipe drop constraint if exists fk_recipe_nutritional_information_id;

alter table recipe_ingredient drop constraint if exists fk_recipe_ingredient_recipe;
drop index if exists ix_recipe_ingredient_recipe;

alter table recipe_ingredient drop constraint if exists fk_recipe_ingredient_ingredient;
drop index if exists ix_recipe_ingredient_ingredient;

drop table if exists ingredient;

drop table if exists nutritional_information;

drop table if exists recipe;

drop table if exists recipe_ingredient;

drop table if exists shop;

