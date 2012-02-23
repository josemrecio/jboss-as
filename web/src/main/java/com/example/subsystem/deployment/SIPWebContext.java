/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package com.example.subsystem.deployment;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.core.StandardContext;
import org.jboss.logging.Logger;

/**
 * The SIP specific implementation of the jboss-web {@code StandardContext}.
 *
 *
 * @author Emanuel Muckenhuber
 */
public class SIPWebContext extends StandardContext {

    private static final Logger log = Logger.getLogger(SIPWebContext.class);

    private final String name;
    public SIPWebContext(String name) {
        this.name = name;
    }

    @Override
    public void start() throws LifecycleException {
        log.infof("Starting sip web context for deployment %s", name);
        super.start();
    }
}
