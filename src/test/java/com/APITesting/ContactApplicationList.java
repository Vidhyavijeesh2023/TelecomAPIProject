package com.APITesting;

import static io.restassured.RestAssured.given;

import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.response.Response;


public class ContactApplicationList {
  String token1;
  String email;
  String UpdatedPswd;
  String id;
  @Test(priority=1)
  public void AddNewUserProfile() {
	  System.out.println("-------------Create New User-------------------");
	  //customized email which gets changed on each run
	  email="Vidhyasooriya"+System.currentTimeMillis()+"@gmail.com";
	  
	  
	 //payload
	  HashMap<String, Object> payload= new HashMap<String, Object>();
	  payload.put("firstName", "Vidhya");
	  payload.put("lastName", "Ganeshan");
	  payload.put("email", email);
	  payload.put("password", "Vidhya@123");
	  
	  //response for the payload
	 Response response= given().
			       header("Content-Type", "application/json").
			       body(payload)
	               .when().post("https://thinking-tester-contact-list.herokuapp.com/users");
	 System.out.println("Response code is: "+response.getStatusCode());
	
	 response.then().log().body();
	 //Assertion to test the response is correct or not
	 Assert.assertEquals(response.getStatusCode(), 201);
	 System.out.println("Status code matched with expected code!");
	    //read token from the response
	 token1=response.jsonPath().getString("token");
	 System.out.println(token1);  
	 System.out.println(" Status code: "+response.statusCode());
  }
  
  @Test(priority=2,dependsOnMethods="AddNewUserProfile")
  public void GetUserProfile() {
	  
	  System.out.println("------Get profile request --------------");
	  Response response=given().header("Authorization","Bearer "+token1)
	  .when().get("https://thinking-tester-contact-list.herokuapp.com/users/me");
	  response.then().log().body();
	  Assert.assertEquals(response.getStatusCode(), 200);
	  System.out.println("Status code matched with expected code!");
	  System.out.println(" Status code: "+response.statusCode());
  }
  
  @Test(priority=3)
  public void UpdateUserProfile() {
	  System.out.println("--------Update password--------------");
	UpdatedPswd="Vidhya@1994";
	  
	  HashMap<String,Object> updatedPayload=new HashMap<String,Object>();
	  updatedPayload.put("password", UpdatedPswd);
	  
	  Response response=given()
			  .header("Content-Type", "application/json")
			  .header("Authorization", "Bearer " + token1)
			  .body(updatedPayload)
			  .when()
			  .patch("https://thinking-tester-contact-list.herokuapp.com/users/me");
	  
	  response.then().log().body();
	  Assert.assertEquals(response.getStatusCode(), 200);
	  System.out.println(" Status code: "+response.statusCode());
  }
  
  @Test(priority=4)
  public void LoginUser() {
	System.out.println("--------Login using the mail id : "+email+" and password : "+UpdatedPswd+" -----------");
	HashMap<String,Object> loginPayload=new HashMap<String,Object>();
	loginPayload.put("email", email);
	loginPayload.put("password", UpdatedPswd);
	Response response = given()
            .header("Content-Type", "application/json")
            .body(loginPayload)
            .when()
            .post("https://thinking-tester-contact-list.herokuapp.com/users/login");
	
	response.then().log().body();
	Assert.assertEquals(response.getStatusCode(), 200);
	
	System.out.println(" Status code: "+response.statusCode());
  }
  
