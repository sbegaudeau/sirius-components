/*******************************************************************************
 * Copyright (c) 2019, 2020 Obeo.
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.eclipse.sirius.web.graphql.datafetchers.subscriptions;

import java.security.Principal;
import java.util.Objects;

import org.eclipse.sirius.web.annotations.spring.graphql.SubscriptionDataFetcher;
import org.eclipse.sirius.web.collaborative.api.services.IProjectEventProcessorRegistry;
import org.eclipse.sirius.web.collaborative.api.services.IRepresentationEventProcessor;
import org.eclipse.sirius.web.collaborative.api.services.SubscriptionDescription;
import org.eclipse.sirius.web.collaborative.forms.api.FormConfiguration;
import org.eclipse.sirius.web.collaborative.forms.api.IFormEventProcessor;
import org.eclipse.sirius.web.collaborative.forms.api.dto.FormEventInput;
import org.eclipse.sirius.web.graphql.datafetchers.IDataFetchingEnvironmentService;
import org.eclipse.sirius.web.graphql.schema.SubscriptionTypeProvider;
import org.eclipse.sirius.web.services.api.dto.IPayload;
import org.eclipse.sirius.web.spring.graphql.api.IDataFetcherWithFieldCoordinates;
import org.reactivestreams.Publisher;

import graphql.schema.DataFetchingEnvironment;
import reactor.core.publisher.Flux;

/**
 * The data fetcher used to send the refreshed form to a subscription.
 * <p>
 * It will be used to fetch the data for the following GraphQL field:
 * </p>
 *
 * <pre>
 * type Subscription {
 *   formEvent(input: FormEventInput): FormEventPayload
 * }
 * </pre>
 *
 * @author sbegaudeau
 * @author pcdavid
 */
@SubscriptionDataFetcher(type = SubscriptionTypeProvider.TYPE, field = SubscriptionTypeProvider.FORM_EVENT_FIELD)
public class SubscriptionFormEventDataFetcher implements IDataFetcherWithFieldCoordinates<Publisher<IPayload>> {

    private final IDataFetchingEnvironmentService dataFetchingEnvironmentService;

    private final IProjectEventProcessorRegistry projectEventProcessorRegistry;

    public SubscriptionFormEventDataFetcher(IDataFetchingEnvironmentService dataFetchingEnvironmentService, IProjectEventProcessorRegistry projectEventProcessorRegistry) {
        this.dataFetchingEnvironmentService = Objects.requireNonNull(dataFetchingEnvironmentService);
        this.projectEventProcessorRegistry = Objects.requireNonNull(projectEventProcessorRegistry);
    }

    @Override
    public Publisher<IPayload> get(DataFetchingEnvironment environment) throws Exception {
        var input = this.dataFetchingEnvironmentService.getInput(environment, FormEventInput.class);
        var context = this.dataFetchingEnvironmentService.getContext(environment);

        Principal principal = this.dataFetchingEnvironmentService.getPrincipal(environment).orElse(null);
        String subscriptionId = this.dataFetchingEnvironmentService.getSubscriptionId(environment);

        var formConfiguration = new FormConfiguration(input.getObjectId());

        // @formatter:off
        return this.projectEventProcessorRegistry.getOrCreateProjectEventProcessor(input.getProjectId())
                .flatMap(processor -> processor.acquireRepresentationEventProcessor(IFormEventProcessor.class, formConfiguration, new SubscriptionDescription(principal, subscriptionId), context))
                .map(IRepresentationEventProcessor::getOutputEvents)
                .orElse(Flux.empty());
        // @formatter:on
    }

}
