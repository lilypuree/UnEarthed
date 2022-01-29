package lilypuree.unearthed.core;

import lilypuree.unearthed.block.LichenBlock;
import lilypuree.unearthed.block.PuddleBlock;
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
    public static Block PUDDLE;
    public static Block LICHEN;
    public static Block PYROXENE;

    public static void init() {
        SoundType WATER = new SoundType(1.0F, 1.0F, SoundEvents.WET_GRASS_BREAK, SoundEvents.GENERIC_SPLASH, SoundEvents.WET_GRASS_PLACE, SoundEvents.WET_GRASS_HIT, SoundEvents.GENERIC_SPLASH);

        UEBlocks.PYROXENE = new GravelBlock(BlockBehaviour.Properties.of(Material.SAND, MaterialColor.COLOR_BLACK).strength(0.6F).sound(SoundType.GRAVEL));
        UEBlocks.PUDDLE = new PuddleBlock(BlockBehaviour.Properties.of(Material.WATER).noOcclusion().randomTicks().friction(0.98f).instabreak().sound(WATER));
        UEBlocks.LICHEN = new LichenBlock(BlockBehaviour.Properties.of(Material.PLANT).noOcclusion().randomTicks().strength(0.2f).sound(SoundType.GRASS));
        UEBlocks.LIGNITE_BRIQUETTES = new Block(BlockBehaviour.Properties.copy(Blocks.COAL_BLOCK));
    }
}
