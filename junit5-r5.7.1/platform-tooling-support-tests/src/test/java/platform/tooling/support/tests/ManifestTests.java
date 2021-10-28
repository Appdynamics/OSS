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

import static aQute.bnd.osgi.Constants.VERSION_ATTRIBUTE;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static platform.tooling.support.Helper.createJarPath;

import java.io.File;
import java.lang.module.ModuleFinder;
import java.util.jar.Attributes;

import aQute.bnd.osgi.Domain;
import aQute.bnd.osgi.Jar;
import aQute.bnd.version.MavenVersion;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.osgi.framework.Version;
import org.osgi.framework.VersionRange;
import platform.tooling.support.Helper;

/**
 * @since 1.5
 */
class ManifestTests {

	@ParameterizedTest
	@MethodSource("platform.tooling.support.Helper#loadModuleDirectoryNames")
	void manifestEntriesAdhereToConventions(String module) throws Exception {
		var version = Helper.version(module);
		var modulePath = createJarPath(module);
		var uri = ModuleFinder.of(modulePath).findAll().iterator().next().location().orElseThrow();
		var jarFile = new File(uri);
		try (var jar = new Jar(jarFile)) {
			var manifest = jar.getManifest();
			var attributes = manifest.getMainAttributes();
			assertValue(attributes, "Built-By", "JUnit Team");
			assertValue(attributes, "Specification-Title", module);
			assertValue(attributes, "Specification-Version", specificationVersion(version));
			assertValue(attributes, "Specification-Vendor", "junit.org");
			assertValue(attributes, "Implementation-Title", module);
			assertValue(attributes, "Implementation-Version", version);
			assertValue(attributes, "Implementation-Vendor", "junit.org");
			assertValue(attributes, "Automatic-Module-Name", null);
			assertValue(attributes, "Bundle-ManifestVersion", "2");
			assertValue(attributes, "Bundle-SymbolicName", module);
			assertValue(attributes, "Bundle-Version",
				MavenVersion.parseMavenString(version).getOSGiVersion().toString());
			switch (module) {
				case "junit-platform-commons":
					assertValue(attributes, "Multi-Release", "true");
					break;
				case "junit-platform-console":
					assertValue(attributes, "Main-Class", "org.junit.platform.console.ConsoleLauncher");
					break;
			}
			var domain = Domain.domain(manifest);
			domain.getExportPackage().forEach((pkg, attrs) -> {
				final String stringVersion = attrs.get(VERSION_ATTRIBUTE);
				assertNotNull(stringVersion);
				assertDoesNotThrow(() -> new Version(stringVersion));
			});
			domain.getImportPackage().forEach((pkg, attrs) -> {
				final String stringVersionRange = attrs.get(VERSION_ATTRIBUTE);
				if (stringVersionRange == null) {
					return;
				}
				assertDoesNotThrow(() -> new VersionRange(stringVersionRange));
			});
		}
	}

	private static String specificationVersion(String version) {
		var dash = version.indexOf('-');
		if (dash < 0) {
			return version;
		}
		return version.substring(0, dash);
	}

	private static void assertValue(Attributes attributes, String name, String expected) {
		var actual = attributes.getValue(name);
		assertEquals(expected, actual,
			String.format("Manifest attribute %s expected to be %s, but is: %s", name, expected, actual));
	}
}
