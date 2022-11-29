package com.predic8.membrane.core.openapi.model;

import com.fasterxml.jackson.databind.*;

import java.io.*;

public class Response extends Message<Response> {

    private int statusCode;

    public Response(int statusCode) {
        this.statusCode = statusCode;
    }

    public Response(int statusCode, String mediaType) {
        this.statusCode = statusCode;
        this.mediaType = mediaType;
    }

    public static Response statusCode(int statusCode) {
        return new Response(statusCode);
    }

    public static Response statusCode(int statusCode, String mediaType) {
        return new Response(statusCode, mediaType);
    }

    public Response body(Body body) {
        this.body = body;
        return this;
    }

    public Response body(InputStream inputStream) {
        this.body = new InputStreamBody(inputStream);
        return this;
    }

    public Response body(JsonNode n) {
        this.body = new JsonBody(n);
        return this;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public boolean sameStatusCode(String other) {
        return Integer.toString(statusCode).equals(other);
    }
}
