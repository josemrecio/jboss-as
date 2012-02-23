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

import org.jboss.as.ee.structure.DeploymentType;
import org.jboss.as.ee.structure.DeploymentTypeMarker;
import org.jboss.as.server.deployment.Attachments;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;
import org.jboss.metadata.parser.util.NoopXMLResolver;
import org.jboss.vfs.VirtualFile;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.IOException;
import java.io.InputStream;

/**
 * {@code DeploymentUnitProcessor} responsible for parsing the sip.xml metadata.
 *
 * @author Emanuel Muckenhuber
 */
public class SipMetaDataDeploymentProcessor implements DeploymentUnitProcessor {

    public static final DeploymentUnitProcessor INSTANCE = new SipMetaDataDeploymentProcessor();

    private static final String SIP_XML = "WEB-INF/sip.xml";

    @Override
    public void deploy(final DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        // The sip.xml has to be part of a web deployment, therefore we can just
        // skip all other deployment types
        if (!DeploymentTypeMarker.isType(DeploymentType.WAR, deploymentUnit)) {
            return;
        }
        // Check if the .war contains a sip.xml
        final VirtualFile deploymentRoot = deploymentUnit.getAttachment(Attachments.DEPLOYMENT_ROOT).getRoot();
        final VirtualFile sipXMl = deploymentRoot.getChild(SIP_XML);
        // In case there is a sip.xml, parse and attach it to the DeploymentUnit
        if (sipXMl.exists()) {
            InputStream is = null;
            try {

                // TODO parse and attach sip metadata implementation
                deploymentUnit.putAttachment(SipMetaData.ATTACHMENT_KEY, new SipMetaData() { });

            } catch (Exception e) {
                throw new DeploymentUnitProcessingException(e);
            } finally {
                if (is != null)
                try {
                    is.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    @Override
    public void undeploy(DeploymentUnit context) {
        //
    }
}
