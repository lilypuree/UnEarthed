package lilypuree.unearthed.misc;

import com.google.gson.JsonElement;
import commoble.databuddy.data.CodecJsonDataManager;
import lilypuree.unearthed.Constants;
import lilypuree.unearthed.world.feature.gen.StoneType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

public abstract class StoneTypeCodecJsonDataManager extends CodecJsonDataManager<StoneType> {

    public StoneTypeCodecJsonDataManager() {
        super("uedata/stone_types", StoneType.DIRECT_CODEC, Constants.LOG);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsons, ResourceManager resourceManager, ProfilerFiller profiler) {
        super.apply(jsons, resourceManager, profiler);
        List<ResourceLocation> toRemove = new ArrayList<>();
        for (Map.Entry<ResourceLocation, StoneType> entry : getData().entrySet()) {
            if (!fireEvent(entry.getValue())) {
                toRemove.add(entry.getKey());
            } else {
                entry.getValue().toImmutable();
            }
        }
        toRemove.forEach(rl -> data.put(rl, new StoneType(null) {
            @Override
            public BlockState replace(BlockState original) {
                return original;
            }
        }));
    }

    /**
     * @return false if the event is cancelled and the stonetype should be removed
     */
    public abstract boolean fireEvent(StoneType stoneType);
}
