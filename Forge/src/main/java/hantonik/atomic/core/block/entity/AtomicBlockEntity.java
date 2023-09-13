package hantonik.atomic.core.block.entity;

import hantonik.atomic.core.utils.helpers.BlockEntityHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class AtomicBlockEntity extends BlockEntity implements IAtomicBlockEntityCallback, IAtomicPacketHandler {
    public AtomicBlockEntity(BlockEntityType<? extends AtomicBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection manager, ClientboundBlockEntityDataPacket packet) {
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

    public void markDirty() {
        super.setChanged();
    }

    public void markDirtyAndDispatch() {
        this.markDirty();

        BlockEntityHelper.dispatchToNearbyPlayers(this);
    }

    public boolean playerWithinDistance(Player player, double distanceSq) {
        return !this.isRemoved() && this.worldPosition.distToCenterSqr(player.position()) <= distanceSq;
    }

    @Override
    public BlockState state() {
        return this.getBlockState();
    }

    @Override
    public BlockPos pos() {
        return this.worldPosition;
    }

    @Override
    public Level level() {
        return this.level;
    }
}
