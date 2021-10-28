/*
 * Copyright 2015-2020 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package platform.tooling.support.tests;

import static org.junit.jupiter.api.Assertions.assertLinesMatch;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * @since 1.6
 */
class GradleModuleFileTests {

	@Test
	void jupiterAggregatorGradleModuleMetadataVariants() throws Exception {
		var expected = List.of(">> HEAD >>", //
			"{", //
			"  \"formatVersion\": \"1.1\",", //
			"  \"component\": {", //
			"    \"group\": \"org.junit.jupiter\",", //
			"    \"module\": \"junit-jupiter\",", //
			">> VERSION >>", //
			"    \"attributes\": {", //
			">> STATUS >>", //
			"    }", //
			"  },", //
			">> CREATED_BY >>", //
			"  \"variants\": [", //
			"    {", //
			"      \"name\": \"apiElements\",", //
			"      \"attributes\": {", //
			"        \"org.gradle.category\": \"library\",", //
			"        \"org.gradle.dependency.bundling\": \"external\",", //
			"        \"org.gradle.jvm.version\": 8,", //
			"        \"org.gradle.libraryelements\": \"jar\",", //
			"        \"org.gradle.usage\": \"java-api\"", //
			"      },", //
			"      \"dependencies\": [", //
			"        {", //
			"          \"group\": \"org.junit\",", //
			"          \"module\": \"junit-bom\",", //
			"          \"version\": {", //
			">> VERSION >>", //
			"          },", //
			"          \"attributes\": {", //
			"            \"org.gradle.category\": \"platform\"", //
			"          },", //
			"          \"endorseStrictVersions\": true", //
			"        },", //
			"        {", //
			"          \"group\": \"org.junit.jupiter\",", //
			"          \"module\": \"junit-jupiter-api\",", //
			"          \"version\": {", //
			">> VERSION >>", //
			"          }", //
			"        },", //
			"        {", //
			"          \"group\": \"org.junit.jupiter\",", //
			"          \"module\": \"junit-jupiter-params\",", //
			"          \"version\": {", //
			">> VERSION >>", //
			"          }", //
			"        }", //
			"      ],", //
			"      \"files\": [", //
			"        {", //
			">> JAR_FILE_DETAILS >>", //
			"        }", //
			"      ]", //
			"    },", //
			"    {", //
			"      \"name\": \"runtimeElements\",", //
			"      \"attributes\": {", //
			"        \"org.gradle.category\": \"library\",", //
			"        \"org.gradle.dependency.bundling\": \"external\",", //
			"        \"org.gradle.jvm.version\": 8,", //
			"        \"org.gradle.libraryelements\": \"jar\",", //
			"        \"org.gradle.usage\": \"java-runtime\"", //
			"      },", //
			"      \"dependencies\": [", //
			"        {", //
			"          \"group\": \"org.junit\",", //
			"          \"module\": \"junit-bom\",", //
			"          \"version\": {", //
			">> VERSION >>", //
			"          },", //
			"          \"attributes\": {", //
			"            \"org.gradle.category\": \"platform\"", //
			"          },", //
			"          \"endorseStrictVersions\": true", //
			"        },", //
			"        {", //
			"          \"group\": \"org.junit.jupiter\",", //
			"          \"module\": \"junit-jupiter-api\",", //
			"          \"version\": {", //
			">> VERSION >>", //
			"          }", //
			"        },", //
			"        {", //
			"          \"group\": \"org.junit.jupiter\",", //
			"          \"module\": \"junit-jupiter-params\",", //
			"          \"version\": {", //
			">> VERSION >>", //
			"          }", //
			"        },", //
			"        {", //
			"          \"group\": \"org.junit.jupiter\",", //
			"          \"module\": \"junit-jupiter-engine\",", //
			"          \"version\": {", //
			">> VERSION >>", //
			"          }", //
			"        }", //
			"      ],", //
			"      \"files\": [", //
			"        {", //
			">> JAR_FILE_DETAILS >>", //
			"        }", //
			"      ]", //
			"    },", //
			"    {", //
			"      \"name\": \"javadocElements\",", //
			"      \"attributes\": {", //
			"        \"org.gradle.category\": \"documentation\",", //
			"        \"org.gradle.dependency.bundling\": \"external\",", //
			"        \"org.gradle.docstype\": \"javadoc\",", //
			"        \"org.gradle.usage\": \"java-runtime\"", //
			"      },", //
			"      \"files\": [", //
			"        {", //
			">> JAR_FILE_DETAILS >>", //
			"        }", //
			"      ]", //
			"    },", //
			"    {", //
			"      \"name\": \"sourcesElements\",", //
			"      \"attributes\": {", //
			"        \"org.gradle.category\": \"documentation\",", //
			"        \"org.gradle.dependency.bundling\": \"external\",", //
			"        \"org.gradle.docstype\": \"sources\",", //
			"        \"org.gradle.usage\": \"java-runtime\"", //
			"      },", //
			"      \"files\": [", //
			"        {", //
			">> JAR_FILE_DETAILS >>", //
			"        }", //
			"      ]", //
			"    }", //
			"  ]", //
			"}");

		System.out.println(Path.of(".").toAbsolutePath().normalize());

		assertLinesMatch(expected,
			Files.readAllLines(Path.of("../junit-jupiter/build/publications/maven/module.json")));
	}
}
