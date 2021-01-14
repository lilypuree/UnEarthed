package net.oriondevcorgitaco.unearthed.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
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

import java.util.Random;

public class PuddleBlock extends Block {

    public static final VoxelShape PUDDLE_SHAPE = Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 0.1, 16.0);

    public PuddleBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing == Direction.DOWN) {
            if (!facingState.isSolidSide(worldIn, facingPos, Direction.UP)) {
                return Blocks.AIR.getDefaultState();
            }
        }
        return stateIn;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return PUDDLE_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, IBlockReader reader, BlockPos pos) {
        return PUDDLE_SHAPE;
    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        super.randomTick(state, worldIn, pos, random);
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.down()).getMaterial().isSolid();
    }
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (entityIn.getEntityWorld().isRemote() && entityIn instanceof PlayerEntity){
            Vector3d v = entityIn.getMotion();
            float strength = MathHelper.sqrt(v.x * v.x * 0.2D + v.y * v.y + v.z * v.z * 0.2D) * 0.2f;
            if (strength > 0.01f){
                entityIn.playSound(SoundEvents.ENTITY_GENERIC_SWIM, strength * 8, 1.0F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.4F);

                for(int j = 0; (float)j < 10.0F + entityIn.getWidth() * 20.0F; ++j) {
                    float rx = (worldIn.rand.nextFloat() * 2.0F - 1.0F) * entityIn.getWidth();
                    float rz = (worldIn.rand.nextFloat() * 2.0F - 1.0F) * entityIn.getWidth();
                    worldIn.addParticle(ParticleTypes.SPLASH, entityIn.getPosX() + rx, pos.getY() + 0.1f, entityIn.getPosZ() + rz, v.x + (worldIn.rand.nextFloat() - 0.5f) * strength * 20, v.y, v.z + (worldIn.rand.nextFloat() - 0.5f) * strength * 20);
                }
            }
        }
    }
}
