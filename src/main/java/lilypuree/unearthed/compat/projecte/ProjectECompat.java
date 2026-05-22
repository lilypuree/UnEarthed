package lilypuree.unearthed.compat.projecte;

import lilypuree.unearthed.Constants;
import lilypuree.unearthed.block.schema.BlockSchema;
import lilypuree.unearthed.block.schema.BlockSchemas;
import lilypuree.unearthed.block.schema.SchemaEntry;
import lilypuree.unearthed.core.Registration;
import moze_intel.projecte.api.imc.CustomEMCRegistration;
import moze_intel.projecte.api.imc.IMCMethods;
import moze_intel.projecte.api.imc.WorldTransmutationEntry;
import moze_intel.projecte.api.nss.NSSItem;
import moze_intel.projecte.api.nss.NormalizedSimpleStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class ProjectECompat {

    public static void init(InterModEnqueueEvent event) {
        addEMCValues();
        addTransmutation();
    }

    private static void addEMCValues() {
        for (BlockSchema schema : BlockSchemas.ROCK_TYPES) {
            for (SchemaEntry entry : schema.entries()) {
                Block block = entry.getBlock();
                Item item = block.asItem();
                ResourceLocation key = ForgeRegistries.BLOCKS.getKey(block);
                if (key != null && key.getNamespace().equals(Constants.MOD_ID) && item instanceof BlockItem) {
                    String path = key.getPath();
                    long emc = getEMCForName(path);
                    if (emc > 0) {
                        NormalizedSimpleStack stack = NSSItem.createItem(item);
                        InterModComms.sendTo("projecte", IMCMethods.REGISTER_CUSTOM_EMC, () -> new CustomEMCRegistration(stack, emc));
                    }
                }
            }
        }

        NormalizedSimpleStack lichen = NSSItem.createItem(Registration.LICHEN_ITEM.get());
        InterModComms.sendTo("projecte", IMCMethods.REGISTER_CUSTOM_EMC, () -> new CustomEMCRegistration(lichen, 1));

        NormalizedSimpleStack pyroxene = NSSItem.createItem(Registration.PYROXENE_ITEM.get());
        InterModComms.sendTo("projecte", IMCMethods.REGISTER_CUSTOM_EMC, () -> new CustomEMCRegistration(pyroxene, 1));

        NormalizedSimpleStack regolith = NSSItem.createItem(Registration.REGOLITH.get());
        InterModComms.sendTo("projecte", IMCMethods.REGISTER_CUSTOM_EMC, () -> new CustomEMCRegistration(regolith, 1));
    }

    private static void addTransmutation() {
        for (BlockSchema schema : BlockSchemas.ROCK_TYPES) {
            Block baseBlock = schema.getBaseBlock();
            if (baseBlock != null && ForgeRegistries.BLOCKS.getKey(baseBlock) != null) {
                InterModComms.sendTo("projecte", IMCMethods.REGISTER_WORLD_TRANSMUTATION, () -> new WorldTransmutationEntry(baseBlock.defaultBlockState(), Blocks.STONE.defaultBlockState(), Blocks.COBBLESTONE.defaultBlockState()));
            }
        }
    }

    private static long getEMCForName(String name) {
        if (name.contains("_stairs")) {
            return 16;
        }
        if (name.contains("_slab")) {
            return 8;
        }
        if (name.contains("_wall")) {
            return 16;
        }
        if (name.contains("_button")) {
            return 1;
        }
        if (name.contains("_pressure_plate")) {
            return 2;
        }
        if (name.contains("_pillar")) {
            return 32;
        }
        if (name.startsWith("mossy_") || name.contains("_mossy")) {
            return 9;
        }
        if (name.contains("_cut") || name.startsWith("cut_")) {
            return 4;
        }
        if (name.contains("_smooth") || name.startsWith("smooth_")) {
            return 4;
        }
        if (name.startsWith("cobbled_") || name.contains("_brick") || name.startsWith("cracked_") || name.contains("_tile")) {
            return 1;
        }
        if (name.contains("chiseled_")) {
            return 2;
        }
        if (name.contains("_mosaic")) {
            return 2;
        }

        if (name.contains("_iron_ore") || name.contains("_coal_ore") || name.contains("_copper_ore")
                || name.contains("_gold_ore") || name.contains("_lapis_ore") || name.contains("_redstone_ore")
                || name.contains("_diamond_ore") || name.contains("_emerald_ore")) {
            return 0;
        }

        return 1;
    }
}
