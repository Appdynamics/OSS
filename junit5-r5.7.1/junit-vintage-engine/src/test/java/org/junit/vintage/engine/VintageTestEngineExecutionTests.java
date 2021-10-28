/*
 * Copyright 2015-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.vintage.engine;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectMethod;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectUniqueId;
import static org.junit.platform.testkit.engine.EventConditions.abortedWithReason;
import static org.junit.platform.testkit.engine.EventConditions.container;
import static org.junit.platform.testkit.engine.EventConditions.displayName;
import static org.junit.platform.testkit.engine.EventConditions.dynamicTestRegistered;
import static org.junit.platform.testkit.engine.EventConditions.engine;
import static org.junit.platform.testkit.engine.EventConditions.event;
import static org.junit.platform.testkit.engine.EventConditions.finishedSuccessfully;
import static org.junit.platform.testkit.engine.EventConditions.finishedWithFailure;
import static org.junit.platform.testkit.engine.EventConditions.skippedWithReason;
import static org.junit.platform.testkit.engine.EventConditions.started;
import static org.junit.platform.testkit.engine.EventConditions.test;
import static org.junit.platform.testkit.engine.EventConditions.uniqueIdSubstring;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.instanceOf;
import static org.junit.platform.testkit.engine.TestExecutionResultConditions.message;
import static org.junit.runner.Description.createSuiteDescription;
import static org.junit.runner.Description.createTestDescription;

import org.assertj.core.api.Condition;
import org.junit.AssumptionViolatedException;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.EngineExecutionListener;
import org.junit.platform.engine.ExecutionRequest;
import org.junit.platform.engine.TestDescriptor;
import org.junit.platform.engine.TestEngine;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.UniqueId;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.testkit.engine.EngineExecutionResults;
import org.junit.platform.testkit.engine.EngineTestKit;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.vintage.engine.samples.junit3.JUnit3ParallelSuiteWithSubsuites;
import org.junit.vintage.engine.samples.junit3.JUnit3SuiteWithSubsuites;
import org.junit.vintage.engine.samples.junit3.PlainJUnit3TestCaseWithSingleTestWhichFails;
import org.junit.vintage.engine.samples.junit4.CompletelyDynamicTestCase;
import org.junit.vintage.engine.samples.junit4.EmptyIgnoredTestCase;
import org.junit.vintage.engine.samples.junit4.EnclosedJUnit4TestCase;
import org.junit.vintage.engine.samples.junit4.IgnoredJUnit4TestCase;
import org.junit.vintage.engine.samples.junit4.IgnoredParameterizedTestCase;
import org.junit.vintage.engine.samples.junit4.JUnit4SuiteOfSuiteWithIgnoredJUnit4TestCase;
import org.junit.vintage.engine.samples.junit4.JUnit4SuiteOfSuiteWithJUnit4TestCaseWithAssumptionFailureInBeforeClass;
import org.junit.vintage.engine.samples.junit4.JUnit4SuiteOfSuiteWithJUnit4TestCaseWithErrorInBeforeClass;
import org.junit.vintage.engine.samples.junit4.JUnit4SuiteWithExceptionThrowingRunner;
import org.junit.vintage.engine.samples.junit4.JUnit4SuiteWithIgnoredJUnit4TestCase;
import org.junit.vintage.engine.samples.junit4.JUnit4SuiteWithJUnit3SuiteWithSingleTestCase;
import org.junit.vintage.engine.samples.junit4.JUnit4SuiteWithJUnit4TestCaseWithAssumptionFailureInBeforeClass;
import org.junit.vintage.engine.samples.junit4.JUnit4SuiteWithJUnit4TestCaseWithErrorInBeforeClass;
import org.junit.vintage.engine.samples.junit4.JUnit4SuiteWithJUnit4TestCaseWithFailingDescriptionThatIsNotReportedAsFinished;
import org.junit.vintage.engine.samples.junit4.JUnit4SuiteWithPlainJUnit4TestCaseWithSingleTestWhichIsIgnored;
import org.junit.vintage.engine.samples.junit4.JUnit4TestCaseWithAssumptionFailureInBeforeClass;
import org.junit.vintage.engine.samples.junit4.JUnit4TestCaseWithErrorCollectorStoringMultipleFailures;
import org.junit.vintage.engine.samples.junit4.JUnit4TestCaseWithErrorInAfterClass;
import org.junit.vintage.engine.samples.junit4.JUnit4TestCaseWithErrorInBeforeClass;
import org.junit.vintage.engine.samples.junit4.JUnit4TestCaseWithExceptionThrowingRunner;
import org.junit.vintage.engine.samples.junit4.JUnit4TestCaseWithFailingDescriptionThatIsNotReportedAsFinished;
import org.junit.vintage.engine.samples.junit4.JUnit4TestCaseWithIndistinguishableOverloadedMethod;
import org.junit.vintage.engine.samples.junit4.JUnit4TestCaseWithRunnerWithCustomUniqueIdsAndDisplayNames;
import org.junit.vintage.engine.samples.junit4.JUnit4TestCaseWithRunnerWithDuplicateChangingChildDescriptions;
import org.junit.vintage.engine.samples.junit4.MalformedJUnit4TestCase;
import org.junit.vintage.engine.samples.junit4.ParameterizedTestCase;
import org.junit.vintage.engine.samples.junit4.PlainJUnit4TestCaseWithFiveTestMethods;
import org.junit.vintage.engine.samples.junit4.PlainJUnit4TestCaseWithLifecycleMethods;
import org.junit.vintage.engine.samples.junit4.PlainJUnit4TestCaseWithSingleTestWhichFails;
import org.junit.vintage.engine.samples.junit4.PlainJUnit4TestCaseWithSingleTestWhichIsIgnored;
import org.junit.vintage.engine.samples.junit4.PlainJUnit4TestCaseWithTwoTestMethods;
import org.junit.vintage.engine.samples.spock.SpockTestCaseWithUnrolledAndRegularFeatureMethods;
import org.opentest4j.MultipleFailuresError;

/**
 * @since 4.12
 */
