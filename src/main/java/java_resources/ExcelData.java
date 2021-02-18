package java_resources;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelData {
	
	// Field to keep track of current column no. (in order to return row containing error/issue in spreadsheet)
	private static int colNo = 1;
	
	// return type is a list of string lists
	// testCaseName is the required test case name in the first column of spreadsheet
	public static ArrayList<ArrayList<String>> getExcelData(String excelFileName, String worksheetName, String testCaseName) throws Exception
	{
		String projectPath = System.getProperty("user.dir");
	
		FileInputStream bookIS = new FileInputStream(projectPath + "/" + excelFileName);
		XSSFWorkbook bookWB = new XSSFWorkbook(bookIS);
		XSSFSheet bookDS = bookWB.getSheet(worksheetName);
		
		Iterator<Row> rows = bookDS.rowIterator();
		Row currentRow = rows.next();
		// Variable to keep track of row number (in order to return row containing error/issue in spreadsheet)
		int rowNo = 1;
		Iterator<Cell> rowCells = currentRow.cellIterator();
	
		ArrayList<ArrayList<String>> excelRecords = new ArrayList<>();
		ArrayList<String> excelRecord = new ArrayList<>();

		rowCells.next();
		excelRecord = getExcelRecord(rowCells, rowNo, bookWB);
		excelRecords.add(excelRecord);
		// First row of spreadsheet contains data names and so the no. of data values should be the same in spreadsheet
		int noOfDataFields = excelRecord.size();
		
		while (rows.hasNext())
		{
			currentRow = rows.next();
			rowNo++; // Increment row no. 
			rowCells = currentRow.cellIterator();
			colNo = 1; // Reset colNo to 1 (as starting new row)
			String testCase = stringValue(rowCells.next(), rowNo, bookWB);
			
			// if first column of row matches required testCaseName
			if (testCase.equalsIgnoreCase(testCaseName))
			{
				// Add row into a list
				excelRecord = getExcelRecord(rowCells, rowNo, bookWB);
				
				// If fields in record is more than number of data names in first row of spreadsheet
				if (excelRecord.size() > noOfDataFields)
				{
					// close bookWB and throw error
					bookWB.close();
					throw new Exception("Too many data values in row " + rowNo);
				}
				else
				{
					// otherwise add list to list of lists
					excelRecords.add(excelRecord);
				}
			}
		}
		
		bookWB.close();
		
		return excelRecords; // return list of lists
	}
	
	// method to add row data into an array list and return it
	private static ArrayList<String> getExcelRecord(Iterator<Cell> rowCells, int rNo, XSSFWorkbook bookWB) throws Exception
	{
		ArrayList<String> excelRecord = new ArrayList<>();
		
		while (rowCells.hasNext())
		{
			colNo++;
			excelRecord.add(stringValue(rowCells.next(), rNo, bookWB));
		}
		
		return excelRecord;
	}
	
	// method to store cell values as strings
	private static String stringValue(Cell c, int rowNo, XSSFWorkbook bookWB) throws Exception
	{
		CellType cType = c.getCellType();
		String stringCValue = "";
		
		if (cType.equals(CellType.FORMULA))
			cType = c.getCachedFormulaResultType();
		
		if (cType.equals(CellType.BLANK))
			stringCValue = "Empty Value";
		else if (cType.equals(CellType.STRING))
			stringCValue = c.getStringCellValue();
		else if (cType.equals(CellType.NUMERIC))
			stringCValue = NumberToTextConverter.toText(c.getNumericCellValue());
		else if (cType.equals(CellType.BOOLEAN))
			stringCValue = String.valueOf(c.getBooleanCellValue());
		else if (cType.equals(CellType.ERROR))
		{
			bookWB.close();
			throw new Exception("Row " + rowNo + ", column " + colNo + " contains an error");
		}
		else
		{
			bookWB.close();
			throw new Exception("Row " + rowNo + ", column " + colNo + " contains an unknown value");
		}
		
		return stringCValue;
	}
}