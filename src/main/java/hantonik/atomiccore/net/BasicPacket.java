package hantonik.atomiccore.net;

import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class BasicPacket {
    public void done(Supplier<NetworkEvent.Context> context) {
        context.get().setPacketHandled(true);
    }
}
