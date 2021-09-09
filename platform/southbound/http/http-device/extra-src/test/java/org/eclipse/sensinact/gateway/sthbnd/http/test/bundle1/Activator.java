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

package org.eclipse.sensinact.gateway.sthbnd.http.test.bundle1;

import org.eclipse.sensinact.gateway.generic.Task;
import org.eclipse.sensinact.gateway.sthbnd.http.HttpPacket;
import org.eclipse.sensinact.gateway.sthbnd.http.annotation.HttpTaskConfiguration;
import org.eclipse.sensinact.gateway.sthbnd.http.annotation.HttpTasks;
import org.eclipse.sensinact.gateway.sthbnd.http.annotation.SimpleHttpTask;
import org.eclipse.sensinact.gateway.sthbnd.http.smpl.HttpActivator;
import org.osgi.annotation.bundle.Header;
import org.osgi.framework.Constants;

@HttpTasks( tasks =
{
	@SimpleHttpTask(
		commands = Task.CommandType.GET,
		configuration = @HttpTaskConfiguration(
			host = "127.0.0.1", 
			port="8899", 
			path = "/get")
			),
	@SimpleHttpTask(
		commands = Task.CommandType.SET,
		configuration = @HttpTaskConfiguration(
			host = "127.0.0.1", 
			port="8899",
			httpMethod = "POST",
			path = "/set",
			content=ContentBuilderImpl.class)
			)
})
@Header(name = Constants.BUNDLE_ACTIVATOR, value = "${@class}")
public class Activator extends HttpActivator
{
	/**
	 * @inheritDoc
	 *
	 * @see HttpActivator#getPacketType()
	 */
	@Override
	protected Class<? extends HttpPacket> getPacketType()
	{
		return HttpTestPacket.class;
	}
}
