package lilypuree.unearthed.compat;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public interface IStoneType {

    BlockState getBaseBlock();

    BlockState getCobbleBlock();

    void setCobbleBlock(BlockState cobbleBlock);

    BlockState getDirtReplace();

    void setDirtReplace(BlockState dirtReplace);

    BlockState getGrassReplace();

    void setGrassReplace(BlockState grassReplace);

    Map<Block, BlockState> getOreMap();
}
