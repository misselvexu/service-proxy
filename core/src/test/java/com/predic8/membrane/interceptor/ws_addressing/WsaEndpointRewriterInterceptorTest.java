package com.predic8.membrane.interceptor.ws_addressing;

import com.predic8.membrane.core.exchange.Exchange;
import com.predic8.membrane.core.http.Body;
import com.predic8.membrane.core.interceptor.DispatchingInterceptor;
import com.predic8.membrane.core.interceptor.Outcome;
import com.predic8.membrane.core.interceptor.ws_addressing.DecoupledEndpointRegistry;
import com.predic8.membrane.core.interceptor.ws_addressing.WsaEndpointRewriterInterceptor;
import com.predic8.membrane.core.rules.Rule;
import com.predic8.membrane.core.rules.SOAPProxy;
import com.predic8.membrane.core.rules.ServiceProxy;
import com.predic8.membrane.core.rules.ServiceProxyKey;
import com.predic8.membrane.core.util.MessageUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

@Ignore
public class WsaEndpointRewriterInterceptorTest {
    private WsaEndpointRewriterInterceptor rewriter;
    private Exchange exc;

    @Before
    public void setUp() {
        rewriter = new WsaEndpointRewriterInterceptor();
        exc = new Exchange(null);
    }

    @Test
    public void testRewriterInterceptor() throws Exception {
        exc.setRequest(MessageUtil.getPostRequest("http://localhost:9000/SoapContext/SoapPort?wsdl"));
        InputStream input = WsaEndpointRewriterTest.class.getResourceAsStream("/interceptor/ws_addressing/body.xml");
        exc.getRequest().setBody(new Body(input));

        assertEquals(Outcome.CONTINUE, rewriter.handleRequest(exc));
        assertEquals(exc.getProperty("messageId"), "urn:uuid:62a0de08-055a-4da7-aefa-730af9dbc6b6");
    }
}