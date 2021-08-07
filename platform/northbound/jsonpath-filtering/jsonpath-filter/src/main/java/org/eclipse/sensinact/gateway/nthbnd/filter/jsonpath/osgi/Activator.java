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
package org.eclipse.sensinact.gateway.nthbnd.filter.jsonpath.osgi;

import java.util.Hashtable;

import org.eclipse.sensinact.gateway.common.bundle.AbstractActivator;
import org.eclipse.sensinact.gateway.common.bundle.Mediator;
import org.eclipse.sensinact.gateway.core.filtering.Filtering;
import org.eclipse.sensinact.gateway.nthbnd.filter.jsonpath.internal.JsonPathFiltering;
import org.osgi.annotation.bundle.Header;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

/**
 */
@Header(name = Constants.BUNDLE_ACTIVATOR, value = "${@class}")
public class Activator extends AbstractActivator<Mediator> {
    private static final String TYPE = "jsonpath";

    @Override
    public void doStart() throws Exception {
        super.mediator.info("Registering JSONPath filter");
        super.mediator.register(new JsonPathFiltering(super.mediator), Filtering.class, new Hashtable() {{
            put("type", TYPE);
        }});
    }

    @Override
    public void doStop() throws Exception {
        super.mediator.info("Unregistering JSONPath filter");
    }

    @Override
    public Mediator doInstantiate(BundleContext context) {
        return new Mediator(context);
    }
}
