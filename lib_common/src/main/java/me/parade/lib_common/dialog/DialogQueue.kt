package me.parade.lib_common.dialog;

import android.app.Dialog
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import java.util.PriorityQueue

/**
 * 弹窗队列,避免同时弹出多个dialog
 */
class DialogQueue(private val lifecycleOwner: LifecycleOwner) {

    private var activeDialog: Dialog? = null

    private val pq = PriorityQueue<Task>(11, Comparator { o1, o2 ->
        return@Comparator o1.priority - o2.priority
    })

    init {
        lifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                tryPoll()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                activeDialog?.dismiss()
                activeDialog = null
            }
        })
    }

    private fun tryPoll() {
        if (activeDialog == null && lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
            val task = pq.poll() ?: return
            val nextFunc = fun() {
                activeDialog = null
                tryPoll()
            }
            activeDialog = task.dialogBuilder(nextFunc)
            activeDialog?.show()
            task.showCallBack?.invoke(nextFunc)
        }
    }

    fun offer(tag: String, priority: Int, dialogBuilder: (next: () -> Unit) -> Dialog,showCallBack:((next: () -> Unit)->Unit)? = null) {
        val task = Task(tag, priority, dialogBuilder,showCallBack)
        if (pq.contains(task)) {
            return
        }
        pq.offer(task)
        tryPoll()
    }

    private class Task(
        val tag: String,
        val priority: Int,
        val dialogBuilder: (next: () -> Unit) -> Dialog,
        var showCallBack:((next: () -> Unit)->Unit)? = null
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Task
            if (tag != other.tag) return false
            return true
        }

        override fun hashCode(): Int {
            return tag.hashCode()
        }
    }
}