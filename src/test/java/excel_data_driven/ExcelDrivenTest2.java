package excel_data_driven;

import static io.restassured.RestAssured.given;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

public class ExcelDrivenTest2
{
	public static void main(String[] args)
	{
		RestAssured.baseURI = "http://216.10.245.166";
		String projectPath = System.getProperty("user.dir");
		
		FileInputStream bookIS = null;
		XSSFWorkbook bookWB = null;
		
		try
		{
			bookIS = new FileInputStream(projectPath + "/Books.xlsx");
		}
		catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		
		try
		{
			bookWB = new XSSFWorkbook(bookIS);
		}
		catch (IOException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		XSSFSheet bookDataSheet = bookWB.getSheet("Books");
		
		Iterator<Row> rows = bookDataSheet.rowIterator();
		Row currentRow = rows.next();
		Iterator<Cell> rowCells = currentRow.cellIterator();
		
		ArrayList<String> keys = new ArrayList<>();
		ArrayList<String> keyValues = new ArrayList<>();
		
		int rowNo = 1;
		int colNo = 1;
	
		while (rowCells.hasNext())
		{
			keys.add(stringValue(rowCells.next(), rowNo, colNo, bookWB));
			colNo++;
		}
		
		colNo = 1;
		
		while (rows.hasNext())
		{
			currentRow = rows.next();
			rowCells = currentRow.cellIterator();
			keyValues.clear();
			rowNo++;
			colNo = 1;
			
			while (rowCells.hasNext())
			{
				keyValues.add(stringValue(rowCells.next(), rowNo, colNo, bookWB));
				colNo++;
			}
			
			try
			{
				if (keyValues.size() > keys.size())
				{
					throw new Exception("Too many data values in row " + rowNo);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				try
				{
					bookWB.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
					System.exit(0);
				}
			}
			
			HashMap<String, Object> bookHashMap = new HashMap<>();
			
			for (int i = 0; i < keys.size(); i++)
			{
				bookHashMap.put(keys.get(i), keyValues.get(i));
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
			.body(bookHashMap)
			.when().post("Library/Addbook.php")
			.then().log().all().assertThat().statusCode(200).extract().response().asString();
		
			JsonPath addJson = new JsonPath(addBookResponse);
			String Message = addJson.getString("Msg");
			String ID = addJson.getString("ID");
			
			System.out.println("");
			System.out.println("Message:\t" + Message);
			System.out.println("Book ID:\t" + ID);
			System.out.println("");
		}
		
		try
		{
			bookWB.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	public static String stringValue(Cell c, int rowNo, int colNo, XSSFWorkbook bookWB)
	{
		CellType cType = c.getCellType();
		String stringCValue = "";
		
		if (cType.equals(CellType.FORMULA))
			cType = c.getCachedFormulaResultType();
		
		try
		{
			if (cType.equals(CellType.BLANK))
				stringCValue = "Empty Value";
			else if (cType.equals(CellType.STRING))
				stringCValue = c.getStringCellValue();
			else if (cType.equals(CellType.NUMERIC))
				stringCValue = NumberToTextConverter.toText(c.getNumericCellValue());
			else if (cType.equals(CellType.BOOLEAN))
				stringCValue = String.valueOf(c.getBooleanCellValue());
			else if (cType.equals(CellType.ERROR))
				throw new Exception("Row " + rowNo + ", column " + colNo + " contains an error");
			else
				throw new Exception("Row " + rowNo + ", column " + colNo + " contains an unknown value");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				bookWB.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
				System.exit(0);
			}
		}
		
		return stringCValue;
	}
}
