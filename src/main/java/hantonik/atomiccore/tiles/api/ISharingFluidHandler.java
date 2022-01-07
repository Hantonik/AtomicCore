package hantonik.atomiccore.tiles.api;

import net.minecraft.core.Direction;

public interface ISharingFluidHandler {
    int getMaxFluidAmountToSplitShare();

    boolean doesShareFluid();

    Direction[] getFluidShareSides();
}
