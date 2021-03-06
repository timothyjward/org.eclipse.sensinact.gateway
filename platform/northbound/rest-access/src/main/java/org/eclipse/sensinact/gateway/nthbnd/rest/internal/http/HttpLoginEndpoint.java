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
package org.eclipse.sensinact.gateway.nthbnd.rest.internal.http;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.sensinact.gateway.core.security.AuthenticationToken;
import org.eclipse.sensinact.gateway.core.security.Credentials;
import org.eclipse.sensinact.gateway.core.security.InvalidCredentialException;
import org.eclipse.sensinact.gateway.nthbnd.endpoint.LoginResponse;
import org.eclipse.sensinact.gateway.nthbnd.endpoint.NorthboundMediator;
import org.eclipse.sensinact.gateway.nthbnd.rest.internal.RestAccessConstants;

/**
 * This class is the REST interface between each others classes
 * that perform a task and jersey
 */
@SuppressWarnings("serial")
@WebServlet()
public class HttpLoginEndpoint extends HttpServlet {
    private NorthboundMediator mediator;

    /**
     * Constructor
     */
    public HttpLoginEndpoint(NorthboundMediator mediator) {
        this.mediator = mediator;
    }

    /**
     * @inheritDoc
     * @see javax.servlet.http.HttpServlet#
     * doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.doExecute(request, response);
    }

    /**
     * @inheritDoc
     * @see javax.servlet.http.HttpServlet#
     * doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        this.doExecute(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws IOException
     */
    private final void doExecute(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (response.isCommitted()) {
            return;
        }
        try {
            LoginResponse loginResponse = null;
            String tokenHeader = request.getHeader("X-Auth-Token");
            String authorizationHeader = request.getHeader("Authorization");

            if (tokenHeader != null) {
                loginResponse = mediator.getAccessingEndpoint().reactivateEndpoint(new AuthenticationToken(tokenHeader));

            } else if (authorizationHeader != null) {
                loginResponse = mediator.getAccessingEndpoint().createNorthboundEndpoint(new Credentials(authorizationHeader));
            }
            byte[] resultBytes = loginResponse.getJSON().getBytes();
            response.setContentType(RestAccessConstants.JSON_CONTENT_TYPE);
            response.setContentLength(resultBytes.length);
            response.setBufferSize(resultBytes.length);

            ServletOutputStream output = response.getOutputStream();
            output.write(resultBytes);

            response.setStatus(200);

        } catch (InvalidCredentialException e) {
            mediator.error(e);
            response.sendError(403, e.getMessage());

        } catch (Exception e) {
            mediator.error(e);
            response.sendError(520, "Internal server error");

        }
    }
}
