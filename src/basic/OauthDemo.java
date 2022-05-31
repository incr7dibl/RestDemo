package basic;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.GetCourses;

import static io.restassured.RestAssured.*;

public class OauthDemo {
	@Test
	public void getCourse() {

		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\mohit\\eclipse-workspace\\RestDemo\\chromedriver.exe");

		WebDriver driver = new ChromeDriver();
		driver.get(
				"https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");

		waitFor(1000);
		driver.findElement(By.xpath("//input[@type='email']")).sendKeys("harshautomate@gmail.com");
		waitFor(1000);
		driver.findElement(By.xpath(
				"//button[@class='VfPpkd-LgbsSe VfPpkd-LgbsSe-OWXEXe-k8QpJ VfPpkd-LgbsSe-OWXEXe-dgl2Hf nCP5yc AjY5Oe DuMIQc qfvgSe qIypjc TrZEUc lw1w4b']"))
				.click();
		waitFor(5000);
		driver.findElement(By.xpath("//input[@type='password']")).sendKeys("RestAssured");
		waitFor(1000);
		driver.findElement(By.xpath(
				"//button[@class='VfPpkd-LgbsSe VfPpkd-LgbsSe-OWXEXe-k8QpJ VfPpkd-LgbsSe-OWXEXe-dgl2Hf nCP5yc AjY5Oe DuMIQc qfvgSe qIypjc TrZEUc lw1w4b']"))
				.click();
		waitFor(2000);
		String urlString = driver.getCurrentUrl();
		String partialCodeString = urlString.split("code=")[1];
		String codeString = partialCodeString.split("&scope=")[0];
//		System.out.println(codeString);
		String accesstokenResponseString = given().urlEncodingEnabled(false).queryParam("code", codeString)
				.queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
				.queryParam("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
				.queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
				.queryParam("grant_type", "authorization_code").when()
				.post("https://www.googleapis.com/oauth2/v4/token").asString();
		JsonPath js1 = new JsonPath(accesstokenResponseString);
		String accessToken = js1.getString("access_token");
//		System.out.println(accesstokenResponseString);

		GetCourses responseString = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
				.when().get("https://rahulshettyacademy.com/getCourse.php").as(GetCourses.class);
		System.out.println(responseString.getInstructor() + "\n" + responseString.getLinkedIn());
	}

	public void waitFor(int milliSeconds) {
		try {
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