  @Test(priority=5)
  public void AddContact() {
	System.out.println("---------Add details of the user-------");
	HashMap<String,Object> ContactPayload=new HashMap<String,Object>();
	ContactPayload.put("firstName", "Vidhya");
	ContactPayload.put("lastName", "Ganeshan");
	ContactPayload.put("birthdate", "1994-01-23");
	ContactPayload.put("email", email);
	ContactPayload.put("phone", "9865376059");
	ContactPayload.put("street1", "345-NehruNagar");
	ContactPayload.put("street2", "KR Puram");
	ContactPayload.put("city", "Bangalore");
	ContactPayload.put("stateProvince", "Karnataka");
	ContactPayload.put("postalCode", "645671");
	ContactPayload.put("country", "India");
	Response response = given()
            .header("Content-Type", "application/json")
            .header("Accept","application/json")
            .header("Authorization", "Bearer " + token1)
            .body(ContactPayload)
            .when()
            .post("https://thinking-tester-contact-list.herokuapp.com/contacts");
	response.then().log().body();
	Assert.assertEquals(response.getStatusCode(), 201);
	System.out.println("The status code is matched with the expected one");
	id=response.jsonPath().getString("_id");
	System.out.println(" Status code: "+response.statusCode());
  }
  @Test(priority=6)
  public void fetchcontactDetails() {
	  System.out.println("------------fetch the contact details of the user-------------------");
	  Response response=given()
			  .header("Authorization", "Bearer " + token1)
			  .when()
			  .get("https://thinking-tester-contact-list.herokuapp.com/contacts");
	  response.then().log().body();
	  System.out.println("Contact list with code: "+response.statusCode());
			  
  }
  @Test(priority=7)
  public void fetchcontact() {
	  System.out.println("------------fetch the contact details-------------------");
	  Response response=given()
			  .header("Authorization", "Bearer " + token1)
			  .when()
			  .get("https://thinking-tester-contact-list.herokuapp.com/contacts/");
	  response.then().log().body();
	  System.out.println("Contact list with code: "+response.statusCode());
			  
  }
  
  @Test(priority=8)
  public void UpdateEmail() {
	  System.out.println("------------update the email-------------------");
	  HashMap<String,Object> ContactPayload=new HashMap<String,Object>();
		ContactPayload.put("firstName", "Vidhya");
		ContactPayload.put("lastName", "Ganeshan");
		ContactPayload.put("birthdate", "1994-01-23");
		ContactPayload.put("email", "Vidhyaviju@gmail.com");
		ContactPayload.put("phone", "9865376059");
		ContactPayload.put("street1", "345-NehruNagar");
		ContactPayload.put("street2", "KR Puram");
		ContactPayload.put("city", "Bangalore");
		ContactPayload.put("stateProvince", "Karnataka");
		ContactPayload.put("postalCode", "645671");
		ContactPayload.put("country", "India");
	  Response response=given()                                                                                                                      
			   .header("Content-Type","application/json")
			  .header("Accept","application/json")
			  .header("Authorization", "Bearer " + token1).body(ContactPayload)
			  .when()
			  .put("https://thinking-tester-contact-list.herokuapp.com/contacts/"+id);
	  response.then().log().body();
	  System.out.println("Contact list with code: "+response.statusCode());
			
  }
  @Test(priority=9)
  public void UpdatecontactPatch() {
	  System.out.println("------------update the email-------------------");
	  HashMap<String,Object> ContactPayload=new HashMap<String,Object>();
		ContactPayload.put("firstName", "Vishali");
		ContactPayload.put("lastName", "Krishnan");
		
	  Response response=given()                                                                                                                      
			   .header("Content-Type","application/json")
			  .header("Accept","application/json")
			  .header("Authorization", "Bearer " + token1).body(ContactPayload)
			  .when()
			  .patch("https://thinking-tester-contact-list.herokuapp.com/contacts/"+id);
	  response.then().log().body();
	  System.out.println("StatusCode is : "+response.statusCode());
	  System.out.println("First name & Last name is updated");
  }
  
  @Test(priority=10)
  public void Logout() {
	  System.out.println("------------logout user-------------------");
		
	  Response response=given()                                                                                                                      
			   .header("Content-Type","application/json")
			  .header("Accept","application/json")
			  .header("Authorization", "Bearer " + token1)
			  .when()
			  .post("https://thinking-tester-contact-list.herokuapp.com/users/logout");
	  response.then().log().body();
	  System.out.println("StatusCode is : "+response.statusCode());
	  System.out.println("user logged out successfully");
  }
  
}