class VintageTestEngineExecutionTests {

	@Test
	void executesPlainJUnit4TestCaseWithSingleTestWhichFails() {
		Class<?> testClass = PlainJUnit4TestCaseWithSingleTestWhichFails.class;

		execute(testClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(testClass), started()), //
			event(test("failingTest"), started()), //
			event(test("failingTest"),
				finishedWithFailure(instanceOf(AssertionError.class), message("this test should fail"))), //
			event(container(testClass), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesPlainJUnit4TestCaseWithTwoTests() {
		Class<?> testClass = PlainJUnit4TestCaseWithTwoTestMethods.class;

		execute(testClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(testClass), started()), //
			event(test("failingTest"), started()), //
			event(test("failingTest"),
				finishedWithFailure(instanceOf(AssertionError.class), message("this test should fail"))), //
			event(test("successfulTest"), started()), //
			event(test("successfulTest"), finishedSuccessfully()), //
			event(container(testClass), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesPlainJUnit4TestCaseWithFiveTests() {
		Class<?> testClass = PlainJUnit4TestCaseWithFiveTestMethods.class;

		execute(testClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(testClass), started()), //
			event(test("abortedTest"), started()), //
			event(test("abortedTest"),
				abortedWithReason(instanceOf(AssumptionViolatedException.class),
					message("this test should be aborted"))), //
			event(test("failingTest"), started()), //
			event(test("failingTest"),
				finishedWithFailure(instanceOf(AssertionError.class), message("this test should fail"))), //
			event(test("ignoredTest1_withoutReason"), skippedWithReason("")), //
			event(test("ignoredTest2_withReason"), skippedWithReason("a custom reason")), //
			event(test("successfulTest"), started()), //
			event(test("successfulTest"), finishedSuccessfully()), //
			event(container(testClass), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesEnclosedJUnit4TestCase() {
		Class<?> testClass = EnclosedJUnit4TestCase.class;
		Class<?> nestedClass = EnclosedJUnit4TestCase.NestedClass.class;

		execute(testClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(testClass), started()), //
			event(container(nestedClass), started()), //
			event(test("successfulTest"), started()), //
			event(test("successfulTest"), finishedSuccessfully()), //
			event(test("failingTest"), started()), //
			event(test("failingTest"),
				finishedWithFailure(instanceOf(AssertionError.class), message("this test should fail"))), //
			event(container(nestedClass), finishedSuccessfully()), //
			event(container(testClass), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesJUnit4SuiteWithJUnit3SuiteWithSingleTestCase() {
		Class<?> junit4SuiteClass = JUnit4SuiteWithJUnit3SuiteWithSingleTestCase.class;
		Class<?> testClass = PlainJUnit3TestCaseWithSingleTestWhichFails.class;

		execute(junit4SuiteClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(junit4SuiteClass), started()), //
			event(container("TestSuite with 1 tests"), started()), //
			event(container(testClass), started()), //
			event(test("test"), started()), //
			event(test("test"),
				finishedWithFailure(instanceOf(AssertionError.class), message("this test should fail"))), //
			event(container(testClass), finishedSuccessfully()), //
			event(container("TestSuite with 1 tests"), finishedSuccessfully()), //
			event(container(junit4SuiteClass), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesMalformedJUnit4TestCase() {
		Class<?> testClass = MalformedJUnit4TestCase.class;

		execute(testClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(testClass), started()), //
			event(test("initializationError"), started()), //
			event(test("initializationError"),
				finishedWithFailure(message(it -> it.contains("Method nonPublicTest() should be public")))), //
			event(container(testClass), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesJUnit4TestCaseWithErrorInBeforeClass() {
		Class<?> testClass = JUnit4TestCaseWithErrorInBeforeClass.class;

		execute(testClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(testClass), started()), //
			event(container(testClass),
				finishedWithFailure(instanceOf(AssertionError.class), message("something went wrong"))), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesJUnit4SuiteWithJUnit4TestCaseWithErrorInBeforeClass() {
		Class<?> suiteClass = JUnit4SuiteWithJUnit4TestCaseWithErrorInBeforeClass.class;
		Class<?> testClass = JUnit4TestCaseWithErrorInBeforeClass.class;

		execute(suiteClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(suiteClass), started()), //
			event(container(testClass), started()), //
			event(container(testClass),
				finishedWithFailure(instanceOf(AssertionError.class), message("something went wrong"))), //
			event(container(suiteClass), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesJUnit4SuiteOfSuiteWithJUnit4TestCaseWithErrorInBeforeClass() {
		Class<?> suiteOfSuiteClass = JUnit4SuiteOfSuiteWithJUnit4TestCaseWithErrorInBeforeClass.class;
		Class<?> suiteClass = JUnit4SuiteWithJUnit4TestCaseWithErrorInBeforeClass.class;
		Class<?> testClass = JUnit4TestCaseWithErrorInBeforeClass.class;

		execute(suiteOfSuiteClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(suiteOfSuiteClass), started()), //
			event(container(suiteClass), started()), //
			event(container(testClass), started()), //
			event(container(testClass),
				finishedWithFailure(instanceOf(AssertionError.class), message("something went wrong"))), //
			event(container(suiteClass), finishedSuccessfully()), //
			event(container(suiteOfSuiteClass), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesJUnit4TestCaseWithAssumptionFailureInBeforeClass() {
		Class<?> testClass = JUnit4TestCaseWithAssumptionFailureInBeforeClass.class;

		execute(testClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(testClass), started()), //
			event(container(testClass),
				abortedWithReason(instanceOf(AssumptionViolatedException.class), message("assumption violated"))), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesJUnit4SuiteOfSuiteWithJUnit4TestCaseWithAssumptionFailureInBeforeClass() {
		Class<?> suiteOfSuiteClass = JUnit4SuiteOfSuiteWithJUnit4TestCaseWithAssumptionFailureInBeforeClass.class;
		Class<?> suiteClass = JUnit4SuiteWithJUnit4TestCaseWithAssumptionFailureInBeforeClass.class;
		Class<?> testClass = JUnit4TestCaseWithAssumptionFailureInBeforeClass.class;

		execute(suiteOfSuiteClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(suiteOfSuiteClass), started()), //
			event(container(suiteClass), started()), //
			event(container(testClass), started()), //
			event(container(testClass),
				abortedWithReason(instanceOf(AssumptionViolatedException.class), message("assumption violated"))), //
			event(container(suiteClass), finishedSuccessfully()), //
			event(container(suiteOfSuiteClass), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesJUnit4TestCaseWithErrorInAfterClass() {
		Class<?> testClass = JUnit4TestCaseWithErrorInAfterClass.class;

		execute(testClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(testClass), started()), //
			event(test("failingTest"), started()), //
			event(test("failingTest"),
				finishedWithFailure(instanceOf(AssertionError.class), message("expected to fail"))), //
			event(test("succeedingTest"), started()), //
			event(test("succeedingTest"), finishedSuccessfully()), //
			event(container(testClass),
				finishedWithFailure(instanceOf(AssertionError.class), message("error in @AfterClass"))), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesJUnit4TestCaseWithOverloadedMethod() {
		Class<?> testClass = JUnit4TestCaseWithIndistinguishableOverloadedMethod.class;

		execute(testClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(testClass), started()), //
			event(test("theory(" + JUnit4TestCaseWithIndistinguishableOverloadedMethod.class.getName() + ")[0]"),
				started()), //
			event(test("theory(" + JUnit4TestCaseWithIndistinguishableOverloadedMethod.class.getName() + ")[0]"),
				finishedWithFailure()), //
			event(test("theory(" + JUnit4TestCaseWithIndistinguishableOverloadedMethod.class.getName() + ")[1]"),
				started()), //
			event(test("theory(" + JUnit4TestCaseWithIndistinguishableOverloadedMethod.class.getName() + ")[1]"),
				finishedWithFailure()), //
			event(container(testClass), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesIgnoredJUnit4TestCase() {
		Class<?> testClass = IgnoredJUnit4TestCase.class;

		execute(testClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(testClass), skippedWithReason("complete class is ignored")), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesEmptyIgnoredTestClass() {
		Class<?> testClass = EmptyIgnoredTestCase.class;

		execute(testClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(test(testClass.getName()), skippedWithReason("empty")), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void reportsExecutionEventsAroundLifecycleMethods() {
		Class<?> testClass = PlainJUnit4TestCaseWithLifecycleMethods.class;
		PlainJUnit4TestCaseWithLifecycleMethods.EVENTS.clear();

		EngineExecutionListener listener = new EngineExecutionListener() {

			@Override
			public void executionStarted(TestDescriptor testDescriptor) {
				PlainJUnit4TestCaseWithLifecycleMethods.EVENTS.add(
					"executionStarted:" + testDescriptor.getDisplayName());
			}

			@Override
			public void executionFinished(TestDescriptor testDescriptor, TestExecutionResult testExecutionResult) {
				PlainJUnit4TestCaseWithLifecycleMethods.EVENTS.add(
					"executionFinished:" + testDescriptor.getDisplayName());
			}

			@Override
			public void executionSkipped(TestDescriptor testDescriptor, String reason) {
				PlainJUnit4TestCaseWithLifecycleMethods.EVENTS.add(
					"executionSkipped:" + testDescriptor.getDisplayName());
			}

			@Override
			public void dynamicTestRegistered(TestDescriptor testDescriptor) {
			}

			@Override
			public void reportingEntryPublished(TestDescriptor testDescriptor, ReportEntry entry) {
			}
		};

		execute(testClass, listener);

		// @formatter:off
		assertThat(PlainJUnit4TestCaseWithLifecycleMethods.EVENTS).containsExactly(
			"executionStarted:JUnit Vintage",
				"executionStarted:" + testClass.getSimpleName(),
					"beforeClass",
						"executionStarted:failingTest",
							"before",
								"failingTest",
							"after",
						"executionFinished:failingTest",
						"executionSkipped:skippedTest",
						"executionStarted:succeedingTest",
							"before",
								"succeedingTest",
							"after",
						"executionFinished:succeedingTest",
					"afterClass",
				"executionFinished:" + testClass.getSimpleName(),
			"executionFinished:JUnit Vintage"
		);
		// @formatter:on
	}

	@Test
	void executesJUnit4SuiteWithPlainJUnit4TestCaseWithSingleTestWhichIsIgnored() {
		Class<?> suiteClass = JUnit4SuiteWithPlainJUnit4TestCaseWithSingleTestWhichIsIgnored.class;
		Class<?> testClass = PlainJUnit4TestCaseWithSingleTestWhichIsIgnored.class;

		execute(suiteClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(suiteClass), started()), //
			event(container(testClass), started()), //
			event(test("ignoredTest"), skippedWithReason("ignored test")), //
			event(container(testClass), finishedSuccessfully()), //
			event(container(suiteClass), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesJUnit4SuiteOfSuiteWithIgnoredJUnit4TestCase() {
		Class<?> suiteOfSuiteClass = JUnit4SuiteOfSuiteWithIgnoredJUnit4TestCase.class;
		Class<?> suiteClass = JUnit4SuiteWithIgnoredJUnit4TestCase.class;
		Class<?> testClass = IgnoredJUnit4TestCase.class;

		execute(suiteOfSuiteClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(suiteOfSuiteClass), started()), //
			event(container(suiteClass), started()), //
			event(container(testClass), skippedWithReason("complete class is ignored")), //
			event(container(suiteClass), finishedSuccessfully()), //
			event(container(suiteOfSuiteClass), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesParameterizedTestCase() {
		Class<?> testClass = ParameterizedTestCase.class;

		execute(testClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(testClass), started()), //
			event(container("[foo]"), started()), //
			event(test("test[foo]"), started()), //
			event(test("test[foo]"), finishedSuccessfully()), //
			event(container("[foo]"), finishedSuccessfully()), //
			event(container("[bar]"), started()), //
			event(test("test[bar]"), started()), //
			event(test("test[bar]"),
				finishedWithFailure(instanceOf(AssertionError.class), message("expected:<[foo]> but was:<[bar]>"))), //
			event(container("[bar]"), finishedSuccessfully()), //
			event(container(testClass), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesIgnoredParameterizedTestCase() {
		Class<?> testClass = IgnoredParameterizedTestCase.class;

		execute(testClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(testClass), started()), //
			event(container("[foo]"), started()), //
			event(test("test[foo]"), skippedWithReason("")), //
			event(container("[foo]"), finishedSuccessfully()), //
			event(container("[bar]"), started()), //
			event(test("test[bar]"), skippedWithReason("")), //
			event(container("[bar]"), finishedSuccessfully()), //
			event(container(testClass), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesJUnit4TestCaseWithExceptionThrowingRunner() {
		Class<?> testClass = JUnit4TestCaseWithExceptionThrowingRunner.class;

		execute(testClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(test(testClass.getName()), started()), //
			event(test(testClass.getName()), finishedWithFailure()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesJUnit4SuiteWithExceptionThrowingRunner() {
		Class<?> testClass = JUnit4SuiteWithExceptionThrowingRunner.class;

		execute(testClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(testClass), started()), //
			event(container(testClass), finishedWithFailure()), //
			event(engine(), finishedSuccessfully()));
	}

	public static class DynamicSuiteRunner extends Runner {

		private final Class<?> testClass;

		public DynamicSuiteRunner(Class<?> testClass) {
			this.testClass = testClass;
		}

		@Override
		public Description getDescription() {
			return createSuiteDescription(testClass);
		}

		@Override
		public void run(RunNotifier notifier) {
			Description dynamicDescription = createTestDescription(testClass, "dynamicTest");
			notifier.fireTestStarted(dynamicDescription);
			notifier.fireTestFinished(dynamicDescription);
		}

	}

	@RunWith(DynamicSuiteRunner.class)
	public static class DynamicTestClass {
	}

	@Test
	void reportsDynamicTestsForUnknownDescriptions() {
		Class<?> testClass = DynamicTestClass.class;

		execute(testClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(test(testClass.getName()), started()), //
			event(dynamicTestRegistered("dynamicTest")), //
			event(test("dynamicTest"), started()), //
			event(test("dynamicTest"), finishedSuccessfully()), //
			event(test(testClass.getName()), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	public static class DynamicAndStaticChildrenRunner extends Runner {

		private final Class<?> testClass;

		public DynamicAndStaticChildrenRunner(Class<?> testClass) {
			this.testClass = testClass;
		}

		@Override
		public Description getDescription() {
			Description suiteDescription = createSuiteDescription(testClass);
			suiteDescription.addChild(createTestDescription(testClass, "staticTest"));
			return suiteDescription;
		}

		@Override
		public void run(RunNotifier notifier) {
			Description staticDescription = getDescription().getChildren().get(0);
			notifier.fireTestStarted(staticDescription);
			notifier.fireTestFinished(staticDescription);
			Description dynamicDescription = createTestDescription(testClass, "dynamicTest");
			notifier.fireTestStarted(dynamicDescription);
			notifier.fireTestFinished(dynamicDescription);
		}

	}

	@RunWith(DynamicAndStaticChildrenRunner.class)
	public static class DynamicAndStaticTestClass {
	}

	@RunWith(Suite.class)
	@SuiteClasses(DynamicAndStaticTestClass.class)
	public static class SuiteWithDynamicAndStaticTestClass {
	}

	@Test
	void reportsIntermediateContainersFinishedAfterTheirDynamicChildren() {
		Class<?> suiteClass = SuiteWithDynamicAndStaticTestClass.class;
		Class<?> testClass = DynamicAndStaticTestClass.class;

		execute(suiteClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(suiteClass.getName()), started()), //
			event(container(testClass.getName()), started()), //
			event(test("staticTest"), started()), //
			event(test("staticTest"), finishedSuccessfully()), //
			event(dynamicTestRegistered("dynamicTest")), //
			event(test("dynamicTest"), started()), //
			event(test("dynamicTest"), finishedSuccessfully()), //
			event(container(testClass.getName()), finishedSuccessfully()), //
			event(container(suiteClass.getName()), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	public static class MisbehavingChildlessRunner extends Runner {

		private final Class<?> testClass;

		public MisbehavingChildlessRunner(Class<?> testClass) {
			this.testClass = testClass;
		}

		@Override
		public Description getDescription() {
			return createSuiteDescription(testClass);
		}

		@Override
		public void run(RunNotifier notifier) {
			notifier.fireTestStarted(createTestDescription(testClass, "doesNotExist"));
		}

	}

	@RunWith(MisbehavingChildlessRunner.class)
	public static class MisbehavingChildTestClass {

	}

	@Test
	void ignoreEventsForUnknownDescriptionsByMisbehavingChildlessRunner() {
		Class<?> testClass = MisbehavingChildTestClass.class;

		execute(testClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(test(testClass.getName()), started()), //
			event(dynamicTestRegistered("doesNotExist")), //
			event(test("doesNotExist"), started()), //
			event(test(testClass.getName()), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesJUnit4TestCaseWithRunnerWithCustomUniqueIds() {
		Class<?> testClass = JUnit4TestCaseWithRunnerWithCustomUniqueIdsAndDisplayNames.class;

		execute(testClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(testClass), started()), //
			event(uniqueIdSubstring(testClass.getName()), started()), //
			event(uniqueIdSubstring(testClass.getName()), finishedWithFailure()), //
			event(container(testClass), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesJUnit4TestCaseWithErrorCollectorStoringMultipleFailures() {
		Class<?> testClass = JUnit4TestCaseWithErrorCollectorStoringMultipleFailures.class;

		execute(testClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(testClass), started()), //
			event(test("example"), started()), //
			event(test("example"), //
				finishedWithFailure(instanceOf(MultipleFailuresError.class), //
					new Condition<>(throwable -> ((MultipleFailuresError) throwable).getFailures().size() == 3,
						"Must contain multiple errors (3)"))), //
			event(container(testClass), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesJUnit4TestCaseWithFailingDescriptionThatIsNotReportedAsFinished() {
		Class<?> testClass = JUnit4TestCaseWithFailingDescriptionThatIsNotReportedAsFinished.class;

		execute(testClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(testClass), started()), //
			event(test("testWithMissingEvents"), started()), //
			event(test("testWithMissingEvents"), finishedWithFailure()), //
			event(container(testClass), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesJUnit4SuiteWithJUnit4TestCaseWithFailingDescriptionThatIsNotReportedAsFinished() {
		Class<?> suiteClass = JUnit4SuiteWithJUnit4TestCaseWithFailingDescriptionThatIsNotReportedAsFinished.class;
		Class<?> firstTestClass = JUnit4TestCaseWithFailingDescriptionThatIsNotReportedAsFinished.class;
		Class<?> secondTestClass = PlainJUnit4TestCaseWithSingleTestWhichFails.class;

		execute(suiteClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(suiteClass), started()), //
			event(container(firstTestClass), started()), //
			event(test("testWithMissingEvents"), started()), //
			event(test("testWithMissingEvents"), finishedWithFailure()), //
			event(container(firstTestClass), finishedSuccessfully()), //
			event(container(secondTestClass), started()), //
			event(test("failingTest"), started()), //
			event(test("failingTest"), finishedWithFailure()), //
			event(container(secondTestClass), finishedSuccessfully()), //
			event(container(suiteClass), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesCompletelyDynamicTestCaseDiscoveredByUniqueId() {
		Class<?> testClass = CompletelyDynamicTestCase.class;
		var request = LauncherDiscoveryRequestBuilder.request().selectors(
			selectUniqueId(VintageUniqueIdBuilder.uniqueIdForClass(testClass))).build();

		execute(request).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(displayName(testClass.getSimpleName()), started()), //
			event(dynamicTestRegistered("Test #0")), //
			event(test("Test #0"), started()), //
			event(test("Test #0"), finishedSuccessfully()), //
			event(displayName(testClass.getSimpleName()), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesJUnit3ParallelSuiteWithSubsuites() {
		var suiteClass = JUnit3ParallelSuiteWithSubsuites.class;
		var results = execute(suiteClass);
		results.containerEvents() //
				.assertStatistics(stats -> stats.started(4).dynamicallyRegistered(0).finished(4).succeeded(4)) //
				.assertEventsMatchExactly( //
					event(engine(), started()), //
					event(container(suiteClass), started()), //
					event(container("Case"), started()), //
					event(container("Case")), //
					event(container("Case")), //
					event(container("Case"), finishedSuccessfully()), //
					event(container(suiteClass), finishedSuccessfully()), //
					event(engine(), finishedSuccessfully()));
		results.testEvents() //
				.assertStatistics(stats -> stats.started(2).dynamicallyRegistered(0).finished(2).succeeded(2)) //
				.assertEventsMatchExactly( //
					event(test("hello"), started()), //
					event(test("hello")), //
					event(test("hello")), //
					event(test("hello"), finishedSuccessfully()));
	}

	@Test
	void executesJUnit3SuiteWithSubsuites() {
		var suiteClass = JUnit3SuiteWithSubsuites.class;
		execute(suiteClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(suiteClass), started()), //
			event(container("Case1"), started()), //
			event(test("hello"), started()), //
			event(test("hello"), finishedSuccessfully()), //
			event(container("Case1"), finishedSuccessfully()), //
			event(container("Case2"), started()), //
			event(test("hello"), started()), //
			event(test("hello"), finishedSuccessfully()), //
			event(container("Case2"), finishedSuccessfully()), //
			event(container(suiteClass), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesJUnit4TestCaseWithRunnerWithDuplicateChangingChildDescriptions() {
		Class<?> testClass = JUnit4TestCaseWithRunnerWithDuplicateChangingChildDescriptions.class;
		execute(testClass).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(testClass), started()), //
			event(container("1st"), started()), //
			event(test("0"), skippedWithReason(__ -> true)), //
			event(test("1"), started()), //
			event(test("1"), finishedSuccessfully()), //
			event(container("1st"), finishedSuccessfully()), //
			event(container("2nd"), started()), //
			event(test("0"), skippedWithReason(__ -> true)), //
			event(test("1"), started()), //
			event(test("1"), finishedSuccessfully()), //
			event(container("2nd"), finishedSuccessfully()), //
			event(container(testClass), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesUnrolledSpockFeatureMethod() {
		Class<?> testClass = SpockTestCaseWithUnrolledAndRegularFeatureMethods.class;
		var request = LauncherDiscoveryRequestBuilder.request().selectors(
			selectMethod(testClass, "unrolled feature for #input")).build();
		execute(request).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(uniqueIdSubstring(testClass.getName()), started()), //
			event(dynamicTestRegistered("unrolled feature for 23")), //
			event(test("unrolled feature for 23"), started()), //
			event(test("unrolled feature for 23"), finishedWithFailure()), //
			event(dynamicTestRegistered("unrolled feature for 42")), //
			event(test("unrolled feature for 42"), started()), //
			event(test("unrolled feature for 42"), finishedSuccessfully()), //
			event(uniqueIdSubstring(testClass.getName()), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	@Test
	void executesRegularSpockFeatureMethod() {
		Class<?> testClass = SpockTestCaseWithUnrolledAndRegularFeatureMethods.class;
		var request = LauncherDiscoveryRequestBuilder.request().selectors(selectMethod(testClass, "regular")).build();
		execute(request).allEvents().assertEventsMatchExactly( //
			event(engine(), started()), //
			event(container(testClass), started()), //
			event(test("regular"), started()), //
			event(test("regular"), finishedSuccessfully()), //
			event(container(testClass), finishedSuccessfully()), //
			event(engine(), finishedSuccessfully()));
	}

	private static EngineExecutionResults execute(Class<?> testClass) {
		return execute(request(testClass));
	}

	private static EngineExecutionResults execute(LauncherDiscoveryRequest request) {
		return EngineTestKit.execute(new VintageTestEngine(), request);
	}

	private static void execute(Class<?> testClass, EngineExecutionListener listener) {
		TestEngine testEngine = new VintageTestEngine();
		LauncherDiscoveryRequest discoveryRequest = request(testClass);
		TestDescriptor engineTestDescriptor = testEngine.discover(discoveryRequest,
			UniqueId.forEngine(testEngine.getId()));
		testEngine.execute(
			new ExecutionRequest(engineTestDescriptor, listener, discoveryRequest.getConfigurationParameters()));
	}

	private static LauncherDiscoveryRequest request(Class<?> testClass) {
		return LauncherDiscoveryRequestBuilder.request().selectors(selectClass(testClass)).build();
	}

}
