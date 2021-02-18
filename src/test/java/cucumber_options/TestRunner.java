package cucumber_options;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
					features = {"src/test/java/features"},
					glue = {"step_definitions"},
					plugin = "json:target/jsonReports/cucumber-report.json",
					tags = "@OnlyAdd or @OnlyPut or @DeletePlace"
					// Can also be run from Maven using the following command:
					// mvn test -Dcucumber.options="--tags @OnlyPut or @OnlyDelete"
				)
public class TestRunner {

	
}