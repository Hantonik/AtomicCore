package hantonik.atomic.core.net.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface IAtomicPacket<T extends IAtomicPacket<T>> extends CustomPacketPayload {
    T decode(FriendlyByteBuf buffer);

    void encode(T packet, FriendlyByteBuf buffer);

    void onPacket(T packet, IPayloadContext context);
}
