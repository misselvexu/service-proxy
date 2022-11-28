package com.predic8.membrane.core.openapi;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.predic8.membrane.core.openapi.model.*;
import com.predic8.membrane.core.openapi.validators.*;
import org.junit.*;

import java.io.*;
import java.math.*;

import static org.junit.Assert.*;


public class IntegerTest {

    OpenAPIValidator validator;
    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Before
    public void setUp() {
        validator = new OpenAPIValidator(getResourceAsStream("/openapi/integer.yml"));
    }

    @Test
    public void validMinimumInQuery() {
        ValidationErrors errors = validator.validate(Request.get().path("/integer?minimum=3"));
//        System.out.println("errors = " + errors);
        assertEquals(1,errors.size());
    }

    @Test
    public void validMaximumInQuery() {
        ValidationErrors errors = validator.validate(Request.get().path("/integer?maximum=13"));
//        System.out.println("errors = " + errors);
        assertEquals(1,errors.size());
    }

    @Test
    public void validMinimumInBody() {
        ValidationErrors errors = validator.validate(Request.post().path("/integer").body(new JsonBody(getNumbers("minimum",new BigDecimal(7)))));
//        System.out.println("errors = " + errors);
        assertEquals(0,errors.size());
    }

    @Test
    public void validMinimumInBodyExact() {
        ValidationErrors errors = validator.validate(Request.post().path("/integer").body(new JsonBody(getNumbers("minimum",new BigDecimal(5)))));
//        System.out.println("errors = " + errors);
        assertEquals(0,errors.size());
    }

    @Test
    public void invalidMinimumInBodyExactExclusive() {
        ValidationErrors errors = validator.validate(Request.post().path("/integer").body(new JsonBody(getNumbers("exclusiveMinimum",new BigDecimal(5)))));
//        System.out.println("errors = " + errors);
        assertEquals(1,errors.size());
        ValidationError e = errors.get(0);
        assertTrue(e.getMessage().contains("exclusive"));
    }

    @Test
    public void invalidMinimumInBody() {
        ValidationErrors errors = validator.validate(Request.post().path("/integer").body(new JsonBody(getNumbers("minimum",new BigDecimal(3)))));
//        System.out.println("errors = " + errors);
        assertEquals(1,errors.size());
        ValidationError e = errors.get(0);
        assertTrue(e.getMessage().contains("minimum"));
    }

    @Test
    public void validMaximumInBody() {
        ValidationErrors errors = validator.validate(Request.post().path("/integer").body(new JsonBody(getNumbers("maximum",new BigDecimal(3)))));
//        System.out.println("errors = " + errors);
        assertEquals(0,errors.size());
    }

    @Test
    public void validMaximumInBodyExact() {
        ValidationErrors errors = validator.validate(Request.post().path("/integer").body(new JsonBody(getNumbers("maximum",new BigDecimal(5)))));
//        System.out.println("errors = " + errors);
        assertEquals(0,errors.size());
    }

    @Test
    public void invalidMaximumInBodyExactExclusive() {
        ValidationErrors errors = validator.validate(Request.post().path("/integer").body(new JsonBody(getNumbers("exclusiveMaximum",new BigDecimal(5)))));
//        System.out.println("errors = " + errors);
        assertEquals(1,errors.size());
        ValidationError e = errors.get(0);
        assertTrue(e.getMessage().contains("exclusive"));
    }

    @Test
    public void invalidMaximumInBody() {
        ValidationErrors errors = validator.validate(Request.post().path("/integer").body(new JsonBody(getNumbers("maximum",new BigDecimal(13)))));
//        System.out.println("errors = " + errors);
        assertEquals(1,errors.size());
        ValidationError e = errors.get(0);
        assertTrue(e.getMessage().contains("maximum"));
    }



    private JsonNode getNumbers(String name, BigDecimal n) {
        ObjectNode root = objectMapper.createObjectNode();
        root.put(name,n);
        return root;
    }

    private JsonNode getStrings(String name, String value) {
        ObjectNode root = objectMapper.createObjectNode();
        root.put(name,value);
        return root;
    }

    private InputStream getResourceAsStream(String fileName) {
        return this.getClass().getResourceAsStream(fileName);
    }

}