package it.pagopa.selfcare.cucumber.utils;

import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import it.pagopa.selfcare.cucumber.utils.model.JwtData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class CommonSteps {

    private final SharedStepData sharedStepData;
    private final TestDataProvider testDataProvider;
    private final TestJwtGenerator testJwtGenerator;

    public CommonSteps(SharedStepData sharedStepData, TestDataProvider testDataProvider, TestJwtGenerator testJwtGenerator) {
        this.sharedStepData = sharedStepData;
        this.testDataProvider = testDataProvider;
        this.testJwtGenerator = testJwtGenerator;
    }

    @Before
    public void clearSharedStepData() {
        log.info("Clearing sharedStepData");
        sharedStepData.clear();
    }

    @Given("User login with username {string} and password {string}")
    public void login(String username, String password) {
        JwtData jwtData = testDataProvider.getTestData().getJwtData().stream()
                .filter(data -> data.getUsername().equals(username) && data.getPassword().equals(password))
                .findFirst()
                .orElse(null);
        sharedStepData.setToken(testJwtGenerator.generateToken(jwtData));
    }

    @Given("A bad jwt token")
    public void badToken() {
        sharedStepData.setToken("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Imp3dF9hMjo3YTo0NjozYjoyYTo2MDo1Njo0MDo4ODphMDo1ZDphNDpmODowMToxZTozZSJ9.eyJmYW1pbHlfbmFtZSI6IlNhcnRvcmkiLCJmaXNjYWxfbnVtYmVyIjoiU1JUTkxNMDlUMDZHNjM1UyIsIm5hbWUiOiJBbnNlbG1vIiwic3BpZF9sZXZlbCI6Imh0dHBzOi8vd3d3LnNwaWQuZ292Lml0L1NwaWRMMiIsImZyb21fYWEiOmZhbHNlLCJ1aWQiOiI1MDk2ZTRjNi0yNWExLTQ1ZDUtOWJkZi0yZmI5NzRhN2MxYzgiLCJsZXZlbCI6IkwyIiwiaWF0IjoxNzM5MzYxMzUzLCJhdWQiOiJhcGkuZGV2LnNlbGZjYXJlLnBhZ29wYS5pdCIsImlzcyI6IlNQSUQiLCJqdGkiOiJfOWE2M2ZiNTQyYzk4NDJjZWMyNmQifQ.X9zoPHuLq6GafRM6zV6hnN09SQ1rL0rFWK5d-RfwJACHam1nPjqX6INx9Qd-_E69GFlr4O1JzzIzc3wfnbIhRlKMVTLjw5xjadc_sxoq-6sH-8Ek_aPeWqL44m_RKcngFCzh-7KrD32wrh4fyC_tdhFbS0SSWjTLgDy0mn3gGPLwFGmv2ASW7xZvw-DfQpsNZhEDJAOQgQ4qC5Lyxo_RriBHDIq1pZvtmW6RkIYsLJ8EGNoOGM4SzUOM3ZSSieh-48DLb8HsDwJgrle6gJJZoqZ0saeAN-7Gy-q55tl3E0hLhfif81RQ_nFH7nc3I9kLffaxWfpH7Oym5F3Nur-btg");
    }

    @And("The following request body:")
    public void setRequestBody(String requestBody) {
        sharedStepData.setRequestBody(requestBody);
    }

    @And("The following path params:")
    public void setPathParams(Map<String, String> pathParams) {
        sharedStepData.setPathParams(pathParams);
    }

    @And("The following query params:")
    public void setQueryParams(Map<String, String> queryParams) {
        sharedStepData.setQueryParams(queryParams);
    }

    @When("I send a GET request to {string}")
    public void sendGetRequest(String url) {
        final String token = sharedStepData.getToken();
        sharedStepData.setResponse(RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .pathParams(Optional.ofNullable(sharedStepData.getPathParams()).orElse(Collections.emptyMap()))
                .queryParams(Optional.ofNullable(sharedStepData.getQueryParams()).orElse(Collections.emptyMap()))
                .when()
                .get(url)
                .then()
                .extract()
        );
    }

    @When("I send a POST request to {string}")
    public void sendPostRequest(String url) {
        final String token = sharedStepData.getToken();
        sharedStepData.setResponse(RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .pathParams(Optional.ofNullable(sharedStepData.getPathParams()).orElse(Collections.emptyMap()))
                .queryParams(Optional.ofNullable(sharedStepData.getQueryParams()).orElse(Collections.emptyMap()))
                .body(sharedStepData.getRequestBody())
                .when()
                .post(url)
                .then()
                .extract()
        );
    }

    @When("I send a PUT request to {string}")
    public void sendPutRequest(String url) {
        final String token = sharedStepData.getToken();
        sharedStepData.setResponse(RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .pathParams(Optional.ofNullable(sharedStepData.getPathParams()).orElse(Collections.emptyMap()))
                .queryParams(Optional.ofNullable(sharedStepData.getQueryParams()).orElse(Collections.emptyMap()))
                .body(sharedStepData.getRequestBody())
                .when()
                .put(url)
                .then()
                .extract()
        );
    }

    @When("I send a HEAD request to {string}")
    public void sendHeadRequest(String url) {
        final String token = sharedStepData.getToken();
        sharedStepData.setResponse(RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .pathParams(Optional.ofNullable(sharedStepData.getPathParams()).orElse(Collections.emptyMap()))
                .queryParams(Optional.ofNullable(sharedStepData.getQueryParams()).orElse(Collections.emptyMap()))
                .when()
                .head(url)
                .then()
                .extract());
    }

    @When("I send a DELETE request to {string}")
    public void sendDeleteRequest(String url) {
        final String token = sharedStepData.getToken();
        sharedStepData.setResponse(RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .pathParams(Optional.ofNullable(sharedStepData.getPathParams()).orElse(Collections.emptyMap()))
                .queryParams(Optional.ofNullable(sharedStepData.getQueryParams()).orElse(Collections.emptyMap()))
                .when()
                .delete(url)
                .then()
                .extract()
        );
    }

    @Then("The status code is {int}")
    public void checkStatusCode(int expectedStatusCode) {
        Assertions.assertEquals(expectedStatusCode, sharedStepData.getResponse().statusCode());
    }

    @And("The response body contains:")
    public void checkResponseBody(Map<String, String> expectedKeyValues) {
        expectedKeyValues.forEach((expectedKey, expectedValue) -> {
            final String currentValue = sharedStepData.getResponse().body().jsonPath().getString(expectedKey);
            Assertions.assertEquals(expectedValue, currentValue, String.format("The field %s does not contain the expected value", expectedKey));
        });
    }

    @And("The response body contains field {string}")
    public void checkResponseBodyKey(String expectedJsonPath) {
        final String currentValue = sharedStepData.getResponse().body().jsonPath().getString(expectedJsonPath);
        Assertions.assertNotNull(currentValue);
    }

    @And("The response body doesn't contain field {string}")
    public void checkResponseBodyMissingKey(String expectedJsonPath) {
        final String currentValue = sharedStepData.getResponse().body().jsonPath().getString(expectedJsonPath);
        Assertions.assertNull(currentValue);
    }

    @And("The response body contains the list {string} of size {int}")
    public void checkResponseBodyListSize(String expectedJsonPath, int expectedSize) {
        final int currentSize = sharedStepData.getResponse().body().jsonPath().getList(expectedJsonPath).size();
        Assertions.assertEquals(expectedSize, currentSize);
    }

    @And("The response body contains at path {string} the following list of values in any order:")
    public void checkResponseBodyList(String expectedJsonPath, List<String> expectedValues) {
        final List<String> currentValues = sharedStepData.getResponse().body().jsonPath().getList(expectedJsonPath, String.class)
                .stream().filter(Objects::nonNull).toList();
        Assertions.assertEquals(expectedValues.size(), currentValues.size(), String.format("The lists have different sizes. Expected: %s, Current: %s", expectedValues, currentValues));
        final Map<String, Long> expectedTimes = expectedValues.stream().collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        final Map<String, Long> currentTimes = currentValues.stream().collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        Assertions.assertTrue(
                expectedTimes.entrySet().stream().allMatch(entry -> Objects.equals(entry.getValue(), currentTimes.getOrDefault(entry.getKey(), 0L))),
                String.format("The lists differ (in any order). Expected: %s, Current %s", expectedValues, currentValues)
        );
    }

    @And("The response body contains at path {string} the following list of objects in any order:")
    public void checkResponseBodyObjectList(String expectedJsonPath, List<Map<String, Object>> expectedObjects) {
        final List<Map<String, Object>> currentObjects = sharedStepData.getResponse().body().jsonPath().getList(expectedJsonPath);

        Assertions.assertEquals(expectedObjects.size(), currentObjects.size(),
                String.format("The lists have different sizes. Expected: %s, Current: %s", expectedObjects, currentObjects));

        final Set<String> expectedKeySet = expectedObjects.get(0).keySet();
        final Set<Map<String, String>> expectedSet = expectedObjects.stream()
                .map(m -> expectedKeySet.stream()
                        .collect(Collectors.toMap(k -> k, k -> Optional.ofNullable(m.get(k)).map(Object::toString).orElse(""))))
                .collect(Collectors.toSet());
        final Set<Map<String, String>> currentSet = currentObjects.stream()
                .map(m -> expectedKeySet.stream()
                        .collect(Collectors.toMap(k -> k, k -> Optional.ofNullable(m.get(k)).map(Object::toString).orElse(""))))
                .collect(Collectors.toSet());

        Assertions.assertEquals(expectedSet, currentSet,
                String.format("The lists contain different objects. Expected: %s, Current: %s", expectedSet, currentSet));
    }

}
