package hantonik.atomiccore.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

import java.util.function.Function;

public class BasicBlock extends Block {
    public BasicBlock(Material material, Function<Properties, Properties> properties) {
        super(properties.apply(Properties.of(material)));
    }

    public BasicBlock(Material material, SoundType soundType, float hardness, float resistance) {
        super(Properties.of(material)
                .sound(soundType)
                .strength(hardness, resistance));
    }

    public BasicBlock(Material material, SoundType sound, float hardness, float resistance, boolean tool) {
        super(tool ? Properties.of(material)
                     .sound(sound)
                     .strength(hardness, resistance)
                     .requiresCorrectToolForDrops()
                   : Properties.of(material)
                     .sound(sound)
                     .strength(hardness, resistance));
    }

    public static boolean isEnergy(Direction facing, WorldGenLevel world, BlockPos facingPos) {
        return hasCapabilityDirection(facing, world, facingPos, CapabilityEnergy.ENERGY);
    }

    private static boolean hasCapabilityDirection(Direction facing, WorldGenLevel world, BlockPos facingPos, Capability<?> capability) {
        if (facing == null)
            return false;

        var neighbor = world.getBlockEntity(facingPos);

        return neighbor != null && neighbor.getCapability(capability, facing.getOpposite()).isPresent();
    }
}
