package tools.kyro.client

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryonet.Client
import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import tools.kyro.common.EventSender

class GameClient<K>(): EventSender<K>() {
    private var client: Client? = null

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

    fun sendTCP(obj: Any){
        client?.sendTCP(obj)
    }

    fun sendUDP(obj: Any){
        client?.sendUDP(obj)
    }

    fun stop() {
        client?.dispose()
        client = null
    }

    fun start(address: String, port: Int, bufferSize: Int, custom: (Kryo) -> Unit){
        if (client != null) return
        val client = Client(bufferSize, bufferSize)
        this.client = client

        val kryo = client.kryo
        custom.invoke(kryo)

        client.addListener(listener)
        client.start()
        try {
            client.connect(TIME_OUT, address, port, port)
        } catch (e: Throwable) {
            this.client?.dispose()
            this.client = null
            notifyOnError(e)
        }
    }

    companion object {
        const val TIME_OUT = 5000
    }
}