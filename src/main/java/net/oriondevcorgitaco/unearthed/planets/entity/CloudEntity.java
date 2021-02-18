package net.oriondevcorgitaco.unearthed.planets.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;
import net.oriondevcorgitaco.unearthed.core.UEEntities;
import net.oriondevcorgitaco.unearthed.planets.planetcore.MantleCoreTile;
import org.jetbrains.annotations.NotNull;

public class CloudEntity extends Entity {
    private static float MAX_VOLUME = 64;
    private static final DataParameter<Float> WIDTH = EntityDataManager.createKey(CloudEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> HEIGHT = EntityDataManager.createKey(CloudEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> LENGTH = EntityDataManager.createKey(CloudEntity.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> BRIGHTNESS = EntityDataManager.createKey(CloudEntity.class, DataSerializers.FLOAT);
    private BlockPos planetCenter;
    private float angularSpeed;
    private float orbitRadius;
    private int lives = 3;

    private boolean unStable;

    public CloudEntity(EntityType<?> type, World world) {
        super(type, world);
        this.noClip = true;
    }

    public CloudEntity(World world) {
        this(UEEntities.CLOUD, world);
    }

    public CloudEntity(World worldIn, BlockPos planetCenter, float angularSpeed, float orbitRadius, boolean unStable) {
        this(worldIn);
        this.planetCenter = planetCenter;
        this.angularSpeed = angularSpeed;
        this.orbitRadius = orbitRadius;
        this.unStable = unStable;
    }

    @Override
    public boolean hitByEntity(@NotNull Entity entityIn) {
        this.unStable = true;
        return true;
    }

    @Override
    public ActionResultType applyPlayerInteraction(PlayerEntity player, Vector3d vec, Hand hand) {
        ItemStack itemStack = player.getHeldItem(hand);
        if (itemStack.getItem() == Items.WATER_BUCKET) {
            addVolume(4);
            if (!player.isCreative()) {
                player.setHeldItem(hand, new ItemStack(Items.BUCKET));
            }
            return ActionResultType.SUCCESS;
        }
        return super.applyPlayerInteraction(player, vec, hand);
    }

    @Override
    public void tick() {
        if (!world.isRemote()){
            if (ticksExisted % 20 == 0) {
                if (!world.getBlockState(planetCenter).isIn(UEBlocks.MANTLE_CORE)) {
                    world.addParticle(ParticleTypes.CLOUD, this.getPosX(), this.getPosY(), this.getPosZ(), 0, 0, 0);
                    remove();
                } else if (getVolume() > MAX_VOLUME || getCloudBrightness() < 0.5f) {
                    unStable = true;
                } else if (rand.nextInt(100) == 0) {
                    unStable = false;
                }
            }
            if (unStable && rand.nextInt((int) (getVolume() * 5)) == 0) {
                split();
            }
            double dx = this.getPosX() - (planetCenter.getX() + 0.5f);
            double dz = this.getPosZ() - (planetCenter.getZ() + 0.5f);
            double r2 = dx * dx + dz * dz;
            double radiiRatio = Math.sqrt(orbitRadius / Math.sqrt(r2));
            dx *= radiiRatio;
            dz *= radiiRatio;
            this.setPosition(planetCenter.getX() + 0.5f + dx, this.getPosY(), planetCenter.getZ() + 0.5f + dz);
            double vx = dz * angularSpeed;
            double vz = -dx * angularSpeed;
            this.setMotion(vx, 0, vz);
        }

        super.tick();

    }

    private void split() {
        float volume = getVolume();
        if (volume > 1) {
            float length = getCloudLength();
            float height = getCloudHeight();
            float width = getCloudWidth();
            float maxDimension = Math.max(Math.max(length, height), width);
            float split = MathHelper.clamp((float) rand.nextGaussian() * maxDimension + maxDimension / 2, maxDimension * 0.2f, maxDimension * 0.8f);
            CloudEntity cloud1 = new CloudEntity(world, planetCenter, angularSpeed * (1 - rand.nextFloat() * 0.01f), orbitRadius + rand.nextFloat() * 0.1f, unStable);
            CloudEntity cloud2 = new CloudEntity(world, planetCenter, angularSpeed * (1 + rand.nextFloat() * 0.01f), orbitRadius + rand.nextFloat() * 0.1f, unStable);
            Vector3f coords1;
            Vector3f coords2;
            if (maxDimension == length) {
                cloud1.setDimensions(width, height, split);
                cloud2.setDimensions(width, height, length - split);
                coords1 = convertCloudCoords(new Vector3f(0, 0, length / 2 - split / 2));
                coords2 = convertCloudCoords(new Vector3f(0, 0, -split / 2));
            } else if (maxDimension == height) {
                cloud1.setDimensions(width, split, length);
                cloud2.setDimensions(width, height - split, length);
                coords1 = convertCloudCoords(new Vector3f(0, height / 2 - split / 2, 0));
                coords2 = convertCloudCoords(new Vector3f(0, -split / 2, 0));
            } else {
                cloud1.setDimensions(split, height, length);
                cloud2.setDimensions(width - split, height, length);
                coords1 = convertCloudCoords(new Vector3f(width / 2 - split / 2, 0, 0));
                coords2 = convertCloudCoords(new Vector3f(-split / 2, 0, 0));
            }
            cloud1.setPosition(coords1.getX(), coords1.getY(), coords1.getZ());
            cloud2.setPosition(coords2.getX(), coords2.getY(), coords2.getZ());

            double motionX = rand.nextGaussian() * 0.1f;
            double motionY = rand.nextGaussian() * 0.1f;
            double motionZ = rand.nextGaussian() * 0.1f;
            cloud1.setMotion(this.getMotion().add(motionX, motionY, motionZ));
            cloud2.setMotion(this.getMotion().subtract(motionX, motionY, motionZ));
            world.addEntity(cloud1);
            world.addEntity(cloud2);
            remove();
        } else {
            TileEntity tileEntity = world.getTileEntity(planetCenter);
            if (tileEntity instanceof MantleCoreTile) {
                if (((MantleCoreTile) tileEntity).addCloudPrecipitation(this.getPosition(), volume)) {
                    die();
                } else if (--lives == 0) {
                    ((MantleCoreTile) tileEntity).addPrecipitation(volume);
                    die();
                }
            } else {
                die();
            }

        }
    }

    private void die() {
        world.addParticle(ParticleTypes.CLOUD, this.getPosX(), this.getPosY(), this.getPosZ(), 0, 0, 0);
        remove();
    }

    private Vector3f convertCloudCoords(Vector3f cloudCoords) {
        Vector3d cloudZ = new Vector3d(getMotion().x, 0, getMotion().z);
        cloudZ.normalize();
        float coordinateAngle = (float) Math.atan2(cloudZ.z, cloudZ.x);
        cloudCoords.transform(new Quaternion(Vector3f.YP, coordinateAngle, false));
        cloudCoords.setX((float) getPosX() + cloudCoords.getX() * 0.25f);
        cloudCoords.setY((float) getPosY() + cloudCoords.getY() * 0.25f);
        cloudCoords.setZ((float) getPosZ() + cloudCoords.getZ() * 0.25f);
        return cloudCoords;
    }


    @Override
    protected void readAdditional(CompoundNBT compound) {

        dataManager.set(WIDTH, compound.getFloat("width"));
        dataManager.set(HEIGHT, compound.getFloat("height"));
        dataManager.set(LENGTH, compound.getFloat("length"));
        dataManager.set(BRIGHTNESS, compound.getFloat("brightness"));
        this.planetCenter = NBTUtil.readBlockPos(compound.getCompound("center"));
        this.angularSpeed = compound.getFloat("angularSpeed");
        this.orbitRadius = compound.getFloat("orbitRadius");
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putFloat("width", getCloudWidth());
        compound.putFloat("height", getCloudHeight());
        compound.putFloat("length", getCloudLength());
        compound.putFloat("brightness", dataManager.get(BRIGHTNESS));
        compound.put("center", NBTUtil.writeBlockPos(planetCenter));
        compound.putFloat("angularSpeed", angularSpeed);
        compound.putFloat("orbitRadius", orbitRadius);
    }


    @Override
    protected void registerData() {
        this.dataManager.register(WIDTH, 1.0f);
        this.dataManager.register(HEIGHT, 1.0f);
        this.dataManager.register(LENGTH, 1.0f);
        this.dataManager.register(BRIGHTNESS, 1.0f);
    }

    private float getVolume() {
        return getCloudWidth() * getCloudHeight() * getCloudLength();
    }

    private void addVolume(float volume) {
        float currentVolume = getVolume();
        float newVolume = currentVolume + volume;
        float ratio = (float) Math.pow(newVolume / currentVolume, 1.0 / 3);
        dataManager.set(WIDTH, getCloudWidth() * ratio);
        dataManager.set(HEIGHT, getCloudHeight() * ratio);
        dataManager.set(LENGTH, getCloudLength() * ratio);
    }

    @NotNull
    private Float getCloudWidth() {
        return dataManager.get(WIDTH);
    }

    @NotNull
    private Float getCloudLength() {
        return dataManager.get(LENGTH);
    }

    @NotNull
    private Float getCloudHeight() {
        return dataManager.get(HEIGHT);
    }

    private void setDimensions(float width, float height, float length) {
        dataManager.set(WIDTH, width);
        dataManager.set(HEIGHT, height);
        dataManager.set(LENGTH, length);
    }

    private void setVolume(float volume) {
        float cubed = (float) Math.pow(volume, 1.0 / 3);
        float height = rand.nextFloat() * 0.4f + 0.3f;
        float length = (float) Math.sqrt(volume / height) * (1.1f + rand.nextFloat());
        dataManager.set(HEIGHT, height);
        dataManager.set(LENGTH, length);
        dataManager.set(WIDTH, volume / height / length);
    }

    @Override
    public EntitySize getSize(Pose poseIn) {
        return super.getSize(poseIn).scale(getCloudWidth(), getCloudHeight());
    }

    public Vector3f getDimensions() {
        return new Vector3f(getCloudWidth(), getCloudHeight(), getCloudLength());
    }

    public float getCloudBrightness() {
        return this.dataManager.get(BRIGHTNESS);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return new SSpawnObjectPacket(this);
    }
}
