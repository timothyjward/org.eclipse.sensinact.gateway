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
package org.eclipse.sensinact.gateway.agent.http.onem2m.osgi;

import org.eclipse.sensinact.gateway.agent.http.onem2m.internal.SnaEventOneM2MHttpHandler;
import org.eclipse.sensinact.gateway.common.annotation.Property;
import org.eclipse.sensinact.gateway.common.bundle.AbstractActivator;
import org.eclipse.sensinact.gateway.common.bundle.Mediator;
import org.eclipse.sensinact.gateway.common.execution.Executable;
import org.eclipse.sensinact.gateway.core.Core;
import org.eclipse.sensinact.gateway.core.message.SnaFilter;
import org.eclipse.sensinact.gateway.core.message.SnaMessage;
import org.osgi.annotation.bundle.Header;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

/**
 * Extended {@link AbstractActivator}
 */
@Header(name = Constants.BUNDLE_ACTIVATOR, value = "${@class}")
public class Activator extends AbstractActivator<Mediator> {


    @Property(name = "org.eclipse.sensinact.gateway.northbound.http.onem2m.csebase")
    public String cseBase;

    private SnaEventOneM2MHttpHandler handler;

    private String registration;

    /**
     * @inheritDoc
     * @see AbstractActivator#doStart()
     */
    @Override
    public void doStart() throws Exception {

        this.handler = new SnaEventOneM2MHttpHandler(cseBase);
        this.registration = super.mediator.callService(Core.class, new Executable<Core, String>() {
            @Override
            public String execute(Core core) throws Exception {
                //Expression used in tests: "(\\/slider\\/cursor\\/position(\\/[^\\/]+)*)|(\\/(slider|(lora\\-tracker\\-[0-9]+))\\/admin\\/location(\\/[^\\/]+)*)"
                SnaFilter filter = new SnaFilter(mediator, ".*", true, false);
                filter.addHandledType(SnaMessage.Type.values());
                return core.registerAgent(mediator, handler, filter);
            }
        });
    }

    @Override
    public void doStop() throws Exception {
        
        this.handler.stop();
        this.handler = null;
        this.registration = null;
    }

    @Override
    public Mediator doInstantiate(BundleContext context) {

        return new Mediator(context);
    }
}
