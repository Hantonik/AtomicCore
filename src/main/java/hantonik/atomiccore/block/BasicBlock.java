package hantonik.atomiccore.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;

import java.util.function.Function;

public class BasicBlock extends Block {
    private final ToolAction toolAction;

    public BasicBlock(Material material, Function<Properties, Properties> properties, ToolAction toolAction) {
        super(properties.apply(Properties.of(material)));

        this.toolAction = toolAction;
    }

    public BasicBlock(Material material, SoundType soundType, ToolAction toolAction, float hardness, float resistance) {
        super(Properties.of(material)
                .sound(soundType)
                .strength(hardness, resistance)
                .requiresCorrectToolForDrops());

        this.toolAction = toolAction;
    }

    public Tag.Named<Block> getRequiredTool() {
        if (this.toolAction.equals(ToolActions.PICKAXE_DIG))
            return BlockTags.MINEABLE_WITH_PICKAXE;

        else if (this.toolAction.equals(ToolActions.AXE_DIG))
            return BlockTags.MINEABLE_WITH_AXE;

        else if (this.toolAction.equals(ToolActions.SHOVEL_DIG))
            return BlockTags.MINEABLE_WITH_SHOVEL;

        else if (this.toolAction.equals(ToolActions.HOE_DIG))
            return BlockTags.MINEABLE_WITH_HOE;

        else
            return null;
    }

    public static boolean isEnergy(Direction facing, WorldGenLevel world, BlockPos facingPos) {
        return hasCapabilityDirection(facing, world, facingPos, CapabilityEnergy.ENERGY);
    }

    private static boolean hasCapabilityDirection(Direction facing, WorldGenLevel world, BlockPos facingPos, Capability<?> capability) {
        if (facing == null)
            return false;

        BlockEntity neighbor = world.getBlockEntity(facingPos);

        return neighbor != null && neighbor.getCapability(capability, facing.getOpposite()).isPresent();
    }
}
