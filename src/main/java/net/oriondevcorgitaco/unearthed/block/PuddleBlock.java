package net.oriondevcorgitaco.unearthed.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.oriondevcorgitaco.unearthed.block.properties.ModBlockProperties;
import net.oriondevcorgitaco.unearthed.core.UEBlocks;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

import net.minecraft.block.AbstractBlock.Properties;

public class PuddleBlock extends Block {

    public static final VoxelShape PUDDLE_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 0.1, 16.0);
    public static BooleanProperty TRANSIENT = ModBlockProperties.TRANSIENT;

    public PuddleBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(TRANSIENT, false));
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing == Direction.DOWN) {
            if (!canSurvive(stateIn, worldIn, currentPos)) {
                return Blocks.AIR.defaultBlockState();
            }
        }

        return stateIn;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(TRANSIENT);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return PUDDLE_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (worldIn.canSeeSkyFromBelowWater(pos) && worldIn.isDay() || worldIn.getMaxLocalRawBrightness(pos) >= 14) {
            worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        } else {
            if (worldIn.isAreaLoaded(pos, 4)) {
                int range = 1;
                BlockPos randomPos = new BlockPos(pos.getX() - range + random.nextInt(range * 2 + 1), pos.getY() - range + random.nextInt(range * 2 + 1), pos.getZ() - range + random.nextInt(range * 2 + 1));
                Direction direction = Direction.getRandom(random);
                if (!LichenBlock.hasEnoughLichen(worldIn, pos, 6, 3, 2)) {
                    ((LichenBlock) UEBlocks.LICHEN).tryGrowInto(worldIn, randomPos, direction);
                }
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (state.getValue(TRANSIENT)) {
            worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }
    }

    public static int getEvaporationDelay(World world, BlockPos pos) {
        float downFall = world.getBiome(pos).getDownfall();
        return ((int) (10 + (0.5 + world.getRandom().nextFloat()) * downFall * 100));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return defaultBlockState().setValue(TRANSIENT, false);
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockState down = worldIn.getBlockState(pos.below());
        return Block.isFaceFull(down.getCollisionShape(worldIn, pos.below()), Direction.UP);
    }

    @Override
    public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof PlayerEntity) {
            if (entityIn.getCommandSenderWorld().isClientSide) {
                Vector3d v = entityIn.getDeltaMovement();
                float strength = MathHelper.sqrt(v.x * v.x * 0.2D + v.y * v.y + v.z * v.z * 0.2D) * 0.2f;
                if (strength > 0.01f) {
                    entityIn.playSound(SoundEvents.GENERIC_SWIM, strength * 8, 1.0F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.4F);

                    for (int j = 0; (float) j < 10.0F + entityIn.getBbWidth() * 20.0F; ++j) {
                        float rx = (worldIn.random.nextFloat() * 2.0F - 1.0F) * entityIn.getBbWidth();
                        float rz = (worldIn.random.nextFloat() * 2.0F - 1.0F) * entityIn.getBbWidth();
                        worldIn.addParticle(ParticleTypes.SPLASH, entityIn.getX() + rx, pos.getY() + 0.1f, entityIn.getZ() + rz, v.x + (worldIn.random.nextFloat() - 0.5f) * strength * 20, v.y, v.z + (worldIn.random.nextFloat() - 0.5f) * strength * 20);
                    }
                }
            } else if (state.getValue(TRANSIENT)) {
                worldIn.setBlockAndUpdate(pos, state.setValue(TRANSIENT, false));
            }
        }
    }


}
