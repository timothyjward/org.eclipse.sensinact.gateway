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
package ${package}.app.servlet;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mirror servlet
 * Registered only to avoid 404 not found error - but never effectively called
 */
public class MirrorServlet extends HttpServlet implements Servlet {

	private static final Logger LOG = LoggerFactory.getLogger(MirrorServlet.class);
	
    @Override
    public void init(ServletConfig config) throws ServletException {
    	try {
    		super.init(config);
    	}catch(Exception e) {
    		LOG.error(e.getMessage(),e);
    		throw new ServletException(e);
    	} 
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
       response.setStatus(200);
       response.flushBuffer();
    }

}
