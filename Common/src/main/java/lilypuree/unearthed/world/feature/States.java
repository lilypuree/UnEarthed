package lilypuree.unearthed.world.feature;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class States {
    public static final ChunkFiller.State GOLD = new ChunkFiller.State(Blocks.GOLD_BLOCK.defaultBlockState());
    public static final ChunkFiller.State GLASS = new ChunkFiller.State(Blocks.GLASS.defaultBlockState());
    public static final ChunkFiller.State STONE = new ChunkFiller.State(Blocks.STONE.defaultBlockState());
    public static final ChunkFiller.State PHYLLITE = new ChunkFiller.State(BlockStates.PHYLLITE);
    public static final ChunkFiller.State SLATE = new ChunkFiller.State(BlockStates.SLATE);
    public static final ChunkFiller.State GABBRO = new ChunkFiller.State(BlockStates.GABBRO);
    public static final ChunkFiller.State GRANODIORITE = new ChunkFiller.State(BlockStates.GRANODIORITE);
    public static final ChunkFiller.State RHYOLITE = new ChunkFiller.State(BlockStates.RHYOLITE);
    public static final ChunkFiller.State WHITE_GRANITE = new ChunkFiller.State(BlockStates.WHITE_GRANITE);
    public static final ChunkFiller.State PILLOW_BASALT = new ChunkFiller.State(BlockStates.PILLOW_BASALT);
    public static final ChunkFiller.State BEIGE_LIMESTONE = new ChunkFiller.State(BlockStates.BEIGE_LIMESTONE);
    public static final ChunkFiller.State GREY_LIMESTONE = new ChunkFiller.State(BlockStates.GREY_LIMESTONE);
    public static final ChunkFiller.State LIMESTONE = new ChunkFiller.State(BlockStates.LIMESTONE);
    public static final ChunkFiller.State MUDSTONE = new ChunkFiller.State(BlockStates.MUDSTONE);
    public static final ChunkFiller.State SILTSTONE = new ChunkFiller.State(BlockStates.SILTSTONE);
    public static final ChunkFiller.State CONGLOMERATE = new ChunkFiller.State(BlockStates.CONGLOMERATE);
}
