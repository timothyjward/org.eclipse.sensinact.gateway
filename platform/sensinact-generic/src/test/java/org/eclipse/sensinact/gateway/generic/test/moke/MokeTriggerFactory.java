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
package org.eclipse.sensinact.gateway.generic.test.moke;

import org.eclipse.sensinact.gateway.common.bundle.Mediator;
import org.eclipse.sensinact.gateway.common.primitive.InvalidValueException;
import org.eclipse.sensinact.gateway.core.method.trigger.AccessMethodTrigger;
import org.eclipse.sensinact.gateway.core.method.trigger.AccessMethodTriggerFactory;
import org.json.JSONObject;
import aQute.bnd.annotation.spi.ServiceProvider;

/**
 *
 */
@ServiceProvider(AccessMethodTriggerFactory.class)
public class MokeTriggerFactory implements AccessMethodTriggerFactory {
    /**
     * @inheritDoc
     * @see AccessMethodTriggerFactory#handle(String)
     */
    @Override
    public boolean handle(String type) {
        return "VARIATIONTEST_TRIGGER".equals(type);
    }

    /**
     * @inheritDoc
     * @see AccessMethodTriggerFactory#newInstance(Mediator, JSONObject)
     */
    @Override
    public AccessMethodTrigger newInstance(Mediator mediator, JSONObject trigger) throws InvalidValueException {
        return new MokeTrigger();
    }
}
