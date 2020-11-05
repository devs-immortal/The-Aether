package com.legacy.aether.blocks.portal;

import com.google.common.cache.LoadingCache;
import com.legacy.aether.api.AetherAPI;
import com.legacy.aether.particle.AetherParticleTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.block.NetherPortalBlock;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.World;

import java.util.Random;

public class BlockAetherPortal extends NetherPortalBlock {

    public BlockAetherPortal() {
        super(FabricBlockSettings.copy(Blocks.NETHER_PORTAL).build());
    }

    public static BlockPattern.Result findPortal(WorldAccess worldIn, BlockPos p_181089_2_) {
        Direction.Axis enumfacing$axis = Direction.Axis.Z;
        AetherPortalSize blockportal$size = new AetherPortalSize(worldIn, p_181089_2_, Direction.Axis.X);
        LoadingCache<BlockPos, CachedBlockPosition> loadingcache = BlockPattern.makeCache(worldIn, true);

        if (!blockportal$size.isValid()) {
            enumfacing$axis = Direction.Axis.X;
            blockportal$size = new AetherPortalSize(worldIn, p_181089_2_, Direction.Axis.Z);
        }

        if (!blockportal$size.isValid()) {
            return new BlockPattern.Result(p_181089_2_, Direction.NORTH, Direction.UP, loadingcache, 1, 1, 1);
        } else {
            int[] aint = new int[Direction.AxisDirection.values().length];
            Direction enumfacing = blockportal$size.rightDir.rotateYCounterclockwise();
            BlockPos blockpos = blockportal$size.bottomLeft.up(blockportal$size.getHeight() - 1);

            for (Direction.AxisDirection enumfacing$axisdirection : Direction.AxisDirection.values()) {
                BlockPattern.Result blockpattern$patternhelper = new BlockPattern.Result(enumfacing.getDirection() == enumfacing$axisdirection ? blockpos : blockpos.offset(blockportal$size.rightDir, blockportal$size.getWidth() - 1), Direction.get(enumfacing$axisdirection, enumfacing$axis), Direction.UP, loadingcache, blockportal$size.getWidth(), blockportal$size.getHeight(), 1);

                for (int i = 0; i < blockportal$size.getWidth(); ++i) {
                    for (int j = 0; j < blockportal$size.getHeight(); ++j) {
                        CachedBlockPosition blockworldstate = blockpattern$patternhelper.translate(i, j, 1);

                        if (blockworldstate.getBlockState() != null && blockworldstate.getBlockState().getMaterial() != Material.AIR) {
                            ++aint[enumfacing$axisdirection.ordinal()];
                        }
                    }
                }
            }

            Direction.AxisDirection enumfacing$axisdirection1 = Direction.AxisDirection.POSITIVE;

            for (Direction.AxisDirection enumfacing$axisdirection2 : Direction.AxisDirection.values()) {
                if (aint[enumfacing$axisdirection2.ordinal()] < aint[enumfacing$axisdirection1.ordinal()]) {
                    enumfacing$axisdirection1 = enumfacing$axisdirection2;
                }
            }

            return new BlockPattern.Result(enumfacing.getDirection() == enumfacing$axisdirection1 ? blockpos : blockpos.offset(blockportal$size.rightDir, blockportal$size.getWidth() - 1), Direction.get(enumfacing$axisdirection1, enumfacing$axis), Direction.UP, loadingcache, blockportal$size.getWidth(), blockportal$size.getHeight(), 1);
        }
    }

    @Override
    public void scheduledTick(BlockState blockState_1, ServerWorld world, BlockPos pos, Random random) {

    }

    @Override
    public void onEntityCollision(BlockState stateIn, World worldIn, BlockPos posIn, Entity entityIn) {
        if (entityIn instanceof PlayerEntity) {
            AetherAPI.get((PlayerEntity) entityIn).setInPortal();
        }
    }

    public static boolean createPortalAt(WorldAccess worldIn, BlockPos pos) {
        AetherPortalSize aetherportal$size = new AetherPortalSize(worldIn, pos, Direction.Axis.X);

        if (aetherportal$size.isValid() && aetherportal$size.portalBlockCount == 0) {
            aetherportal$size.placePortalBlocks();

            return true;
        } else {
            AetherPortalSize aetherportal$size1 = new AetherPortalSize(worldIn, pos, Direction.Axis.Z);

            if (aetherportal$size1.isValid() && aetherportal$size1.portalBlockCount == 0) {
                aetherportal$size1.placePortalBlocks();

                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState blockState_1, Direction direction_1, BlockState blockState_2, WorldAccess WorldAccess_1, BlockPos pos, BlockPos blockPos_2) {
        Direction.Axis direction$Axis_1 = direction_1.getAxis();
        Direction.Axis direction$Axis_2 = blockState_1.get(AXIS);
        boolean boolean_1 = direction$Axis_2 != direction$Axis_1 && direction$Axis_1.isHorizontal();

        return !boolean_1 && blockState_2.getBlock() != this && !(new AetherPortalSize(WorldAccess_1, pos, direction$Axis_2)).wasAlreadyValid() ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(blockState_1, direction_1, blockState_2, WorldAccess_1, pos, blockPos_2);
    }

    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(100) == 0) {
            world.playSound((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, SoundEvents.BLOCK_PORTAL_AMBIENT, SoundCategory.BLOCKS, 0.5F, random.nextFloat() * 0.4F + 0.8F, false);
        }

        for (int int_1 = 0; int_1 < 4; ++int_1) {
            double xCoord = (float) pos.getX() + random.nextFloat();
            double yCoord = (float) pos.getY() + random.nextFloat();
            double zCoord = (float) pos.getZ() + random.nextFloat();
            double xSpeed = ((double) random.nextFloat() - 0.5D) * 0.5D;
            double ySpeed = ((double) random.nextFloat() - 0.5D) * 0.5D;
            double zSpeed = ((double) random.nextFloat() - 0.5D) * 0.5D;
            int offset = random.nextInt(2) * 2 - 1;

            if (world.getBlockState(pos.west()).getBlock() != this && world.getBlockState(pos.east()).getBlock() != this) {
                xCoord = (double) pos.getX() + 0.5D + 0.25D * (double) offset;
                xSpeed = random.nextFloat() * 2.0F * (float) offset;
            } else {
                zCoord = (double) pos.getZ() + 0.5D + 0.25D * (double) offset;
                zSpeed = random.nextFloat() * 2.0F * (float) offset;
            }

            //world.addParticle(AetherParticleTypes.AETHER_PORTAL, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed);
            //net.minecraft.client.MinecraftClient.getInstance().particleManager.addParticle(new ParticleAetherPortal(world, xCoord, yCoord, zCoord, xSpeed, ySpeed, zSpeed));
        }
    }

}