package com.avito.android.build_verdict.internal.task.lifecycle

import com.avito.android.build_verdict.internal.DefaultTestListener
import com.avito.android.build_verdict.internal.LogsTextBuilder
import org.gradle.api.tasks.testing.Test
import org.gradle.api.tasks.testing.TestDescriptor
import org.gradle.api.tasks.testing.TestResult
import org.gradle.util.Path

class TestTaskLifecycleListener(
    override val logs: MutableMap<Path, LogsTextBuilder>
) : TaskLifecycleListener<Test>() {

    override fun beforeExecute(task: Test) {
        task.addTestListener(
            object : DefaultTestListener() {
                override fun afterTest(testDescriptor: TestDescriptor, result: TestResult) {
                    if (result.resultType == TestResult.ResultType.FAILURE) {
                        logs.getOrPut(Path.path(task.path), { LogsTextBuilder("FAILED tests:") })
                            .addLine("\t${testDescriptor.className}.${testDescriptor.displayName}")
                    }
                }
            }
        )
    }

    override fun afterSucceedExecute(task: Test) {
        // empty
    }

    override fun afterFailedExecute(task: Test) {
        // empty
    }
}
