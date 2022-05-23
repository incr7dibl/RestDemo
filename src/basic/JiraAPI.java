package basic;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class JiraAPI {

	@Test
	public void addComment() {

		RestAssured.baseURI = "http://localhost:8081";
		SessionFilter sessionFilter = new SessionFilter(); // Use it to store session
		// Cookie based authentication
		given().filter(sessionFilter).header("Content-Type", "application/json")
				.body("{ \"username\": \"harsh\", \"password\": \"Infy@123\" }").when().post("rest/auth/1/session")
				.then().log().all().assertThat().statusCode(200).extract().response().asString();
		// Adding Comments
		given().log().all().header("Content-Type", "application/json").filter(sessionFilter).pathParam("id", "10000")
				.body("{\r\n" + "    \"body\": \"Added the Comment from Rest API Eclipse2\",\r\n"
						+ "    \"visibility\": {\r\n" + "        \"type\": \"role\",\r\n"
						+ "        \"value\": \"Administrators\"\r\n" + "    }\r\n" + "}")
				.when().post("rest/api/2/issue/{id}/comment").then().assertThat().statusCode(201).log().all();
	}
}
