import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;
import java.util.Random;

public class SignupTest {

    FakeValuesService fakeValuesService = new FakeValuesService(
            new Locale("en-GB"), new RandomService());

    String email = fakeValuesService.bothify("????##@gmail.com");
    String username =  fakeValuesService.bothify("???????##");



    String signupBody="{\n    \"username\":"+"\""+username+"\",\n    \"password\":\"123456\",\n    \"email\":"+"\""+email+"\"\n}";
    @Test
    public void verifySuccessfullSignup() throws UnirestException {
        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.post("http://localhost:2021/api/auth/signup")
                .header("Content-Type", "application/json")
                .body(signupBody)
                .asString();

        System.out.println(signupBody);
        System.out.println("review");
        Assert.assertEquals(200,response.getStatus());

    }

}
