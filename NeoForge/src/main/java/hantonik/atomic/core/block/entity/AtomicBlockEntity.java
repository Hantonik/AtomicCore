package hantonik.atomic.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class AtomicBlockEntity extends BlockEntity implements IAtomicBlockEntityCallback, IAtomicBlockEntityPacketHandler {
    public AtomicBlockEntity(BlockEntityType<? extends AtomicBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @NotNull
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection manager, ClientboundBlockEntityDataPacket packet) {
        if (packet != null)
            if (packet.getTag() != null)
                this.load(packet.getTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    public void markChunkUnsaved() {
        if (this.level != null)
            if (this.level.hasChunkAt(this.worldPosition))
                this.level.getChunkAt(this.worldPosition).setUnsaved(true);
    }

    public void markDirtyAndDispatch() {
        this.setChanged();

        if (this.level != null)
            for (var player : this.level.players())
                if (player instanceof ServerPlayer serverPlayer)
                    if (Math.hypot(serverPlayer.getX() - serverPlayer.getZ(), this.worldPosition.getX() + 0.5D - this.worldPosition.getZ() + 0.5D) < 64.0D)
                        serverPlayer.connection.send(this.getUpdatePacket());
    }

    public boolean playerWithinDistance(Player player, double distanceSq) {
        return !this.isRemoved() && this.worldPosition.distToCenterSqr(player.position()) <= distanceSq;
    }
}
