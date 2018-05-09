package com.example.access.control;

import com.jayway.restassured.filter.session.SessionFilter;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.jayway.restassured.specification.ResponseSpecification;

import static com.jayway.restassured.RestAssured.given;

public class TestHelper {

    private static final String USER_PASS = "123";
    private static final String LOGIN_URL = "/login";

    public static Response getRequest(String userName, String url) {

        return givenApi(userName)
                .when()
                .get(url);
    }

    public static ResponseSpecification prepareRequest(String userName) {
        return prepareRequest(userName,null);
    }

    public static ResponseSpecification prepareRequest(String userName, Object body) {

        RequestSpecification requestSpecification = given()
                .contentType(ContentType.JSON)
                .filter(getSessionFilter(userName));

        if (body != null) {
            requestSpecification.body(body);
        }

        return requestSpecification.expect();
    }

    private static SessionFilter getSessionFilter(String userName) {
        SessionFilter sessionFilter = new SessionFilter();

        givenApi(userName)
                .filter(sessionFilter)
                .when()
                .get(LOGIN_URL);

        return sessionFilter;
    }

    private static RequestSpecification givenApi(String userName) {

        return given()
                .contentType(ContentType.JSON)
                .auth().basic(userName, USER_PASS);
    }
}
