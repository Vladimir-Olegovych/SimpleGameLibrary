package tools.kyro.server

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import com.esotericsoftware.kryonet.Server
import tools.kyro.common.EventSender

class GameServer<K>(): EventSender<K>() {
    private var server: Server? = null

    private val listener = object : Listener() {
        override fun connected(connection: Connection) {
            notifyOnConnected(connection)
        }
        override fun disconnected(connection: Connection) {
            notifyOnDisconnected(connection)
        }
        override fun received(connection: Connection, obj: Any) {
            notifyOnReceive(connection, obj)
        }
    }

    fun stop() {
        server?.dispose()
        server = null
    }

    fun start(port: Int, bufferSize: Int, custom: (Kryo) -> Unit){
        if (server != null) return
        val server = Server(bufferSize, bufferSize)
        val kryo = server.kryo
        custom.invoke(kryo)

        //server.bind(InetSocketAddress("0.0.0.0", port), InetSocketAddress("0.0.0.0", port))
        server.bind(port, port)
        server.addListener(listener)
        server.start()

        this.server = server
    }
}