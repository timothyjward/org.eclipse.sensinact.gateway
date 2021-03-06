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
package org.eclipse.sensinact.gateway.commands.gogo.internal;

import org.apache.felix.service.command.Descriptor;
import org.eclipse.sensinact.gateway.commands.gogo.internal.shell.ShellAccess;
import org.eclipse.sensinact.gateway.commands.gogo.osgi.CommandServiceMediator;
import org.eclipse.sensinact.gateway.datastore.api.DataStoreException;
import org.json.JSONObject;

import java.security.InvalidKeyException;

public class DeviceCommands {
    private CommandServiceMediator mediator;

    public DeviceCommands(CommandServiceMediator mediator) throws DataStoreException, InvalidKeyException {
        this.mediator = mediator;
    }

    /**
     * Display the existing sensiNact service providers instances
     */
    @Descriptor("display the existing sensiNact service providers instances")
    public void devices() {
        ShellAccess.proceed(mediator, new JSONObject().put("uri", CommandServiceMediator.uri(null, null, null, true)));
    }

    /**
     * Display the description of a specific sensiNact service provider instance
     *
     * @param serviceProviderID the ID of the service provider
     */
    @Descriptor("display the description of a specific sensiNact service provider instance")
    public void device(@Descriptor("the device ID") String serviceProviderID) {
        ShellAccess.proceed(mediator, new JSONObject().put("uri", CommandServiceMediator.uri(serviceProviderID, null, null, false)));
    }
}
