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
package org.eclipse.sensinact.gateway.datastore.api;

/**
 *
 */
@SuppressWarnings("serial")
public class UnableToLoadDriverClassException extends DataStoreException {
    /**
     * Constructor
     *
     * @param message the error message
     */
    public UnableToLoadDriverClassException(String message) {
        super(message);
    }

    /**
     * Constructor
     *
     * @param message   the error message
     * @param throwable wrapped exception that has made the current one thrown
     */
    public UnableToLoadDriverClassException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
