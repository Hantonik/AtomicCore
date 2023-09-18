package hantonik.atomic.core.net.packet

import net.minecraft.network.FriendlyByteBuf
import net.minecraftforge.network.NetworkEvent.Context

interface IAtomicPacket<T : IAtomicPacket<T>> {
    fun decode(buffer: FriendlyByteBuf): T

    fun encode(packet: T, buffer: FriendlyByteBuf)

    fun onPacket(packet: T, context: () -> Context)
}