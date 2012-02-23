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

package com.example.subsystem;

import com.example.subsystem.deployment.SipContextFactoryDeploymentProcessor;
import com.example.subsystem.deployment.SipMetaDataDeploymentProcessor;
import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.ServiceVerificationHandler;
import org.jboss.as.server.AbstractDeploymentChainStep;
import org.jboss.as.server.DeploymentProcessorTarget;
import org.jboss.as.server.deployment.Phase;
import org.jboss.dmr.ModelNode;
import org.jboss.msc.service.ServiceController;

import java.util.List;

/**
 * The example subsystem add
 *
 * @author Emanuel Muckenhuber
 */
public class ExampleSipSubsystemAdd extends AbstractBoottimeAddStepHandler {

    /**
     * TODO give each deploymentProcess its own priority
     */
    static int DEPLOYMENT_PROCESS_PRIORITY = 0x4000;

    static final OperationStepHandler INSTANCE = new ExampleSipSubsystemAdd();

    @Override
    protected void performBoottime(final OperationContext context, final ModelNode operation, final ModelNode model,
                                   final ServiceVerificationHandler verificationHandler, List<ServiceController<?>> newControllers) throws OperationFailedException {

        context.addStep(new AbstractDeploymentChainStep() {
            @Override
            protected void execute(DeploymentProcessorTarget processorTarget) {

                // Add the SIP specific deployment processor
                processorTarget.addDeploymentProcessor(Phase.PARSE, DEPLOYMENT_PROCESS_PRIORITY, SipMetaDataDeploymentProcessor.INSTANCE);
                // Add the context in a different phase
                processorTarget.addDeploymentProcessor(Phase.POST_MODULE, DEPLOYMENT_PROCESS_PRIORITY, SipContextFactoryDeploymentProcessor.INSTANCE);

            }
        }, OperationContext.Stage.RUNTIME);

    }

    @Override
    protected void populateModel(final ModelNode operation, final ModelNode model) throws OperationFailedException {
        // populate subsystem configuration
    }

}
