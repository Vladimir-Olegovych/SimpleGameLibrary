package tools.kyro.common

import com.esotericsoftware.kryonet.Connection

interface GameNetworkListener<K> {
    fun notifyReceiveListener(connection: Connection, obj: Any) {
        try { onReceive(connection, obj as K) } catch (_: Throwable) { }
    }
    fun onConnected(connection: Connection){}
    fun onDisconnected(connection: Connection){}
    fun onReceive(connection: Connection, value: K){}
}