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
package org.eclipse.sensinact.gateway.sthbnd.mqtt.smarttopic.processor.selector;

/**
 * @author <a href="mailto:Jander.BOTELHODONASCIMENTO@cea.fr">Jander Botelho do Nascimento</a>
 */
public class Selector implements SelectorIface {
    private final String name;
    private final String expression;

    public Selector(String name, String expression) {
        this.name = name;
        this.expression = expression;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getExpression() {
        return expression;
    }
}
