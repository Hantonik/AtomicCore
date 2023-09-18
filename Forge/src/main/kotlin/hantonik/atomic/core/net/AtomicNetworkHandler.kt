package hantonik.atomic.core.net

import hantonik.atomic.core.net.packet.IAtomicPacket
import net.minecraft.network.Connection
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.network.NetworkDirection
import net.minecraftforge.network.NetworkEvent.Context
import net.minecraftforge.network.NetworkRegistry
import net.minecraftforge.network.PacketDistributor.PacketTarget
import net.minecraftforge.network.simple.SimpleChannel

open class AtomicNetworkHandler(id: ResourceLocation) {
    val channel: SimpleChannel

    private var id: Int = 0

    init {
        this.channel = NetworkRegistry.newSimpleChannel(id, { "1" }, { true }, { true })
    }

    fun <T : IAtomicPacket<T>> register(clazz: Class<T>, packet: IAtomicPacket<T>) {
        this.channel.messageBuilder(clazz, this.id++)
            .encoder(packet::encode)
            .decoder(packet::decode)
            .consumerMainThread { msg, context -> packet.onPacket(msg, context::get) }
            .add()
    }

    fun <T> sendToServer(packet: T) {
        this.channel.sendToServer(packet)
    }

    fun <T> sendTo(packet: T, connection: Connection, direction: NetworkDirection) {
        this.channel.sendTo(packet, connection, direction)
    }

    fun <T> send(packet: T, target: PacketTarget) {
        this.channel.send(target, packet)
    }

    fun <T> reply(packet: T, context: Context) {
        this.channel.reply(packet, context)
    }
}