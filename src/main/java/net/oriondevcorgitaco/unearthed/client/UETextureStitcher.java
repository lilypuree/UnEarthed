package net.oriondevcorgitaco.unearthed.client;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.oriondevcorgitaco.unearthed.Unearthed;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorHelper;
import net.oriondevcorgitaco.unearthed.block.BlockGeneratorReference;
import net.oriondevcorgitaco.unearthed.block.schema.BlockSchema;
import net.oriondevcorgitaco.unearthed.block.schema.Forms;
import net.oriondevcorgitaco.unearthed.block.schema.StoneTiers;
import net.oriondevcorgitaco.unearthed.datagen.type.IOreType;
import net.oriondevcorgitaco.unearthed.datagen.type.VanillaOreTypes;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

public class UETextureStitcher {

    public static File RESOURCE_PACK_FOLDER;

    private TexturePair[] grassTextures;
    private TexturePair[] snowyTextures;
    private Map<IOreType, TexturePair[]> oreTextures = new HashMap<>();
    private BufferedImage grassSide;
    private BufferedImage grassSnowy;
    private Map<IOreType, BufferedImage> oreMasks = new HashMap<>();
    private static List<ResourceLocation> rawRegolithTexture = new ArrayList<>();
    private static Map<IOreType, List<ResourceLocation>> rawStoneTexture = new HashMap<>();

    static {
        for (IOreType oreType : VanillaOreTypes.values()) {
            rawStoneTexture.put(oreType, new ArrayList<>());
        }
        for (BlockGeneratorHelper helper : BlockGeneratorReference.ROCK_TYPES) {
            for (BlockGeneratorHelper.Entry entry : helper.getEntries()) {
                String namespace = helper.getTier() == StoneTiers.VANILLA ? "minecraft" : Unearthed.MOD_ID;
                String stone = helper.getName();
                BlockSchema.Form form = entry.getForm();
                if (form == Forms.REGOLITH) {
                    rawRegolithTexture.add(new ResourceLocation(Unearthed.MOD_ID, "textures/block/" + entry.getId() + ".png"));
                } else if (form == Forms.OVERGROWN_ROCK) {
                    rawRegolithTexture.add(new ResourceLocation(namespace, "textures/block/" + stone + ".png"));
                } else if (form instanceof Forms.OreForm) {
                    rawStoneTexture.get(((Forms.OreForm) form).getOreType())
                            .add(new ResourceLocation(namespace, "textures/block/" + stone + ".png"));
                }
            }
        }
    }

