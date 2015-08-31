import java.util.HashMap;
// import java.util.ArrayList;
import java.util.List;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;



public class App {
  public static void main(String[] args) {
   staticFileLocation("/public");
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      //categories here can be anything as long as it matches $categories
      model.put("categories", Category.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //HTTP POST METHOD FOR SAVING CATEGORIES
    post("/categories", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      Category newCategory = new Category(name);
      newCategory.save();
      model.put("categories", Category.all());
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //HTTP GET METHOD FOR DISPLAYING SAVED CATEGORIES
    get("/categories/:id", (request, response) -> {
    response.redirect("/categories/" + request.params(":id") + "/tasks");
    return null;
  });

  //HTTP POST METHOD FOR SAVING TASKS TO CATEGORY
    put("/categories/:id/tasks", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Category category = Category.find(Integer.parseInt(request.params(":id")));
      String description = request.queryParams("description");
      Task newTask = new Task(description);
      // newTask.save();
      response.redirect("/categories/" + category.getId() + "/tasks");
      return null;
      // model.put("category", category);
      // model.put("task", newTask);
      // model.put("tasks", Task.allCategoryTasks(Integer.parseInt(request.params(":id"))));
      // model.put("template", "templates/category.vtl");
      // return new ModelAndView(model, layout);
    });
    // }, new VelocityTemplateEngine());

    //HTTP GET METHOD FOR DISPLAYING SAVED TASKS ON CATEGORY PAGE
    get("/categories/:id/tasks", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Category category = Category.find(Integer.parseInt(request.params(":id")));
      List<Task> tasks = Task.all();
      model.put("category", category);
      model.put("tasks", tasks);
      model.put("template", "templates/category.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  //HTTP GET/POST METHODS FOR UPDATING CATEGORY NAME
    get("categories/:id/update", (request, response) -> {
        HashMap<String, Object> model = new HashMap<String, Object>();
        Category category = Category.find(Integer.parseInt(request.params(":id")));
        model.put("category", category);
        model.put("template", "templates/category-update-form.vtl");
        return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

    post("categories/:id/update", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Category category = Category.find(Integer.parseInt(request.params(":id")));
      String name = request.queryParams("name");
      category.update(name);
      response.redirect("/categories/" + category.getId() + "/tasks");
      return null;
    });

    //HTTP GET/POST METHODS FOR UPDATING TASK DESCRIPTION
    get("/categories/:id/tasks/:id/update", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Task task = Task.find(Integer.parseInt(request.params(":id")));
      Category category = Category.find(Integer.parseInt(request.params(":id")));
      model.put("task", task);
      model.put("category", category);
      model.put("template", "templates/task-update-form.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    post("/categories/:id/tasks/:id/update", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Task task = Task.find(Integer.parseInt(request.params(":id")));
      Category category = Category.find(Integer.parseInt(request.params(":id")));
      String description = request.queryParams("description");
      task.update(description);
      response.redirect("/categories/" + category.getId() + "/tasks");
      return null;
    });

    //HTTP POST DELETE METHOD FOR CATEGORY
    post("/categories/:id/delete", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Category category = Category.find(Integer.parseInt(request.params(":id")));
      model.put("template", "templates/index.vtl");
      category.delete();
      model.put("categories", Category.all());
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    //HTTP POST DELETE METHOD FOR TASK
    post("/categories/:id/tasks/:id/delete", (request, response) -> {
      Category category = Category.find(Integer.parseInt(request.params(":id")));
      Task task = Task.find(Integer.parseInt(request.params(":id")));
      task.delete();
      response.redirect("/categories/" + category.getId() + "/tasks");
      return null;
    });

  }
}
