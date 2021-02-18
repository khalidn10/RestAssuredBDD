package excel_data_driven;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;
import java.util.HashMap;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import java_resources.ExcelData;

public class ExcelDrivenTest
{
	public static void main(String[] args) throws Exception
	{
		RestAssured.baseURI = "http://216.10.245.166";
		ArrayList<ArrayList<String>> dataRecords = ExcelData.getExcelData("Books.xlsx", "Books", "Rest Assured");
		HashMap<String, Object> addBookHashMap = new HashMap<>();
		
		for (int i = 1; i < dataRecords.size(); i++)
		{
			for (int j = 0; j < dataRecords.get(0).size(); j++)
			{
				addBookHashMap.put(dataRecords.get(0).get(j), dataRecords.get(i).get(j));
			}
	
			/* Google search on 'rest assured hashmap'
			 * Select the wiki/usage result on GitHub
			 * Scroll down to section 'Create JSON from a HashMap'
			 * Use the example to create key/value pair entries in a HashMap
			 * Use HashMap in the body() method
			 */
			
			/*HashMap<String, Object>  mainMap = new HashMap<>();
			mainMap.put("name", "Learn Appium Automation with Java");
			mainMap.put("isbn", "abc");
			mainMap.put("aisle", "1000053");
			mainMap.put("author", "John foe");*/
		
			/* Use another (nested) HashMap if you have a nested Json:
			 * HashMap<String, Object>  childLocationMap = new HashMap<>();
			 * childLocationMap.put("lat", "12");
			 * childLocationMap.put("lng", "34");
			 * mainMap.put("location", childLocationMap);
			 * 
			 * If location contained an array of lat/lng locations, then you would do the following:
			 * List<HashMap<String, Object>> childLocationList = new ArrayList<>();
			 * childLocationList.add(childLocationMap);
			 * mainMap.put("location", childLocationList);
			 */

			String addBookResponse = given().log().all().contentType(ContentType.JSON)
			/*.body("{\r\n" + 
					"\"name\":\"Learn Appium Automation with Java\",\r\n" + 
					"\"isbn\":\"abc\",\r\n" + 
					"\"aisle\":\"1000052\",\r\n" + 
					"\"author\":\"John foe\"\r\n" + 
					"}")*/
			.body(addBookHashMap)
			.when().post("Library/Addbook.php")
			.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
			JsonPath addJson = new JsonPath(addBookResponse);
			String Message = addJson.getString("Msg");
			String ID = addJson.getString("ID");
			
			System.out.println("");
			System.out.println("Message:\t" + Message);
			System.out.println("Book ID:\t" + ID);
			System.out.println("");
			
			HashMap<String, Object> deleteBookHashMap = new HashMap<>();
			deleteBookHashMap.put("ID", ID);
			
			given().log().all().contentType(ContentType.JSON)
			.body(deleteBookHashMap)
			.when().delete("Library/DeleteBook.php")
			.then().log().all().assertThat().statusCode(200);
		}
	}
}
