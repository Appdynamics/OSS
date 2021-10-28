/*
 * Copyright 2015-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.platform.launcher.listeners.discovery;

import static org.junit.platform.launcher.EngineDiscoveryResult.Status.FAILED;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.platform.engine.DiscoverySelector;
import org.junit.platform.engine.SelectorResolutionResult;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.discovery.UniqueIdSelector;
import org.junit.platform.launcher.EngineDiscoveryResult;
import org.junit.platform.launcher.LauncherDiscoveryListener;

/**
 * @since 1.6
 * @see LauncherDiscoveryListeners#logging()
 */
class LoggingLauncherDiscoveryListener extends LauncherDiscoveryListener {

	private static final Logger logger = LoggerFactory.getLogger(LoggingLauncherDiscoveryListener.class);

	@Override
	public void engineDiscoveryStarted(UniqueId engineId) {
		logger.trace(() -> "Engine " + engineId + " has started discovering tests");
	}

	@Override
	public void engineDiscoveryFinished(UniqueId engineId, EngineDiscoveryResult result) {
		if (result.getStatus() == FAILED) {
			Optional<Throwable> failure = result.getThrowable();
			if (failure.isPresent()) {
				logger.error(failure.get().getCause(), () -> failure.get().getMessage());
			}
			else {
				logger.error(() -> "Engine " + engineId + " failed to discover tests");
			}
		}
		else {
			logger.trace(() -> "Engine " + engineId + " has finished discovering tests");
		}
	}

	@Override
	public void selectorProcessed(UniqueId engineId, DiscoverySelector selector, SelectorResolutionResult result) {
		switch (result.getStatus()) {
			case RESOLVED:
				logger.debug(() -> selector + " was resolved by " + engineId);
				break;
			case FAILED:
				logger.error(result.getThrowable().orElse(null),
					() -> "Resolution of " + selector + " by " + engineId + " failed");
				break;
			case UNRESOLVED:
				Consumer<Supplier<String>> loggingConsumer = logger::debug;
				if (selector instanceof UniqueIdSelector) {
					UniqueId uniqueId = ((UniqueIdSelector) selector).getUniqueId();
					if (uniqueId.hasPrefix(engineId)) {
						loggingConsumer = logger::warn;
					}
				}
				loggingConsumer.accept(() -> selector + " could not be resolved by " + engineId);
				break;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		return getClass() == obj.getClass();
	}

	@Override
	public int hashCode() {
		return 1;
	}

}
