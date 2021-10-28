/*
 * Copyright 2015-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.platform.engine;

import static org.apiguardian.api.API.Status.EXPERIMENTAL;
import static org.apiguardian.api.API.Status.STABLE;

import java.util.List;

import org.apiguardian.api.API;

/**
 * {@code EngineDiscoveryRequest} provides a {@link TestEngine} access to the
 * information necessary to discover tests and containers.
 *
 * <p>A request is comprised of {@linkplain DiscoverySelector selectors} and
 * {@linkplain DiscoveryFilter filters}. While the former <em>select</em>
 * resources that engines can use to discover tests, the latter specify how
 * such resources are to be <em>filtered</em>. All of the <em>filters</em>
 * have to include a resource for it to end up in the test plan.
 *
 * <p>In addition, the supplied {@linkplain ConfigurationParameters
 * configuration parameters} can be used to influence the discovery process.
 *
 * @see TestEngine
 * @see TestDescriptor
 * @see DiscoverySelector
 * @see DiscoveryFilter
 * @see ConfigurationParameters
 * @since 1.0
 */
@API(status = STABLE, since = "1.0")
public interface EngineDiscoveryRequest {

	/**
	 * Get the {@link DiscoverySelector DiscoverySelectors} for this request,
	 * filtered by a particular type.
	 *
	 * @param selectorType the type of {@link DiscoverySelector} to filter by
	 * @return all selectors of this request that are instances of
	 * {@code selectorType}; never {@code null} but potentially empty
	 */
	<T extends DiscoverySelector> List<T> getSelectorsByType(Class<T> selectorType);

	/**
	 * Get the {@link DiscoveryFilter DiscoveryFilters} for this request,
	 * filtered by a particular type.
	 *
	 * <p>The returned filters are to be combined using AND semantics, i.e. all
	 * of them have to include a resource for it to end up in the test plan.
	 *
	 * @param filterType the type of {@link DiscoveryFilter} to filter by
	 * @return all filters of this request that are instances of
	 * {@code filterType}; never {@code null} but potentially empty
	 */
	<T extends DiscoveryFilter<?>> List<T> getFiltersByType(Class<T> filterType);

	/**
	 * Get the {@link ConfigurationParameters} for this request.
	 *
	 * @return the configuration parameters; never {@code null}
	 */
	ConfigurationParameters getConfigurationParameters();

	/**
	 * Get the {@link EngineDiscoveryListener} for this request.
	 *
	 * <p>The default implementation returns a no-op listener that ignores all
	 * calls so that engines that call this methods can be used with an earlier
	 * version of the JUnit Platform that did not yet include this API.
	 *
	 * @return the discovery listener; never {@code null}
	 * @since 1.6
	 */
	@API(status = EXPERIMENTAL, since = "1.6")
	default EngineDiscoveryListener getDiscoveryListener() {
		return EngineDiscoveryListener.NOOP;
	}

}
