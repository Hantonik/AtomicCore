package hantonik.atomiccore.utils.helpers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnergyHelper {
    @Nullable
    public static <T> T get(@Nonnull LazyOptional<T> lazyOptional) {
        if (lazyOptional.isPresent())
            return lazyOptional.orElseThrow(IllegalStateException::new);

        return null;
    }

    public static boolean hasCapability(@Nonnull BlockEntity tile, @Nonnull Direction side) {
        return !tile.isRemoved() && tile.getCapability(CapabilityEnergy.ENERGY, side).isPresent();
    }

    public static boolean canAddEnergy(@Nonnull BlockEntity tile, @Nonnull Direction side) {
        if (!tile.isRemoved()) {
            var storage = get(tile.getCapability(CapabilityEnergy.ENERGY, side));

            if (storage != null)
                return storage.canReceive();
        }

        return false;
    }

    public static boolean canRemoveEnergy(@Nonnull BlockEntity tile, @Nonnull Direction side) {
        if (!tile.isRemoved()) {
            var storage = get(tile.getCapability(CapabilityEnergy.ENERGY, side));

            if (storage != null)
                return storage.canExtract();
        }

        return false;
    }

    public static long addEnergy(long amount, @Nonnull BlockEntity tile, @Nonnull Direction side, boolean simulate) {
        var storage = get(tile.getCapability(CapabilityEnergy.ENERGY, side));

        return storage == null ? 0 : storage.receiveEnergy((int) Math.min(amount, Integer.MAX_VALUE), simulate);
    }

    public static long removeEnergy(long amount, @Nonnull BlockEntity tile, @Nonnull Direction side) {
        var storage = get(tile.getCapability(CapabilityEnergy.ENERGY, side));

        return storage == null ? 0 : storage.extractEnergy((int) Math.min(amount, Integer.MAX_VALUE), false);
    }

    public static boolean hasCapability(@Nonnull ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
    }

    public static boolean canAddEnergy(@Nonnull ItemStack stack) {
        var storage = get(stack.getCapability(CapabilityEnergy.ENERGY));

        return storage != null && storage.canReceive();
    }

    public static boolean canRemoveEnergy(@Nonnull ItemStack stack) {
        var storage = get(stack.getCapability(CapabilityEnergy.ENERGY));

        return storage != null && storage.canExtract();
    }

    public static long addEnergy(long amount, @Nonnull ItemStack stack, boolean simulate) {
        var storage = get(stack.getCapability(CapabilityEnergy.ENERGY));

        return storage == null ? 0 : storage.receiveEnergy((int) Math.min(amount, Integer.MAX_VALUE), simulate);
    }

    public static long removeEnergy(long amount, @Nonnull ItemStack stack) {
        var storage = get(stack.getCapability(CapabilityEnergy.ENERGY));

        return storage == null ? 0 : storage.extractEnergy((int) Math.min(amount, Integer.MAX_VALUE), false);
    }
}
