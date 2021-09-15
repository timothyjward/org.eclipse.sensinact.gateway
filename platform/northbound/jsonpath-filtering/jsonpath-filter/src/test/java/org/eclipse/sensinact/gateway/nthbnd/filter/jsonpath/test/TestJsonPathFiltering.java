/*
* Copyright (c) 2020 Kentyou.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
*    Kentyou - initial API and implementation
 */
package org.eclipse.sensinact.gateway.nthbnd.filter.jsonpath.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.eclipse.sensinact.gateway.nthbnd.filter.jsonpath.http.test.HttpServiceTestClient;
import org.eclipse.sensinact.gateway.nthbnd.filter.jsonpath.ws.test.WsServiceTestClient;
import org.eclipse.sensinact.gateway.util.IOUtils;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * @author <a href="mailto:christophe.munilla@cea.fr">Christophe Munilla</a>
 */
public class TestJsonPathFiltering {
    //********************************************************************//
    //						NESTED DECLARATIONS			  			      //
    //********************************************************************//
    //********************************************************************//
    //						ABSTRACT DECLARATIONS						  //
    //********************************************************************//
    //********************************************************************//
    //						STATIC DECLARATIONS							  //
    //********************************************************************//

    protected static final String HTTP_ROOTURL = "http://127.0.0.1:8899";
    protected static final String WS_ROOTURL = "/sensinact";

    //********************************************************************//
    //						INSTANCE DECLARATIONS						  //
    //********************************************************************//

    /**
     * @throws MalformedURLException
     * @throws IOException
     */
    public TestJsonPathFiltering() throws Exception {
        super();
    }

    /**
     * @inheritDoc
     * @see MidOSGiTest#isExcluded(java.lang.String)
     */
    public boolean isExcluded(String fileName) {
        if ("org.apache.felix.framework.security.jar".equals(fileName)) {
            return true;
        }
        return false;
    }

