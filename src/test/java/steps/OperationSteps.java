package steps;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.response.ResponseOptions;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import java.io.File;
import java.net.URISyntaxException;
import java.util.List;


public class OperationSteps {

    private static ResponseOptions<Response> response;
    public ResponseBody body;
    public JsonPath jsonPathEvaluator;
    public List<String> ResponseActualValueFromList;
    public Response actualResponse;
    private Scenario scenario;
    public RequestSpecification httpRequest;
    Headers headers=null;
   // String New_Fixture_Id = "4";
    @Before
    public void before(Scenario scenario){
        this.scenario = scenario;
    }

    @Given("I perform GET operation {string}")
    public void iPerformGETOperation(String endpoint) throws URISyntaxException {
        RestAssured.baseURI = "http://localhost:3000/"+endpoint;
        httpRequest = RestAssured.given();
        final Header header = new Header("Content-Type", "application/json");
        headers = new Headers(header);
        httpRequest.headers(headers);
        actualResponse = httpRequest
                            .relaxedHTTPSValidation()
                            .when()
                            .get();
        body = actualResponse.getBody();
        scenario.log("Response Body is: " + body.prettyPrint());
    }

    @Then("I should see the {string} and fixtureId_count {string} with the return object")
    public void iShouldSeeTheAndFixtureId_countWithTheReturnObject(String fixtureId, String fixtureExpectedCount) {
        jsonPathEvaluator = body.jsonPath();
        int actualcount=0;
        ResponseActualValueFromList = jsonPathEvaluator.getList(fixtureId);//Key passed
        // Iterate over the list and print individual ID
        for (String listResponseActualValue : ResponseActualValueFromList) {
            scenario.log("Response Body list Key :" + fixtureId);
            scenario.log("Response Body list Value :" + listResponseActualValue);
           actualcount++;
        }
        Assert.assertEquals("Incorrect value of fixtures is presented",
                Integer.parseInt(fixtureExpectedCount),actualcount);
      }

    @And("I validate fixture Id is not null")
    public void iValidateFixtureIdIsNotNull() {
        Assert.assertNotNull(String.valueOf(ResponseActualValueFromList), "fixtureId should not be null");
    }

    @Given("I perform POST operation with test data file {string}")
    public void iPerformPOSTOperationWithTestDataFile(String filepath) {
        RestAssured.baseURI = "http://localhost:3000/fixture";
        httpRequest = RestAssured.given();
        final Header header = new Header("Content-Type", "application/json");
        headers = new Headers(header);
        httpRequest.headers(headers);
        actualResponse = httpRequest
                .relaxedHTTPSValidation()
                .when()
                .body(new File(filepath))
                .post();
        System.out.println(actualResponse.getBody().asPrettyString());
    }

    @And("I should see teams array object has a teamId of {string}")
    public void iShouldSeeTeamsArrayObjectHasATeamIdOfHOME(String teamIdValue) {
        RestAssured.baseURI = "http://localhost:3000/fixture/1";
        httpRequest = RestAssured.given();
        final Header header = new Header("Content-Type", "application/json");
        headers = new Headers(header);
        httpRequest.headers(headers);
        actualResponse = httpRequest
                .relaxedHTTPSValidation()
                .when()
                .get();
        body = actualResponse.getBody();
        jsonPathEvaluator = body.path("$.footballFullState.teams[0].teamId");
        scenario.log(String.valueOf(jsonPathEvaluator));
      Assert.assertTrue(ResponseActualValueFromList.contains(teamIdValue));
    }

    @And("I should see newly fixture ID {string} within the return object")
    public void iShouldSeeNewlyFixtureIDWithinTheReturnObject(String new_Fixture_Id) {
        RestAssured.baseURI = "http://localhost:3000/fixtures";
        httpRequest = RestAssured.given();
        final Header header = new Header("Content-Type", "application/json");
        headers = new Headers(header);
        httpRequest.headers(headers);
        actualResponse = httpRequest
                .relaxedHTTPSValidation()
                .when()
                .get();
        body = actualResponse.getBody();
        jsonPathEvaluator = body.jsonPath();
        ResponseActualValueFromList = jsonPathEvaluator.getList("fixtureId");
        scenario.log("Response Body is: " + body.prettyPrint());
        Assert.assertTrue(String.valueOf(ResponseActualValueFromList).contains(new_Fixture_Id));
    }
}
