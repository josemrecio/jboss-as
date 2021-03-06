/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2011, Red Hat, Inc., and individual contributors
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

package org.jboss.as.web.deployment;

import static org.jboss.metadata.ear.spec.ModuleMetaData.ModuleType.Web;

import org.jboss.as.ee.structure.Attachments;
import org.jboss.as.ee.structure.DeploymentType;
import org.jboss.as.ee.structure.DeploymentTypeMarker;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.module.ResourceRoot;
import org.jboss.metadata.ear.spec.EarMetaData;
import org.jboss.metadata.ear.spec.ModuleMetaData;
import org.jboss.metadata.ear.spec.ModulesMetaData;
import org.jboss.metadata.ear.spec.WebModuleMetaData;
import org.jboss.metadata.sip.jboss.JBoss70ConvergedSipMetaData;
import org.jboss.metadata.web.jboss.JBossWebMetaData;

/**
 * Deployment unit processor responsible for detecting web deployments and determining if they have a parent EAR file and
 * if so applying the EAR defined context root to web metadata.
 *
 * @author John Bailey
 */
public class EarContextRootProcessor extends AbstractDeploymentProcessor {

    @Override
    protected void doDeploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final WarMetaData warMetaData = deploymentUnit.getAttachment(WarMetaData.ATTACHMENT_KEY);
        final ResourceRoot deploymentRoot = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.DEPLOYMENT_ROOT);
        final DeploymentUnit parent = deploymentUnit.getParent();
        final EarMetaData earMetaData = parent.getAttachment(Attachments.EAR_METADATA);
        final ModulesMetaData modulesMetaData = earMetaData.getModules();
        if(modulesMetaData != null) for(ModuleMetaData moduleMetaData : modulesMetaData) {
            if(Web.equals(moduleMetaData.getType()) && moduleMetaData.getFileName().equals(deploymentRoot.getRootName())) {
                String contextRoot = WebModuleMetaData.class.cast(moduleMetaData.getValue()).getContextRoot();

                if(contextRoot == null && (warMetaData.getJbossWebMetaData() == null || warMetaData.getJbossWebMetaData().getContextRoot() == null)) {
                    contextRoot = "/" + parent.getName().substring(0, parent.getName().length() - 4) + "/"
                            + deploymentUnit.getName().substring(0, deploymentUnit.getName().length() - 4);
                }

                if(contextRoot != null) {
                    JBossWebMetaData jBossWebMetaData = warMetaData.getJbossWebMetaData();
                        if(jBossWebMetaData == null) {
                            // jBossWebMetaData = new JBoss70WebMetaData();
                            jBossWebMetaData = new JBoss70ConvergedSipMetaData(); // josemrecio
                            warMetaData.setJbossWebMetaData(jBossWebMetaData);
                        }
                    jBossWebMetaData.setContextRoot(contextRoot);
                }
                return;
            }
        }
    }

    @Override
    protected boolean canHandle(DeploymentUnit deploymentUnit) {
        if (!DeploymentTypeMarker.isType(DeploymentType.WAR, deploymentUnit)) {
            return false;
        }

        final WarMetaData warMetaData = deploymentUnit.getAttachment(WarMetaData.ATTACHMENT_KEY);
        if(warMetaData == null) {
            return false; // Nothing we can do without WarMetaData
        }
        final ResourceRoot deploymentRoot = deploymentUnit.getAttachment(org.jboss.as.server.deployment.Attachments.DEPLOYMENT_ROOT);
        if(deploymentRoot == null) {
            return false; // We don't have a root to work with
        }

        final DeploymentUnit parent = deploymentUnit.getParent();
        if(parent == null || !DeploymentTypeMarker.isType(DeploymentType.EAR, parent)) {
            return false;  // Only care if this war is nested in an EAR
        }

        final EarMetaData earMetaData = parent.getAttachment(Attachments.EAR_METADATA);
        if(earMetaData == null) {
            return false; // Nothing to see here
        }

        return true;
    }
}