    /**
     * @inheritDoc
     * @see MidOSGiTest#doInit(java.util.Map)
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected void doInit(Map configuration) {
        configuration.put("felix.auto.start.1",  
                "file:target/felix/bundle/org.osgi.service.component.jar "+  
                "file:target/felix/bundle/org.osgi.service.cm.jar "+  
                "file:target/felix/bundle/org.osgi.service.metatype.jar "+  
                "file:target/felix/bundle/org.osgi.namespace.extender.jar "+  
                "file:target/felix/bundle/org.osgi.util.promise.jar "+  
                "file:target/felix/bundle/org.osgi.util.function.jar "+  
                "file:target/felix/bundle/org.osgi.util.pushstream.jar "+
                "file:target/felix/bundle/org.osgi.service.log.jar "  +
                "file:target/felix/bundle/org.apache.felix.log.jar " + 
                "file:target/felix/bundle/org.apache.felix.scr.jar " +
        		"file:target/felix/bundle/org.apache.felix.fileinstall.jar " +
        		"file:target/felix/bundle/org.apache.felix.configadmin.jar " + 
        		"file:target/felix/bundle/org.apache.felix.framework.security.jar ");
        configuration.put("felix.auto.install.2",  
        	    "file:target/felix/bundle/org.eclipse.paho.client.mqttv3.jar " + 
                "file:target/felix/bundle/mqtt-utils.jar " + 
        	    "file:target/felix/bundle/sensinact-utils.jar " + 
                "file:target/felix/bundle/sensinact-common.jar " + 
        	    "file:target/felix/bundle/sensinact-datastore-api.jar " + 
                "file:target/felix/bundle/sensinact-security-none.jar " + 
                "file:target/felix/bundle/sensinact-generic.jar " + 
                "file:target/felix/bundle/slf4j-api.jar " + 
                "file:target/felix/bundle/slf4j-simple.jar");
        configuration.put("felix.auto.start.2", 
        		"file:target/felix/bundle/sensinact-signature-validator.jar " + 
        		"file:target/felix/bundle/sensinact-core.jar ");
        configuration.put("felix.auto.start.3", 
        		"file:target/felix/bundle/org.apache.felix.http.servlet-api.jar " + 
                "file:target/felix/bundle/org.apache.felix.http.jetty.jar " + 
        		"file:target/felix/bundle/http.jar " +
        		"file:target/felix/bundle/sensinact-northbound-access.jar " + 
                "file:target/felix/bundle/rest-access.jar");
        configuration.put("felix.auto.start.4",
                "file:target/felix/bundle/slider.jar " + 
        		"file:target/felix/bundle/light.jar " +
                "file:target/felix/bundle/jpath.jar " + 
        		"file:target/felix/bundle/json-provider-jsonorg.jar " +
        		"file:target/felix/bundle/dynamicBundle.jar ");
        configuration.put("org.eclipse.sensinact.gateway.security.jks.filename", "target/felix/bundle/keystore.jks");
        configuration.put("org.eclipse.sensinact.gateway.security.jks.password", "sensiNact_team");

        configuration.put("org.eclipse.sensinact.gateway.location.latitude", "45.2d");
        configuration.put("org.eclipse.sensinact.gateway.location.longitude", "5.7d");

        configuration.put("org.osgi.service.http.port", "8899");
        configuration.put("org.apache.felix.http.jettyEnabled", true);
        configuration.put("org.apache.felix.http.whiteboardEnabled", true);

        try {
        	String fileName = "sensinact.config";
            File testFile = new File(new File("src/test/resources"), fileName);
            URL testFileURL = testFile.toURI().toURL();
            FileOutputStream output = new FileOutputStream(new File(loadDir,fileName));
            byte[] testCng = IOUtils.read(testFileURL.openStream(), true);
            IOUtils.write(testCng, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHttpFiltered() throws Exception {

        String simulated3 = HttpServiceTestClient.newRequest( HTTP_ROOTURL + "/sensinact?jsonpath=$.[?(@.name=='slider')]", null, "GET");
        JSONObject response = new JSONObject("{\"filters\":[{\"definition\":\"$.[?(@.name=='slider')]\",\"type\":\"jsonpath\"}]," + "\"providers\":" + "[{\"name\":\"slider\",\"services\":[{\"name\":\"admin\"," + "\"resources\":" + "[{\"name\":\"friendlyName\",\"type\":\"PROPERTY\"}," + "{\"name\":\"location\",\"type\":\"PROPERTY\"}," + "{\"name\":\"bridge\",\"type\":\"PROPERTY\"}," + "{\"name\":\"icon\",\"type\":\"PROPERTY\"}]}," + "{\"name\":\"cursor\",\"resources\":" + "[{\"name\":\"position\",\"type\":\"SENSOR\"}]" + "}]" + ",\"location\":\"45.2:5.7\"}]}");
        JSONAssert.assertEquals(response, new JSONObject(simulated3), false);

        String simulated1 = HttpServiceTestClient.newRequest( HTTP_ROOTURL + "/sensinact/providers", null, "GET");
        response = new JSONObject("{\"statusCode\":200,\"providers\":[\"slider\",\"light\"]," + "\"type\":\"PROVIDERS_LIST\",\"uri\":\"/\"}");
        JSONAssert.assertEquals(response, new JSONObject(simulated1), false);

        String simulated2 = HttpServiceTestClient.newRequest( HTTP_ROOTURL + "/sensinact/providers?jsonpath=$.[:1]", null, "GET");
        response = new JSONObject("{\"statusCode\":200,\"providers\":[\"" + new JSONObject(simulated1).getJSONArray("providers").getString(0) + "\"]," + "\"filters\":[{\"type\":\"jsonpath\", \"definition\":\"$.[:1]\"}], " + "\"type\":\"PROVIDERS_LIST\",\"uri\":\"/\"}");
        JSONAssert.assertEquals(response, new JSONObject(simulated2), false);
    }

    @Test
    public void testWsFiltered() throws Exception {
        JSONObject response;
        WsServiceTestClient client = new WsServiceTestClient();

        new Thread(client).start();
        String simulated3 = this.synchronizedRequest(client, "/sensinact", "[{\"name\":\"jsonpath\",\"type\":\"string\",\"value\":\"$.[?(@.name=='slider')]\"}]");

        //System.out.println(simulated3);

        response = new JSONObject("{\"filters\":[{\"definition\":\"$.[?(@.name=='slider')]\",\"type\":\"jsonpath\"}]," + "\"providers\":" + "[{\"name\":\"slider\",\"services\":[{\"name\":\"admin\"," + "\"resources\":" + "[{\"name\":\"friendlyName\",\"type\":\"PROPERTY\"}," + "{\"name\":\"location\",\"type\":\"PROPERTY\"}," + "{\"name\":\"bridge\",\"type\":\"PROPERTY\"}," + "{\"name\":\"icon\",\"type\":\"PROPERTY\"}]}," + "{\"name\":\"cursor\"," + "\"resources\":" + "[{\"name\":\"position\",\"type\":\"SENSOR\"}]}]" + ",\"location\":\"45.2:5.7\"}]}");
        JSONObject obj = new JSONObject(simulated3);
        JSONAssert.assertEquals(response, obj, false);

        String simulated1 = this.synchronizedRequest(client, "/sensinact/providers", null);

        response = new JSONObject("{\"statusCode\":200,\"providers\":[\"slider\",\"light\"]," + "\"type\":\"PROVIDERS_LIST\",\"uri\":\"/\"}");
        //System.out.println(simulated1);
        obj = new JSONObject(simulated1);
        obj.remove("X-Auth-Token");
        JSONAssert.assertEquals(response, obj, false);
        String simulated2 = this.synchronizedRequest(client, "/sensinact/providers", "[{\"name\":\"jsonpath\",\"type\":\"string\",\"value\":\"$.[:1]\"}]");
        response = new JSONObject("{\"statusCode\":200,\"providers\":[\"" + new JSONObject(simulated1).getJSONArray("providers").getString(0) + "\"]," + "\"filters\":[{\"type\":\"jsonpath\", \"definition\":\"$.[:1]\"}], " + "\"type\":\"PROVIDERS_LIST\",\"uri\":\"/\"}");

        obj = new JSONObject(simulated2);
        obj.remove("X-Auth-Token");

        //System.out.println(simulated2);
        JSONAssert.assertEquals(response, obj, false);

        client.close();
    }

    private String synchronizedRequest(WsServiceTestClient client, String url, String content) {
        String simulated = null;
        long wait = 1000;

        client.newRequest(url, content);

        while (!client.isAvailable() && wait > 0) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
        }
        if (client.isAvailable()) {
            simulated = client.getResponseMessage();
        }
        return simulated;
    }
}
