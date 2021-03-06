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
package org.eclipse.sensinact.gateway.agent.storage.http.internal;

import org.eclipse.sensinact.gateway.common.bundle.Mediator;
import org.eclipse.sensinact.gateway.historic.storage.agent.generic.StorageConnection;
import org.eclipse.sensinact.gateway.protocol.http.client.ConnectionConfiguration;
import org.eclipse.sensinact.gateway.protocol.http.client.ConnectionConfigurationImpl;
import org.eclipse.sensinact.gateway.protocol.http.client.Request;
import org.eclipse.sensinact.gateway.protocol.http.client.Response;
import org.eclipse.sensinact.gateway.protocol.http.client.SimpleRequest;
import org.eclipse.sensinact.gateway.protocol.http.client.SimpleResponse;
import org.eclipse.sensinact.gateway.util.crypto.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * HTTP Agent dedicated to storage service
 *
 * @author <a href="mailto:christophe.munilla@cea.fr">Christophe Munilla</a>
 */
public class HttpStorageConnection extends StorageConnection {
	private static final Logger LOG = LoggerFactory.getLogger(HttpStorageConnection.class);

    protected String broker;
    protected String authorization;

	private String login;
	private String password;

	/**
	 * Constructor
	 *
	 * @param mediator the associated {@link Mediator}
	 * @param uri      the string URI of the storage server
	 * @param login    the user login
	 * @param password the user password
	 * @throws IOException Exception on connection problem
	 */
	public HttpStorageConnection(Mediator mediator, String uri, String login, String password) throws IOException {
		super(mediator);
		this.login = login;
		this.password = password;
		this.broker = uri;
		this.authorization = Base64.encodeBytes((this.login + ":" + this.password).getBytes());
	}

	/**
	 * Executes the HTTP request defined by the method, target, headers and entity
	 * arguments
	 */
	protected void store(JSONObject object) {
		ConnectionConfiguration<SimpleResponse, SimpleRequest> configuration = new ConnectionConfigurationImpl<>();
		try {
			configuration.setContentType("application/json");
			configuration.setAccept("application/json");
			configuration.setUri(this.broker);
			configuration.setContent(object.toString());
			configuration.setHttpMethod("POST");
			configuration.addHeader("Authorization", "Basic " + authorization);
			configuration.addHeader("User-Agent", "java/sensiNact-storage");
			Request request = new SimpleRequest(configuration);
			Response response = request.send();
			if (mediator.isDebugLoggable()) {
				this.mediator.debug(" >> response status code: " + response.getStatusCode());
			}
			Iterator<Map.Entry<String, List<String>>> iterator = response.getHeaders().entrySet().iterator();
			if (!iterator.hasNext()) {
				return;
			}
			Map.Entry<String, List<String>> entry = iterator.next();
			for (; iterator.hasNext(); entry = iterator.next()) {
				this.mediator.debug(entry.getKey() + " :: " + (entry.getValue() == null ? "null"
						: Arrays.toString(entry.getValue().toArray(new String[0]))));
			}
		} catch (Exception e) {
			LOG.error("Can't send request", e);
			if (this.mediator.isErrorLoggable()) {
				this.mediator.error(e.getMessage(), e);
			}
		}
	}
}
