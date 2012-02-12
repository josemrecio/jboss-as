package org.jboss.as.web.deployment;

import org.jboss.as.ee.structure.Attachments;
import org.jboss.as.ee.structure.DeploymentType;
import org.jboss.as.ee.structure.DeploymentTypeMarker;
import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.web.WebSubsystemServices;
import org.jboss.msc.service.ServiceName;

/**
 * Processor that adds web deployments to the list
 *
 *
 * @author Stuart Douglas
 */
public class WebInitializeInOrderProcessor extends AbstractDeploymentProcessor {

    private final String defaultHost;

    public WebInitializeInOrderProcessor(String defaultHost) {
        if (defaultHost == null) {
            throw new IllegalArgumentException("null default host");
        }
        this.defaultHost = defaultHost;
    }

    @Override
    protected void doDeploy(final DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException {
        final DeploymentUnit deploymentUnit = phaseContext.getDeploymentUnit();
        final WarMetaData metaData = deploymentUnit.getAttachment(WarMetaData.ATTACHMENT_KEY);
        final String hostName = WarDeploymentProcessor.hostNameOfDeployment(metaData, defaultHost);
        processDeployment(hostName, metaData, deploymentUnit);
    }

    protected void processDeployment(final String hostName, final WarMetaData metaData, final DeploymentUnit deploymentUnit) {
        final String pathName = WarDeploymentProcessor.pathNameOfDeployment(deploymentUnit, metaData.getMergedJBossWebMetaData());
        final ServiceName deploymentServiceName = WebSubsystemServices.deploymentServiceName(hostName, pathName);
        final ServiceName realmServiceName = deploymentServiceName.append("realm");
        deploymentUnit.addToAttachmentList(Attachments.INITIALISE_IN_ORDER_SERVICES, deploymentServiceName);
        deploymentUnit.addToAttachmentList(Attachments.INITIALISE_IN_ORDER_SERVICES, realmServiceName);
    }

    @Override
    protected boolean canHandle(DeploymentUnit deploymentUnit) {
        if (!DeploymentTypeMarker.isType(DeploymentType.WAR, deploymentUnit)) {
            return false;
        }

        final WarMetaData warMetaData = deploymentUnit.getAttachment(WarMetaData.ATTACHMENT_KEY);
        if(warMetaData == null) {
            return false;
        }

        return true;
    }
}