    public static void setupFolders() {
        File modData = new File("./mod_data/" + Unearthed.MOD_ID + "/");
        modData.mkdirs();


        RESOURCE_PACK_FOLDER = new File(modData, "resource_pack");
        new File(RESOURCE_PACK_FOLDER, "assets").mkdirs();
        new File(RESOURCE_PACK_FOLDER + "/assets/unearthed/textures/block/icon.png").mkdirs();
//        rawTextures.forEach(fullpath -> {
//            String fileName = fullpath.toString().replaceFirst("minecraft", "decorative_winter")
//                    .replaceFirst(":", "/").replaceAll("_log.png", "_thin_branch.png");
//            File outputFile = new File(RESOURCE_PACK_FOLDER + "/assets/" + fileName);
//            outputFile.getParentFile().mkdirs();
//        });
        try {
            if (!new File(RESOURCE_PACK_FOLDER, "pack.mcmeta").exists())
                Files.copy(Thread.currentThread().getContextClassLoader().getResourceAsStream("/assets/mcmeta_template"), new File(RESOURCE_PACK_FOLDER, "pack.mcmeta").toPath());
//                if (!new File(RESOURCE_PACK_FOLDER, "pack.png").exists())
//                    Files.copy(Thread.currentThread().getContextClassLoader().getResourceAsStream("/assets/pack.png"), new File(RESOURCE_PACK_FOLDER, "pack.png").toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UETextureStitcher prepare() {
        grassSide = readImage(new ResourceLocation(Unearthed.MOD_ID, "textures/block/grass_block_side_overlay.png"));
        grassSnowy = readImage(new ResourceLocation(Unearthed.MOD_ID, "textures/block/grass_block_snow.png"));
        grassSide = readImage(new ResourceLocation(Unearthed.MOD_ID, "textures/block/grass_block_side_overlay.png"));
        for (IOreType oreType : VanillaOreTypes.values()) {
            oreMasks.put(oreType, readImage(new ResourceLocation(Unearthed.MOD_ID, "textures/block/ore/" + oreType.getName() + "_ore_mask.png")));
            oreTextures.put(oreType, rawStoneTexture.get(oreType).stream().map(loc -> new TexturePair(loc, readImage(loc))).toArray(TexturePair[]::new));
        }
        grassTextures = rawRegolithTexture.stream().map(loc -> {
            return new TexturePair(loc, readImage(loc));
        }).toArray(TexturePair[]::new);
        snowyTextures = rawRegolithTexture.stream().map(loc -> {
            return new TexturePair(loc, readImage(loc));
        }).toArray(TexturePair[]::new);
        return this;
    }

    private BufferedImage readImage(ResourceLocation location) {
        try {
            IResource resource = Minecraft.getInstance().getResourceManager().getResource(location);
            InputStream input = new BufferedInputStream(resource.getInputStream());
            return ImageIO.read(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void generate() {
        Arrays.stream(grassTextures).parallel().forEach(texturePair -> {
            try {
                texturePair.setImage(combine(texturePair.getImage(), grassSide));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Arrays.stream(snowyTextures).parallel().forEach(texturePair -> {
            try {
                texturePair.setImage(combine(texturePair.getImage(), grassSnowy));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        for (IOreType oreType : VanillaOreTypes.values()) {
            Arrays.stream(oreTextures.get(oreType)).parallel().forEach(texturePair -> {
                try {
                    texturePair.setImage(combine(texturePair.getImage(), oreMasks.get(oreType)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        Arrays.stream(grassTextures).parallel().forEach(texturePair -> {
            String name = texturePair.getLoc().getPath();
            if (name.contains("regolith")) {
                writeToTexture(texturePair, "regolith", "grassy_regolith_side", "side/");
            } else {
                writeToTexture(texturePair, ".png", "_side.png", "side/overgrown_");
            }
        });
        Arrays.stream(snowyTextures).parallel().forEach(texturePair -> {
            String name = texturePair.getLoc().getPath();
            if (name.contains("regolith")) {
                writeToTexture(texturePair, "regolith", "grassy_regolith_snow", "side/");
            } else {
                writeToTexture(texturePair, ".png", "_snow.png", "side/overgrown_");
            }
        });
        for (IOreType oreType : VanillaOreTypes.values()) {
            Arrays.stream(oreTextures.get(oreType)).forEach(texturePair ->
                    writeToTexture(texturePair, ".png", "_" + oreType.getName() + "_ore.png", "ore/")
            );
        }
    }

    private void writeToTexture(TexturePair texturePair, String toReplace, String replacement, String subfolder) {
        try {
            String fileName = texturePair.loc.toString()
                    .replaceAll(toReplace, replacement)
                    .replaceFirst("minecraft", "unearthed")
                    .replaceFirst("block/", "block/" + subfolder)
                    .replaceFirst(":", "/");
            File outputFile = new File(RESOURCE_PACK_FOLDER + "/assets/" + fileName);
            outputFile.getParentFile().mkdirs();
            ImageIO.write(texturePair.getImage(), "png", outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BufferedImage combine(BufferedImage a, BufferedImage b) {
        BufferedImage newImage = new BufferedImage(a.getWidth(), a.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = newImage.createGraphics();
        graphics.setComposite(AlphaComposite.SrcOver);
        graphics.drawImage(a, 0, 0, null);
        graphics.drawImage(b, 0, 0, null);
        graphics.dispose();
        return newImage;
    }

    public static BufferedImage upscale(BufferedImage sourceImage) {
        int x = sourceImage.getWidth();
        int y = sourceImage.getHeight();
        BufferedImage newImage = new BufferedImage(x * 2, y * 2, sourceImage.getType());
        Graphics2D graphics = newImage.createGraphics();
        graphics.drawImage(sourceImage, 0, 0, null);
        graphics.drawImage(sourceImage, x, 0, null);
        graphics.drawImage(sourceImage, 0, y, null);
        graphics.drawImage(sourceImage, x, y, null);
        graphics.dispose();
        return newImage;
    }

    private Image transformGrayToTransparency(BufferedImage image) {
        ImageFilter filter = new RGBImageFilter() {
            public final int filterRGB(int x, int y, int rgb) {
                return (rgb << 8) & 0xFF000000;
            }
        };

        ImageProducer ip = new FilteredImageSource(image.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    public static BufferedImage mask(BufferedImage sourceImage, Image mask) {
        BufferedImage dest = new BufferedImage(
                sourceImage.getWidth(), sourceImage.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        Graphics2D graphics = dest.createGraphics();
        graphics.drawImage(sourceImage, 0, 0, null);
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.DST_IN, 1.0f);
        graphics.setComposite(ac);
        graphics.drawImage(mask, 0, 0, null);
        graphics.dispose();
        return dest;
    }

    public static class TexturePair {

        private ResourceLocation loc;
        private BufferedImage image;

        public TexturePair(ResourceLocation loc, BufferedImage image) {
            this.loc = loc;
            this.image = image;
        }

        public ResourceLocation getLoc() {
            return loc;
        }

        public BufferedImage getImage() {
            return image;
        }

        public void setImage(BufferedImage image) {
            this.image = image;
        }
    }
}
