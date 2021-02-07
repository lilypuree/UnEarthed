package net.lilycorgitaco.unearthed.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.lilycorgitaco.unearthed.core.UEBlocks;

import java.util.Random;

public class PuddleBlock extends Block {

    public static final VoxelShape PUDDLE_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 0.1, 16.0);
    public static BooleanProperty TRANSIENT = ModBlockProperties.TRANSIENT;

    public PuddleBlock(Settings properties) {
        super(properties);
        this.setDefaultState(this.getStateManager().getDefaultState().with(TRANSIENT, false));
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facing, BlockState facingState, WorldAccess worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing == Direction.DOWN) {
            if (!canPlaceAt(stateIn, worldIn, currentPos)) {
                return Blocks.AIR.getDefaultState();
            }
        }

        return stateIn;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TRANSIENT);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView worldIn, BlockPos pos, ShapeContext context) {
        return PUDDLE_SHAPE;
    }

    @Override
    public VoxelShape getSidesShape(BlockState state, BlockView reader, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (worldIn.isSkyVisibleAllowingSea(pos) && worldIn.isDay() || worldIn.getLightLevel(pos) >= 14) {
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
        } else {
            if (random.nextInt(4) == 0) {
                int range = 1;
                BlockPos randomPos = new BlockPos(pos.getX() - range + random.nextInt(range * 2 + 1), pos.getY() - range + random.nextInt(range * 2 + 1), pos.getZ() - range + random.nextInt(range * 2 + 1));
                Direction direction = Direction.random(random);
                if (!LichenBlock.hasEnoughLichen(worldIn, pos, 6, 3, 2)) {
                    BlockState newState = ((LichenBlock) UEBlocks.LICHEN).tryPlaceOnto(state, worldIn, randomPos, direction);
                    if (newState != null) {
                        worldIn.setBlockState(randomPos, newState);
                    }
                }
            }
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (state.get(TRANSIENT)) {
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    public static int getEvaporationDelay(World world, BlockPos pos) {
        float downFall = world.getBiome(pos).getDownfall();
        return ((int) (10 + (0.5 + world.getRandom().nextFloat()) * downFall * 100));
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView worldIn, BlockPos pos) {
        BlockState down = worldIn.getBlockState(pos.down());
        return Block.isFaceFullSquare(down.getCollisionShape(worldIn, pos.down()), Direction.UP);
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof PlayerEntity) {
            if (entityIn.getEntityWorld().isClient) {
                Vec3d v = entityIn.getVelocity();
                float strength = MathHelper.sqrt(v.x * v.x * 0.2D + v.y * v.y + v.z * v.z * 0.2D) * 0.2f;
                if (strength > 0.01f) {
                    entityIn.playSound(SoundEvents.ENTITY_GENERIC_SWIM, strength * 8, 1.0F + (worldIn.random.nextFloat() - worldIn.random.nextFloat()) * 0.4F);

                    for (int j = 0; (float) j < 10.0F + entityIn.getWidth() * 20.0F; ++j) {
                        float rx = (worldIn.random.nextFloat() * 2.0F - 1.0F) * entityIn.getWidth();
                        float rz = (worldIn.random.nextFloat() * 2.0F - 1.0F) * entityIn.getWidth();
                        worldIn.addParticle(ParticleTypes.SPLASH, entityIn.getX() + rx, pos.getY() + 0.1f, entityIn.getZ() + rz, v.x + (worldIn.random.nextFloat() - 0.5f) * strength * 20, v.y, v.z + (worldIn.random.nextFloat() - 0.5f) * strength * 20);
                    }
                }
            } else if (state.get(TRANSIENT)) {
                worldIn.setBlockState(pos, state.with(TRANSIENT, false));
            }
        }
    }


}
