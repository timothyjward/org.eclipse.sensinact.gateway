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
package org.eclipse.sensinact.studio.web;

import java.net.HttpURLConnection;
import java.net.URL;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

//@ExtendWith(BundleContextExtension.class)
//@ExtendWith(ServiceExtension.class)
public class StudioWebServiceTest {

	@Test
	public void testRedirect() throws Exception {
		URL url = new URL("http://localhost:8080/studio-web/");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setInstanceFollowRedirects(false);
		connection.setRequestMethod("GET");
		connection.connect();

		Assertions.assertThat(connection.getResponseCode()).isEqualTo(302);
		Assertions.assertThat(connection.getHeaderField("location"))
				.isEqualTo("http://localhost:8080/studio-web/index.html");
	}

	@Test
	public void testIndex() throws Exception {
		URL url = new URL("http://localhost:8080/studio-web/index.html");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();

		Assertions.assertThat(connection.getResponseCode()).isEqualTo(200);

	}

	@Test
	public void testScript() throws Exception {
		URL url = new URL("http://localhost:8080/studio-web/scripts/sensinact.js");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();

		Assertions.assertThat(connection.getResponseCode()).isEqualTo(200);

	}

	@Test
	public void testImageOn() throws Exception {
		URL url = new URL("http://localhost:8080/studio-web/images/on.png");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();

		Assertions.assertThat(connection.getResponseCode()).isEqualTo(200);

	}

	@Test
	public void testImageOff() throws Exception {
		URL url = new URL("http://localhost:8080/studio-web/images/off.png");
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();

		Assertions.assertThat(connection.getResponseCode()).isEqualTo(200);

	}

}
