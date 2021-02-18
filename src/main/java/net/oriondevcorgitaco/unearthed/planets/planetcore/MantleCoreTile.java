package net.oriondevcorgitaco.unearthed.planets.planetcore;

import com.google.common.collect.AbstractIterator;
import it.unimi.dsi.fastutil.longs.LongArraySet;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;
import net.oriondevcorgitaco.unearthed.core.UEEntities;
import net.oriondevcorgitaco.unearthed.planets.block.PlanetLavaBlock;
import net.oriondevcorgitaco.unearthed.planets.block.SurfaceTypes;
import net.oriondevcorgitaco.unearthed.planets.data.CorePositionSavedData;
import net.oriondevcorgitaco.unearthed.planets.entity.AsteroidEntity;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class MantleCoreTile extends TileEntity implements ITickableTileEntity {
    private int EROSION_ATMOSPHERE;
    private int EROSION_OCEANS;
    private int PRECIPITATION_THRESHOLD;
    private int PRECIPITATION_LIMIT;
    private float radius;
    private int atmosphere;
    private int oceans;
    private float preciptation;
    private float magmaComposition = 0;

    private int volcanoes;
    private int maxVolcanoes;
    private int faults;
    private int maxFaults;

    private VoidFiller voidFiller;
    private List<VolcanoHandler> volcanoHandlers;
    private MetamorphicHandler metamorphicHandler;
    private List<BlockPos> exposedBlocks;

    public MantleCoreTile() {
        super(UEEntities.MANTLE_CORE);
        primePlanet(5);
    }

    @Override
    public void setWorldAndPos(World world, BlockPos pos) {
        super.setWorldAndPos(world, pos);
        if (!world.isRemote) {
            if (this.voidFiller == null) this.voidFiller = new VoidFiller(this);
            else voidFiller.init();
            if (this.metamorphicHandler == null) this.metamorphicHandler = new MetamorphicHandler(this);
            else metamorphicHandler.init();
            if (this.volcanoHandlers == null) this.volcanoHandlers = new ArrayList<>();
            else volcanoHandlers.forEach(VolcanoHandler::init);
            int approxR = ((int) Math.ceil(radius));
            CorePositionSavedData.get((ServerWorld) world).add(pos, approxR);
        }
    }

    @Override
    public void tick() {
        if (!world.isRemote()) {
            if (voidFiller.stoneTick()) {
                rescan();
                addActiveBlocks();
            }
            if (voidFiller.oceanTick()) { //when the ocean filling is over, retrieve the # of Oceans filled
                int filledAmount = voidFiller.getFilledAndReset();
                this.oceans += filledAmount;
                this.preciptation -= filledAmount;
            }

            for (int i = volcanoHandlers.size() - 1; i >= 0; i--) {
                VolcanoHandler eruption = volcanoHandlers.get(i);
                if (eruption.isActive()) {
                    eruption.tick();
                } else {
                    createAtmosphere(eruption.getEruptions());
                    volcanoHandlers.remove(i);
                }
            }
            if (metamorphicHandler.isActive()) {
                if (metamorphicHandler.tick()) { //when the metamorphic event is over
                    rescan();
                }
            }
        }
    }

    public void makePlanet(World world, BlockPos corePos, float radius) {
        primePlanet(radius);
        int approxR = ((int) Math.ceil(radius));
        CorePositionSavedData.get((ServerWorld) world).add(corePos, approxR);
        exposedBlocks = new ArrayList<>();
        forEachPlanetBlock().forEach(planetPos -> {
            BlockState existing = world.getBlockState(planetPos);
            if (isCoveredOnAllSides(planetPos.getX() - pos.getX(), planetPos.getY() - pos.getY(), planetPos.getZ() - pos.getZ(), radius)) {
                if (isReplaceable(existing)) {
                    world.setBlockState(planetPos, getIntrusiveBlock());
                }
            } else {
                if (isReplaceable(existing)) {
                    PlanetLavaBlock.setPlanetLavaBlock(world, planetPos, getIntrusiveBlock());
                    exposedBlocks.add(planetPos.toImmutable());
                }
            }
        });
        addActiveBlocks();
    }

    private void primePlanet(float radius) {
        this.radius = radius;
        this.maxVolcanoes = MathHelper.clamp((int) (radius * radius * 4 * Math.PI * 0.02), 1, 99);
        this.maxFaults = MathHelper.clamp((int) (radius * radius * 4 * Math.PI * 0.01), 1, 40);
        EROSION_OCEANS = (int) (radius * radius * 10);
        double surface = Math.PI * 4 * radius * radius;

        EROSION_ATMOSPHERE = (int) (surface * 1.2);
        PRECIPITATION_THRESHOLD = EROSION_OCEANS / 10;
        PRECIPITATION_LIMIT = PRECIPITATION_THRESHOLD * 10;
        preciptation = 0;
        oceans = 0;
        atmosphere = 0;
        volcanoes = 0;
        faults = 0;
        magmaComposition = 1.0f;
    }

    public void onMantleBroken() {
        voidFiller.startFillingVoids();
    }

    public void rescan() {
        volcanoes = 0;
        faults = 0;
        oceans = 0;
        exposedBlocks = new ArrayList<>();
        forEachPlanetBlock().forEach(pos -> {
            BlockState block = world.getBlockState(pos);
            boolean isExposed = !isConcealed(world, pos);
            if (isExposed) {
                exposedBlocks.add(pos.toImmutable());
            }
            if (block.isIn(UEBlocks.VOLCANO)) {
                if (isExposed) {
                    volcanoes++;
                } else {
                    world.setBlockState(pos, getExtrusiveBlock());
                }
            } else if (block.isIn(UEBlocks.FAULT)) {
                if (isExposed) {
                    faults++;
                } else {
                    world.setBlockState(pos, getIntrusiveBlock());
                }
            } else if (block.isIn(UEBlocks.PLANET_WATER)) {
                oceans++;
            }
        });
        if (volcanoes == 0 || faults == 0) {
            for (int i = 1; i < world.rand.nextInt(4); i++) {
                spawnAsteroids();
            }
        }
        markDirty();
    }

    private Vector3d randomTarget() {
        Random rand = world.getRandom();
        double theta = rand.nextFloat() * 2 * Math.PI;
        double phi = rand.nextFloat() * Math.PI;
        double dirX = Math.cos(theta) * Math.sin(phi);
        double dirZ = Math.sin(theta) * Math.sin(phi);
        double dirY = Math.cos(phi);
        return new Vector3d(dirX, dirY, dirZ);
    }

    public void spawnAsteroids() {
        Random rand = world.rand;
        Vector3d axis = randomTarget();
        float orbitRadius = radius * 1.1f + radius * rand.nextFloat() * 0.6f;
        float velocity = 0.008f * (1 + rand.nextFloat());
        double angle = rand.nextFloat() * Math.PI * 2;
        Vector3d axisYP = new Vector3d(0, 1, 0);
        Quaternion rotation = getRotationBetween(axisYP, axis);
        Vector3f pos = new Vector3f((float) Math.cos(angle) * orbitRadius, 0, (float) Math.sin(angle) * orbitRadius);
        Vector3f speed = rand.nextBoolean() ?
                new Vector3f((float) -Math.sin(angle) * velocity, 0, (float) Math.cos(angle) * velocity) :
                new Vector3f((float) Math.sin(angle) * velocity, 0, -(float) Math.cos(angle) * velocity);
        pos.transform(rotation);
        speed.transform(rotation);
//        float angularSpeed = 1;
        AsteroidEntity asteroid = new AsteroidEntity(world, this.pos, new Vector3d(pos), new Vector3d(speed));
        world.addEntity(asteroid);
    }

    private Quaternion getRotationBetween(Vector3d u, Vector3d v) {
        u = u.normalize();
        v = v.normalize();
        double k_cos_theta = u.dotProduct(v);
        Vector3d axis;
        if (k_cos_theta == -1) {
            axis = new Vector3d(0, 1, 0).crossProduct(u);
            if (axis.lengthSquared() < 0.01) {
                axis = new Vector3d(1, 0, 1).crossProduct(u);
            }
            axis = axis.normalize();
            return new Quaternion(new Vector3f(axis), 180, true);
        }
        axis = u.crossProduct(v);
        float s = (float) Math.sqrt((1 + k_cos_theta) * 2);
        float invs = 1 / s;
        return new Quaternion((float) axis.getX() * invs, (float) axis.getY() * invs, (float) axis.getZ() * invs, s / 2);
    }

    public void asteroidImpact(AsteroidEntity entity, BlockPos pos) {
        forEachInSphere(pos, MathHelper.clamp(2 + world.rand.nextFloat() * 4, 2, getRadius() / 4)).forEach(mutable -> {
            if (isInsideSphere(mutable)) {
                destroyBlock(mutable);
            }
        });
        exposedBlocks = new ArrayList<>();
        forEachPlanetBlock().forEach(mutable -> {
            if (!isConcealed(world, mutable)) {
                exposedBlocks.add(mutable.toImmutable());
            }
        });
        addActiveBlocks();
        markDirty();
    }

    private void addActiveBlocks() {
        if (volcanoes < maxVolcanoes) {
            int count = 0;
            int volcanoesToAdd = world.rand.nextInt(maxVolcanoes - volcanoes) + 1;
            for (int i = 0; i < volcanoesToAdd && count < 100; count++) {
                BlockPos exposedPos = exposedBlocks.get(world.rand.nextInt(exposedBlocks.size()));
                if (canSetBlock(exposedPos, false, false)) {
                    setBlock(exposedPos, UEBlocks.VOLCANO.getDefaultState());
                    volcanoes++;
                    i++;
                }
            }
        }
        if (faults < maxFaults) {
            int count = 0;
            int faultsToAdd = world.rand.nextInt(maxFaults - faults) + 1;
            for (int i = 0; i < faultsToAdd && count < 100; count++) {
                BlockPos exposedPos = exposedBlocks.get(world.rand.nextInt(exposedBlocks.size()));
                if (canSetBlock(exposedPos, false, false)) {
                    setBlock(exposedPos, UEBlocks.FAULT.getDefaultState());
                    faults++;
                    i++;
                }
            }
        }
    }

    public void metamorphicEvent(BlockPos start) {
        faults--;
        if (!metamorphicHandler.isActive()) {
            metamorphicHandler.startMetamorphicEvent();
        }
    }

    public void volcanicExplosion(BlockPos volcano) {
        volcanoes--;
        VolcanoHandler eruption = new VolcanoHandler(this);
        eruption.volcanicExplosion(volcano, getVolcanicExplosionIntensity());
        volcanoHandlers.add(eruption);
    }

    private int getVolcanicExplosionIntensity() {
        double surface = Math.PI * 4 * radius * radius;
        int min = (int) (radius * 2);
        int max = Math.max(min, (int) (surface * 0.2));
        return world.rand.nextInt(max - min) + min;
    }

    public boolean addCloudPrecipitation(BlockPos cloudPos, float volume) {
        if (atmosphere > EROSION_ATMOSPHERE && oceans > EROSION_OCEANS) {
            Set<Long> visited = new LongArraySet();
            LinkedList<BlockPos> queue = new LinkedList<>();
            visited.add(cloudPos.toLong());
            queue.add(cloudPos);
            while (queue.size() != 0 && visited.size() < 256) {
                BlockPos pos = queue.poll();
                if (!isConcealed(world, pos) && canSetBlock(pos, false, false)) {
                    addSurfaceBlock(pos);
                    return true;
                }
                for (Direction dir : Direction.values()) {
                    BlockPos nextBlockPos = pos.offset(dir);
                    if (!visited.contains(nextBlockPos.toLong())) {
                        visited.add(nextBlockPos.toLong());
                        queue.add(nextBlockPos);
                    }
                }
            }
            markDirty();
        }
        return false;
    }

    public void addPrecipitation(float addition) {
        this.preciptation += addition;
        if (oceans < EROSION_OCEANS && this.preciptation > PRECIPITATION_THRESHOLD) {
            voidFiller.startFillingOceans((int) this.preciptation);
        }
        if (this.preciptation > PRECIPITATION_LIMIT) {
            metamorphicHandler.startMetamorphicEvent();
            this.preciptation = 0;
        } else if (this.preciptation > PRECIPITATION_THRESHOLD) {
            depositSediment();
        }
    }

    public void depositSediment() {
        List<BlockPos> surfaces = forEachPlanetBlock().filter(pos -> {
            BlockState block = world.getBlockState(pos);
            return block.isIn(UEBlocks.SURFACE);
        }).collect(Collectors.toList());
        if (surfaces.size() > 0) {
            BlockPos randomPos = surfaces.get(world.rand.nextInt(surfaces.size()));
            BlockState randomState = world.getBlockState(randomPos);
            UEBlocks.SURFACE.onPlayerDestroy(world, randomPos, randomState);
        }
        markDirty();
    }

    public boolean addSediment(SurfaceTypes surface) {
        BlockPos pos = voidFiller.removeBottomMostOcean();
        if (pos != null) {
            if (canSetBlock(pos, true, true)) {
                setBlock(pos, getSedimentaryBlock(surface));
                markDirty();
                return true;
            }
        }
        return false;
    }

    public BlockState getIntrusiveBlock() {
        return Blocks.GRANITE.getDefaultState();
    }

    public BlockState getExtrusiveBlock() {
        return Blocks.BLACKSTONE.getDefaultState();
    }

    public BlockState getMetamorphosedBlock(BlockState state) {
        return Blocks.QUARTZ_BLOCK.getDefaultState();
    }

    public BlockState getSedimentaryBlock(SurfaceTypes surfaceType) {
        switch (surfaceType) {

        }
        return Blocks.SANDSTONE.getDefaultState();
    }

    public void addSurfaceBlock(BlockPos pos) {
        setBlock(pos, UEBlocks.SURFACE.getDefaultState());
    }

    protected Stream<BlockPos> forEachPlanetBlock() {
        return forEachInSphere(this.pos, this.radius);
    }

    protected Stream<BlockPos> forEachInSphere(BlockPos center, float radius) {
        return StreamSupport.stream(getAllInSphereMutable(center, radius).spliterator(), false);
    }

    public static Iterable<BlockPos> getAllInSphereMutable(BlockPos center, float radius) {
        return () -> new AbstractIterator<BlockPos>() {
            private final BlockPos.Mutable mutablePos = new BlockPos.Mutable();
            private int approxR = ((int) Math.ceil(radius));
            private int y = -approxR;
            private int x = -approxR;
            private int z = -approxR;
            private boolean depleted = false;

            @Override
            protected BlockPos computeNext() {
                while (x * x + y * y + z * z >= radius * radius && !depleted) {
                    iterateOverBox();
                }
                if (depleted) return this.endOfData();
                if (((x | y) | z) == 0) iterateOverBox();
                BlockPos pos = this.mutablePos.setAndOffset(center, x, y, z);
                iterateOverBox();
                return pos;
            }

            private void iterateOverBox() {
                if (z == approxR) {
                    if (x == approxR) {
                        if (y == approxR) {
                            depleted = true;
                        } else {
                            y++;
                        }
                        x = -approxR;
                    } else {
                        x++;
                    }
                    z = -approxR;
                } else {
                    z++;
                }
            }
        };
    }

    public float getRadius() {
        return radius;
    }


    private void createAtmosphere(int eruptions) {
        atmosphere += eruptions * 0.5f;
    }


    protected void addLavaBlockIfExposed(BlockPos pos, BlockState actualBlock) {
        if (isConcealed(world, pos)) {
            setBlock(pos, actualBlock);
        } else {
            PlanetLavaBlock.setPlanetLavaBlock(world, pos, actualBlock);
        }
    }

    protected void destroyBlock(BlockPos pos) {
        BlockState existing = world.getBlockState(pos);
        if (isReplaceable(existing) || existing.isIn(UEBlocks.PLANET_WATER)) {
            if (existing.isIn(UEBlocks.VOLCANO)) {
                volcanicExplosion(pos);
            } else if (existing.isIn(UEBlocks.FAULT)) {
                faults--;
            }
            world.setBlockState(pos, Blocks.AIR.getDefaultState());
        } else if (existing.isIn(UEBlocks.MANTLE_CORE)) {
            onMantleBroken();
        }
    }

    protected boolean canSetBlock(BlockPos pos, boolean replaceWater, boolean replaceAir) {
        if (isInsideSphere(pos)) {
            BlockState existing = world.getBlockState(pos);
            if (existing.isIn(UEBlocks.PLANET_WATER)) {
                return replaceWater;
            } else if (existing.isAir()) {
                return replaceAir;
            } else return isGeneratedBlock(existing);
        }
        return false;
    }

    protected void setBlock(BlockPos pos, BlockState intended) {
        Block existing = world.getBlockState(pos).getBlock();
        if (existing == UEBlocks.VOLCANO) {
            volcanoes--;
        } else if (existing == UEBlocks.FAULT) {
            faults--;
        } else if (existing == UEBlocks.PLANET_WATER) {
            oceans--;
        }
        world.setBlockState(pos, intended);
    }

//    protected boolean setBlock(BlockPos pos, BlockState intended, boolean replaceWater) {
//        BlockState existing = world.getBlockState(pos);
//        if (isReplaceable(existing)) {
//            if (existing.isIn(UEBlocks.VOLCANO)) {
//                volcanoes--;
//            } else if (existing.isIn(UEBlocks.FAULT)) {
//                faults--;
//            }
//            world.setBlockState(pos, intended);
//            return true;
//        } else if (replaceWater && existing.isIn(UEBlocks.PLANET_WATER)) {
//            world.setBlockState(pos, intended);
//            oceans--;
//            return true;
//        } else {
//            return false;
//        }
//    }

    public boolean isInsideSphere(BlockPos to) {
        return to.distanceSq(pos.getX(), pos.getY(), pos.getZ(), false) < radius * radius;
    }

    /**
     * returns true for all planet-related blocks
     */
    protected boolean isGeneratedBlock(BlockState blockstate) {
        Block block = blockstate.getBlock();
        return block == Blocks.GRANITE || block == Blocks.BLACKSTONE || block == Blocks.QUARTZ_BLOCK || block == Blocks.SANDSTONE
                || block == UEBlocks.PLANET_LAVA || block == UEBlocks.PLANET_WATER || block == UEBlocks.SURFACE || block == UEBlocks.VOLCANO
                || block == UEBlocks.FAULT;
    }

    /**
     * returns true for generated blocks and air
     */
    protected boolean isReplaceable(BlockState block) {
        return isGeneratedBlock(block) || block.isAir();
    }

    protected boolean isConcealed(World world, BlockPos pos) {
        BlockPos.Mutable mutablePos = pos.toMutable();
        for (Direction dir : Direction.values()) {
            mutablePos.setAndMove(pos, dir);
//                if (!world.getBlockState(mutablePos).isSolidSide(world, mutablePos, dir.getOpposite())) {
//                    return false;
//                }
            BlockState block = world.getBlockState(mutablePos);
            if (block.isAir() || block.getBlock() == UEBlocks.PLANET_WATER) {
                return false;
            }
        }
        return true;
    }

    private boolean isCoveredOnAllSides(int x, int y, int z, float r) {
        int x2 = x * x;
        int y2 = y * y;
        int z2 = z * z;
        float r2 = r * r;
        return (x - 1) * (x - 1) + y2 + z2 < r2 &&
                (x + 1) * (x + 1) + y2 + z2 < r2 &&
                x2 + (y - 1) * (y - 1) + z2 < r2 &&
                x2 + (y + 1) * (y + 1) + z2 < r2 &&
                x2 + y2 + (z - 1) * (z - 1) < r2 &&
                x2 + y2 + (z + 1) * (z + 1) < r2;
    }


    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        radius = nbt.getFloat("radius");
        atmosphere = nbt.getInt("atmosphere");
        preciptation = nbt.getFloat("precipitation");
        magmaComposition = nbt.getFloat("magmaComposition");
        primePlanet(radius);

        oceans = nbt.getInt("oceans");
        volcanoes = nbt.getInt("volcanoes");
        faults = nbt.getInt("faults");

        metamorphicHandler = new MetamorphicHandler(this);
        metamorphicHandler.read(nbt.getCompound("meatamorphicHandler"));
        voidFiller = new VoidFiller(this);
        voidFiller.read(nbt.getCompound("voidFiller"));
        ListNBT list = nbt.getList("volcanoes", 10);
        volcanoHandlers = new ArrayList<>();
        list.forEach(inbt -> {
            VolcanoHandler handler = new VolcanoHandler(this);
            handler.read((CompoundNBT) inbt);
            volcanoHandlers.add(handler);
        });
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putFloat("radius", radius);
        compound.putInt("atmosphere", atmosphere);
        compound.putFloat("precipitation", preciptation);
        compound.putFloat("magmaComposition", magmaComposition);

        compound.putInt("oceans", oceans);
        compound.putInt("volcanoes", volcanoes);
        compound.putInt("faults", faults);

        compound.put("metamorphicHandler", metamorphicHandler.write(new CompoundNBT()));
        compound.put("voidFiller", voidFiller.write(new CompoundNBT()));
        ListNBT list = new ListNBT();
        for (VolcanoHandler handler : volcanoHandlers) {
            list.add(handler.write(new CompoundNBT()));
        }
        compound.put("volcanoes", list);
        return super.write(compound);
    }

    @Override
    public void remove() {
        if (world instanceof ServerWorld) {
            CorePositionSavedData.get((ServerWorld) world).remove(this.pos);
        }
        super.remove();
    }
}
