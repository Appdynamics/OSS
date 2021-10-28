import java.util.jar.JarEntry
import java.util.jar.JarFile
import java.util.jar.JarOutputStream
import org.gradle.api.internal.file.archive.ZipCopyAction

// This registers a `doLast` action to rewrite the timestamps of the project's output JAR
afterEvaluate {
	val jarTask = (tasks.findByName("shadowJar") ?: tasks["jar"]) as Jar

	jarTask.doLast {

		val newFile = createTempFile("rewrite-timestamp")
		val originalOutput = jarTask.archiveFile.get().getAsFile()

		newFile.outputStream().use { os ->

			val newJarStream = JarOutputStream(os)
			val oldJar = JarFile(originalOutput)

			fun sortAlwaysFirst(name: String): Comparator<JarEntry> =
				Comparator { a, b ->
					when {
						a.name == name -> -1
						b.name == name -> 1
						else -> 0
					}
				}

			oldJar.entries()
					.toList()
					.distinctBy { it.name }
					.sortedWith(sortAlwaysFirst("META-INF/")
							.then(sortAlwaysFirst("META-INF/MANIFEST.MF"))
							.thenBy { it.name })
					.forEach { entry ->
						val jarEntry = JarEntry(entry.name)

						// Use the same constant as the fixed timestamps in normal copy actions
						jarEntry.time = ZipCopyAction.CONSTANT_TIME_FOR_ZIP_ENTRIES

						newJarStream.putNextEntry(jarEntry)

						oldJar.getInputStream(entry).copyTo(newJarStream)
					}

			newJarStream.finish()
		}

		newFile.renameTo(originalOutput)
	}
}
