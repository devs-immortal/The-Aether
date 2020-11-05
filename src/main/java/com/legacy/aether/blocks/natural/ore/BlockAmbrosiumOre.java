package com.legacy.aether.blocks.natural.ore;

import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;

public class BlockAmbrosiumOre extends Block {

    public static final BooleanProperty DOUBLE_DROP = BooleanProperty.of("double_drop");

    public BlockAmbrosiumOre() {
        super(FabricBlockSettings.of(Material.STONE).strength(3.0F, 5.0F).sounds(BlockSoundGroup.STONE).build());

        this.setDefaultState(this.getDefaultState().with(DOUBLE_DROP, true));
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> propertyBuilderIn) {
        propertyBuilderIn.add(DOUBLE_DROP);
    }

}