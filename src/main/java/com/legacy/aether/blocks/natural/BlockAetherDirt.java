package com.legacy.aether.blocks.natural;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;

public class BlockAetherDirt extends Block {

    public static final BooleanProperty DOUBLE_DROP = BooleanProperty.of("double_drop");

    public BlockAetherDirt() {
        super(FabricBlockSettings.of(Material.SOIL, MaterialColor.ICE).strength(0.2F, -1.0F).sounds(BlockSoundGroup.GRAVEL).build());

        this.setDefaultState(this.getDefaultState().with(DOUBLE_DROP, true));
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> propertyBuilderIn) {
        propertyBuilderIn.add(DOUBLE_DROP);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return super.getPlacementState(context).with(DOUBLE_DROP, false);
    }

}