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
package org.eclipse.sensinact.gateway.core;

import java.util.List;

/**
 * A collection of {@link Resource} service
 * 
 * @author <a href="mailto:christophe.munilla@cea.fr">Christophe Munilla</a>
 */
public interface ResourceCollection {
	/**
	 * Returns the set of all {@link Resource}s of this collection
	 * 
	 * @return the set of held {@link Resource}s
	 */
	List<Resource> getResources();

	/**
	 * Returns the {@link Resource} held by this collection and whose name is passed
	 * as parameter
	 * 
	 * @param resource
	 *            the name of the {@link Resource}
	 * @return the {@link Resource} of this collection with the specified name
	 */
	<R extends Resource> R getResource(String resource);
}
