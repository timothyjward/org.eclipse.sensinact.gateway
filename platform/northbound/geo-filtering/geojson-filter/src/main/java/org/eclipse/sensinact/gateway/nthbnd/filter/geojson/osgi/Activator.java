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
package org.eclipse.sensinact.gateway.nthbnd.filter.geojson.osgi;

import org.eclipse.sensinact.gateway.common.bundle.AbstractActivator;
import org.eclipse.sensinact.gateway.common.bundle.Mediator;
import org.eclipse.sensinact.gateway.core.filtering.Filtering;
import org.eclipse.sensinact.gateway.nthbnd.filter.geojson.internal.GeoJSONFiltering;
import org.osgi.annotation.bundle.Header;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

import java.util.Hashtable;

/**
 */
@Header(name = Constants.BUNDLE_ACTIVATOR, value = "${@class}")
public class Activator extends AbstractActivator<Mediator> {
    private static final String TYPE = "geojson";

    @Override
    public void doStart() throws Exception {
        super.mediator.info("Registering GeoJSON filter");
        super.mediator.register(new GeoJSONFiltering(super.mediator), Filtering.class, new Hashtable() {{
            put("type", TYPE);
        }});
    }

    @Override
    public void doStop() throws Exception {
        super.mediator.info("Unregistering GeoJSON filter");
    }

    @Override
    public Mediator doInstantiate(BundleContext context) {
        return new Mediator(context);
    }
}
