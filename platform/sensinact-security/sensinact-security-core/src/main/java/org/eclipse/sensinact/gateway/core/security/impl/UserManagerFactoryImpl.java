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
import org.eclipse.sensinact.gateway.core.security.AuthenticationService;
import org.eclipse.sensinact.gateway.core.security.SecuredAccessException;
import org.eclipse.sensinact.gateway.core.security.SecuredAccessFactory;
import org.eclipse.sensinact.gateway.core.security.UserManager;
import org.eclipse.sensinact.gateway.core.security.UserManagerFactory;

import aQute.bnd.annotation.Resolution;
import aQute.bnd.annotation.spi.ServiceProvider;

/**
 * @author christophe
 *
 */
@ServiceProvider(value = UserManagerFactory.class, resolution = Resolution.OPTIONAL)
public class UserManagerFactoryImpl implements UserManagerFactory<UserManagerImpl> {
	/**
	 * @inheritDoc
	 * 
	 * @see org.eclipse.sensinact.gateway.core.security.UserManagerFactory#getType()
	 */
	@Override
	public Class<UserManagerImpl> getType() {
		return UserManagerImpl.class;
	}

	/**
	 * @inheritDoc
	 * 
	 * @see org.eclipse.sensinact.gateway.core.security.UserManagerFactory#
	 *      newInstance(org.eclipse.sensinact.gateway.common.bundle.Mediator)
	 */
	@Override
	public void newInstance(Mediator mediator) throws SecuredAccessException {
		UserManager manager = new UserManagerImpl(mediator);

		mediator.register(null, manager, new Class<?>[] { UserManager.class, AuthenticationService.class });
	}
}
