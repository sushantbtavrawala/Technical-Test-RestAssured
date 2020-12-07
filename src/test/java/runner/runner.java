package runner;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(plugin={"pretty", "html:target/cucumber","json:target/jsonReports/cucumber.json"},
        features = "classpath:features",
        glue= "steps")
public class runner {


}
