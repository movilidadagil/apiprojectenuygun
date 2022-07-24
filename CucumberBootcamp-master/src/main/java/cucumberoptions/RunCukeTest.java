package cucumberoptions;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
          features = "src/main/resources/features",
        glue = "steps",
        tags="@votes",
        plugin = { "pretty", "json:target/cucumber-reports.json" }
)

public class RunCukeTest {

}
