package hantonik.atomiccore.integration.energy;

import hantonik.atomiccore.api.energy.IItemEnergyHandler;
import hantonik.atomiccore.utils.helpers.EnergyHelper;
import hantonik.atomiccore.api.energy.IBlockEntityEnergyHandler;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nonnull;

public class ForgeEnergyHandler implements IBlockEntityEnergyHandler, IItemEnergyHandler {
    public static final ForgeEnergyHandler INSTANCE = new ForgeEnergyHandler();

    private ForgeEnergyHandler() {}

    @Override
    public boolean hasCapability(@Nonnull BlockEntity tile, @Nonnull Direction side) {
        return !tile.isRemoved() && tile.getCapability(CapabilityEnergy.ENERGY, side).isPresent();
    }

    @Override
    public boolean canAddEnergy(@Nonnull BlockEntity tile, @Nonnull Direction side) {
        if (!tile.isRemoved()) {
            IEnergyStorage storage = EnergyHelper.get(tile.getCapability(CapabilityEnergy.ENERGY, side));

            if (storage != null)
                return storage.canReceive();
        }

        return false;
    }

    @Override
    public boolean canRemoveEnergy(@Nonnull BlockEntity tile, @Nonnull Direction side) {
        if (!tile.isRemoved()) {
            IEnergyStorage storage = EnergyHelper.get(tile.getCapability(CapabilityEnergy.ENERGY, side));

            if (storage != null)
                return storage.canExtract();
        }

        return false;
    }

    @Override
    public long addEnergy(long amount, @Nonnull BlockEntity tile, @Nonnull Direction side, boolean simulate) {
        IEnergyStorage storage = EnergyHelper.get(tile.getCapability(CapabilityEnergy.ENERGY, side));
        return storage == null ? 0 : storage.receiveEnergy((int) Math.min(amount, Integer.MAX_VALUE), simulate);
    }

    @Override
    public long removeEnergy(long amount, @Nonnull BlockEntity tile, @Nonnull Direction side) {
        IEnergyStorage storage = EnergyHelper.get(tile.getCapability(CapabilityEnergy.ENERGY, side));
        return storage == null ? 0 : storage.extractEnergy((int) Math.min(amount, Integer.MAX_VALUE), false);
    }

    @Override
    public boolean hasCapability(@Nonnull ItemStack stack) {
        return stack.getCapability(CapabilityEnergy.ENERGY).isPresent();
    }

    @Override
    public boolean canAddEnergy(@Nonnull ItemStack stack) {
        IEnergyStorage storage = EnergyHelper.get(stack.getCapability(CapabilityEnergy.ENERGY));
        return storage != null && storage.canReceive();
    }

    @Override
    public boolean canRemoveEnergy(@Nonnull ItemStack stack) {
        IEnergyStorage storage = EnergyHelper.get(stack.getCapability(CapabilityEnergy.ENERGY));
        return storage != null && storage.canExtract();
    }

    @Override
    public long addEnergy(long amount, @Nonnull ItemStack stack, boolean simulate) {
        IEnergyStorage storage = EnergyHelper.get(stack.getCapability(CapabilityEnergy.ENERGY));
        return storage == null ? 0 : storage.receiveEnergy((int) Math.min(amount, Integer.MAX_VALUE), simulate);
    }

    @Override
    public long removeEnergy(long amount, @Nonnull ItemStack stack) {
        IEnergyStorage storage = EnergyHelper.get(stack.getCapability(CapabilityEnergy.ENERGY));
        return storage == null ? 0 : storage.extractEnergy((int) Math.min(amount, Integer.MAX_VALUE), false);
    }
}
