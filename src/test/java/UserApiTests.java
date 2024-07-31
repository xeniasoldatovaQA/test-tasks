import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserApiTests extends ApiTestSetup {
  String id = "10";
  String username = "testUser";
  String initialUser = "{\n" +
          "  \"id\": \"" + id + "\",\n" +
          "  \"username\": \"" + username + "\",\n" +
          "  \"firstName\": \"Xenia\",\n" +
          "  \"lastName\": \"Soldatova\",\n" +
          "  \"email\": \"test@example.com\",\n" +
          "  \"password\": \"12345\",\n" +
          "  \"phone\": \"1234567890\",\n" +
          "  \"userStatus\": 1\n" +
          "}";

  String updatedUser = "{\n" +
          "  \"id\": \"" + id + "\",\n" +
          "  \"username\": \"" + username + "\",\n" +
          "  \"firstName\": \"User\",\n" +
          "  \"lastName\": \"Test\",\n" +
          "  \"email\": \"test@example.com\",\n" +
          "  \"password\": \"654321\",\n" +
          "  \"phone\": \"1234567890\",\n" +
          "  \"userStatus\": 1\n" +
          "}";

  @Test
  void newUserTest() {
    createUser(initialUser);
    verifyUser(initialUser);
  }

  @Test
  void updatedUserTest() {
    createUser(initialUser);
    updateUser(updatedUser);
    verifyUser(updatedUser);
  }

  @Test
  void shouldDeleteUser() {
    createUser(initialUser);
    deleteUser(username);
    verifyUserDeleted(username);
  }

  private void createUser(String userObject) {
    given()
            .contentType(ContentType.JSON)
            .body(userObject)
            .when()
            .post("/user")
            .then()
            .statusCode(200)
            .body("message", equalTo(id));
  }

  private void updateUser(String userObject) {
    given()
            .contentType(ContentType.JSON)
            .body(userObject)
            .when()
            .put("/user/" + username)
            .then()
            .statusCode(200)
            .body("message", equalTo(id));
  }

  private void verifyUser(String userObject) {
    JsonNode userNode = parseJson(userObject);
    given()
            .pathParam("username", username)
            .when()
            .get("/user/{username}")
            .then()
            .statusCode(200)
            .body("id", equalTo(userNode.get("id").asInt()))
            .body("username", equalTo(userNode.get("username").asText()))
            .body("firstName", equalTo(userNode.get("firstName").asText()))
            .body("lastName", equalTo(userNode.get("lastName").asText()))
            .body("email", equalTo(userNode.get("email").asText()))
            .body("password", equalTo(userNode.get("password").asText()))
            .body("phone", equalTo(userNode.get("phone").asText()))
            .body("userStatus", equalTo(userNode.get("userStatus").asInt()));
  }

  private void deleteUser(String username) {
    given()
            .pathParam("username", username)
            .when()
            .delete("/user/{username}")
            .then()
            .statusCode(200)
            .body("message", equalTo(username));
  }

  private void verifyUserDeleted(String username) {
    given()
            .pathParam("username", username)
            .when()
            .get("/user/{username}")
            .then()
            .statusCode(404)
            .body("message", equalTo("User not found"));
  }

  private JsonNode parseJson(String jsonString) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      return mapper.readTree(jsonString);
    } catch (IOException e) {
      throw new RuntimeException("Failed to parse JSON", e);
    }
  }
}