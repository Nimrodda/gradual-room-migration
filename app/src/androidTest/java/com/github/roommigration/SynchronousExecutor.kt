package com.github.roommigration

import java.util.concurrent.Executor

class SynchronousExecutor : Executor {
    override fun execute(command: Runnable) {
        command.run()
    }
}