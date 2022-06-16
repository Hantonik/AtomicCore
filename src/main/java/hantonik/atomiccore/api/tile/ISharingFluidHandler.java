package hantonik.atomiccore.api.tile;

import net.minecraft.core.Direction;

public interface ISharingFluidHandler {
    int getMaxFluidAmountToSplitShare();

    boolean doesShareFluid();

    Direction[] getFluidShareSides();
}
