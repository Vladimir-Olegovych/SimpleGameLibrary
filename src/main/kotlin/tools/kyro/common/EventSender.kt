package tools.kyro.common

import com.esotericsoftware.kryonet.Connection
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentLinkedQueue

open class EventSender<K>() {

    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
    private var customDispatcher: CoroutineDispatcher? = null

    private val lifecycleScope = CoroutineScope(defaultDispatcher)

    protected val listeners = ConcurrentLinkedQueue<GameNetworkListener<out K>>()

    protected fun notifyOnConnected(connection: Connection) {
        listeners.forEach {
            lifecycleScope.launch(customDispatcher?: defaultDispatcher) {
                it.onConnected(connection)
            }
        }
    }

    protected fun notifyOnError(e: Throwable) {
        listeners.forEach {
            lifecycleScope.launch(customDispatcher?: defaultDispatcher) {
                it.onError(e)
            }
        }
    }

    protected fun notifyOnDisconnected(connection: Connection) {
        listeners.forEach {
            lifecycleScope.launch(customDispatcher?: defaultDispatcher) {
                it.onDisconnected(connection)
            }
        }
    }

    protected fun notifyOnReceive(connection: Connection, obj: Any) {
        listeners.forEach {
            lifecycleScope.launch(customDispatcher?: defaultDispatcher) {
                it.notifyReceiveListener(connection, obj)
            }
        }
    }

    fun setCustomDispatcher(dispatcher: CoroutineDispatcher?){
        this.customDispatcher = dispatcher
    }

    fun subscribe(listener: GameNetworkListener<out K>) {
        listeners.add(listener)
    }

    fun unSubscribe(listener: GameNetworkListener<out K>) {
        listeners.remove(listener)
    }

    fun unSubscribeAll() {
        listeners.clear()
    }

    fun <T: K>subscribe(
        onConnected: ((GameNetworkListener<T>, Connection) -> Unit)? = null,
        onDisconnected: ((GameNetworkListener<T>, Connection) -> Unit)? = null,
        onReceive: ((GameNetworkListener<T>, Connection, T) -> Unit)? = null
    ): GameNetworkListener<T> {
        val listener = object : GameNetworkListener<T> {
            override fun onConnected(connection: Connection) {
                onConnected?.invoke(this, connection)
            }

            override fun onDisconnected(connection: Connection) {
                onDisconnected?.invoke(this, connection)
            }

            override fun onReceive(connection: Connection, value: T) {
                onReceive?.invoke(this, connection, value)
            }
        }

        try {
            return listener
        } finally {
            listeners.add(listener)
        }
    }

}