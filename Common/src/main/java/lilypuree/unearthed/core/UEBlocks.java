package lilypuree.unearthed.core;

import lilypuree.unearthed.block.LichenBlock;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GravelBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class UEBlocks {
    public static Block LIGNITE_BRIQUETTES;
    public static Block LICHEN;
    public static Block PYROXENE;

    public static void init() {
        UEBlocks.PYROXENE = new GravelBlock(BlockBehaviour.Properties.of(Material.SAND, MaterialColor.COLOR_BLACK).strength(0.6F).sound(SoundType.GRAVEL));
        UEBlocks.LICHEN = new LichenBlock(BlockBehaviour.Properties.of(Material.PLANT).noOcclusion().randomTicks().strength(0.2f).sound(SoundType.GRASS));
        UEBlocks.LIGNITE_BRIQUETTES = new Block(BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK));
    }
}
