package hantonik.atomic.core.storage;

import net.minecraft.network.FriendlyByteBuf;

public interface INetworkHandler {
    void readFromBuffer(FriendlyByteBuf buffer);

    void writeToBuffer(FriendlyByteBuf buffer);
}
