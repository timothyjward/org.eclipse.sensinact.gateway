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
package org.eclipse.sensinact.gateway.core.security.impl;

import org.eclipse.sensinact.gateway.common.bundle.Mediator;
import org.eclipse.sensinact.gateway.core.security.SecuredAccess;
import org.eclipse.sensinact.gateway.core.security.SecuredAccessException;
import org.eclipse.sensinact.gateway.core.security.SecuredAccessFactory;

import aQute.bnd.annotation.Resolution;
import aQute.bnd.annotation.spi.ServiceProvider;

/**
 * Implementation of a {@link SecuredAccessFactory}
 * 
 * @see SecuredAccessFactory
 * 
 * @author <a href="mailto:christophe.munilla@cea.fr">Christophe Munilla</a>
 */
@ServiceProvider(value = SecuredAccessFactory.class, resolution = Resolution.OPTIONAL)
public class SecuredAccessFactoryImpl implements SecuredAccessFactory {

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

	/**
	 * @throws SecuredAccessException
	 * @throws DataStoreException
	 * @inheritedDoc
	 *
	 * @see SecuredAccessFactory#
	 *      newInstance(org.eclipse.sensinact.gateway.common.bundle.Mediator)
	 */
	@Override
	public SecuredAccess newInstance(Mediator mediator) throws SecuredAccessException {
		return new SecuredAccessImpl(mediator);
	}
}
