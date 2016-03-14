package com.keithcollison.main;

import au.com.dius.pact.consumer.Pact;
import au.com.dius.pact.consumer.PactProviderRule;
import au.com.dius.pact.consumer.PactVerification;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.model.PactFragment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class ConsumerTest {


    private static final int ALT_PORT = 8086;
    private static final int ORIG_PORT = 8085;
    private static final String COLOR_PROVIDER = "color-provider";
    private static final String SHAPE_PROVIDER = "shape-provider";
    @Rule
    public PactProviderRule provider = new PactProviderRule(SHAPE_PROVIDER, "localhost", ORIG_PORT, this);

    @Rule
    public PactProviderRule altProvider = new PactProviderRule(COLOR_PROVIDER, "localhost", ALT_PORT, this);


    @Pact(provider = COLOR_PROVIDER, consumer = "test_consumer")
    public PactFragment createAltFragment(PactDslWithProvider builder) {

        return builder
                .uponReceiving("ExampleJavaConsumerPactRuleTest test interaction")
                .path("/color/red")
                .method("GET")
                .willRespondWith()
                .body("{\"name\":\"red\"}")
                .status(200)
                .toFragment();
    }
    @Pact(provider = SHAPE_PROVIDER, consumer = "test_consumer")
    public PactFragment createFragment(PactDslWithProvider builder) {
        return builder
                .uponReceiving("ExampleJavaConsumerPactRuleTest test interaction")
                .path("/shape/circle")
                .method("GET")
                .willRespondWith()
                .body("{\"name\":\"circle\"}")
                .status(200)
                .toFragment();
    }

    @Test
    @PactVerification(SHAPE_PROVIDER)
    public void runShapeTest() throws IOException {
        Map expectedResponse = new HashMap();

        expectedResponse.put("name","circle");
        assertEquals(new ConsumerClient("http://localhost:" + ORIG_PORT).getAsMap("/shape/circle"), expectedResponse);
    }
    @Test
    @PactVerification(COLOR_PROVIDER)
    public void runColorTest() throws IOException {
        Map expectedResponse = new HashMap();

        expectedResponse.put("name", "red");
        assertEquals(new ConsumerClient("http://localhost:" + ALT_PORT).getAsMap("/color/red"), expectedResponse);

    }
}