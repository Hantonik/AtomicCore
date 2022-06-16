package hantonik.atomiccore.api.energy;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nonnull;

public interface IBlockEntityEnergyHandler {
    boolean hasCapability(@Nonnull BlockEntity blockEntity, @Nonnull Direction dir);

    boolean canAddEnergy(@Nonnull BlockEntity blockEntity, @Nonnull Direction dir);

    boolean canRemoveEnergy(@Nonnull BlockEntity blockEntity, @Nonnull Direction dir);

    long addEnergy(long amount, @Nonnull BlockEntity blockEntity, @Nonnull Direction dir, boolean simulate);

    long removeEnergy(long amount, @Nonnull BlockEntity blockEntity, @Nonnull Direction dir);
}
