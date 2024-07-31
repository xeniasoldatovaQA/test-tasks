import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class PetApiTests extends ApiTestSetup {

  private static final int PET_ID = 1234;
  private static final String PET_NAME = "Loki";
  private static final String UPDATED_PET_NAME = "Richard";
  private static final String PET_STATUS = "available";
  private static final String BASE_PET_JSON = "{ \"id\": %d, \"name\": \"%s\", \"status\": \"%s\" }";

  private String petJson;
  private String updatedPetJson;

  @BeforeEach
  void setupPet() {
    petJson = String.format(BASE_PET_JSON, PET_ID, PET_NAME, PET_STATUS);
    updatedPetJson = String.format(BASE_PET_JSON, PET_ID, UPDATED_PET_NAME, PET_STATUS);
  }

  @Test
  void shouldAddNewPet() {
    given()
            .contentType(ContentType.JSON)
            .body(petJson)
            .when()
            .post("/pet")
            .then()
            .statusCode(200)
            .body("name", equalTo(PET_NAME));
  }

  @Test
  void shouldReturnPetById() {
    given()
            .pathParam("petId", PET_ID)
            .when()
            .get("/pet/{petId}")
            .then()
            .statusCode(200)
            .body("id", equalTo(PET_ID));
  }

  @Test
  void shouldUpdateName() {
    given()
            .contentType(ContentType.JSON)
            .body(updatedPetJson)
            .when()
            .put("/pet")
            .then()
            .statusCode(200)
            .body("name", equalTo(UPDATED_PET_NAME));
  }

  @Test
  void shouldDeletePet() {
    given()
            .pathParam("petId", PET_ID)
            .when()
            .delete("/pet/{petId}")
            .then()
            .statusCode(200);
  }

  // Example of negative test
  @Test
  void negativeShouldReturnPetById() {
    given()
            .pathParam("petId", "invalidPetId")
            .when()
            .get("/pet/{petId}")
            .then()
            .statusCode(404);
  }
}
