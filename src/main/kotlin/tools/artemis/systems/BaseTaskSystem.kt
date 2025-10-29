package tools.artemis.systems

import com.artemis.BaseSystem
import java.util.concurrent.ConcurrentLinkedQueue

abstract class BaseTaskSystem: BaseSystem() {

    private val tasks = ConcurrentLinkedQueue<() -> Unit>()

    fun addTask(task: () -> Unit) {
        tasks.add(task)
    }

    fun getAddTasks(): Iterable<() -> Unit> {
        return tasks.asIterable()
    }

    fun removeTask(task: () -> Unit) {
        tasks.remove(task)
    }

    fun clearTasks() {
        tasks.clear()
    }
}