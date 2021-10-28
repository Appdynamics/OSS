/*
 * Copyright 2015-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.platform.commons.util;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.junit.platform.commons.util.CollectionUtils.toUnmodifiableList;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.platform.commons.PreconditionViolationException;

/**
 * Unit tests for {@link CollectionUtils}.
 *
 * @since 1.0
 */
class CollectionUtilsTests {

	@Test
	void getOnlyElementWithNullCollection() {
		var exception = assertThrows(PreconditionViolationException.class, () -> CollectionUtils.getOnlyElement(null));
		assertEquals("collection must not be null", exception.getMessage());
	}

	@Test
	void getOnlyElementWithEmptyCollection() {
		var exception = assertThrows(PreconditionViolationException.class,
			() -> CollectionUtils.getOnlyElement(Set.of()));
		assertEquals("collection must contain exactly one element: []", exception.getMessage());
	}

	@Test
	void getOnlyElementWithSingleElementCollection() {
		var expected = new Object();
		var actual = CollectionUtils.getOnlyElement(Set.of(expected));
		assertSame(expected, actual);
	}

	@Test
	void getOnlyElementWithMultiElementCollection() {
		var exception = assertThrows(PreconditionViolationException.class,
			() -> CollectionUtils.getOnlyElement(List.of("foo", "bar")));
		assertEquals("collection must contain exactly one element: [foo, bar]", exception.getMessage());
	}

	@Test
	void toUnmodifiableListThrowsOnMutation() {
		var numbers = Stream.of(1).collect(toUnmodifiableList());
		assertThrows(UnsupportedOperationException.class, numbers::clear);
	}

	@Test
	void toStreamWithNull() {
		Exception exception = assertThrows(PreconditionViolationException.class, () -> CollectionUtils.toStream(null));

		assertThat(exception).hasMessage("Object must not be null");
	}

	@Test
	void toStreamWithUnsupportedObjectType() {
		Exception exception = assertThrows(PreconditionViolationException.class,
			() -> CollectionUtils.toStream("unknown"));

		assertThat(exception).hasMessage("Cannot convert instance of java.lang.String into a Stream: unknown");
	}

	@Test
	void toStreamWithExistingStream() {
		var input = Stream.of("foo");

		var result = CollectionUtils.toStream(input);

		assertThat(result).isSameAs(input);
	}

	@Test
	@SuppressWarnings("unchecked")
	void toStreamWithDoubleStream() {
		var input = DoubleStream.of(42.23);

		var result = (Stream<Double>) CollectionUtils.toStream(input);

		assertThat(result).containsExactly(42.23);
	}

	@Test
	@SuppressWarnings("unchecked")
	void toStreamWithIntStream() {
		var input = IntStream.of(23, 42);

		var result = (Stream<Integer>) CollectionUtils.toStream(input);

		assertThat(result).containsExactly(23, 42);
	}

	@Test
	@SuppressWarnings("unchecked")
	void toStreamWithLongStream() {
		var input = LongStream.of(23L, 42L);

		var result = (Stream<Long>) CollectionUtils.toStream(input);

		assertThat(result).containsExactly(23L, 42L);
	}

	@Test
	@SuppressWarnings({ "unchecked", "serial" })
	void toStreamWithCollection() {
		var collectionStreamClosed = new AtomicBoolean(false);
		Collection<String> input = new ArrayList<>() {

			{
				add("foo");
				add("bar");
			}

			@Override
			public Stream<String> stream() {
				return super.stream().onClose(() -> collectionStreamClosed.set(true));
			}
		};

		try (var stream = (Stream<String>) CollectionUtils.toStream(input)) {
			var result = stream.collect(toList());
			assertThat(result).containsExactly("foo", "bar");
		}

		assertThat(collectionStreamClosed.get()).describedAs("collectionStreamClosed").isTrue();
	}

	@Test
	@SuppressWarnings("unchecked")
	void toStreamWithIterable() {

		Iterable<String> input = () -> List.of("foo", "bar").iterator();

		var result = (Stream<String>) CollectionUtils.toStream(input);

		assertThat(result).containsExactly("foo", "bar");
	}

	@Test
	@SuppressWarnings("unchecked")
	void toStreamWithIterator() {
		var input = List.of("foo", "bar").iterator();

		var result = (Stream<String>) CollectionUtils.toStream(input);

		assertThat(result).containsExactly("foo", "bar");
	}

	@Test
	@SuppressWarnings("unchecked")
	void toStreamWithArray() {
		var result = (Stream<String>) CollectionUtils.toStream(new String[] { "foo", "bar" });

		assertThat(result).containsExactly("foo", "bar");
	}

	@TestFactory
	Stream<DynamicTest> toStreamWithPrimitiveArrays() {
		//@formatter:off
		return Stream.of(
				dynamicTest("boolean[]",
						() -> toStreamWithPrimitiveArray(new boolean[] { true, false })),
				dynamicTest("byte[]",
						() -> toStreamWithPrimitiveArray(new byte[] { 0, Byte.MIN_VALUE, Byte.MAX_VALUE })),
				dynamicTest("char[]",
						() -> toStreamWithPrimitiveArray(new char[] { 0, Character.MIN_VALUE, Character.MAX_VALUE })),
				dynamicTest("double[]",
						() -> toStreamWithPrimitiveArray(new double[] { 0, Double.MIN_VALUE, Double.MAX_VALUE })),
				dynamicTest("float[]",
						() -> toStreamWithPrimitiveArray(new float[] { 0, Float.MIN_VALUE, Float.MAX_VALUE })),
				dynamicTest("int[]",
						() -> toStreamWithPrimitiveArray(new int[] { 0, Integer.MIN_VALUE, Integer.MAX_VALUE })),
				dynamicTest("long[]",
						() -> toStreamWithPrimitiveArray(new long[] { 0, Long.MIN_VALUE, Long.MAX_VALUE })),
				dynamicTest("short[]",
						() -> toStreamWithPrimitiveArray(new short[] { 0, Short.MIN_VALUE, Short.MAX_VALUE }))
		);
		//@formatter:on
	}

	private void toStreamWithPrimitiveArray(Object primitiveArray) {
		assertTrue(primitiveArray.getClass().isArray());
		assertTrue(primitiveArray.getClass().getComponentType().isPrimitive());
		var result = CollectionUtils.toStream(primitiveArray).toArray();
		for (var i = 0; i < result.length; i++) {
			assertEquals(Array.get(primitiveArray, i), result[i]);
		}
	}

}
