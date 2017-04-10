import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    //homepage
    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("animals", Animal.all());
      model.put("endangeredAnimals", EndangeredAnimal.all());
      model.put("sightings", Sighting.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //add endangered animal sighting
    post("/endangered_sighting", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String rangerName = request.queryParams("rangerName");
      EndangeredAnimal endangeredAnimal = EndangeredAnimal.find(Integer.parseInt(request.queryParams("endangeredAnimalSelected")));
      String latLong = request.queryParams("latLong");
      try {
        Sighting sighting = new Sighting(endangeredAnimal.getId(), latLong, rangerName);
        sighting.save();
        String url = String.format("/endangered_animal/%d", endangeredAnimal.getId());
        response.redirect(url);
      } catch (IllegalArgumentException exception) {
        exception.getMessage();
        response.redirect("/error");
      }
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //add animal sighting
    post("/animal_sighting", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String rangerName = request.queryParams("rangerName");
      int animalIdSelected = Integer.parseInt(request.queryParams("animalSelected"));
      String latLong = request.queryParams("latLong");
      try {
        Sighting sighting = new Sighting(animalIdSelected, latLong, rangerName);
        sighting.save();
        String url = String.format("/animal/%d", animalIdSelected);
        response.redirect(url);
      } catch (IllegalArgumentException exception) {
        exception.getMessage();
        response.redirect("/error");
      }
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //add animal
    post("/animal/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      boolean endangered = request.queryParamsValues("endangered")!=null;
      String name = request.queryParams("name");
      try {
        if (endangered) {
          String health = request.queryParams("health");
          String age = request.queryParams("age");
          EndangeredAnimal endangeredAnimal = new EndangeredAnimal(name, health, age);
          endangeredAnimal.save();
        } else {
          Animal animal = new Animal(name);
          animal.save();
        }
        response.redirect("/");
      } catch (IllegalArgumentException exception) {
        exception.getMessage();
        response.redirect("/error");
      }
      return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

    //view animal
    get("/animal/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Animal animal = Animal.find(Integer.parseInt(request.params("id")));
      model.put("animal", animal);
      model.put("template", "templates/animal.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //view endangered animal
    get("/endangered_animal/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      EndangeredAnimal endangeredAnimal = EndangeredAnimal.find(Integer.parseInt(request.params("id")));
      model.put("endangeredAnimal", endangeredAnimal);
      model.put("template", "templates/endangered_animal.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //destroy sighting from endangered animal
    post("/endangered_animal/sighting/:sighting_id/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Sighting sighting = Sighting.find(Integer.parseInt(request.params("sighting_id")));
      EndangeredAnimal endangeredAnimal = EndangeredAnimal.find(sighting.getAnimalId());
      sighting.delete();
      String url = String.format("/endangered_animal/%d", endangeredAnimal.getId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //destroy sighting from animal
    post("/animal/sighting/:sighting_id/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Sighting sighting = Sighting.find(Integer.parseInt(request.params("sighting_id")));
      Animal animal = Animal.find(sighting.getAnimalId());
      sighting.delete();
      String url = String.format("/animal/%d", animal.getId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //destroy endangered animal
    post("/endangered_animal/:endangered_animal_id/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      EndangeredAnimal endangeredAnimal = EndangeredAnimal.find(Integer.parseInt(request.params("endangered_animal_id")));
      endangeredAnimal.delete();
      response.redirect("/");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //destroy animal
    post("/animal/:animal_id/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Animal animal = Animal.find(Integer.parseInt(request.params("animal_id")));
      animal.delete();
      response.redirect("/");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //update sighting from endangered animal
    post("/sighting/:sighting_id/update", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Sighting sighting = Sighting.find(Integer.parseInt(request.params("sighting_id")));
      int animalId = Integer.parseInt(request.queryParams("animalId"));
      String location = request.queryParams("location");
      String rangerName = request.queryParams("rangerName");
      sighting.update(animalId, location, rangerName);
      EndangeredAnimal endangeredAnimal = EndangeredAnimal.find(sighting.getAnimalId());
      String url = String.format("/endangered_animal/%d", endangeredAnimal.getId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //error page
    get("/error", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/error.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }
}
