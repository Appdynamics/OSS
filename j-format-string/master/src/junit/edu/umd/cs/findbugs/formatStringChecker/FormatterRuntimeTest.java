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

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.IllegalFormatConversionException;
import java.util.MissingFormatArgumentException;
import java.util.UnknownFormatConversionException;

import org.junit.Test;

import edu.umd.cs.findbugs.annotations.ExpectWarning;
import edu.umd.cs.findbugs.annotations.NoWarning;

public class FormatterRuntimeTest {

	@Test
	@NoWarning("FS")
	public void shouldWork() {
		System.out.println(String.format("%d%n%d", 42, (short) 42));
		System.out.println(String.format("%d%n", new BigInteger("42")));
		System.out.println(String.format("%f%n", new BigDecimal(42)));

	}

	@Test(expected = IllegalFormatConversionException.class)
	@ExpectWarning("FS")
	public void stringWhereIntegerExpected() {
		System.out.println(String.format("%d", "test"));
	}

	@Test(expected = MissingFormatArgumentException.class)
	@ExpectWarning("FS")
	public void notEnoughParameters() {
		System.out.println(String.format("%s%s", "test"));
	}

	@ExpectWarning("VA_FORMAT_STRING_BAD_CONVERSION_FROM_ARRAY")
	public void passingAnArray() {
		System.out.println(System.out.printf("%s", new int[] { 42, 17 }));
	}

	@ExpectWarning("FS")
	public void passingAnIntToABoolean() {
		System.out.println(System.out.printf("%b", 0));
	}

	@Test(expected = UnknownFormatConversionException.class)
	@ExpectWarning("FS")
	public void formatDateWithY() {
		System.out.println(String.format("%Y", new Date()));
	}

	@Test
	@NoWarning("FS")
	public void testBug1874856FalsePositive() {
		// None of these should yield warnings
		Calendar c = new GregorianCalendar(1993, 4, 23);
		String s1 = String.format("s1 Duke's Birthday: %1$tm %1$te, %1$tY", c);
		System.out.println(s1);
		String s2 = String.format("s2 Duke's Birthday: %1$tm %<te, %<tY", c);
		System.out.println(s2);
		String s3 = String.format("s3 Duke's Birthday: %2$tm %<te, %<tY", c, c);
		System.out.println(s3);
		String s4 = String.format(
				"s4 Duke's Birthday: %2$tm %<te, %te %<tY %te", c, c);
		System.out.println(s4);
		String s6 = String.format("s6 Duke's Birthday: %1.1f %2$te, %1$f", 1.0,
				c);
		System.out.println(s6);
	}

	@Test(expected = MissingFormatArgumentException.class)
	@ExpectWarning("FS")
	public void testBug1874856TruePositive() {
		Calendar c = new GregorianCalendar(1993, 4, 23);
		// Actually, this one should generate a warning
		String s5 = String.format(
				"s5 Duke's Birthday: %<te, %te %<tY %te %12$tm ", c, c, c, c,
				c, c, c, c, c, c, c, c);
		System.out.println(s5);

	}

	@Test(expected = IllegalFormatConversionException.class)
	@ExpectWarning("FS")
	public void testDateMismatch() {
		System.out.printf("%tY\n", "2008");
	}

	@Test
	@ExpectWarning("FS")
	public void testSqlDates() {
		Calendar c = new GregorianCalendar(1993, 4, 23, 12, 34, 56);
		java.sql.Date date = new java.sql.Date(c.getTimeInMillis());
		java.sql.Time time = new java.sql.Time(c.getTimeInMillis());
		java.sql.Timestamp timestamp = new java.sql.Timestamp(
				c.getTimeInMillis());
		assertEquals("05/23/93 12:34:56",
				String.format("%1$tD %1$tT", date, date));
		assertEquals("05/23/93 12:34:56",
				String.format("%1$tD %1$tT", time, time));
		assertEquals("05/23/93 12:34:56",
				String.format("%1$tD %1$tT", timestamp, timestamp));

	}

}
