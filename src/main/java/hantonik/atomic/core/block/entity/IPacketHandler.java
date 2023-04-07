package hantonik.atomic.core.block.entity;

import net.minecraft.network.FriendlyByteBuf;

public interface IPacketHandler {
    default FriendlyByteBuf getGuiPacket(FriendlyByteBuf buffer) {
        return buffer;
    }

    default void handleGuiPacket(FriendlyByteBuf buffer) {}

    default FriendlyByteBuf getStatePacket(FriendlyByteBuf buffer) {
        return buffer;
    }

    default void handleStatePacket(FriendlyByteBuf buffer) {}
}
