package lilypuree.unearthed.block;

import lilypuree.unearthed.block.properties.ModBlockProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class PuddleBlock extends Block {

    public static final VoxelShape PUDDLE_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 0.1, 16.0);
    public static BooleanProperty TRANSIENT = ModBlockProperties.TRANSIENT;

    public PuddleBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.getStateDefinition().any().setValue(TRANSIENT, false));
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing == Direction.DOWN) {
            if (!canSurvive(stateIn, worldIn, currentPos)) {
                return Blocks.AIR.defaultBlockState();
            }
        }

        return stateIn;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TRANSIENT);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return PUDDLE_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel worldIn, BlockPos pos, Random random) {
        if (worldIn.canSeeSkyFromBelowWater(pos) && worldIn.isDay() || worldIn.getMaxLocalRawBrightness(pos) >= 14) {
            worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        } else {
            if (worldIn.isLoaded(pos)) {
                int range = 1;
                BlockPos randomPos = new BlockPos(pos.getX() - range + random.nextInt(range * 2 + 1), pos.getY() - range + random.nextInt(range * 2 + 1), pos.getZ() - range + random.nextInt(range * 2 + 1));
                Direction direction = Direction.getRandom(random);
                if (!LichenBlock.hasEnoughLichen(worldIn, pos, 6, 3, 2)) {
//                    ((LichenBlock) UEBlocks.LICHEN).spreadToFace(worldIn, randomPos, direction, true);
                }
            }
        }
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, Random rand) {
        if (state.getValue(TRANSIENT)) {
            worldIn.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        }
    }

    public static int getEvaporationDelay(Level world, BlockPos pos) {
        float downFall = world.getBiome(pos).getDownfall();
        return ((int) (10 + (0.5 + world.getRandom().nextFloat()) * downFall * 100));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(TRANSIENT, false);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockState down = worldIn.getBlockState(pos.below());
        return Block.isFaceFull(down.getCollisionShape(worldIn, pos.below()), Direction.UP);
    }

    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn instanceof Player) {
            if (entityIn.getCommandSenderWorld().isClientSide) {
                Vec3 v = entityIn.getDeltaMovement();
                float strength = Mth.sqrt((float) (v.x * v.x * 0.2D + v.y * v.y + v.z * v.z * 0.2D)) * 0.2f;
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
