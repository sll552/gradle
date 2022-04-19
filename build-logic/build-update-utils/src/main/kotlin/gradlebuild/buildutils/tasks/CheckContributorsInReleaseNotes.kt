/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gradlebuild.buildutils.tasks

import org.gradle.api.tasks.TaskAction
import java.util.TreeSet

abstract class CheckContributorsInReleaseNotes : AbstractCheckOrUpdateContributorsInReleaseNote() {
    @TaskAction
    fun check() {
        val contributorsInReleaseNotes = TreeSet(getContributorsInReleaseNotes().map { it.login })
        val contributorsFromPullRequests = TreeSet(getContributorsFromPullRequests().map { it.login })
        val contributorsInPullRequestsButNotInReleaseNotes = contributorsFromPullRequests.minus(contributorsInReleaseNotes)

        if (contributorsInPullRequestsButNotInReleaseNotes.isNotEmpty()) {
            throw IllegalStateException(
                """The contributors in the release notes $releaseNotes don't match the contributors in the PRs.
                Release note:   $contributorsInReleaseNotes
                Pull requests:  $contributorsFromPullRequests
                Missed in notes:$contributorsInPullRequestsButNotInReleaseNotes

                You can run `./gradlew updateContributorsInReleaseNote` to update the release note with correct contributors automatically.
                """.trimIndent()
            )
        }
    }
}
