package hantonik.atomiccore.utils.helpers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class WorldHelper {
    public static void doEnergyInteraction(BlockEntity entityFrom, BlockEntity entityTo, Direction sideTo, int maxTransfer) {
        if (maxTransfer > 0) {
            var opp = sideTo == null ? null : sideTo.getOpposite();

            var handlerFrom = entityFrom.getCapability(CapabilityEnergy.ENERGY, sideTo);
            var handlerTo = entityTo.getCapability(CapabilityEnergy.ENERGY, opp);

            handlerFrom.ifPresent((from) -> handlerTo.ifPresent((to) -> {
                var drain = from.extractEnergy(maxTransfer, true);

                if (drain > 0) {
                    var filled = to.receiveEnergy(drain, false);

                    from.extractEnergy(filled, false);
                }
            }));
        }
    }

    public static void doFluidInteraction(BlockEntity entityFrom, BlockEntity entityTo, Direction sideTo, int maxTransfer) {
        if (maxTransfer > 0) {
            var optionalFrom = entityFrom.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, sideTo);
            var optionalTo = entityTo.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, sideTo.getOpposite());

            optionalFrom.ifPresent((from) -> optionalTo.ifPresent((to) -> {
                var drain = from.drain(maxTransfer, IFluidHandler.FluidAction.SIMULATE);

                if (!drain.isEmpty()) {
                    var filled = to.fill(drain.copy(), IFluidHandler.FluidAction.EXECUTE);

                    from.drain(filled, IFluidHandler.FluidAction.EXECUTE);
                }
            }));
        }
    }
}
