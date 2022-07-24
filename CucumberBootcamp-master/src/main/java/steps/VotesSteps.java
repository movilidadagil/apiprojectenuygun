package steps;

import filter.CustomLogFilter;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class VotesSteps {

    String key;
    int initialVoteCount=-1;

    Response response;
    CustomLogFilter customLogFilter ;


    @Given("x-api-key and baseURI are already acquired.")
    public void x_api_key_and_base_uri_are_already_acquired() {
        key = "8adf71fc-c27b-40ef-8662-19ab891129e3";
        baseURI = "https://api.thedogapi.com/v1/";
        customLogFilter = new CustomLogFilter();
    }

    @When("I check number of votes for this {string}")
    public void i_check_number_of_votes_for_this(String sub_id) {

        //https://api.thedogapi.com/v1/votes?sub_id=my-user-1234

        response = given().headers("x-api-key",key)
                .accept(ContentType.JSON)
                .pathParam("sub_id",sub_id)
                .when()
                .get("votes?sub_id={sub_id}")
                .then()
                .statusCode(200)
                .extract().response();
    }

    @Then("I see numbers")
    public void i_see_numbers() {
        List voteList = response.getBody().jsonPath().get();
        initialVoteCount = voteList.size();
    }

    @When("I will create one more vote for this {string}")
    public void i_will_create_one_more_vote(String sub_id) {

        //https://api.thedogapi.com/v1/votes
        /*

                {
  "image_id": "k4vWnFdL2",
  "sub_id": "my-user-1234",
  "value": "ad"
}
         */
        String requestBody = "{\n" +
                "  \"image_id\": \"foo3\",\n" +
                "  \"sub_id\": \""+sub_id+"\""+",\n"+
                "  \"value\": \"add\"\n}";


       try {
           response = given()
                   .headers("x-api-key",key)
                   .contentType(ContentType.JSON)
                   .filter(customLogFilter)
                   .and()
                   .body(requestBody)
                   .when()
                   .post("votes")
                   .then()
                   .statusCode(200)
                   .and()
                   .contentType(ContentType.JSON)
                   .extract().response();
       }catch (AssertionError assertionError){
           System.out.println(assertionError.getMessage());
           System.out.println(customLogFilter.getRequestBuilder().toString());
           System.out.println(customLogFilter.getResponseBuilder().toString());
       }




    }


    @Then("I have numbers plus one votes for this {string}")
    public void i_have_numbers_plus_one_votes_for_this(String sub_id) {
        i_check_number_of_votes_for_this(sub_id);
        List voteList = response.getBody().jsonPath().get();
        int lastVoteCount = voteList.size();
        Assert.assertTrue(lastVoteCount==initialVoteCount+1);
    }

}
