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

import org.eclipse.sensinact.gateway.core.method.ActResponse;

/**
 * Extended {@link Resource} for Action
 *
 * @author <a href="mailto:christophe.munilla@cea.fr">Christophe Munilla</a>
 */
public interface ActionResource extends Resource {
	/**
	 * The action resource's type attribute value.
	 */
	public static final Resource.Type TYPE_VALUE = Resource.Type.ACTION;

	/**
	 * Executes an act access method on this ActionResource and returns its 
	 * {@link ActResponse}.
	 * 
	 * @param parameters
	 *            objects array parameterizing the actuation
	 *            
	 * @return the {@link ActResponse} of the executed act access method
	 */
	ActResponse act(Object... parameters);
}
