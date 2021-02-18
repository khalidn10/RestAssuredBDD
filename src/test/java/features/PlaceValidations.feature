Feature: Validating Place API's

@AddPlace @OnlyAdd @Smoke
Scenario Outline: Validate AddPlaceAPI adds place (with scope check)
Given a valid AddPlaceAPI payload with data <lat> <accuracy> "<name>"
When AddPlaceAPI request is sent as Post HTTP request
Then success response is sent back with status code 200
And status value in response is "OK"
And scope value in response is "APP"

Examples:
|lat		|accuracy	|name				|
|18.383494	|10			|First Line House	|
#|28.383494	|20			|Second Line House	|

@AddPlace @OnlyGet @Smoke
Scenario Outline: Validate GetPlaceAPI retrieves place (following AddPlaceAPI)
Given a valid GetPlaceAPI request
When GetPlaceAPI request is sent as Get HTTP request
Then success response is sent back with status code 200
And place_id maps to name "<name>" using GetPlaceAPI

Examples:
|name				|
|First Line House	|

@PutPlace @OnlyPut
Scenario Outline: Validate PutPlaceAPI updates place
Given a valid PutPlaceAPI payload with data "<address>"
When PutPlaceAPI request is sent as Put HTTP request
Then success response is sent back with status code 200
And msg value in response is "Address successfully updated"

Examples:
|address			|
|1 Summer walk, USA	|

@PutPlace @OnlyGet
Scenario Outline: Validate GetPlaceAPI retrieves place (following PutPlaceAPI)
Given a valid GetPlaceAPI request
When GetPlaceAPI request is sent as Get HTTP request
Then success response is sent back with status code 200
And place_id maps to address "<address>" using GetPlaceAPI

Examples:
|address			|
|1 Summer walk, USA	|

@DeletePlace @OnlyDelete
Scenario: Validate DeletePlaceAPI deletes place
Given a valid DeletePlaceAPI request
When DeletePlaceAPI request is sent as Delete HTTP request
Then success response is sent back with status code 200
And status value in response is "OK"

@DeletePlace @OnlyGet
Scenario: Validate GetPlaceAPI does not retrieve place (following DeletePlaceAPI)
Given a valid GetPlaceAPI request
When GetPlaceAPI request is sent as Get HTTP request
Then fail response is sent back with status code 404
And msg value in response is "Get operation failed, looks like place_id  doesn't exists"

#Scenario Outline: Validate AddPlaceAPI adds place (without scope check)
#Given a valid AddPlaceAPI payload with data <lat> <accuracy> "<name>"
#When AddPlaceAPI request is sent as Post HTTP request
#Then success response is sent back with status code 200
#And status value in response is "OK"

#Examples:
#|lat		|accuracy	|name				|
#|58.383494	|50			|Third Line House	|