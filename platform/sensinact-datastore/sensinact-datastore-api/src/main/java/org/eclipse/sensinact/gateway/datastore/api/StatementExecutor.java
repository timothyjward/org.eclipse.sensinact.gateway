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

import org.eclipse.sensinact.gateway.common.execution.Executable;

/**
 * {@link DataStoreService} statement executor service
 *
 * @param <T> the type returned by this StatementExecutor
 * @author <a href="mailto:christophe.munilla@cea.fr">Christophe Munilla</a>
 */
public interface StatementExecutor<T> extends Executable<DataStoreService, T> {
    T execute(DataStoreService service) throws DataStoreException;
}
