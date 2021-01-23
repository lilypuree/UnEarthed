package net.oriondevcorgitaco.unearthed.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
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
import net.oriondevcorgitaco.unearthed.core.UEBlocks;

import java.util.Random;

public class PuddleBlock extends Block {

    public static final VoxelShape PUDDLE_SHAPE = Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 0.1, 16.0);
    public static BooleanProperty TRANSIENT = ModBlockProperties.TRANSIENT;

    public PuddleBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.getStateContainer().getBaseState().with(TRANSIENT, false));
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing == Direction.DOWN) {
            if (!isValidPosition(stateIn, worldIn, currentPos)) {
                return Blocks.AIR.getDefaultState();
            }
        }

        return stateIn;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(TRANSIENT);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return PUDDLE_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos) {
        return VoxelShapes.empty();
    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (worldIn.canBlockSeeSky(pos) && worldIn.isDaytime() || worldIn.getLight(pos) >= 14) {
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
        } else {
            if (random.nextInt(4) == 0 && worldIn.isAreaLoaded(pos, 4)) {
                int range = 1;
                BlockPos randomPos = new BlockPos(pos.getX() - range + random.nextInt(range * 2 + 1), pos.getY() - range + random.nextInt(range * 2 + 1), pos.getZ() - range + random.nextInt(range * 2 + 1));
                Direction direction = Direction.getRandomDirection(random);
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
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (state.get(TRANSIENT)) {
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    public static int getEvaporationDelay(World world, BlockPos pos) {
        float downFall = world.getBiome(pos).getDownfall();
        return ((int) (10 + (0.5 + world.getRandom().nextFloat()) * downFall * 100));
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockState down = worldIn.getBlockState(pos.down());
        return Block.doesSideFillSquare(down.getCollisionShape(worldIn, pos.down()), Direction.UP);
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof PlayerEntity) {
            if (entityIn.getEntityWorld().isRemote) {
                Vector3d v = entityIn.getMotion();
                float strength = MathHelper.sqrt(v.x * v.x * 0.2D + v.y * v.y + v.z * v.z * 0.2D) * 0.2f;
                if (strength > 0.01f) {
                    entityIn.playSound(SoundEvents.ENTITY_GENERIC_SWIM, strength * 8, 1.0F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.4F);

                    for (int j = 0; (float) j < 10.0F + entityIn.getWidth() * 20.0F; ++j) {
                        float rx = (worldIn.rand.nextFloat() * 2.0F - 1.0F) * entityIn.getWidth();
                        float rz = (worldIn.rand.nextFloat() * 2.0F - 1.0F) * entityIn.getWidth();
                        worldIn.addParticle(ParticleTypes.SPLASH, entityIn.getPosX() + rx, pos.getY() + 0.1f, entityIn.getPosZ() + rz, v.x + (worldIn.rand.nextFloat() - 0.5f) * strength * 20, v.y, v.z + (worldIn.rand.nextFloat() - 0.5f) * strength * 20);
                    }
                }
            } else if (state.get(TRANSIENT)) {
                worldIn.setBlockState(pos, state.with(TRANSIENT, false));
            }
        }
    }


}
