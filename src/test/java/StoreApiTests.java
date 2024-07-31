import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class StoreApiTests extends ApiTestSetup {
  private static final int ORDER_ID = 1;
  private static final int PET_ID = 1234;
  private static final String ORDER_STATUS = "placed";
  private static final String BASE_ORDER_JSON = "{ \"id\": %d, \"petId\": %d, \"status\": \"%s\" }";

  private String orderJson;

  @BeforeEach
  void setupOrder() {
    orderJson = String.format(BASE_ORDER_JSON, ORDER_ID, PET_ID, ORDER_STATUS);
  }

  @Test
  void shouldAddOrder() {
    given()
            .contentType(ContentType.JSON)
            .body(orderJson)
            .when()
            .post("/store/order")
            .then()
            .statusCode(200)
            .body("id", equalTo(ORDER_ID))
            .body("petId", equalTo(PET_ID))
            .body("status", equalTo(ORDER_STATUS));
  }

  @Test
  void shouldReturnOrderById() {
    given()
            .pathParam("orderId", ORDER_ID)
            .when()
            .get("/store/order/{orderId}")
            .then()
            .statusCode(200)
            .body("id", equalTo(ORDER_ID));
  }

  @Test
  void shouldDeleteOrder() {
    given()
            .pathParam("orderId", ORDER_ID)
            .when()
            .delete("/store/order/{orderId}")
            .then()
            .statusCode(200);
  }
}
