/*
 * Copyright 2013 by Bill Pugh. 
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  This
 * particular file as subject to the "Classpath" exception.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */
package edu.umd.cs.findbugs.formatStringChecker;

import org.junit.Test;

public class FormatterCompileTimeTest {

	@Test
	public void shouldWork()
			throws FormatterException {
		Formatter.check("%d%n%d", "Ljava/lang/Integer;", "Ljava/lang/Short;");

		Formatter.check("%d\n", "Ljava/math/BigInteger;");
		Formatter.check("%f\n", "Ljava/math/BigDecimal;");
	}

	@Test(expected = IllegalFormatConversionException.class)
	public void stringWhereIntegerExpected()
			throws FormatterException {
		Formatter.check("%d", "Ljava/lang/String;");
	}

	@Test(expected = MissingFormatArgumentException.class)
	public void notEnoughParameters()
			throws FormatterException {
		Formatter.check("%s%s", "Ljava/lang/String;");
	}

	@Test(expected = IllegalFormatConversionException.class)
	public void passingAnArray()
			throws FormatterException {
		Formatter.check("%s", "[I");
	}

	@Test(expected = IllegalFormatConversionException.class)
	public void passingAnIntToABoolean()
			throws FormatterException {
		Formatter.check("%b", "Ljava/lang/Integer;");
	}

	@Test(expected = ExtraFormatArgumentsException.class)
	public void tooManyParameters()
			throws FormatterException {
		Formatter.check("%s%s", "Ljava/lang/String;", "Ljava/lang/String;",
				"Ljava/lang/String;");
	}

	@Test
	public void testBug1874856FalsePositive()
			throws FormatterException {
		Formatter.check("s1 Duke's Birthday: %1$tm %1$te, %1$tY",
				"Ljava/util/GregorianCalendar;");
		Formatter.check("s2 Duke's Birthday: %1$tm %<te, %<tY",
				"Ljava/util/GregorianCalendar;");
		Formatter.check("s3 Duke's Birthday: %2$tm %<te, %<tY",
				"Ljava/util/GregorianCalendar;",
				"Ljava/util/GregorianCalendar;");
		Formatter.check("s4 Duke's Birthday: %2$tm %<te, %te %<tY %te",
				"Ljava/util/GregorianCalendar;",
				"Ljava/util/GregorianCalendar;");
		Formatter.check("s6 Duke's Birthday: %1.1f %2$te, %1$f",
				"Ljava/lang/Float;", "Ljava/util/GregorianCalendar;");
	}

	@Test(expected = MissingFormatArgumentException.class)
	public void testBug1874856TruePositive()
			throws FormatterException {
		Formatter.check("s5 Duke's Birthday: %<te, %te %<tY %te %12$tm ",
				"Ljava/util/GregorianCalendar;",
				"Ljava/util/GregorianCalendar;",
				"Ljava/util/GregorianCalendar;",
				"Ljava/util/GregorianCalendar;",
				"Ljava/util/GregorianCalendar;",
				"Ljava/util/GregorianCalendar;",
				"Ljava/util/GregorianCalendar;",
				"Ljava/util/GregorianCalendar;",
				"Ljava/util/GregorianCalendar;",
				"Ljava/util/GregorianCalendar;",
				"Ljava/util/GregorianCalendar;",
				"Ljava/util/GregorianCalendar;");
	}

	@Test
	public void testHandleSqlDates()
			throws FormatterException {
		Formatter.check("%tT", "Ljava/sql/Date;");
		Formatter.check("%tT", "Ljava/sql/Time;");
		Formatter.check("%tT", "Ljava/sql/Timestamp;");

	}

}
