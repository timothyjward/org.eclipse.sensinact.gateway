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

package org.eclipse.sensinact.gateway.core.security.test;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;

import org.eclipse.sensinact.gateway.common.primitive.Describable;
import org.eclipse.sensinact.gateway.core.ActionResource;
import org.eclipse.sensinact.gateway.core.Resource;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 *
 * @author <a href="mailto:christophe.munilla@cea.fr">Christophe Munilla</a>
 */
public class TestSecurity {
	// ********************************************************************//
	// NESTED DECLARATIONS //
	// ********************************************************************//

	// ********************************************************************//
	// ABSTRACT DECLARATIONS //
	// ********************************************************************//

	// ********************************************************************//
	// STATIC DECLARATIONS //
	// ********************************************************************//

	// ********************************************************************//
	// INSTANCE DECLARATIONS //
	// ********************************************************************//

	Method getDescription = null;
	Method getMethod = null;
	Method setMethod = null;
	Method actMethod = null;

	public TestSecurity() throws Exception {
		super();
		getDescription = Describable.class.getDeclaredMethod("getDescription");
		getMethod = Resource.class.getDeclaredMethod("get", new Class<?>[] { String.class });
		setMethod = Resource.class.getDeclaredMethod("set", new Class<?>[] { String.class, Object.class });
		actMethod = ActionResource.class.getDeclaredMethod("act", new Class<?>[] { Object[].class });
	}

	/**
	 * @inheritDoc
	 *
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
	 *
	 * @see MidOSGiTest#doInit(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	protected void doInit(Map configuration) {
		configuration.put("org.osgi.framework.system.packages.extra",
				"org.eclipse.sensinact.gateway.test," + "com.sun.net.httpserver," + "javax.net.ssl,"
						+ "javax.xml.parsers," + "javax.imageio," + "javax.management," + "javax.naming," + "javax.sql,"
						+ "javax.swing," + "javax.swing.border," + "javax.swing.event," + "javax.mail,"
						+ "javax.mail.internet," + "javax.management.modelmbean," + "javax.management.remote,"
						+ "javax.xml.parsers," + "javax.security.auth," + "javax.security.cert," + "junit.framework,"
						+ "junit.textui," + "org.w3c.dom," + "org.xml.sax," + "org.xml.sax.helpers," + "sun.misc,"
						+ "sun.security.action");

		configuration.put("org.eclipse.sensinact.simulated.gui.enabled", "false");

		configuration.put("org.eclipse.sensinact.gateway.security.jks.filename", "target/felix/bundle/keystore.jks");
		configuration.put("org.eclipse.sensinact.gateway.security.jks.password", "sensiNact_team");

		configuration.put("org.eclipse.sensinact.gateway.security.database",
				new File("src/test/resources/sensinact.sqlite").getAbsolutePath());

		configuration.put("felix.auto.start.1",
				"file:target/felix/bundle/org.osgi.compendium.jar "
						+ "file:target/felix/bundle/org.apache.felix.framework.security.jar "
						+ "file:target/felix/bundle/org.apache.felix.configadmin.jar "
						+ "file:target/felix/bundle/org.apache.felix.fileinstall.jar");

		configuration.put("felix.auto.install.2",
				"file:target/felix/bundle/sensinact-utils.jar "
						+ "file:target/felix/bundle/sensinact-datastore-api.jar "
						+ "file:target/felix/bundle/sensinact-sqlite-connector.jar "
						+ "file:target/felix/bundle/sensinact-common.jar "
						+ "file:target/felix/bundle/sensinact-framework-extension.jar "
						+ "file:target/felix/bundle/dynamicBundle.jar");

		configuration.put("felix.auto.start.2", "file:target/felix/bundle/sensinact-test-configuration.jar "
				+ "file:target/felix/bundle/sensinact-signature-validator.jar ");

		configuration.put("felix.auto.start.3",
				"file:target/felix/bundle/sensinact-core.jar " + "file:target/felix/bundle/sensinact-generic.jar ");

		configuration.put("felix.auto.start.4", "file:target/felix/bundle/slider.jar "
				+ "file:target/felix/bundle/fan.jar " + "file:target/felix/bundle/button.jar ");
	}

	@Disabled
	@Test
	public void testSecurityAccessInitialization() throws Throwable {
//		MidProxy<Core> mid = new MidProxy<Core>(classloader, this, Core.class);
//
//		Core core = mid.buildProxy();
//		Session session = core.getAnonymousSession();
//		assertNotNull(session);
//
//		Set providers = session.serviceProviders();
//		Iterator iterator = providers.iterator();
//
//		while (iterator.hasNext()) {
//			MidProxy<ServiceProvider> provider = new MidProxy<ServiceProvider>(classloader, this,
//					ServiceProvider.class);
//
//			ServiceProvider serviceProvider = provider.buildProxy(iterator.next());
//
//			System.out.println(serviceProvider.getDescription().getJSON());
//		}
//		System.out.println("============================================");
//
//		MidProxy<Authentication> midCredentials = new MidProxy<Authentication>(classloader, this, Authentication.class);
//
//		midCredentials.buildProxy(Credentials.class.getCanonicalName(), new Class<?>[] { String.class, String.class },
//				new Object[] { "cea", "sensiNact_team" });
//
//		Method method = mid.getContextualizedType().getDeclaredMethod("getSession",
//				new Class<?>[] { midCredentials.getContextualizedType() });
//
//		session = (Session) mid.toOSGi(method, new Object[] { midCredentials.getInstance() });
//
//		assertNotNull(session);
//
//		providers = session.serviceProviders();
//		iterator = providers.iterator();
//
//		while (iterator.hasNext()) {
//			MidProxy<ServiceProvider> provider = new MidProxy<ServiceProvider>(classloader, this,
//					ServiceProvider.class);
//
//			ServiceProvider serviceProvider = provider.buildProxy(iterator.next());
//
//			System.out.println(serviceProvider.getDescription().getJSON());
//		}

	}

}
