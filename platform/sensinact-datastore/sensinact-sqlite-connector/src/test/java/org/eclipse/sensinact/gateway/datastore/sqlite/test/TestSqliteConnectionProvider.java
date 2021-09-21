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
package org.eclipse.sensinact.gateway.datastore.sqlite.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;


import java.sql.Connection;

import org.eclipse.sensinact.gateway.common.bundle.Mediator;
import org.eclipse.sensinact.gateway.datastore.api.UnableToConnectToDataStoreException;
import org.eclipse.sensinact.gateway.datastore.api.UnableToFindDataStoreException;
import org.eclipse.sensinact.gateway.datastore.sqlite.SQLiteConnectionProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.service.log.LogService;

public class TestSqliteConnectionProvider {
	private static final String LOG_FILTER = "(" + Constants.OBJECTCLASS + "=" + LogService.class.getCanonicalName()
			+ ")";

	private static final String FAKE_DATABASE_PATH = "target/test-resources/fake.db";
	private static final String TEST_DATABASE_PATH = "target/test-resources/sample.db";

	private static final String MOCK_BUNDLE_NAME = "MockedBundle";
	private static final long MOCK_BUNDLE_ID = 1;

	private final BundleContext context = Mockito.mock(BundleContext.class);
	private final Bundle bundle = Mockito.mock(Bundle.class);

	private SQLiteConnectionProvider sqliteProvider;
	private Mediator mediator;

	@BeforeEach
	public void init() throws UnableToConnectToDataStoreException, InvalidSyntaxException {
		Filter filter = Mockito.mock(Filter.class);
		Mockito.when(filter.toString()).thenReturn(LOG_FILTER);

		Mockito.when(context.createFilter(LOG_FILTER)).thenReturn(filter);
		Mockito.when(context.getServiceReferences((String) null, LOG_FILTER)).thenReturn(null);
		Mockito.when(context.getServiceReference(LOG_FILTER)).thenReturn(null);
		Mockito.when(context.getProperty(Mockito.eq("org.eclipse.sensinact.gateway.security.database")))
				.thenReturn("src/test/resources/sample.db");

		Mockito.when(context.getBundle()).thenReturn(bundle);
		Mockito.when(bundle.getSymbolicName()).thenReturn(MOCK_BUNDLE_NAME);
		Mockito.when(bundle.getBundleId()).thenReturn(MOCK_BUNDLE_ID);

		mediator = new Mediator(context);
	}

	@Test
	public void testOpenConnection() throws UnableToConnectToDataStoreException, UnableToFindDataStoreException {
		sqliteProvider = new SQLiteConnectionProvider(mediator, TEST_DATABASE_PATH);
		Connection connection = sqliteProvider.openConnection();
		assertNotNull(connection);
		sqliteProvider.closeConnection();
	}

	@Test
	public void testOpenConnectionFail() throws UnableToFindDataStoreException, UnableToConnectToDataStoreException {
	Assertions.assertThrows(UnableToFindDataStoreException.class, ()->{
		
		sqliteProvider = new SQLiteConnectionProvider(mediator, FAKE_DATABASE_PATH);
	});
	}
}
