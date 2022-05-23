package basic;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

public class DyanamicJson {

	@Test(dataProvider = "addbook")
	public void addBook(String isbn, String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";
		String responseString = given().log().all().header("Content-Type", "application/json")
				.body(Payload.addBook(isbn, aisle)).when().post("Library/Addbook.php").then().log().all().assertThat()
				.statusCode(200).extract().response().asString();

		JsonPath js1 = new JsonPath(responseString);

		System.out.println("Added book ID is :" + js1.getString("ID"));
		String bookIdString = js1.getString("ID");
		given().log().all().header("Content-Type", "application/json").body(Payload.deleteBook(bookIdString)).when()
				.post("Library/DeleteBook.php").then().log().all().assertThat().statusCode(200);
	}

	@DataProvider(name = "addbook")
	public Object[][] getData() {

		return new Object[][] { { "ABCDE", "12845" }, { "QWERTY", "09876" }, { "ASDFG", "56325" } };
	}
}
