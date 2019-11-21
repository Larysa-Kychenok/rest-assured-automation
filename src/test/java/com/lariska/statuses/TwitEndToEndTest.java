package com.lariska.statuses;

import static io.restassured.RestAssured.given;

import com.lariska.common.RestUtilities;
import com.lariska.constants.Endpoints;
import com.lariska.constants.Path;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TwitEndToEndTest
{
    RequestSpecification reqSpec;
    ResponseSpecification resSpec;

    String tweetId;

    @BeforeClass
    public void setup()
    {
        reqSpec = RestUtilities.getRequestSpecification();
        reqSpec.queryParam("user_id", "lariska");
        reqSpec.basePath(Path.STATUSES);

        resSpec = RestUtilities.getResponseSpecification();
    }

    @Test
    public void testPostTweet()
    {
        RestUtilities.setEndPoint(Endpoints.STATUSES_TWEET_POST);
        Response res = RestUtilities.getResponse(RestUtilities.createQueryParam(reqSpec, "status",
                "My another great Tweet "
                        + RandomStringUtils.randomNumeric(5)), "post");

        tweetId = RestUtilities.getJsonPath(res).getString("id_str");

        System.out.println("Created tweet with id = " + tweetId);

    }

    @Test(dependsOnMethods = {"testPostTweet"})
    public void testReadTweet()
    {
        Response res =
                given()
                        .spec(RestUtilities.createQueryParam(reqSpec, "id", tweetId))
                        .when()
                        .get(Endpoints.STATUSES_TWEET_READ_SINGLE)
                        .then()
                        .spec(resSpec)
                        .extract()
                        .response();

        String tweetContent = RestUtilities.getJsonPath(res).getString("text");
        System.out.println("Read tweet with text = \"" + tweetContent + "\"");
    }

    @Test(dependsOnMethods = {"testReadTweet"})
    public void testRemoveTweet()
    {
        RestUtilities.setEndPoint(Endpoints.STATUSES_TWEET_DESTROY);
        Response res = RestUtilities.getResponse(RestUtilities.createQueryParam(reqSpec, "id", tweetId), "post");

        System.out.println("Removed tweet with id = " + tweetId);

    }
}
