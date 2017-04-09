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
      Sighting sighting = new Sighting(endangeredAnimal.getId(), latLong, rangerName);
      sighting.save();
      String url = String.format("/endangered_animal/%d", endangeredAnimal.getId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //add animal sighting
    post("/animal_sighting", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      String rangerName = request.queryParams("rangerName");
      int animalIdSelected = Integer.parseInt(request.queryParams("animalSelected"));
      String latLong = request.queryParams("latLong");
      Sighting sighting = new Sighting(animalIdSelected, latLong, rangerName);
      sighting.save();
      String url = String.format("/animal/%d", animalIdSelected);
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/animal/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("animals", Animal.all());
      model.put("endangeredAnimals", EndangeredAnimal.all());
      model.put("template", "templates/animal-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/animal/new", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      boolean endangered = request.queryParamsValues("endangered")!=null;
      if (endangered) {
        String name = request.queryParams("name");
        String health = request.queryParams("health");
        String age = request.queryParams("age");
        EndangeredAnimal endangeredAnimal = new EndangeredAnimal(name, health, age);
        endangeredAnimal.save();
        model.put("animals", Animal.all());
        model.put("endangeredAnimals", EndangeredAnimal.all());
      } else {
        String name = request.queryParams("name");
        Animal animal = new Animal(name);
        animal.save();
        model.put("animals", Animal.all());
        model.put("endangeredAnimals", EndangeredAnimal.all());
      }
      response.redirect("/");
        return null;
      });

    get("/animal/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Animal animal = Animal.find(Integer.parseInt(request.params("id")));
      model.put("animal", animal);
      model.put("template", "templates/animal.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/endangered_animal/:id", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      EndangeredAnimal endangeredAnimal = EndangeredAnimal.find(Integer.parseInt(request.params("id")));
      model.put("endangeredAnimal", endangeredAnimal);
      model.put("template", "templates/endangered_animal.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("/error", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/error.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //remove sighting from endangered animal
    post("/endangered_animal/sighting/:sighting_id/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Sighting sighting = Sighting.find(Integer.parseInt(request.params("sighting_id")));
      EndangeredAnimal endangeredAnimal = EndangeredAnimal.find(sighting.getAnimalId());
      sighting.delete();
      String url = String.format("/endangered_animal/%d", endangeredAnimal.getId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //remove sighting from animal
    post("/animal/sighting/:sighting_id/delete", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Sighting sighting = Sighting.find(Integer.parseInt(request.params("sighting_id")));
      Animal animal = Animal.find(sighting.getAnimalId());
      sighting.delete();
      String url = String.format("/animal/%d", animal.getId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //update sighting from endangered animal
    post("/sighting/:sighting_id/update", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      Sighting sighting = Sighting.find(Integer.parseInt(request.params("sighting_id")));
      EndangeredAnimal endangeredAnimal = EndangeredAnimal.find(sighting.getAnimalId());

      String url = String.format("/endangered_animal/%d", endangeredAnimal.getId());
      response.redirect(url);
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());
  }
}
