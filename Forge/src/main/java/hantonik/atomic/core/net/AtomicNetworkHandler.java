package hantonik.atomic.core.net;

import hantonik.atomic.core.net.packet.IAtomicPacket;
import net.minecraft.network.Connection;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

public class AtomicNetworkHandler {
    private final SimpleChannel channel;
    private int id = 0;

    public AtomicNetworkHandler(ResourceLocation id) {
        this.channel = ChannelBuilder.named(id)
                .networkProtocolVersion(1)
                .clientAcceptedVersions((status, version) -> true)
                .serverAcceptedVersions((status, version) -> true)
                .simpleChannel();
    }

    public <T extends IAtomicPacket<T>> void register(Class<T> clazz, IAtomicPacket<T> packet) {
        this.channel.messageBuilder(clazz, this.id++)
                .encoder(packet::encode)
                .decoder(packet::decode)
                .consumerMainThread(packet::onPacket)
                .add();
    }

    public <T> void sendToServer(T packet) {
        this.channel.send(packet, PacketDistributor.SERVER.noArg());
    }

    public <T> void sendTo(T packet, Connection connection) {
        this.channel.send(packet, connection);
    }

    public <T> void send(T packet, PacketDistributor.PacketTarget target) {
        this.channel.send(packet, target);
    }

    public <T> void reply(T packet, CustomPayloadEvent.Context context) {
        this.channel.reply(packet, context);
    }
}
