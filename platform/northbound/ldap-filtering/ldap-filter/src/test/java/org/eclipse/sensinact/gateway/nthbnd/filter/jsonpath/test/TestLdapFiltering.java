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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.sensinact.gateway.common.bundle.Mediator;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectInstalledBundle;
import org.osgi.test.junit5.context.InstalledBundleExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * @author <a href="mailto:christophe.munilla@cea.fr">Christophe Munilla</a>
 */
@ExtendWith(InstalledBundleExtension.class)
@ExtendWith(ServiceExtension.class)
public class TestLdapFiltering {
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
    public TestLdapFiltering() throws Exception {
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
        		"file:target/felix/bundle/dynamicBundle.jar " + 
                "file:target/felix/bundle/slider.jar " + 
        		"file:target/felix/bundle/light.jar ");
        configuration.put("org.eclipse.sensinact.gateway.security.jks.filename", "target/felix/bundle/keystore.jks");
        configuration.put("org.eclipse.sensinact.gateway.security.jks.password", "sensiNact_team");

        configuration.put("org.eclipse.sensinact.gateway.location.latitude", "45.2d");
        configuration.put("org.eclipse.sensinact.gateway.location.longitude", "5.7d");

        configuration.put("org.osgi.service.http.port", "8899");
        configuration.put("org.apache.felix.http.jettyEnabled", true);
        configuration.put("org.apache.felix.http.whiteboardEnabled", true);
    }

    @Test
    public void testLdapFilter(
    			@InjectInstalledBundle(value = "extra.jar", start = true) Bundle bundle,
    			@InjectBundleContext BundleContext context
    		) throws Exception {
//        File tmpDirectory = new File("./target/felix/tmp");
//
//        new File(tmpDirectory, "props.xml").delete();
//        new File(tmpDirectory, "resources.xml").delete();
//        new File(tmpDirectory, "dynamicBundle.jar").delete();
//
//        super.createDynamicBundle(new File("./extra-src/test/resources/MANIFEST.MF"), tmpDirectory, new File("./extra-src/test/resources/meta"), new File("./src/test/resources/resources.xml"), new File("./target/extra-test-classes"));
//
//        super.installDynamicBundle(new File(tmpDirectory, "dynamicBundle.jar").toURI().toURL()).start();
        Thread.sleep(5000);

        Mediator mediator = new Mediator(context);
        String simulated1 = HttpServiceTestClient.newRequest(mediator, HTTP_ROOTURL + "/sensinact?ldap='(service1.humidity.accessible=false)'", null, "GET");

        System.out.println(simulated1);

        assertTrue(new JSONObject(simulated1).getJSONArray("providers").length() == 0);

        String simulated2 = HttpServiceTestClient.newRequest(mediator, HTTP_ROOTURL + "/sensinact/sensor0/service1/humidity/SET", "[{\"name\":\"attributeName\",\"type\":\"string\", \"value\":\"accessible\"}," + "{\"name\":\"value\",\"type\":\"boolean\", \"value\":false}]", "POST");
        System.out.println(simulated2);

        Thread.sleep(2000);

        simulated1 = HttpServiceTestClient.newRequest(mediator, HTTP_ROOTURL + "/sensinact?ldap='(service1.humidity.accessible=false)'", null, "GET");
        assertTrue(new JSONObject(simulated1).getJSONArray("providers").length() == 1);
        assertEquals("sensor0", new JSONObject(simulated1).getJSONArray("providers").getJSONObject(0).getString("name"));

        System.out.println(simulated1);
        String simulated3 = HttpServiceTestClient.newRequest(mediator, HTTP_ROOTURL + "/sensinact/sensor3/service1/humidity/SET", "[{\"name\":\"attributeName\",\"type\":\"string\", \"value\":\"accessible\"}," + "{\"name\":\"value\",\"type\":\"boolean\", \"value\":false}]", "POST");
        System.out.println(simulated3);

        Thread.sleep(2000);

        simulated1 = HttpServiceTestClient.newRequest(mediator, HTTP_ROOTURL + "/sensinact?ldap='(service1.humidity.accessible=false)'", null, "GET");

        System.out.println(simulated1);
        List<String> list = new ArrayList<String>();
        list.add(new JSONObject(simulated1).getJSONArray("providers").getJSONObject(0).getString("name"));
        list.add(new JSONObject(simulated1).getJSONArray("providers").getJSONObject(1).getString("name"));

        assertTrue(new JSONObject(simulated1).getJSONArray("providers").length() == 2);
        assertTrue(list.contains("sensor0"));
        assertTrue(list.contains("sensor3"));
        String simulated4 = HttpServiceTestClient.newRequest(mediator, HTTP_ROOTURL + "/sensinact/sensor3/service1/temperature/SET", "[{\"name\":\"value\",\"type\":\"float\", \"value\":2.5}]", "POST");

        System.out.println(simulated4);

        Thread.sleep(2000);
        simulated1 = HttpServiceTestClient.newRequest(mediator, HTTP_ROOTURL + "/sensinact?ldap='(service1.temperature.value%20<=%204.0)'", null, "GET");

        System.out.println(simulated1);
        assertTrue(new JSONObject(simulated1).getJSONArray("providers").length() == 1);
        assertEquals("sensor3", new JSONObject(simulated1).getJSONArray("providers").getJSONObject(0).getString("name"));

        String simulated5 = HttpServiceTestClient.newRequest(mediator, HTTP_ROOTURL + "/sensinact/sensor2/service1/temperature/SET", "[{\"name\":\"value\",\"type\":\"float\", \"value\":2.5}]", "POST");
        System.out.println(simulated5);
        Thread.sleep(2000);

        simulated1 = HttpServiceTestClient.newRequest(mediator, HTTP_ROOTURL + "/sensinact?ldap='(service1.temperature.value<=4.0)'", null, "GET");

        list = new ArrayList<String>();
        list.add(new JSONObject(simulated1).getJSONArray("providers").getJSONObject(0).getString("name"));
        list.add(new JSONObject(simulated1).getJSONArray("providers").getJSONObject(1).getString("name"));

        assertTrue(new JSONObject(simulated1).getJSONArray("providers").length() == 2);
        assertTrue(list.contains("sensor2"));
        assertTrue(list.contains("sensor3"));
        System.out.println(simulated1);

        simulated1 = HttpServiceTestClient.newRequest(mediator, HTTP_ROOTURL + "/sensinact?ldap='(%26(service1.humidity.accessible=false)(service1.temperature.value<=4))'", null, "GET");
        assertTrue(new JSONObject(simulated1).getJSONArray("providers").length() == 1);
        assertEquals("sensor3", new JSONObject(simulated1).getJSONArray("providers").getJSONObject(0).getString("name"));

        System.out.println(simulated1);
    }
}
