package hantonik.atomic.core.net.packet;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.event.network.CustomPayloadEvent;

public interface IAtomicPacket<T extends IAtomicPacket<T>> {
    T decode(FriendlyByteBuf buffer);

    void encode(T packet, FriendlyByteBuf buffer);

    void onPacket(T packet, CustomPayloadEvent.Context context);
}
