package com.lariska.common;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.lessThan;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.lariska.constants.Auth;
import com.lariska.constants.Path;
import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

public class RestUtilities
{
    public static String ENDPOINT;

    public static RequestSpecBuilder REQUEST_BUILDER;
    public static RequestSpecification REQUEST_SPEC;

    public static ResponseSpecBuilder RESPONSE_BUILDER;
    public static ResponseSpecification RESPONSE_SPEC;

    public static void setEndPoint(String endpoint)
    {
        ENDPOINT = endpoint;
    }

    public static RequestSpecification getRequestSpecification()
    {
        AuthenticationScheme authScheme = RestAssured.oauth(Auth.CONSUMER_KEY,
                Auth.CONSUMER_SECRET,
                Auth.ACCESS_TOKEN,
                Auth.ACCESS_SECRET);

        REQUEST_BUILDER = new RequestSpecBuilder().setBaseUri(Path.BASE_URI)
                .setAuth(authScheme);

        REQUEST_SPEC = REQUEST_BUILDER.build();

        return REQUEST_SPEC;
    }

    public static ResponseSpecification getResponseSpecification()
    {
        RESPONSE_BUILDER = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectResponseTime(lessThan(2L), TimeUnit.SECONDS);

        RESPONSE_SPEC = RESPONSE_BUILDER.build();
        return RESPONSE_SPEC;
    }

    public static RequestSpecification createQueryParam(RequestSpecification rspec, String param, String value)
    {
        return rspec.queryParam(param, value);
    }

    public static RequestSpecification createQueryParam(RequestSpecification rspec, Map<String, String> queryMap)
    {
        return rspec.queryParams(queryMap);
    }

    public static RequestSpecification createPathParam(RequestSpecification rspec, String param, String value)
    {
        return rspec.pathParam(param, value);
    }

    public static RequestSpecification createPathParam(RequestSpecification rspec, Map<String, String> queryMap)
    {
        return rspec.pathParams(queryMap);
    }

    public static Response getResponse()
    {
        return given().get(ENDPOINT);
    }

    public static Response getResponse(RequestSpecification reqSpec, String type)
    {
        REQUEST_SPEC.spec(reqSpec);
        Response response = null;
        if (type.equalsIgnoreCase("get"))
        {
            response = given().spec(REQUEST_SPEC).get(ENDPOINT);
        }
        else if (type.equalsIgnoreCase("post"))
        {
            response = given().spec(REQUEST_SPEC).post(ENDPOINT);
        }
        else if (type.equalsIgnoreCase("put"))
        {
            response = given().spec(REQUEST_SPEC).put(ENDPOINT);
        }
        else if (type.equalsIgnoreCase("delete"))
        {
            response = given().spec(REQUEST_SPEC).delete(ENDPOINT);
        }
        else
        {
            System.out.println("Type is not supported: " + type);
            return response;
        }

        response.then().spec(RESPONSE_SPEC);
        return response;
    }

    public static JsonPath getJsonPath(Response resp)
    {
        String sPath = resp.asString();
        return new JsonPath(sPath);
    }

    public static XmlPath getXmlPath(Response resp)
    {
        String sPath = resp.asString();
        return new XmlPath(sPath);
    }

    public static void resetBasePath()
    {
        RestAssured.basePath = null;
    }

    public static void setContentType(ContentType type)
    {
        given().contentType(type);
    }
}
