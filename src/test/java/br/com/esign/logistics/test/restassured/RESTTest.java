/*
 * The MIT License
 *
 * Copyright 2015 Esign Consulting Ltda.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package br.com.esign.logistics.test.restassured;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import static org.hamcrest.Matchers.*;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 *
 * @author gustavomunizdocarmo
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RESTTest {
    
    private static String slug;
    
    @Before
    public void setup() {
        RestAssured.basePath = "/logistics/api/maps";
    }
    
    /**
     * Creates a map.
     */
    @Test
    public void testA() {
        slug = RestAssured
            .given()
                .contentType(ContentType.JSON)
                .body("{\"name\": \"REST-assured Test\"}")
            .when()
                .post()
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("code", equalTo(200))
                .body("status", equalTo("success"))
                .body("data", notNullValue())
                .body("data.slug", notNullValue())
            .extract()
                .path("data.slug");
    }
    
    /**
     * Checks map existence.
     */
    @Test
    public void testB() {
        RestAssured
            .when()
                .get()
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("code", equalTo(200))
                .body("status", equalTo("success"))
                .body("data", hasSize(greaterThan(0)))
                .body("data", hasItem(hasEntry("slug", slug)));
    }
    
    /**
     * Creates routes for the map.
     */
    @Test
    public void testC() {
        RestAssured
            .given()
                .contentType(ContentType.JSON)
                .body("{\"origin\": {\"name\": \"A\"}, \"destination\": {\"name\": \"B\"}, \"distance\": 10}")
                .pathParam("slug", slug)
            .when()
                .post("/{slug}/routes")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("code", equalTo(200))
                .body("status", equalTo("success"))
                .body("data", hasSize(2));
        
        RestAssured
            .given()
                .contentType(ContentType.JSON)
                .body("{\"origin\": {\"name\": \"B\"}, \"destination\": {\"name\": \"D\"}, \"distance\": 15}")
                .pathParam("slug", slug)
            .when()
                .post("/{slug}/routes")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("code", equalTo(200))
                .body("status", equalTo("success"))
                .body("data", hasSize(2));
        
        RestAssured
            .given()
                .contentType(ContentType.JSON)
                .body("{\"origin\": {\"name\": \"A\"}, \"destination\": {\"name\": \"C\"}, \"distance\": 20}")
                .pathParam("slug", slug)
            .when()
                .post("/{slug}/routes")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("code", equalTo(200))
                .body("status", equalTo("success"))
                .body("data", hasSize(2));
    }
    
    /**
     * Removes the map.
     */
    @Test
    public void testF() {
        RestAssured
            .given()
                .pathParam("slug", slug)
            .when()
                .delete("/{slug}")
            .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("code", equalTo(200))
                .body("status", equalTo("success"))
                .body("data", nullValue());
    }
    
}
