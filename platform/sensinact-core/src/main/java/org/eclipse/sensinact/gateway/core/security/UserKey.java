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
package org.eclipse.sensinact.gateway.core.security;

/**
 * A UserKey wrapped the String public key of a user
 *
 * @author <a href="mailto:christophe.munilla@cea.fr">Christophe Munilla</a>
 */
public class UserKey {
	private final String publicKey;

	/**
	 * @param publicKey
	 */
	public UserKey(String publicKey) {
		this.publicKey = publicKey;
	}

	/**
	 * @return
	 */
	public String getPublicKey() {
		return this.publicKey;
	}
}
