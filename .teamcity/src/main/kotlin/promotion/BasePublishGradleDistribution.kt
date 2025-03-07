/*
 * Copyright 2019 the original author or authors.
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

package promotion

import jetbrains.buildServer.configs.kotlin.v2019_2.RelativeId
import vcsroots.gradlePromotionMaster

abstract class BasePublishGradleDistribution(
    // The branch to be promoted
    val promotedBranch: String,
    val triggerName: String,
    val gitUserName: String = "bot-teamcity",
    val gitUserEmail: String = "bot-teamcity@gradle.com",
    val extraParameters: String = "",
    vcsRootId: String = gradlePromotionMaster,
    cleanCheckout: Boolean = true
) : BasePromotionBuildType(vcsRootId, cleanCheckout) {

    init {
        artifactRules = """
        **/build/git-checkout/subprojects/base-services/build/generated-resources/build-receipt/org/gradle/build-receipt.properties
        **/build/distributions/*.zip => promote-build-distributions
        **/build/website-checkout/data/releases.xml
        **/build/git-checkout/build/reports/integTest/** => distribution-tests
        **/smoke-tests/build/reports/tests/** => post-smoke-tests
        """.trimIndent()

        dependencies {
            snapshot(RelativeId("Check_Stage_${this@BasePublishGradleDistribution.triggerName}_Trigger")) {
            }
        }
    }
}
