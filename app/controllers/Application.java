package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Model;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.ir.ObjectNode;
import models.Users;
import org.h2.engine.User;
import org.mindrot.jbcrypt.BCrypt;
import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.*;

import views.html.*;

import java.lang.Integer;
import java.util.HashMap;
import java.util.Map;

public class Application extends Controller {

    public Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public Result signup() {
        return ok(signup.render("Signup"));
    }

    public Result addUsers() {
        Users user;
        user = Form.form(Users.class).bindFromRequest().get();
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.save();
        return redirect(routes.Application.index());
    }

    public Result findUsers() {
        DynamicForm requestData = Form.form().bindFromRequest();
        String firstname = requestData.get("firstName");
        String lastname = requestData.get("lastName");
        String password = requestData.get("password");
        Model.Finder<String, Users> finder = new Model.Finder<String, Users>(String.class, Users.class);
        Users users = finder.where().eq("first_name", firstname).eq("last_name", lastname).findUnique();
        String acPass = "";
        try {
            acPass = users.getPassword();
        } catch (NullPointerException npe) {
            // It's fine if findUser throws a NPE
        }

        if (BCrypt.checkpw(password, acPass))
            return ok(" Hello " + firstname + " " + lastname + " " + password);
        else
            return ok("Error ");

    }
    String salary;
    String HRA;
    String savings;

    @BodyParser.Of(BodyParser.Json.class)
    public Result calculate() throws JsonProcessingException {
        com.fasterxml.jackson.databind.node.ObjectNode result = Json.newObject();
        JsonNode json = request().body().asJson();
        Map<String, Object> map = new HashMap<>();
        Map<String, String> map1 = new HashMap<>();
//        if (salary==-1&& HRA==-1 && savings==-1) {
//            salary = json.findPath("contexts").findPath("parameters").findPath("income.original").asInt();
//            map1.put("speech", "Now tell me about ur HRA");
//            map1.put("source", "tax-calculator");
//            map1.put("displayText", "Now tell me about ur HRA");
//        }
//        else if (salary != -1 && HRA==-1 && savings == -1) {
//            HRA = json.findPath("contexts").findPath("parameters").findPath("income.original").asInt();
//            map1.put("speech", "Now tell me about ur savings");
//            map1.put("source", "tax-calculator");
//            map1.put("displayText", "Now tell me about ur savings");
//        }
//        else {
            salary = json.findValues("contexts").get(0).findPath("income").findPath("income.original").asText();
            HRA = json.findValues("contexts").get(0).findPath("hra").findPath("hra.original").asText();
            savings = json.findValues("contexts").get(0).findPath("savings").findPath("savings.original").asText();
            int ans = Integer.parseInt(salary) - Integer.parseInt(HRA) - Integer.parseInt(savings)-250000;

            if (ans<0){
                map1.put("speech", "Relax,No tax");
                map1.put("source", "tax-calculator");
                map1.put("displayText", "Relax,No tax");
            }
            else{
                if (ans<250000){
                    ans = ans/10;
                }
                else if (ans<500000){
                    ans = 25000+(ans-250000)/5;
                }
                else{
                    ans = 75000+(ans-500000)*3/10;
                }
            map1.put("speech", "your tax is "+ans);
            map1.put("source", "tax-calculator");
            map1.put("displayText", "your tax is " + ans);
            System.out.println(salary);
            System.out.println(HRA);
            System.out.println(savings);
            salary = "";
            HRA = "";
            savings = "";

        }

        ObjectMapper mapper = new ObjectMapper();
        String obj = mapper.writeValueAsString(map1);
        System.out.println(map1);
        System.out.println(map1);
        System.out.println(Json.parse(obj));
        return ok(Json.parse(obj));
    }
}

//    "fulfillment":
//
//    {
//        "speech":"Your tax is ans",
//            "source":"tax-calculator",
//            "displayText":"Your tax is ans"
//    }
//
//    @BodyParser.Of(BodyParser.Json.class)
//    public static Result authenticate() {
//        User user = null;
//        ObjectNode result = Json.newObject();
//        JsonNode json = request().body().asJson();
////		System.out.println("user login request " +json.toString());
//        logger.debug("user login request {}", json.toString());
//        String emailId = json.findPath("emailId").textValue();
//        String passwd = json.findPath("password").textValue();
//        if (emailId != null) {
//            emailId = emailId.trim();
//        }
//        if (passwd != null) {
//            passwd = passwd.trim();
//        }
//        if (emailId == null || "".equals(emailId
//
//    }
