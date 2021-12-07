package hantonik.atomiccore.block;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

import java.util.function.Function;

public abstract class BasicTileBlock extends BaseEntityBlock {
    private final ToolAction toolAction;

    public BasicTileBlock(Material material, Function<Properties, Properties> properties, ToolAction toolAction) {
        super(properties.apply(Properties.of(material)));

        this.toolAction = toolAction;
    }

    public BasicTileBlock(Material material, Function<Properties, Properties> properties) {
        super(properties.apply(Properties.of(material)));

        this.toolAction = null;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
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

    @Override
    public boolean isRandomlyTicking(BlockState p_49921_) {
        return true;
    }
}
