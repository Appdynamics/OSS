/*
 * Copyright 2015-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package example;

// tag::user_guide[]

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.IndicativeSentencesGeneration;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DisplayNameGeneratorDemo {

	@Nested
	@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
	class A_year_is_not_supported {

		@Test
		void if_it_is_zero() {
		}

		@DisplayName("A negative value for year is not supported by the leap year computation.")
		@ParameterizedTest(name = "For example, year {0} is not supported.")
		@ValueSource(ints = { -1, -4 })
		void if_it_is_negative(int year) {
		}

	}

	@Nested
	@IndicativeSentencesGeneration(separator = " -> ", generator = DisplayNameGenerator.ReplaceUnderscores.class)
	class A_year_is_a_leap_year {

		@Test
		void if_it_is_divisible_by_4_but_not_by_100() {
		}

		@ParameterizedTest(name = "Year {0} is a leap year.")
		@ValueSource(ints = { 2016, 2020, 2048 })
		void if_it_is_one_of_the_following_years(int year) {
		}

	}

}
// end::user_guide[]
