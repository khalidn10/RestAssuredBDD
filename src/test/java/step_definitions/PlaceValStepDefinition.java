package step_definitions;

import static org.junit.Assert.*;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import test_resources.Utilities;
import test_resources.PlaceReqSpecs;
import test_resources.PlaceRespSpecs;

public class PlaceValStepDefinition {

	private RequestSpecification fullPlaceReqSpec = null;
	private Response placeResponse = null;
	private static String place_id = "";
	
	public static String getPlaceId()
	{
		return place_id;
	}
	
	@Given("a(n) valid/invalid AddPlaceAPI payload with data {double} {int} {string}")
	public void an_add_place_api_payload_with_data(double lat, int accuracy, String name)
	{
		// Given() part of AddPlaceAPI request
		fullPlaceReqSpec = PlaceReqSpecs.addPlaceReqSpec(lat, accuracy, name);
	}

	@When("{word} request is sent as {word} HTTP request")
	public void request_is_sent_as_http_request(String api, String requestType)
	{
		// When() part of request
		placeResponse = Utilities.getResource(fullPlaceReqSpec, api, requestType);
		
		// Set place_id using response from AddPlaceAPI request
		if (api.equalsIgnoreCase("AddPlaceAPI"))
		{
			place_id = Utilities.getResponseValue(placeResponse, "place_id");
		}
	}
	
	@Then("success/fail response is sent back with status code {int}")
	public void response_is_sent_back_with_status_code(int code) 
	{
	    // Then() part of request
		placeResponse = PlaceRespSpecs.applyRespSpec(placeResponse, code);
	}
	
	@Then("{word} value in response is {string}")
	public void value_in_response_is(String data, String expectedValue) 
	{
	    // Retrieve actual value from response
		String actualValue = Utilities.getResponseValue(placeResponse, data);
		
		// Check actual data value matches expected data value in response
		assertEquals(actualValue, expectedValue);
	}
	
	@Given("a valid GetPlaceAPI request")
	public void a_valid_get_place_api_request()
	{
		// Given() part of GetPlaceAPI request
		fullPlaceReqSpec = PlaceReqSpecs.getPlaceReqSpec(place_id);
	}
	
	@Then("place_id maps to {word} {string} using GetPlaceAPI")
	public void place_id_maps_to_data_using_get_place_api(String data, String expectedValue)
	{
		// Get data from GetPlaceAPI response
		String actualValue = Utilities.getResponseValue(placeResponse, data);
		
		// Check actual data value matches expected data value in response
		assertEquals(actualValue, expectedValue);
	}
	
	@Given("a(n) valid/invalid PutPlaceAPI payload with data {string}")
	public void a_put_place_api_payload_with_data(String address)
	{
	    // Given() part of PutPlaceAPI request
		fullPlaceReqSpec = PlaceReqSpecs.putPlaceReqSpec(place_id, address);
	}
	
	@Given("a valid DeletePlaceAPI request")
	public void a_valid_delete_place_api_request()
	{
		// Given() part of DeeletePlaceAPI request
		fullPlaceReqSpec = PlaceReqSpecs.deletePlaceReqSpec(place_id);
	}
}
