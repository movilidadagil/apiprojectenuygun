import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.User;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Locale;
import java.util.Random;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SignupTest {

    Response response;

    User user;
    HttpHeaders headers;
    RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public SignupTest(){
        baseURI = "http://localhost:2021/api/auth/";
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate = new RestTemplate();
        user = new User();

    }

    FakeValuesService fakeValuesService = new FakeValuesService(
            new Locale("en-GB"), new RandomService());

    String email = fakeValuesService.bothify("????##@gmail.com");
    String username =  fakeValuesService.bothify("???????##");



    String signupBody="{\n    \"username\":"+"\""+username+"\",\n    \"password\":\"123456\",\n    \"email\":"+"\""+email+"\"\n}";


    @Test
    public void verifySuccessfullSignup() throws UnirestException, JsonProcessingException {

        //I used fakervaluesservice for more generic email and username
        String email = fakeValuesService.bothify("????##@gmail.com");
        String username =  fakeValuesService.bothify("???????##");
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("123456");
        ObjectMapper mapper = new ObjectMapper();
        //Converting the Object to JSONString
        String jsonString = mapper.writeValueAsString(user);
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("http://localhost:2021/api/auth/signup")
                .header("Content-Type", "application/json")
                .body(jsonString)
                .asString();

        Assert.assertEquals(200,response.getStatus());
    }

    @Test
    public void verifySuccessfullSignupAsSjon() throws UnirestException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:2021/api/auth/signup")
                .header("Content-Type", "application/json")
                .body(signupBody)
                .asJson();

        Assert.assertEquals(200,response.getStatus());
        Assert.assertEquals(response.getBody().getObject().get("message"),"User registered successfully!");
    }

    @Test
    public void verifiySuccuessfullSignupByRestAssured() throws JsonProcessingException {
        String email = fakeValuesService.bothify("????##@gmail.com");
        String username =  fakeValuesService.bothify("???????##");
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("123456");
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(user);

        response = given()
                .header("Content-Type","application/json")
                .body(jsonString)
                .when()
                .post("signup")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract().response();

        JsonPath signupJson =  response.jsonPath();
        Assert.assertEquals(response.getBody().asString(),"{\"message\":\"User registered successfully!\"}");
        Assert.assertEquals(signupJson.get("message"),"User registered successfully!");
    }

    @Test
    public void successfullSignupByHttpEntity() throws IOException {
        HttpEntity<String> request =
                new HttpEntity<String>(signupBody, headers);

        String signupResultAsJsonStr =
                restTemplate.postForObject("http://localhost:2021/api/auth/signup", request, String.class);
        com.fasterxml.jackson.databind.JsonNode root = objectMapper.readTree(signupResultAsJsonStr);

        assertNotNull(signupResultAsJsonStr);
        assertNotNull(root);
        assertNotNull(root.path("message").asText());
        assertEquals(root.path("message").asText(),"User registered successfully!");
    }

    @Test
    public void successfulSignupWithModel() throws IOException {
        String email = fakeValuesService.bothify("????##@gmail.com");
        String username =  fakeValuesService.bothify("???????##");

        user.setUsername(username);
        user.setEmail(email);
        user.setPassword("123456");
        HttpEntity<User> request =
                new HttpEntity<User>(user, headers);
        String signupResultAsJsonStr =
                restTemplate.postForObject("http://localhost:2021/api/auth/signup", request, String.class);
        com.fasterxml.jackson.databind.JsonNode root = objectMapper.readTree(signupResultAsJsonStr);

        assertNotNull(signupResultAsJsonStr);
        assertNotNull(root);
        assertNotNull(root.path("message").asText());
        assertEquals(root.path("message").asText(),"User registered successfully!");


    }

}
