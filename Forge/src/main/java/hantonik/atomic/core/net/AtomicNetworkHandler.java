package hantonik.atomic.core.net;

import hantonik.atomic.core.net.packet.IAtomicPacket;
import lombok.Getter;
import net.minecraft.network.Connection;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class AtomicNetworkHandler {
    @Getter
    private final SimpleChannel channel;
    private int id = 0;

    public AtomicNetworkHandler(ResourceLocation id) {
        this.channel = NetworkRegistry.newSimpleChannel(id, () -> "1", client -> true, server -> true);
    }

    public <T extends IAtomicPacket<T>> void register(Class<T> clazz, IAtomicPacket<T> packet) {
        this.channel.messageBuilder(clazz, this.id++)
                .encoder(packet::encode)
                .decoder(packet::decode)
                .consumerMainThread(packet::onPacket)
                .add();
    }

    public <T> void sendToServer(T packet) {
        this.channel.sendToServer(packet);
    }

    public <T> void sendTo(T message, Connection manager, NetworkDirection direction) {
        this.channel.sendTo(message, manager, direction);
    }

    public <T> void send(PacketDistributor.PacketTarget target, T packet) {
        this.channel.send(target, packet);
    }

    public <T> void reply(T packet, NetworkEvent.Context context) {
        this.channel.reply(packet, context);
    }
}
