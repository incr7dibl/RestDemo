package basic;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

import java.io.File;

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

		given().header("X-Atlassian-Token", "no-check").pathParam("key", 10000).filter(sessionFilter)
				.multiPart("file", new File("Jira.txt")).header("Content-Type", "multipart/form-data").when()
				.post("rest/api/2/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);
	}

	@Test
	public void addAndViewComment() {
		RestAssured.baseURI = "http://localhost:8081";
		SessionFilter sessionFilter = new SessionFilter(); // Use it to store session
		// Cookie based authentication
		given().relaxedHTTPSValidation().filter(sessionFilter).header("Content-Type", "application/json")
				.body("{ \"username\": \"harsh\", \"password\": \"Infy@123\" }").when().post("rest/auth/1/session")
				.then().assertThat().statusCode(200).extract().response().asString();
		String expectedComment = "New comment added for E2E testing";
		// Adding Comments
		String addedComment = given().header("Content-Type", "application/json").filter(sessionFilter)
				.pathParam("id", "10000")
				.body("{\r\n" + "    \"body\": \"" + expectedComment + "\",\r\n" + "    \"visibility\": {\r\n"
						+ "        \"type\": \"role\",\r\n" + "        \"value\": \"Administrators\"\r\n" + "    }\r\n"
						+ "}")
				.when().post("rest/api/2/issue/{id}/comment").then().assertThat().statusCode(201).extract().asString();
		JsonPath js1 = new JsonPath(addedComment);
		int newCommentId = js1.getInt("id");
		System.out.println(newCommentId);

		String issueResponseString = given().pathParam("id", "10000").queryParam("fields", "comment")
				.filter(sessionFilter).when().get("rest/api/2/issue/{id}").then().extract().asString();
		JsonPath js2 = new JsonPath(issueResponseString);
		System.out.println(js2.getInt("fields.comment.comments.size()"));
		for (int i = 0; i < js2.getInt("fields.comment.comments.size()"); i++) {
			// System.out.println(js2.getString("fields.comment.comments[" + i + "].body"));
			if (js2.getInt("fields.comment.comments[" + i + "].id") == newCommentId) {

				Assert.assertEquals(js2.getString("fields.comment.comments[" + i + "].body"), expectedComment);
			}
		}
		System.out.println(js2);

	}
}
