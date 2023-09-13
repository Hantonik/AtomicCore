package hantonik.atomic.core.block.entity

import net.minecraft.network.FriendlyByteBuf

interface IAtomicPacketHandler {
    fun getMenuPacket(buffer: FriendlyByteBuf) = buffer

    fun getStatePacket(buffer: FriendlyByteBuf) = buffer

    fun handleMenuPacket(buffer: FriendlyByteBuf) {}

    fun handleStatePacket(buffer: FriendlyByteBuf) {}
}