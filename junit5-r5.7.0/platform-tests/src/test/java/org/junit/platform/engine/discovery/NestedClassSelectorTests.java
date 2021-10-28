/*
 * Copyright 2015-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.platform.engine.discovery;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.platform.AbstractEqualsAndHashCodeTests;
import org.junit.platform.commons.PreconditionViolationException;

/**
 * Unit tests for {@link NestedClassSelector}.
 *
 * @since 1.6
 * @see DiscoverySelectorsTests
 */
class NestedClassSelectorTests extends AbstractEqualsAndHashCodeTests {

	@Test
	void equalsAndHashCode() {
		var selector1 = new NestedClassSelector(List.of("org.example.EnclosingTestClass"),
			"org.example.NestedTestClass");
		var selector2 = new NestedClassSelector(List.of("org.example.EnclosingTestClass"),
			"org.example.NestedTestClass");
		var selector3 = new NestedClassSelector(List.of("org.example.X"), "org.example.Y");

		assertEqualsAndHashCode(selector1, selector2, selector3);
	}

	@Test
	void preservesOriginalExceptionWhenTryingToLoadEnclosingClasses() {
		var selector = new NestedClassSelector(List.of("org.example.EnclosingTestClass"),
			"org.example.NestedTestClass");

		var exception = assertThrows(PreconditionViolationException.class, selector::getEnclosingClasses);

		assertThat(exception).hasMessage("Could not load class with name: org.example.EnclosingTestClass") //
				.hasCauseInstanceOf(ClassNotFoundException.class);
	}

	@Test
	void preservesOriginalExceptionWhenTryingToLoadNestedClass() {
		var selector = new NestedClassSelector(List.of("org.example.EnclosingTestClass"),
			"org.example.NestedTestClass");

		var exception = assertThrows(PreconditionViolationException.class, selector::getNestedClass);

		assertThat(exception).hasMessage("Could not load class with name: org.example.NestedTestClass") //
				.hasCauseInstanceOf(ClassNotFoundException.class);
	}
}
