package com.legacy.aether.item.tool;

import com.legacy.aether.item.AetherItemGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ItemParachute extends Item {

    public ItemParachute(int maxDamage) {
        super(new Settings().maxCount(1).maxDamage(maxDamage).group(AetherItemGroup.AETHER_MISC));
    }

    public ItemParachute() {
        super(new Settings().maxCount(1).group(AetherItemGroup.AETHER_MISC));
    }

    @Override
    public TypedActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
    	/*if(EntityParachute.entityHasRoomForCloud(world, entityplayer))
    	{
			if(this == ItemsAether.golden_parachute)
			{
				itemstack.damageItem(1, entityplayer);
			} 
			else 
			{
				itemstack.stackSize--;
			}

			world.spawnEntityInWorld(new EntityParachute(world, entityplayer, this == ItemsAether.golden_parachute));

	        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
    	}*/

        return super.use(worldIn, playerIn, handIn);
    }

}