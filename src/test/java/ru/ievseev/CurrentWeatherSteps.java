package ru.ievseev;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static java.lang.System.getProperty;
import static org.hamcrest.Matchers.equalTo;

public class CurrentWeatherSteps {

    private final RequestSpecification spec = new RequestSpecBuilder()
            .setBaseUri("http://api.weatherstack.com/")
            .addFilter(new RequestLoggingFilter(LogDetail.URI))
            .addFilter(new ResponseLoggingFilter(LogDetail.STATUS))
            .addQueryParam("access_key",
                    getProperty("access_key", "46ce7dd086fcc06f94481a48497503d8"))
            .build();
    Response response;

    @When("получает текущую погоду в {string}")
    public void getCurrentWeatherIn(String arg0) {
        response = given()
                .spec(spec)
                .queryParam("query", arg0)
                .when()
                .get("/current");
    }

    @Then("в городе {string} погода")
    public void thenInCityMustBe(String arg0) {
        response
                .then().assertThat().body("current.weather_descriptions[0]", equalTo(arg0));
    }

    @When("выполняет запрос со следующими параметрами {string} {string}")
    public void getCurrentWeatherWithParams(String arg0, String arg1) {
        response = given()
                .spec(spec)
                .queryParam("query", arg0)
                .queryParam("units", arg1)
                .when()
                .get("/current");
    }

    @Then("получает в ответ {int}")
    public void thenGetError(Integer arg0) {
        response
                .then().assertThat().body("error.code", equalTo(arg0));
    }
}
