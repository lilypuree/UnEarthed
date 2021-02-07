package net.lilycorgitaco.unearthed.client;

import java.io.File;
import java.util.function.Consumer;
import net.minecraft.resource.DirectoryResourcePack;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;

public class UEPackFinder implements ResourcePackProvider {
    private File folder;

    public UEPackFinder(File file) {
        this.folder = file;
    }

    @Override
    public void register(Consumer<ResourcePackProfile> consumer, ResourcePackProfile.Factory packInfoFactory) {
        if (folder.exists() && folder.isDirectory()) {
            ResourcePackProfile t = ResourcePackProfile.of("Unearthed Generated Assets", true, () -> new DirectoryResourcePack(folder), packInfoFactory, ResourcePackProfile.InsertionPosition.TOP, ResourcePackSource.field_25347);
            if (t == null) return;
            consumer.accept(t);
        }
    }
}
