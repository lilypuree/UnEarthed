package lilypuree.unearthed.compat.projecte;

import lilypuree.unearthed.Constants;
import lilypuree.unearthed.block.schema.BlockForm;
import lilypuree.unearthed.block.schema.BlockSchema;
import lilypuree.unearthed.block.schema.BlockSchemas;
import lilypuree.unearthed.block.schema.BlockVariant;
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

import java.util.HashMap;
import java.util.Map;

public class ProjectECompat {

    public static void init(InterModEnqueueEvent event) {
        addEMCValues();
        addTransmutation();
    }

    private static void addEMCValues() {
        Map<String, Long> emcMap = new HashMap<>();

        for (BlockSchema schema : BlockSchemas.ROCK_TYPES) {
            for (SchemaEntry entry : schema.entries()) {
                Block block = entry.getBlock();
                ResourceLocation key = ForgeRegistries.BLOCKS.getKey(block);
                if (key != null && key.getNamespace().equals(Constants.MOD_ID)) {
                    String path = key.getPath();
                    emcMap.put(path, calculateEMC(schema, entry, emcMap));
                }
            }
        }

        for (BlockSchema schema : BlockSchemas.ROCK_TYPES) {
            for (SchemaEntry entry : schema.entries()) {
                Block block = entry.getBlock();
                Item item = block.asItem();
                ResourceLocation key = ForgeRegistries.BLOCKS.getKey(block);
                if (key != null && key.getNamespace().equals(Constants.MOD_ID) && item instanceof BlockItem) {
                    String path = key.getPath();
                    long emc = emcMap.getOrDefault(path, 0L);
                    if (emc > 0) {
                        NormalizedSimpleStack stack = NSSItem.createItem(item);
                        InterModComms.sendTo("projecte", IMCMethods.REGISTER_CUSTOM_EMC, () -> new CustomEMCRegistration(stack, emc));
                    }
                }
            }
        }

        NormalizedSimpleStack lichen = NSSItem.createItem(Registration.LICHEN_ITEM.get());
        InterModComms.sendTo("projecte", IMCMethods.REGISTER_CUSTOM_EMC, () -> new CustomEMCRegistration(lichen, 1));

        NormalizedSimpleStack regolith = NSSItem.createItem(Registration.REGOLITH.get());
        InterModComms.sendTo("projecte", IMCMethods.REGISTER_CUSTOM_EMC, () -> new CustomEMCRegistration(regolith, 1));
    }

    private static long calculateEMC(BlockSchema schema, SchemaEntry entry, Map<String, Long> emcMap) {
        BlockForm form = entry.getForm();
        BlockVariant variant = entry.getVariant();
        SchemaEntry baseEntry = schema.getBaseEntry();
        String schemaName = baseEntry != null ? baseEntry.getId() : "";
        String path = ForgeRegistries.BLOCKS.getKey(entry.getBlock()).getPath();

        if (form.getName().contains("ore")) {
            return 0;
        }

        if (!form.isBaseForm()
                && !"stairs".equals(form.getName())
                && !"slab".equals(form.getName())
                && !"wall".equals(form.getName())
                && !"button".equals(form.getName())
                && !"pressure_plate".equals(form.getName())) {
            return 0;
        }

        if (form.isBaseForm()) {
            if (variant.isBaseVariant() || "cobbled".equals(variant.getName())) {
                if (path.equals("beige_limestone") || path.equals("cobbled_beige_limestone")) {
                    return 2;
                }
                if (path.contains("deep")) {
                    return 2;
                }
                return 1;
            }
            return Math.max(0, emcMap.getOrDefault(schemaName, 1L));
        }

        String baseFormPath = variant.getBlockId(schemaName, "");
        long baseEmc = emcMap.getOrDefault(baseFormPath, 0L);
        String formName = form.getName();

        if ("stairs".equals(formName)) {
            return Math.max(0, (baseEmc * 6) / 4);
        }
        if ("slab".equals(formName)) {
            return Math.max(0, (baseEmc * 3) / 6);
        }
        if ("wall".equals(formName)) {
            return Math.max(0, baseEmc);
        }
        if ("button".equals(formName)) {
            return Math.max(0, (baseEmc) / 2);
        }
        if ("pressure_plate".equals(formName)) {
            return Math.max(0, baseEmc * 2);
        }

        return 0;
    }

    private static void addTransmutation() {
        for (BlockSchema schema : BlockSchemas.ROCK_TYPES) {
            Block baseBlock = schema.getBaseBlock();
            ResourceLocation key = ForgeRegistries.BLOCKS.getKey(baseBlock);
            if (key != null && key.getNamespace().equals(Constants.MOD_ID)) {
                String path = key.getPath();
                if (path.contains("deep") || path.contains("dark")) {
                    continue;
                }
            }
            if (baseBlock != null && ForgeRegistries.BLOCKS.getKey(baseBlock) != null) {
                InterModComms.sendTo("projecte", IMCMethods.REGISTER_WORLD_TRANSMUTATION, () -> new WorldTransmutationEntry(baseBlock.defaultBlockState(), Blocks.STONE.defaultBlockState(), Blocks.COBBLESTONE.defaultBlockState()));
            }
        }
    }
}
