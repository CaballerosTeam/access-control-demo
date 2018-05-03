package com.example.access.control;

import com.jayway.restassured.filter.session.SessionFilter;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.jayway.restassured.specification.ResponseSpecification;

import static com.jayway.restassured.RestAssured.given;

public class TestHelper {

    private static final String USER_NAME = "user";
    private static final String USER_PASS = "123";
    private static final String LOGIN_URL = "/login";

    public static Response getRequest(String url) {

        return givenApi()
                .when()
                .get(url);
    }

    public static ResponseSpecification prepareRequest() {
        return prepareRequest(null);
    }

    public static ResponseSpecification prepareRequest(Object body) {

        RequestSpecification requestSpecification = given()
                .contentType(ContentType.JSON)
                .filter(getSessionFilter());

        if (body != null) {
            requestSpecification.body(body);
        }

        return requestSpecification.expect();
    }

    private static SessionFilter getSessionFilter() {
        SessionFilter sessionFilter = new SessionFilter();

        givenApi()
                .filter(sessionFilter)
                .when()
                .get(LOGIN_URL);

        return sessionFilter;
    }

    private static RequestSpecification givenApi() {

        return given()
                .contentType(ContentType.JSON)
                .auth().basic(USER_NAME, USER_PASS);
    }
}
