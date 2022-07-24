package stepsunirest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.exceptions.UnirestException;
import filter.CustomLogFilter;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Assert;

import java.util.List;
import com.mashape.unirest.http.Unirest;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class VotesWithUnirestSteps {

    String key;
    int initialVoteCount=-1;


    HttpResponse<JsonNode> response;

    @Given("x-api-key and baseURI are already acquired.")
    public void x_api_key_and_base_uri_are_already_acquired() {
        key = "8adf71fc-c27b-40ef-8662-19ab891129e3";
        baseURI = "https://api.thedogapi.com/v1/";
    }

    @When("I check number of votes for this {string}")
    public void i_check_number_of_votes_for_this(String sub_id) throws UnirestException {

        //https://api.thedogapi.com/v1/votes?sub_id=my-user-1234
        response = Unirest.get("https://api.thedogapi.com/v1/votes?sub_id=my-user-1234")
                .header("x-api-key", "8adf71fc-c27b-40ef-8662-19ab891129e3")
                .asJson();
        Assert.assertEquals(response.getStatus(),200);

    }

    @Then("I see numbers")
    public void i_see_numbers() {
        initialVoteCount = response.getBody().getArray().length();
    }

    @When("I will create one more vote for this {string}")
    public void i_will_create_one_more_vote(String sub_id) throws UnirestException {

        response = Unirest.post("https://api.thedogapi.com/v1/votes")
                .header("x-api-key", "8adf71fc-c27b-40ef-8662-19ab891129e3")
                .header("Content-Type", "application/json")
                .body(" {\n  \"image_id\": \"foo2\",\n  \"sub_id\": \""+sub_id+"\",\n  \"value\": \"add\"\n}")
                .asJson();

        Assert.assertEquals(response.getStatus(),200);

        for(int i=0; i<response.getBody().getArray().length();i++){
            System.out.println(response.getBody().getArray().get(i));
        }

    }


    @Then("I have numbers plus one votes for this {string}")
    public void i_have_numbers_plus_one_votes_for_this(String sub_id) throws UnirestException {
        i_check_number_of_votes_for_this(sub_id);
        int lastVoteCount = response.getBody().getArray().length();
        Assert.assertTrue(lastVoteCount==initialVoteCount+1);
    }

}
