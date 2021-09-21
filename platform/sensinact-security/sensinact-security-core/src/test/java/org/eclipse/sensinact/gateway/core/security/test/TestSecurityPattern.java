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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.Set;

import org.eclipse.sensinact.gateway.core.Core;
import org.eclipse.sensinact.gateway.core.ServiceProvider;
import org.eclipse.sensinact.gateway.core.Session;
import org.eclipse.sensinact.gateway.core.security.Credentials;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 *
 * @author <a href="mailto:christophe.munilla@cea.fr">Christophe Munilla</a>
 */
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
public class TestSecurityPattern {
	// ********************************************************************//
	// NESTED DECLARATIONS //
	// ********************************************************************//

	// ********************************************************************//
	// ABSTRACT DECLARATIONS //
	// ********************************************************************//

	// ********************************************************************//
	// STATIC DECLARATIONS //
	// ********************************************************************//

	private static final String SLIDERS_DEFAULT = "[\"slider01\",\"slider02\",\"slider11\"]";
	private static final String SLIDERS_PROP = "org.eclipse.sensinact.simulated.sliders";
	private static final String GUI_ENABLED = "org.eclipse.sensinact.simulated.gui.enabled";

	// ********************************************************************//
	// INSTANCE DECLARATIONS //
	// ********************************************************************//

	@Test
	public void testSecurityAccessWithPattern(@InjectService Core core) throws Throwable {
		// slider[0-9]{2} - authenticated access level
		// slider[0-9]{2}/admin - admin authenticated access level
		// cea user is admin on slider[0-9]{2}

		// slider0[0-9] - authenticated access level
		// slider0[0-9]/cursor - authenticated access level
		// fake user is authenticated on slider0[0-9]

		// slider1[0-9] - authenticated access level
		// slider1[0-9]/cursor - authenticated access level
		// fake2 user is authenticated on slider1[0-9]

//		MidProxy<Core> mid = new MidProxy<Core>(classloader, this, Core.class);
//
//		Core core = mid.buildProxy();
		Session session = core.getAnonymousSession();
		assertNotNull(session);

		Set providers = session.serviceProviders();
		System.out.println("====================================>>>>>");
		System.out.println(providers);
		System.out.println("====================================>>>>>");
		assertTrue(providers.isEmpty());
//
//		// ******************************************************
//		// admin
//		// the admin user is suppose to see every thing
//		// service providers and services
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
		Credentials credentials = new Credentials("cea", "sensiNact_team");
		session = core.getSession(credentials);
		
		assertNotNull(session);
//
		providers = session.serviceProviders();
		assertEquals(3, providers.size());
		Iterator<ServiceProvider> iterator = providers.iterator();

//		while (iterator.hasNext()) {
//			MidProxy<ServiceProvider> provider = new MidProxy<ServiceProvider>(classloader, this,
//					ServiceProvider.class);
//
//			ServiceProvider serviceProvider = provider.buildProxy(iterator.next());
//			assertEquals(2, serviceProvider.getServices().size());
//			System.out.println(serviceProvider.getDescription().getJSON());
//		}

		while (iterator.hasNext()) {
			ServiceProvider serviceProvider = iterator.next();
			assertEquals(2, serviceProvider.getServices().size());
			System.out.println(serviceProvider.getDescription().getJSON());
		}
//
//		// *************************************
//		// fake
//		// the fake user is suppose to see only two service providers
//		// and only the cursor service for each one
//		midCredentials = new MidProxy<Authentication>(classloader, this, Authentication.class);
//
//		midCredentials.buildProxy(Credentials.class.getCanonicalName(), new Class<?>[] { String.class, String.class },
//				new Object[] { "fake", "fake" });
//
//		method = mid.getContextualizedType().getDeclaredMethod("getSession",
//				new Class<?>[] { midCredentials.getContextualizedType() });
//
		
//		session = (Session) mid.toOSGi(method, new Object[] { midCredentials.getInstance() });
//
		credentials = new Credentials("fake", "fake");
		session = core.getSession(credentials);
		assertNotNull(session);
//
		providers = session.serviceProviders();

		assertEquals(2, providers.size());
		iterator = providers.iterator();
//
//		while (iterator.hasNext()) {
//			MidProxy<ServiceProvider> provider = new MidProxy<ServiceProvider>(classloader, this,
//					ServiceProvider.class);
//
//			ServiceProvider serviceProvider = provider.buildProxy(iterator.next());
//			assertEquals(1, serviceProvider.getServices().size());
//			System.out.println(serviceProvider.getDescription().getJSON());
//		}
		while (iterator.hasNext()) {

			ServiceProvider serviceProvider = iterator.next();
			assertEquals(1, serviceProvider.getServices().size());
			System.out.println(serviceProvider.getDescription().getJSON());
		}
//
//		// ***************************************
//		// fake2
//		// the fake2 user is suppose to see only one service provider
//		// and only its cursor service
//		midCredentials = new MidProxy<Authentication>(classloader, this, Authentication.class);
//
//		midCredentials.buildProxy(Credentials.class.getCanonicalName(), new Class<?>[] { String.class, String.class },
//				new Object[] { "fake2", "fake2" });
//
//		method = mid.getContextualizedType().getDeclaredMethod("getSession",
//				new Class<?>[] { midCredentials.getContextualizedType() });
//
//		session = (Session) mid.toOSGi(method, new Object[] { midCredentials.getInstance() });
//
//		assertNotNull(session);
//
//		providers = session.serviceProviders();
//		assertEquals(1, providers.size());
//		iterator = providers.iterator();
//
//		while (iterator.hasNext()) {
//			MidProxy<ServiceProvider> provider = new MidProxy<ServiceProvider>(classloader, this,
//					ServiceProvider.class);
//
//			ServiceProvider serviceProvider = provider.buildProxy(iterator.next());
//			assertEquals(1, serviceProvider.getServices().size());
//			System.out.println(serviceProvider.getDescription().getJSON());
//		}
		
		credentials = new Credentials("fake2", "fake2");
		session = core.getSession(credentials);
		assertNotNull(session);

		providers = session.serviceProviders();

		assertEquals(2, providers.size());
		iterator = providers.iterator();

		while (iterator.hasNext()) {

			ServiceProvider serviceProvider = iterator.next();
			assertEquals(1, serviceProvider.getServices().size());
			System.out.println(serviceProvider.getDescription().getJSON());
		}
	}
}
