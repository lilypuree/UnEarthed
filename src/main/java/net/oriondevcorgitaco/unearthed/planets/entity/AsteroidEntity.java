package net.oriondevcorgitaco.unearthed.planets.entity;

import net.minecraft.block.BlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.oriondevcorgitaco.unearthed.core.UEEntities;
import net.oriondevcorgitaco.unearthed.planets.planetcore.MantleCoreTile;

public class AsteroidEntity extends Entity {
    private static final DataParameter<Boolean> ORBIT = EntityDataManager.createKey(AsteroidEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<BlockPos> CENTER = EntityDataManager.createKey(AsteroidEntity.class, DataSerializers.BLOCK_POS);
    private float gravityConstant = 0;
    private static final float DRAG = 0.2f;

    public AsteroidEntity(EntityType<?> entityTypeIn, World worldIn) {
        super(entityTypeIn, worldIn);
        this.noClip = true;
    }

    public AsteroidEntity(World world, float gravityConstant) {
        this(UEEntities.ASTEROID, world);
        this.gravityConstant = gravityConstant;
    }

    public AsteroidEntity(World world, BlockPos center, Vector3d centerOffset, Vector3d motion) {
        this(world, getGravityConstant((float) centerOffset.length(), motion));
        dataManager.set(ORBIT, true);
        dataManager.set(CENTER, center);
        this.setPosition(centerOffset.getX() + center.getX() + 0.5, centerOffset.getY() + center.getY() + 0.5, centerOffset.getZ() + center.getZ() + 0.5);
        this.setMotion(motion);
    }

    private static float getGravityConstant(float radius, Vector3d motion) {
        return radius * (float) motion.lengthSquared();
    }

    //    private boolean onOrbit;
//    private BlockPos planetCenter;
//    private float angularSpeed;
//    private float orbitRadius;
//    private Vector3d axis;

    @Override
    public void tick() {
        this.prevPosX = this.getPosX();
        this.prevPosY = this.getPosY();
        this.prevPosZ = this.getPosZ();
        Vector3d motion = this.getMotion();

        BlockPos planetCenter = dataManager.get(CENTER);
        Vector3d offset = new Vector3d(planetCenter.getX() + 0.5, planetCenter.getY() + 0.5, planetCenter.getZ() + 0.5).subtract(this.getPositionVec());
        Vector3d acceleration;
        if (!dataManager.get(ORBIT)) {
            double radius = offset.length();
            acceleration = offset.scale(gravityConstant / radius * radius * radius).subtract(motion.scale(DRAG));
            world.addParticle(ParticleTypes.FLAME, this.getPosX(), this.getPosY(), this.getPosZ(), rand.nextFloat() * 0.1f, rand.nextFloat() * 0.1f, rand.nextFloat() * 0.1f);
        } else {
            acceleration = offset.scale(motion.lengthSquared() / offset.lengthSquared());
        }
        this.setMotion(motion.add(acceleration));
        this.move(MoverType.SELF, this.getMotion());

        if (!world.isRemote() && ticksExisted % 20 == 0) {
            if (!(world.getTileEntity(planetCenter) instanceof MantleCoreTile)) {
                remove();
            }
        }

//        if (!world.isRemote()) {
//
//            if (onOrbit) {
//                double radiiRatio = orbitRadius / offset.length();
//                offset = offset.scale(radiiRatio);
//                this.setPosition(planetCenter.getX() + 0.5 + offset.getX(), planetCenter.getY() + 0.5 + offset.getY(), planetCenter.getZ() + 0.5 + offset.getZ());
//                Vector3d velocity = offset.crossProduct(axis).scale(angularSpeed);
//                this.setMotion(velocity);
//            } else {
//                Vector3d velocity = this.getMotion();
//                Vector3d acceleration = offset.scale(-angularSpeed * angularSpeed / offset.lengthSquared());
//                this.setMotion(velocity.add(acceleration));
//                if (rand.nextInt() % 20 == 0) {
//                }
//            }
//        }
        super.tick();
    }

    @Override
    public void move(MoverType typeIn, Vector3d pos) {
        super.move(typeIn, pos);
        try {
            this.doBlockCollisions();
        } catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Checking entity block collision");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being checked for collision");
            this.fillCrashReport(crashreportcategory);
            throw new ReportedException(crashreport);
        }
    }

    @Override
    protected void onInsideBlock(BlockState state) {
        if (!dataManager.get(ORBIT) && !state.isAir()) {
            world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.getPosX(), this.getPosY(), this.getPosZ(), 0, 0, 0);
            TileEntity tileEntity = world.getTileEntity(getPlanetCenter());
            if (tileEntity instanceof MantleCoreTile) {
                ((MantleCoreTile) tileEntity).asteroidImpact(this, this.getOnPosition());
            }
            remove();
        }
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
        dataManager.set(ORBIT, compound.getBoolean("onOrbit"));
        dataManager.set(CENTER, NBTUtil.readBlockPos(compound.getCompound("center")));
        gravityConstant = compound.getFloat("gravity");
//        angularSpeed = compound.getFloat("angularSpeed");
//        orbitRadius = compound.getFloat("orbitRadius");
//        long[] axisArray = compound.getLongArray("axis");
//        if (axisArray.length == 3) {
//            axis = new Vector3d(Double.longBitsToDouble(axisArray[0]),
//                    Double.longBitsToDouble(axisArray[1]),
//                    Double.longBitsToDouble(axisArray[2]));
//        }
    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
        compound.putBoolean("onOrbit", dataManager.get(ORBIT));
        compound.put("center", NBTUtil.writeBlockPos(getPlanetCenter()));
        compound.putFloat("gravity", gravityConstant);
//        compound.putFloat("angularSpeed", angularSpeed);
//        compound.putFloat("orbitRadius", orbitRadius);
//        long[] axisArray = new long[]{
//                Double.doubleToLongBits(axis.x),
//                Double.doubleToLongBits(axis.y),
//                Double.doubleToLongBits(axis.z)
//        };
//        compound.putLongArray("axis", axisArray);
    }

    @Override
    public boolean hitByEntity(Entity entityIn) {
        if (dataManager.get(ORBIT)) {
            dataManager.set(ORBIT, false);
            return true;
        } else {
            return false;
        }
    }



    @Override
    protected void registerData() {
        this.dataManager.register(ORBIT, true);
        this.dataManager.register(CENTER, BlockPos.ZERO);
    }

    public BlockPos getPlanetCenter() {
        return dataManager.get(CENTER);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return new SSpawnObjectPacket(this, Float.floatToIntBits(gravityConstant));
    }

}
