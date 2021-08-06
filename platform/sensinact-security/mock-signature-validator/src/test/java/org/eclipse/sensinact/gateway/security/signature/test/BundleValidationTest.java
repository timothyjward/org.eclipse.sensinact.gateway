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
package org.eclipse.sensinact.gateway.security.signature.test;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.assertj.core.api.Assertions;
import org.eclipse.sensinact.gateway.common.bundle.Mediator;
import org.eclipse.sensinact.gateway.security.signature.api.BundleValidation;
import org.eclipse.sensinact.gateway.security.signature.internal.KeyStoreManagerException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.FrameworkUtil;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/*
 * signature validation with embedded archive: embedded archives are to be signed by the same signer as the main archive
 * testCheckNOKWithEmbeddedArchive not performed
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
public class BundleValidationTest {
    private static final Map<String, String> CONFIGURATION = new HashMap<String, String>();

    static {
        CONFIGURATION.put("felix.cache.rootdir", "./target/felix");
        CONFIGURATION.put("org.osgi.framework.storage", "felix-cache");
        CONFIGURATION.put("felix.auto.deploy.dir", "./target/felix/bundle");
        CONFIGURATION.put("felix.auto.deploy.action", "install,start");
        CONFIGURATION.put("felix.log.level", "4");
        CONFIGURATION.put("org.osgi.framework.system.packages.extra", "org.eclipse.sensinact.gateway.generic.core;version= \"2.0.0\"," + "org.eclipse.sensinact.gateway.generic.core.impl;version= \"2.0.0\"," + "org.eclipse.sensinact.gateway.generic.core.packet;version=\"2.0.0\"," + "org.eclipse.sensinact.gateway.generic.stream;version= \"2.0.0\"," + "org.eclipse.sensinact.gateway.generic.uri;version= \"2.0.0\"," + "org.eclipse.sensinact.gateway.generic.parser;version= \"2.0.0\"," + "org.eclipse.sensinact.gateway.generic.automata;version= \"2.0.0\"," + "org.eclipse.sensinact.gateway.generic.annotation;version= \"2.0.0\"," + "org.eclipse.sensinact.gateway.generic.local;version= \"2.0.0\"," + "org.eclipse.sensinact.gateway.util;version= \"2.0.0\"," + "org.eclipse.sensinact.gateway.util.constraint;version= \"2.0.0\"," + "org.eclipse.sensinact.gateway.util.crypto;version= \"2.0.0\"," + "org.eclipse.sensinact.gateway.util.json;version= \"2.0.0\"," + "org.eclipse.sensinact.gateway.util.mediator;version= \"2.0.0\"," + "org.eclipse.sensinact.gateway.util.properties;version= \"2.0.0\"," + "org.eclipse.sensinact.gateway.util.reflect;version= \"2.0.0\"," + "org.eclipse.sensinact.gateway.util.rest;version= \"2.0.0\"," + "org.eclipse.sensinact.gateway.util.xml;version= \"2.0.0\"," + "json-20140107.jar;version= \"2.0.0\"," + "org.json;version;version= \"2.0.0\"," + "org.json.zip;version=\"2.0.0\"");
    }

    private Mediator mediator = null;
    private BundleValidation jval = null;
    private Bundle fan = null;
    private Bundle button = null;
//    private static final String DEFAULT_KEYSTORE_FILE_PATH = "../sensinact-security/cert/keystore.jks";
    private static final String DEFAULT_KEYSTORE_PASSWORD = "sensiNact_team";
	private BundleContext ctx;



    @Test
    public void testCheckFanOK(@InjectService(timeout = 500,filter = "(type=mock)")  BundleValidation jval ) throws BundleException {
        this.fan = ctx.installBundle("file:./target/extra/fan.jar");
        ////logger.log(Level.INFO, "testCheckOK");
        String result = null;
        try {
            result = jval.check(this.fan);
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            this.fan.uninstall();
        }
        Assertions.assertThat(result).isNotNull();
    
    }

    @Test
    public void testCheckButtonOK() throws BundleException {
        this.button = ctx.installBundle("file:./target/extra/button.jar");

        ////logger.log(Level.INFO, "testCheckOK");
        String result = null;
        try {
            result = jval.check(this.button);
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            this.button.uninstall();
        }
        Assertions.assertThat(result).isNotNull();

    }
}
