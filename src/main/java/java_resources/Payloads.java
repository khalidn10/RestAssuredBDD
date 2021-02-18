package java_resources;

import java.util.ArrayList;
import java.util.List;

import pojo.AddPlace;
import pojo.DeletePlace;
import pojo.Location;
import pojo.PutPlace;

public class Payloads {

	public static AddPlace addPlacePayload(double lat, int accuracy, String name)
	{
		AddPlace addPlaceApi = new AddPlace();
		
		Location location = new Location();
		location.setLat(lat);
		location.setLng(33.427362);
		addPlaceApi.setLocation(location);
		
		addPlaceApi.setAccuracy(accuracy);
		addPlaceApi.setName(name);
		addPlaceApi.setPhone_number("(+91) 983 893 3937");
		addPlaceApi.setAddress("29, side layout, cohen 09");
		
		List<String> typesList = new ArrayList<String>();
		typesList.add("shoe park");
		typesList.add("shop");
		addPlaceApi.setTypes(typesList);
		
		addPlaceApi.setWebsite("http://google.com");
		addPlaceApi.setLanguage("French-IN");
		
		return addPlaceApi;
	}
	
	public static PutPlace putPlacePayload(String place_id, String address, String key)
	{
		PutPlace putPlaceApi = new PutPlace();
		
		putPlaceApi.setPlace_id(place_id);
		putPlaceApi.setAddress(address);
		putPlaceApi.setKey(key);
		
		return putPlaceApi;
	}
	
	public static DeletePlace deletePlacePayload(String place_id)
	{
		DeletePlace deletePlaceApi = new DeletePlace();
		
		deletePlaceApi.setPlace_id(place_id);
		
		return deletePlaceApi;
	}
}
