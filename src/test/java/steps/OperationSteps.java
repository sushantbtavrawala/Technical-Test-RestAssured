package steps;

import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;


public class OperationSteps {

    private static byte[] response;
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

    @And("I should see the fixtureId_count 3 with the return object and not null")
    public void iShouldSeeTheFixtureId_countWithTheReturnObjectAndNotNull() {
        baseURI = "http://localhost:3000/";
        ArrayList<String> fixtureId = given().contentType(ContentType.JSON).log().all()
                .get("/fixtures").then()
                .extract().path("fixtureId");

        int actualcount=0;
        for(String m: fixtureId)
        {
            System.out.println("FixtureId: "+ m);
            actualcount++;
        }
        Assert.assertEquals(3,actualcount);
        Assert.assertNotNull(fixtureId);
    }

    @Given("I perform POST operation with test data file {string}")
    public void iPerformPOSTOperationWithTestDataFile(String filepath) {
        RestAssured.baseURI = "http://localhost:3000/fixture";
        Response response  = (Response) given().log().all()
                .contentType(ContentType.JSON).accept(ContentType.JSON)
                .header("Content-Type","application/json")
                .body(new File(filepath))
                .when()
                .post()
                .then()
                .statusCode(200)
                .log().all();

        String new_Fixture_Id = response.path("fixtureId");
        System.out.println(new_Fixture_Id);
    }

    @And("I should see teams array object has a teamId of {string}")
    public void iShouldSeeTeamsArrayObjectHasATeamIdOfHOME(String team) {
        baseURI = "http://localhost:3000";

        Response response = (Response) given().contentType(ContentType.JSON).log().all()
                .get("/fixture/1").getBody();

        response.prettyPrint();
        JsonPath extractor = response.jsonPath();
        String teamID = extractor.get("$.footballFullState.teams[0].teamId");
        System.out.println(teamID);
        Assert.assertEquals(teamID,team);
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
