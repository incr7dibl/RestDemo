package basic;

import io.restassured.RestAssured;
import io.restassured.internal.common.assertion.Assertion;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import static io.restassured.RestAssured.*;

public class Basic {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Validate that add place API is working correct
		System.out.println("\nPost Request\n");
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		String resposnString = given().log().all().queryParam("key", "qaclick123")
				.header("Content-Type", "application/json")
				.body("{\r\n" + "  \"location\": {\r\n" + "    \"lat\": -38.383494,\r\n" + "    \"lng\": 33.427362\r\n"
						+ "  },\r\n" + "  \"accuracy\": 50,\r\n" + "  \"name\": \"Frontline house\",\r\n"
						+ "  \"phone_number\": \"(+91) 983 893 3937\",\r\n"
						+ "  \"address\": \"29, side layout, cohen 09\",\r\n" + "  \"types\": [\r\n"
						+ "    \"shoe park\",\r\n" + "    \"shop\"\r\n" + "  ],\r\n"
						+ "  \"website\": \"://google.com\",\r\n" + "  \"language\": \"French-IN\"\r\n" + "}")
				.when().post("maps/api/place/add/json").then().assertThat().statusCode(200)
				.body("scope", equalTo("APP")).header("Server", "Apache/2.4.41 (Ubuntu)").extract().response()
				.asString();

		JsonPath jsonPath = new JsonPath(resposnString);
		String place_Id = jsonPath.getString("place_id");
		System.out.println("Added Place ID is " + place_Id + "\n");

		// Update an Address

		String newAddress = "HariOm Nagar, Kolhapur";
		System.out.println("\nPut Request\n");
		given().log().all().header("Content-Type", "application/json").queryParam("key", "qaclick123")
				.body("{\r\n" + "\"place_id\":\"" + place_Id + "\",\r\n" + "\"address\":\"" + newAddress + "\",\r\n"
						+ "\"key\":\"qaclick123\"\r\n" + "}")
				.when().put("maps/api/place/update/json/").then().log().all().assertThat().statusCode(200)
				.body("msg", equalTo("Address successfully updated"));

		// Get a Place
		System.out.println("\nGet Request\n");
		String getPlaceResponse = given().log().all().queryParam("place_id", place_Id).queryParam("key", "qaclick123")
				.when().get("maps/api/place/get/json").then().assertThat().log().all().statusCode(200).extract()
				.response().asString();
		JsonPath js1 = new JsonPath(getPlaceResponse);
		String actualAddress = js1.getString("address");
		System.out.println(actualAddress);
		Assert.assertEquals(newAddress, actualAddress);

	}

}
