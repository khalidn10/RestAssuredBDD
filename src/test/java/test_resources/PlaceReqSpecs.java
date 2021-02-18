package test_resources;

import static io.restassured.RestAssured.given;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import java_resources.ExternalData;
import java_resources.Payloads;

public class PlaceReqSpecs {

	private static String baseUri = ExternalData.getGlobalData("baseUri");
	private static String[] keyQueryParam = {"key", "qaclick123"};
	private static ContentType contentType = ContentType.JSON;
	private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	private static LocalDateTime time;
	
	public static RequestSpecification placeReqSpec()
	{
		PrintStream log = null;
		try
		{
			log = new PrintStream(new FileOutputStream("logging.txt", true));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		time = LocalDateTime.now();
		log.append("\n******************************************************\n");
		log.append("***   Place Request Sent at: " + dtf.format(time).toString() + "   ***\n");
		log.append("******************************************************\n\n");
		
		// Create a generic RequestSpecification that can be used for all Place API requests (and not just for AddPlaceAPI)
		RequestSpecification placeReqSpec =	new RequestSpecBuilder()
												.addFilter(RequestLoggingFilter.logRequestTo(log))
												.addFilter(ResponseLoggingFilter.logResponseTo(log))
												.setBaseUri(baseUri)
												.addQueryParam(keyQueryParam[0], keyQueryParam[1])
												.setContentType(contentType)
												.build();
		
		return placeReqSpec;
	}
	
	public static RequestSpecification addPlaceReqSpec(double lat, int accuracy, String name)
	{
		RequestSpecification addPlaceReqSpec = given().spec(PlaceReqSpecs.placeReqSpec()).body(Payloads.addPlacePayload(lat, accuracy, name));
		return addPlaceReqSpec;
	}
	
	public static RequestSpecification getPlaceReqSpec(String place_id)
	{
		RequestSpecification getPlaceReqSpec = given().spec(PlaceReqSpecs.placeReqSpec()).queryParam("place_id", place_id);
		return getPlaceReqSpec;
	}
	
	public static RequestSpecification putPlaceReqSpec(String place_id, String address)
	{
		RequestSpecification putPlaceReqSpec = given().spec(PlaceReqSpecs.placeReqSpec()).body(Payloads.putPlacePayload(place_id, address, keyQueryParam[1]));
		return putPlaceReqSpec;
	}
	
	public static RequestSpecification deletePlaceReqSpec(String place_id)
	{
		RequestSpecification deletePlaceReqSpec = given().spec(PlaceReqSpecs.placeReqSpec()).body(Payloads.deletePlacePayload(place_id));
		return deletePlaceReqSpec;
	}
}
