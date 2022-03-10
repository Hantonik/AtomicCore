package hantonik.atomiccore.tiles.api;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface ISharingEnergyProvider {
    int getEnergyToSplitShare();

    boolean doesShareEnergy();

    boolean canShareTo(BlockEntity tile);

    Direction[] getEnergyShareSides();
}
