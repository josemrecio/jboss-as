package org.jboss.as.web.deployment;

import org.jboss.as.server.deployment.DeploymentPhaseContext;
import org.jboss.as.server.deployment.DeploymentUnit;
import org.jboss.as.server.deployment.DeploymentUnitProcessingException;
import org.jboss.as.server.deployment.DeploymentUnitProcessor;


/**
 * Abstract implementation of {@link DeploymentUnitProcessor} with an explicit check
 * for the deployments it can handle
 *
 * @author Siamak Sadeghianfar (ssadeghi@redhat.com)
 */
public abstract class AbstractDeploymentProcessor implements DeploymentUnitProcessor {

    @Override
    public void deploy(DeploymentPhaseContext phaseContext)
            throws DeploymentUnitProcessingException {
        if (canHandle(phaseContext.getDeploymentUnit())) {
            doDeploy(phaseContext);
        }
    }

    @Override
    public void undeploy(DeploymentUnit context) {
    }

    protected abstract boolean canHandle(DeploymentUnit deploymentUnit);

    protected abstract void doDeploy(DeploymentPhaseContext phaseContext) throws DeploymentUnitProcessingException;
}
