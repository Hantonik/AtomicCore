package hantonik.atomiccore.utils.helpers;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public final class WorldHelper {
    public static void doEnergyInteraction(BlockEntity tileFrom, BlockEntity tileTo, Direction sideTo, int maxTransfer) {
        if (maxTransfer > 0) {
            Direction opp = sideTo == null ? null : sideTo.getOpposite();

            LazyOptional<IEnergyStorage> handlerFrom = tileFrom.getCapability(CapabilityEnergy.ENERGY, sideTo);
            LazyOptional<IEnergyStorage> handlerTo = tileTo.getCapability(CapabilityEnergy.ENERGY, opp);

            handlerFrom.ifPresent((from) -> handlerTo.ifPresent((to) -> {
                int drain = from.extractEnergy(maxTransfer, true);

                if (drain > 0) {
                    int filled = to.receiveEnergy(drain, false);

                    from.extractEnergy(filled, false);
                }
            }));
        }
    }

    public static void doFluidInteraction(BlockEntity tileFrom, BlockEntity tileTo, Direction sideTo, int maxTransfer) {
        if (maxTransfer > 0) {
            LazyOptional<IFluidHandler> optionalFrom = tileFrom.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, sideTo);
            LazyOptional<IFluidHandler> optionalTo = tileTo.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, sideTo.getOpposite());

            optionalFrom.ifPresent((from) -> optionalTo.ifPresent((to) -> {
                FluidStack drain = from.drain(maxTransfer, IFluidHandler.FluidAction.SIMULATE);

                if (!drain.isEmpty()) {
                    int filled = to.fill(drain.copy(), IFluidHandler.FluidAction.EXECUTE);

                    from.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                }
            }));
        }
    }
}
