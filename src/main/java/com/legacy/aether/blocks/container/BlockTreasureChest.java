package com.legacy.aether.blocks.container;

import com.legacy.aether.Aether;
import com.legacy.aether.blocks.entity.TreasureChestBlockEntity;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.Material;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BlockTreasureChest extends ChestBlock {

    public BlockTreasureChest() {
        super(FabricBlockSettings.of(Material.STONE).strength(-1.0F, -1.0F).build(), null); // TODO: Verify

        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(CHEST_TYPE, ChestType.SINGLE).with(WATERLOGGED, false));
    }

    @Override
    public BlockEntity createBlockEntity(BlockView blockViewIn) {
        return new TreasureChestBlockEntity();
    }

    @Override
    public ActionResult onUse(BlockState stateIn, World worldIn, BlockPos posIn, PlayerEntity playerIn, Hand handIn, BlockHitResult hitResult) {
        if (worldIn.isClient) {
            return ActionResult.SUCCESS;
        }

        BlockEntity entity = worldIn.getBlockEntity(posIn);

        if (entity instanceof TreasureChestBlockEntity) {
            TreasureChestBlockEntity chest = (TreasureChestBlockEntity) entity;

            if (chest.isTreasureLocked()) {
                return chest.unlockTreasure(playerIn, handIn);
            }

            ContainerProviderRegistry.INSTANCE.openContainer(Aether.locate("treasure_chest"), playerIn, (byteBuf) -> byteBuf.writeBlockPos(posIn));

            return ActionResult.SUCCESS;
        }

        return ActionResult.FAIL;
    }

}