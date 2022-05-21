package basic;

import org.testng.annotations.Test;

import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {
	public static void main(String[] args) {
//		1. Print No of courses returned by API
//
//		2.Print Purchase Amount
//
//		3. Print Title of the first course
//
//		4. Print All course titles and their respective Prices
//
//		5. Print no of copies sold by RPA Course
//
//		6. Verify if Sum of all Course prices matches with Purchase Amount

		JsonPath js1 = new JsonPath(Payload.CoursePrice());
		// 2.Print Purchase Amount
		System.out.println(js1.getInt("dashboard.purchaseAmount"));
		// 1. Print No of courses returned by API
		System.out.println("\nPrint No of courses returned by API`");
		System.out.println(js1.getString("courses.size()"));
		int totalPrice = 0;

		// Print All course titles and their respective Prices
		System.out.println("\nPrint All course titles and their respective Prices");
		for (int i = 0; i < js1.getInt("courses.size()"); i++) {
			System.out.println("Course Title is " + js1.getString("courses[" + i + "].title") + " with Price "
					+ js1.getString("courses[" + i + "].price"));

		}
		// 5. Print no of copies sold by RPA Course
		System.out.println("\n5. Print no of copies sold by RPA Course");
		for (int i = 0; i < js1.getInt("courses.size()"); i++) {
			if (js1.getString("courses[" + i + "].title").equalsIgnoreCase("RPA")) {
				System.out.println("Course Title is " + js1.getString("courses[" + i + "].title") + " with Copies sold "
						+ js1.getString("courses[" + i + "].copies"));
			}

		}
		// 6. Verify if Sum of all Course prices matches with Purchase Amount
		
	}
	
}
