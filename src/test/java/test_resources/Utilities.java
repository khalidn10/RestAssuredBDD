package test_resources;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java_resources.APIResources;

public class Utilities {

	public static Response getResource(RequestSpecification reqSpec, String api, String requestType)
	{
		// Constructor will be called with the value of the resource you pass
		APIResources resource = APIResources.valueOf(api);
		Response placeResponse = null;
		
		switch (requestType.toLowerCase())
		{
			case "post":
				placeResponse = reqSpec.when().post(resource.getResource());
				break;
			case "get":
				placeResponse = reqSpec.when().get(resource.getResource());
				break;
			case "put":
				placeResponse = reqSpec.when().put(resource.getResource());
				break;
			case "delete":
				placeResponse = reqSpec.when().delete(resource.getResource());
				break;
		}
		
		return placeResponse;
	}
	
	public static String getJsonValue(String json, String data)
	{
		JsonPath respJson = new JsonPath(json);
		String value = respJson.getString(data);
		return value;
	}
	
	public static String getResponseValue(Response resp, String data)
	{
		return getJsonValue(resp.asString(), data);
	}
}
