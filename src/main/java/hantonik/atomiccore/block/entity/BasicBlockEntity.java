package hantonik.atomiccore.block.entity;

import hantonik.atomiccore.utils.helpers.BlockEntityHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BasicBlockEntity extends BlockEntity {
    public BasicBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection manager, ClientboundBlockEntityDataPacket packet) {
        this.load(packet.getTag());
    }

    public CompoundTag save(CompoundTag tag) {
        return tag;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(save(tag));
    }

    @Override
    public final CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    public void markDirtyAndDispatch() {
        super.setChanged();

        BlockEntityHelper.dispatchToNearbyPlayers(this);
    }
}
