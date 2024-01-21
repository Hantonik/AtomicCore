package hantonik.atomic.core.block.entity;

import net.minecraft.network.FriendlyByteBuf;

public interface IAtomicBlockEntityPacketHandler {
    default FriendlyByteBuf getMenuPacket(FriendlyByteBuf buffer) {
        return buffer;
    }

    default void handleMenuPacket(FriendlyByteBuf buffer) {}

    default FriendlyByteBuf getStatePacket(FriendlyByteBuf buffer) {
        return buffer;
    }

    default void handleStatePacket(FriendlyByteBuf buffer) {}
}
