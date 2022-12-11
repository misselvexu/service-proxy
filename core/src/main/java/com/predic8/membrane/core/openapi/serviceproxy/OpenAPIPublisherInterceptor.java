package com.predic8.membrane.core.openapi.serviceproxy;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;
import com.predic8.membrane.core.exchange.*;
import com.predic8.membrane.core.http.*;
import com.predic8.membrane.core.interceptor.*;
import groovy.text.*;
import io.swagger.v3.parser.*;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import static com.predic8.membrane.core.interceptor.Outcome.*;
import static com.predic8.membrane.core.openapi.util.OpenAPIUtil.getIdFromAPI;
import static com.predic8.membrane.core.openapi.util.Utils.getResourceAsStream;

public class OpenAPIPublisherInterceptor extends AbstractInterceptor {

    public static final String HTML_UTF_8 = "text/html; charset=utf-8";
    private final ObjectMapper om = new ObjectMapper();
    private final ObjectWriter ow = new ObjectMapper().writerWithDefaultPrettyPrinter();
    private final ObjectMapper omYaml = ObjectMapperFactory.createYaml();

    public static final String PATH = "/openapi-spec";
    public static final String PATH_UI = "/openapi-spec/ui";

    private static final Pattern patternMeta = Pattern.compile(PATH + "/(.*)");
    private static final Pattern patternUI = Pattern.compile(PATH_UI + "/(.*)");

    protected Map<String,OpenAPIRecord> recs = new HashMap<>();

    private Template swaggerUiHtmlTemplate;
    private Template apiOverviewHtmlTemplate;

    public OpenAPIPublisherInterceptor(List<OpenAPIRecord> apis) throws IOException, ClassNotFoundException {
        createApiMap(apis);

        swaggerUiHtmlTemplate = createTemplate("/openapi/swagger-ui.html");
        apiOverviewHtmlTemplate = createTemplate("/openapi/overview.html");
    }

    private Template createTemplate(String filePath) throws ClassNotFoundException, IOException {
        return new StreamingTemplateEngine().createTemplate(new InputStreamReader(getResourceAsStream(this,filePath)));
    }

    @Override
    public Outcome handleRequest(Exchange exc) throws Exception {

        if (!exc.getRequest().getUri().startsWith(PATH))
            return CONTINUE;

        if (exc.getRequest().getUri().startsWith(PATH_UI)) {
            Matcher m =  patternUI.matcher(exc.getRequest().getUri());
            if (!m.matches()) { // No id specified
                exc.setResponse(Response.ok().contentType("application/json").body("Please specify an Id").build());
                return RETURN;
            }

            String id = m.group(1);

            Map<String,Object> tempCtx = new HashMap<>();
            tempCtx.put("openApiUrl",PATH + "/" + id);

            exc.setResponse(Response.ok().contentType(HTML_UTF_8).body(swaggerUiHtmlTemplate.make(tempCtx).toString()).build());

            return RETURN;
        }

        Matcher m =  patternMeta.matcher(exc.getRequest().getUri());
        if (!m.matches()) { // No id specified
            ObjectNode obj = createDictionaryOfAPIs();

            if (acceptsHtmlExplicit(exc)) {
                Map<String,Object> tempCtx = new HashMap<>();
                tempCtx.put("apis",recs);
                tempCtx.put("openApiUrl",PATH + "/" + id);
                exc.setResponse(Response.ok().contentType(HTML_UTF_8).body(apiOverviewHtmlTemplate.make(tempCtx).toString()).build());

            } else {
                exc.setResponse(Response.ok().contentType("application/json").body(ow.writeValueAsBytes(obj)).build());
            }
            return RETURN;
        }

        String id = m.group(1);

        OpenAPIRecord api = recs.get(id);

        if (api == null) {
            exc.setResponse(Response.notFound().body("not found").build());
            return RETURN;
        }

        exc.setResponse(Response.ok().contentType("application/x-yaml").body(omYaml.writeValueAsBytes(api.node)).build());

        return RETURN;
    }

    private ObjectNode createDictionaryOfAPIs() {
        ObjectNode top = om.createObjectNode();
        for (Map.Entry<String, OpenAPIRecord> e : recs.entrySet()) {
            ObjectNode apiDetails = top.putObject(e.getKey());
            apiDetails.put("openapi", e.getValue().node.get("openapi").asText());
            apiDetails.put("title", e.getValue().node.get("info").get("title").asText());
            apiDetails.put("version", e.getValue().node.get("info").get("version").asText());
            apiDetails.put("openapi_link", PATH + "/" + e.getKey());
            apiDetails.put("ui_link", PATH + "/ui/" + e.getKey());
        }
        return top;
    }

    private void createApiMap(List<OpenAPIRecord> apis) {
        for (OpenAPIRecord rec : apis) {
            this.recs.put(getIdFromAPI(rec.api),rec);
        }
    }

    /**
     * In the Accept Header is html explicitly mentioned.
     */
    private boolean acceptsHtmlExplicit(Exchange exc) {
        if (exc.getRequest().getHeader().getAccept() == null)
            return false;
        return exc.getRequest().getHeader().getAccept().contains("html");
    }
}
