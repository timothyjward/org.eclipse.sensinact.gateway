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
package org.eclipse.sensinact.gateway.system.osgi;

import java.io.IOException;
import java.util.Collections;

import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.sensinact.gateway.common.bundle.AbstractActivator;
import org.eclipse.sensinact.gateway.common.bundle.Mediator;
import org.eclipse.sensinact.gateway.generic.ExtModelConfiguration;
import org.eclipse.sensinact.gateway.generic.ExtModelConfigurationBuilder;
import org.eclipse.sensinact.gateway.generic.local.LocalProtocolStackEndpoint;
import org.eclipse.sensinact.gateway.generic.packet.Packet;
import org.osgi.annotation.bundle.Header;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.xml.sax.SAXException;

@Header(name = Constants.BUNDLE_ACTIVATOR, value = "${@class}")
public class Activator extends AbstractActivator<Mediator> {
    private ExtModelConfiguration<Packet> manager = null;
    private LocalProtocolStackEndpoint<Packet> connector = null;

    /**
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @inheritDoc
     * @see AbstractActivator#doStart()
     */
    public void doStart() throws Exception {
        if (manager == null) {
            manager = ExtModelConfigurationBuilder.instance(super.mediator
            ).withStartAtInitializationTime(true
            ).build("system-resource.xml", Collections.<String, String>emptyMap());
        }
        if (this.connector == null) {
            this.connector = new LocalProtocolStackEndpoint<Packet>(mediator);
        }
        this.connector.connect(manager);
    }

    /**
     * @inheritDoc
     * @see AbstractActivator#doStop()
     */
    public void doStop() {
        if (this.connector != null) {
            this.connector.stop();
            this.connector = null;
        }
        this.manager = null;
    }

    /**
     * @inheritDoc
     * @see AbstractActivator#
     * doInstantiate(org.osgi.framework.BundleContext, int, java.io.FileOutputStream)
     */
    public Mediator doInstantiate(BundleContext context) {
        return new Mediator(context);
    }
}
