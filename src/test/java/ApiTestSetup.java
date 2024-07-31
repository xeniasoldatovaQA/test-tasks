import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public class ApiTestSetup {
    @BeforeAll
    public static void setup() {
      RestAssured.baseURI = "https://petstore.swagger.io/v2";
    }
}
