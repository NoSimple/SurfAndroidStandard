package ru.surfstudio.android.build.tasks.generate_release_notes_diff

import org.eclipse.jgit.revwalk.RevCommit
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import ru.surfstudio.android.build.Components
import ru.surfstudio.android.build.GradleProperties
import ru.surfstudio.android.build.ReleaseNotes
import ru.surfstudio.android.build.exceptions.ComponentNotFoundException
import ru.surfstudio.android.build.model.Component
import ru.surfstudio.android.build.tasks.changed_components.GitCommandRunner
import ru.surfstudio.android.build.tasks.deploy_to_mirror.repository.StandardRepository
import ru.surfstudio.android.build.utils.EMPTY_STRING
import ru.surfstudio.android.build.utils.extractProperty
import ru.surfstudio.android.build.utils.getAllParents
import java.io.File

/**
 * Task to see the differences between two revisions of RELEASE_NOTES.md in each module of a project
 */
open class WriteToFileReleaseNotesDiff : DefaultTask() {

    companion object {
        const val releaseNotesChangesFileUrl = "buildSrc/build/tmp/releaseNotesChanges.txt"
    }

    private lateinit var componentName: String
    private lateinit var revisionToCompare: String
    private lateinit var currentRevision: String
    private val releaseNotesChangesFile = File(releaseNotesChangesFileUrl).apply {
        if (exists()) delete()
        createNewFile()
    }

    private val gitRunner: GitCommandRunner = GitCommandRunner()

    @TaskAction
    fun generate() {
        extractInputArguments()
        writeToFile("")
//        if (componentName.isNotEmpty()) {
//            val component = findComponent()
//            generateComponentDiff(component)
//        } else {
//            Components.value.forEach(::generateComponentDiff)
//        }
    }

    private fun findComponent(): Component =
            Components.value.find { it.name == componentName }
                    ?: throw ComponentNotFoundException(componentName)

    private fun generateComponentDiff(component: Component) {
        val rawDiff = extractRawDiff(component)
        val diffs = parseRawDiff(rawDiff)
        if (diffs.isNotEmpty()) writeToFile(component.name)
        writeDiff(diffs)
        if (diffs.isNotEmpty()) println()
    }

    private fun writeToFile(text: String) {
//        var i = 0
//        var currentCommit = sr.getCommit(revisionToCompare).getAllParents(100)
//        for (j in 0..5) {
//            currentCommit.parents.forEach {
//                releaseNotesChangesFile.appendText(
//                        "${i++} branchName = $branchName\n"
//                )
//            }
//           // currentCommit = currentCommit.getParent(1)
//        }1
//       // var parent = currentCommit.getParent(0)
//       // val branchName = sr.getBranchNameByCommit(currentRevision)

        var i = 1
        val sr = StandardRepository()
        val branchName = sr.getCommit("2d616c2a00525c4e3bcb412f0e10c342125c6ec7")
                .parents.forEach {

            releaseNotesChangesFile.appendText("${i++} ${it.name}\n")
        }
    }

    private fun writeDiff(diffs: List<GitDiff>) {
        var prev: GitDiff? = null
        diffs.forEach { diff ->
            writeLine(diff, prev)
            prev = diff
        }
    }

    private fun writeLine(diff: GitDiff, prev: GitDiff?) {
        val paddingSpaces = getSpaces(diff.lineNumber)
        val lineToPrint = when {
            prev == null -> return
            diff.type == GitDiff.Type.SEPARATE -> "..."
            else -> "${diff.lineNumber}$paddingSpaces${diff.line}"
        }
        writeToFile(lineToPrint)
    }

    private fun parseRawDiff(diff: String): List<GitDiff> =
            SimpleGitDiffParser().parse(diff)

    private fun extractRawDiff(component: Component): String {
        val filePath = ReleaseNotes.getReleaseNotesFilePath(component)
        return gitRunner.getFullDiff(currentRevision, revisionToCompare, filePath) ?: ""
    }

    /**
     * Simple padding method which adds spaces according to line length
     */
    private fun getSpaces(currentLine: Int): String {
        val space = " "
        val spacesCount = when {
            currentLine / 10 == 0 -> 3
            currentLine / 100 == 0 -> 2
            else -> 1
        }
        return space.repeat(spacesCount)
    }

    private fun extractInputArguments() {
        componentName = if (!project.hasProperty(GradleProperties.COMPONENT)) {
            EMPTY_STRING
        } else {
            project.findProperty(GradleProperties.COMPONENT) as String
        }

        revisionToCompare = if (!project.hasProperty(GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE)) {
            EMPTY_STRING
        } else {
            project.findProperty(GradleProperties.COMPONENTS_CHANGED_REVISION_TO_COMPARE) as String
        }

        currentRevision = if (project.hasProperty(GradleProperties.CURRENT_REVISION)) {
            project.findProperty(GradleProperties.CURRENT_REVISION) as String
        } else {
            gitRunner.getCurrentRevisionShort()
        }
    }
}