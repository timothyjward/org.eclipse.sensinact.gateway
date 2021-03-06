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
package org.eclipse.sensinact.gateway.sthbnd.mqtt.smarttopic.processor.formats;

import org.eclipse.sensinact.gateway.sthbnd.mqtt.smarttopic.processor.formats.exception.ProcessorFormatException;
import org.eclipse.sensinact.gateway.sthbnd.mqtt.smarttopic.processor.formats.iface.ProcessorFormatIface;
import org.eclipse.sensinact.gateway.sthbnd.mqtt.smarttopic.processor.selector.SelectorIface;
import org.json.JSONArray;

/**
 * This processor will receive a JSON Array (e.g. ["alpha","bravo"]) and enables to select one of the element based on its index.
 *
 * @author <a href="mailto:Jander.BOTELHODONASCIMENTO@cea.fr">Jander Botelho do Nascimento</a>
 */
public class ProcessorFormatArray implements ProcessorFormatIface {
    @Override
    public String getName() {
        return "array";
    }

    @Override
    public String process(String inData, SelectorIface selector) throws ProcessorFormatException {
        JSONArray array = new JSONArray(inData);
        if (selector.getExpression().equals("last")) {
            return array.get(array.length() - 1).toString();
        } else {
            Integer index = Integer.valueOf(selector.getExpression());
            return array.get(index).toString();
        }
    }
}
