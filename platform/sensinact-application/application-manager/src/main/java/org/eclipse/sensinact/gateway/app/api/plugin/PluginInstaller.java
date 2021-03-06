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
package org.eclipse.sensinact.gateway.app.api.plugin;

import org.eclipse.sensinact.gateway.app.api.function.AbstractFunction;
import org.eclipse.sensinact.gateway.app.manager.json.AppFunction;
import org.json.JSONObject;

/**
 * This interface has to be implemented by plugins in order to
 * participate into the creation of the application.
 *
 * @author Remi Druilhe
 */
public interface PluginInstaller {
    /**
     * Get the JSON schema of the function to validate
     *
     * @param function the function that is going to be validate
     * @return the JSON schema of the function
     */
    //TODO: this function should disappear and be replaced by a check directly based on the name of the JSON file
    JSONObject getComponentJSONSchema(String function);

    /**
     * Creation of the operation node from the JSON file.
     *
     * @param function the JSON function
     * @return the created {@link AbstractFunction}
     */
    AbstractFunction getFunction(AppFunction function);
}
