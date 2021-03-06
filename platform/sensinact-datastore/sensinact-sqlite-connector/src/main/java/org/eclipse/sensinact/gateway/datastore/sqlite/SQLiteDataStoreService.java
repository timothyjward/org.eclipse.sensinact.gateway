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
package org.eclipse.sensinact.gateway.datastore.sqlite;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.eclipse.sensinact.gateway.common.bundle.Mediator;
import org.eclipse.sensinact.gateway.core.security.SecurityDataStoreService;
import org.eclipse.sensinact.gateway.datastore.api.DataStoreConnectionProvider;
import org.eclipse.sensinact.gateway.datastore.api.UnableToConnectToDataStoreException;
import org.eclipse.sensinact.gateway.datastore.api.UnableToFindDataStoreException;
import org.eclipse.sensinact.gateway.datastore.jdbc.JdbcDataStoreService;

/**
 *
 * @author <a href="mailto:christophe.munilla@cea.fr">Christophe Munilla</a>
 */
public class SQLiteDataStoreService extends JdbcDataStoreService 
implements SecurityDataStoreService{
	
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

	private SQLiteConnectionProvider provider;
	private long lastInsertedId = -1;

	/**
	 * @param mediator
	 * @param dbName
	 * @throws UnableToFindDataStoreException
	 * @throws UnableToConnectToDataStoreException
	 */
	public SQLiteDataStoreService(Mediator mediator)
			throws UnableToFindDataStoreException, UnableToConnectToDataStoreException {
		super(mediator);
		String dbName = (String) mediator.getProperty("org.eclipse.sensinact.gateway.security.database");
		this.provider = new SQLiteConnectionProvider(mediator, dbName);
	}

	/**
	 * @inheritDoc
	 *
	 * @see JdbcDataStoreService# getDataBaseConnectionProvider(java.lang.String)
	 */
	@Override
	protected DataStoreConnectionProvider<Connection> getDataBaseConnectionProvider() {
		return this.provider;
	}

	/**
	 * @inheritDoc
	 *
	 * @see JdbcDataStoreService#stop()
	 */
	public void stop() {
		if (provider != null) {
			provider.stop();
			while (provider.getCount() > 0) {
				try {
					Thread.sleep(10);

				} catch (InterruptedException ex) {
					Thread.interrupted();
					ex.printStackTrace();
				}
			}
		}
		provider = null;
	}


	@Override
	public long getLastInsertedId(Statement statement) throws SQLException {
		ResultSet rs = null;
		long lastID = -1;
		rs = statement.executeQuery("SELECT last_insert_rowid() AS LASTID;");
		if (rs.next()) {
			lastID = rs.getLong(1);
		}
		return lastID;
	}
}
