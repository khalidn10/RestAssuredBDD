package step_definitions;

import io.cucumber.java.Before;

public class Hooks {

	@Before("not (@OnlyAdd or (@DeletePlace and @OnlyGet))")
	public void addPlaceStub()
	{
		PlaceValStepDefinition stepDefs = new PlaceValStepDefinition();
		
		if (PlaceValStepDefinition.getPlaceId().isEmpty())
		{
			stepDefs.an_add_place_api_payload_with_data(1.383494, 1, "First Line House");
			stepDefs.request_is_sent_as_http_request("AddPlaceAPI", "Post");
		}
	}
}
