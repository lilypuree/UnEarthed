package net.oriondev.hextension_core;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Hextension implements ModInitializer {

	//sedimentary minerals
	public static final String[] SEDIMENTARY_ROCKS = {"limestone"};
	//cobble
	public static final String[] COBBLE = {"limestone_cobblestone"};

	@Override
	public void onInitialize() {
	//sedimentary
		for (String sedimentary_rock : SEDIMENTARY_ROCKS){
			Identifier sedimentary_rocks = new Identifier("hextension_core", sedimentary_rock);
			Block.Settings blockSettings = FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES).strength(1, 4.5F);
			Block sediment = new Block(blockSettings);
			Item.Settings itemSettings = new Item.Settings().group(ItemGroup.BUILDING_BLOCKS);
			BlockItem sedimentary_rocks_item = new BlockItem(sediment, itemSettings);
			Registry.register(Registry.BLOCK, sedimentary_rock, sediment);
			Registry.register(Registry.ITEM,sedimentary_rocks, sedimentary_rocks_item);
		}
		//cobblestones
		for (String cobblestone : COBBLE){
			Identifier cobblestones = new Identifier("hextension_core", cobblestone);
			Block.Settings blockSettings = FabricBlockSettings.of(Material.STONE).breakByTool(FabricToolTags.PICKAXES);
			Block cobble = new Block(blockSettings);
			Item.Settings itemSettings = new Item.Settings().group(ItemGroup.BUILDING_BLOCKS);
			BlockItem cobblestone_item = new BlockItem(cobble, itemSettings);
			Registry.register(Registry.BLOCK, cobblestone, cobble);
			Registry.register(Registry.ITEM,cobblestones, cobblestone_item);
		}
	}
}
