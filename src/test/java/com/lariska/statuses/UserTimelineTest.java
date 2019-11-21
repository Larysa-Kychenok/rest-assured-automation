package com.lariska.statuses;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItem;

import java.util.ArrayList;

import com.lariska.common.RestUtilities;
import com.lariska.constants.Endpoints;
import com.lariska.constants.Path;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class UserTimelineTest
{
    RequestSpecification reqSpec;
    ResponseSpecification resSpec;

    @BeforeClass
    public void setup()
    {
        reqSpec = RestUtilities.getRequestSpecification();
        reqSpec.queryParam("user_id", "lariska");
        reqSpec.basePath(Path.STATUSES);

        resSpec = RestUtilities.getResponseSpecification();
    }

    @Test
    public void readTweet1()
    {
        given()
                .spec(RestUtilities.createQueryParam(reqSpec, "count", "1"))
                .when()
                .get(Endpoints.STATUSES_USER_TIMELINE)
                .then()
                .log().all()
                .spec(resSpec)
                .body("user.screen_name", hasItem("lariska27269704"));
    }

    @Test
    public void readTweet2()
    {
        RestUtilities.setEndPoint(Endpoints.STATUSES_USER_TIMELINE);

        Response res = RestUtilities.getResponse(
                RestUtilities.createQueryParam(reqSpec, "count", "1"), "get");

        ArrayList<String> screenNameList = res.path("user.screen_name");

        System.out.println(screenNameList);
        Assert.assertTrue(screenNameList.contains("lariska27269704"));
    }
}
