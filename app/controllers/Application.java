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

//    public Result signup() {
//        return ok(signup.render("Signup"));
//    }
//
//    public Result addUsers() {
//        Users user;
//        user = Form.form(Users.class).bindFromRequest().get();
//        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
//        user.save();
//        return redirect(routes.Application.index());
//    }
//
//    public Result findUsers() {
//        DynamicForm requestData = Form.form().bindFromRequest();
//        String firstname = requestData.get("firstName");
//        String lastname = requestData.get("lastName");
//        String password = requestData.get("password");
//        Model.Finder<String, Users> finder = new Model.Finder<String, Users>(String.class, Users.class);
//        Users users = finder.where().eq("first_name", firstname).eq("last_name", lastname).findUnique();
//        String acPass = "";
//        try {
//            acPass = users.getPassword();
//        } catch (NullPointerException npe) {
//            // It's fine if findUser throws a NPE
//        }
//
//        if (BCrypt.checkpw(password, acPass))
//            return ok(" Hello " + firstname + " " + lastname + " " + password);
//        else
//            return ok("Error ");
//
//    }
    String salary;
    String HRA;
    String savings;
    String rent;
    String allowance;
    String source;
    String interest;
    String medical;

    int sources = 0;
    int interests= 0;
    int rentm = 0;
    int saving = 0;
    int bill = 0;


    @BodyParser.Of(BodyParser.Json.class)
    public Result calculate() throws JsonProcessingException {
        com.fasterxml.jackson.databind.node.ObjectNode result = Json.newObject();
        JsonNode json = request().body().asJson();
        Map<String, Object> map = new HashMap<>();
        Map<String, String> map1 = new HashMap<>();

            salary = json.findValues("contexts").get(0).findPath("income").findPath("income.original").asText();
            HRA = json.findValues("contexts").get(0).findPath("hra").findPath("hra.original").asText();
            savings = json.findValues("contexts").get(0).findPath("savings").findPath("savings.original").asText();
            rent = json.findValues("contexts").get(0).findPath("rent").findPath("number.original").asText();
            interest = json.findValues("contexts").get(0).findPath("interest").findPath("number.original").asText();
            allowance = json.findValues("contexts").get(0).findPath("Allowance_Related").findPath("number.original").asText();
            source = json.findValues("contexts").get(0).findPath("source_related").findPath("number.original").asText();
            medical = json.findValues("contexts").get(0).findPath("medical_related").findPath("number.original").asText();



        System.out.println("source " +source);
        System.out.println("interest " +interest);
        System.out.println("rent "+rent);
        System.out.println("allowance"+allowance);
        System.out.println("hra "+HRA);
        System.out.println("salary"+salary);
        System.out.println("savings"+savings);
        System.out.println("billS "+medical);

        rentm = Integer.parseInt(rent)*12;
        saving = Integer.parseInt(savings);
        bill = Integer.parseInt(medical);
        if (saving > 150000){
            saving = 150000;
        }
        if (bill > 15000){
            bill = 15000;
        }

        if (Integer.parseInt(HRA) >rentm){
            HRA = Integer.toString(rentm);
        }

        if (interest != ""){
            interests = Integer.parseInt(interest);
        }
        if (source != ""){
            sources = Integer.parseInt(source);
        }


        System.out.println(sources);
        System.out.println(interests);
        System.out.println(rent);
        System.out.println(allowance);
        System.out.println(HRA);
        System.out.println(savings);


        int ans = Integer.parseInt(salary)+sources - Integer.parseInt(HRA) - saving- interests-(Integer.parseInt(allowance))*12-250000;
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
String salaryTax;
String HRATax;
String savingsTax;
String rentTax;
String allowanceTax;
String sourceTax;
String interestTax;
String medicalTax;

int sourcesTax = 0;
int interestsTax= 0;
int rentmTax = 0;
int savingTax = 0;
int billTax = 0;


    @BodyParser.Of(BodyParser.Json.class)
    public Result calculateTax() throws JsonProcessingException {
        com.fasterxml.jackson.databind.node.ObjectNode result = Json.newObject();
        JsonNode json = request().body().asJson();
        Map<String, Object> map = new HashMap<>();
        Map<String, String> map1 = new HashMap<>();

        salaryTax = json.findValues("contexts").get(0).findPath("parameters").findPath("income").asText();
        HRATax = json.findValues("contexts").get(0).findPath("parameters").findPath("hra").asText();
        savingsTax = json.findValues("contexts").get(0).findPath("parameters").findPath("savings").asText();
        rentTax = json.findValues("contexts").get(0).findPath("parameters").findPath("rent").asText();
        interestTax = json.findValues("contexts").get(0).findPath("parameters").findPath("interest").asText();
        allowanceTax = json.findValues("contexts").get(0).findPath("parameters").findPath("allowance").asText();
        sourceTax = json.findValues("contexts").get(0).findPath("parameters").findPath("other").asText();
        medicalTax = json.findValues("contexts").get(0).findPath("parameters").findPath("medical").asText();



        System.out.println("source " +sourceTax);
        System.out.println("interest " +interestTax);
        System.out.println("rent "+rentTax);
        System.out.println("allowance"+allowanceTax);
        System.out.println("hra "+HRATax);
        System.out.println("salary"+salaryTax);
        System.out.println("savings"+savingsTax);
        System.out.println("billS "+medicalTax);

        rentmTax = Integer.parseInt(rentTax)*12;
        savingTax = Integer.parseInt(savingsTax);
        billTax = Integer.parseInt(medicalTax);
        if (savingTax > 150000){
            savingTax = 150000;
        }
        if (billTax > 15000){
            billTax = 15000;
        }

        if (Integer.parseInt(HRATax) >rentmTax){
            HRATax = Integer.toString(rentmTax);
        }

        if (interestTax != ""){
            interestsTax = Integer.parseInt(interestTax);
        }
        if (sourceTax != ""){
            sourcesTax = Integer.parseInt(sourceTax);
        }


        System.out.println(sourcesTax);
        System.out.println(interestsTax);
        System.out.println(rentTax);
        System.out.println(allowanceTax);
        System.out.println(HRATax);
        System.out.println(savingsTax);


        int ans = Integer.parseInt(salaryTax)+sourcesTax - Integer.parseInt(HRATax) - savingTax- interestsTax-(Integer.parseInt(allowanceTax))*12-250000;
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
            System.out.println(salaryTax);
            System.out.println(HRATax);
            System.out.println(savingsTax);

        }

        ObjectMapper mapper = new ObjectMapper();
        String obj = mapper.writeValueAsString(map1);
        System.out.println(map1);
        System.out.println(map1);
        System.out.println(Json.parse(obj));
        return ok(Json.parse(obj));
    }
}