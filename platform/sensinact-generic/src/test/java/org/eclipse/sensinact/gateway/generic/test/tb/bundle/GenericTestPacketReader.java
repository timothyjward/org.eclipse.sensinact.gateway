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
package org.eclipse.sensinact.gateway.generic.test.tb.bundle;

import org.eclipse.sensinact.gateway.common.bundle.Mediator;
import org.eclipse.sensinact.gateway.generic.packet.InvalidPacketException;
import org.eclipse.sensinact.gateway.generic.packet.Packet;
import org.eclipse.sensinact.gateway.generic.packet.SimplePacketReader;

/**
 *
 */
public class GenericTestPacketReader extends SimplePacketReader<GenericTestPacket> {
    /**
     * @param mediator
     */
    protected GenericTestPacketReader(Mediator mediator) {
        super(mediator);
    }

    /**
     * @inheritDoc
     * @see org.eclipse.sensinact.gateway.generic.packet.PacketReader#parse(Packet)
     */
    @Override
    public void parse(GenericTestPacket packet) throws InvalidPacketException {

    }
}
