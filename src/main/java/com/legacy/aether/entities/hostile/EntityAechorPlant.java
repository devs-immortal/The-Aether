package com.legacy.aether.entities.hostile;

import com.legacy.aether.blocks.BlocksAether;
import com.legacy.aether.entities.EntityTypesAether;
import com.legacy.aether.entities.passive.EntityAetherAnimal;
import com.legacy.aether.entities.projectile.EntityPoisonNeedle;
import com.legacy.aether.item.ItemsAether;
//import com.legacy.aether.world.storage.loot.AetherLootTableList;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.World;

public class EntityAechorPlant extends EntityAetherAnimal implements RangedAttackMob {

    public float sinage;

    public int poisonRemaining, size;

    public EntityAechorPlant(World world) {
        super(EntityTypesAether.AECHOR_PLANT, world);

        this.size = this.random.nextInt(4) + 1;
        this.sinage = this.random.nextFloat() * 6F;
        this.poisonRemaining = this.random.nextInt(4) + 2;

        this.updatePosition(this.getX(), this.getY(), this.getZ());
    }

    public EntityDimensions getSizeForStatus(EntityPose entityPose_1) {
        return EntityDimensions.changing(0.75F + ((float) this.size * 0.125F), 0.5F + ((float) this.size * 0.075F));
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        initAttributes();

        this.goalSelector.add(4, new ProjectileAttackGoal(this, 0.0D, 30, 1.0F));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new FollowTargetGoal<LivingEntity>(this, LivingEntity.class, true));
    }

    @Override
    protected void initAttributes() {
        super.initAttributes();

        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(20.0D);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.hurtTime > 0) {
            this.sinage += 0.9F;
        }

        if (this.getAttacker() != null) {
            this.sinage += 0.3F;
        } else {
            this.sinage += 0.1F;
        }

        if (this.sinage > 3.141593F * 2F) {
            this.sinage -= (3.141593F * 2F);
        }

        if (this.world.getBlockState(this.getBlockPos().down(1)).getBlock() != BlocksAether.aether_grass) {
            this.destroy();
        }
    }

    @Override
    public boolean canSee(Entity entity) {
        double distance = this.distanceTo(entity);

        return distance <= 4.0F && super.canSee(entity);
    }

    @Override
    public boolean canSpawn(WorldAccess worldIn, SpawnReason SpawnReason) {
        return worldIn.getBlockState(this.getBlockPos().down(1)).getBlock() == BlocksAether.aether_grass && this.random.nextInt(400) == 0;
    }

    @Override
    public void attack(LivingEntity targetIn, float arg1) {
        double x = targetIn.getX() - this.getX();
        double z = targetIn.getZ() - this.getZ();
        double y = 0.1D + (Math.sqrt((x * x) + (z * z) + 0.1D) * 0.5D) + ((this.getY() - targetIn.getY()) * 0.25D);

        double distance = 1.5D / Math.sqrt((x * x) + (z * z) + 0.1D);

        EntityPoisonNeedle needle = new EntityPoisonNeedle(this, this.world);

        needle.setVelocity(x * distance, y, z * distance, 0.285F + ((float) y * 0.05F), 1.0F);

        this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.2F / (this.getRandom().nextFloat() * 0.2F + 0.9F));

        this.world.spawnEntity(needle);
    }

    @Override
    public ActionResult interactMob(PlayerEntity playerIn, Hand handIn) {
        ItemStack heldItem = playerIn.getStackInHand(handIn);

        if (heldItem.getItem() == ItemsAether.skyroot_bucket && !playerIn.abilities.creativeMode) {
            heldItem.setCount(heldItem.getCount() - 1);

            if (heldItem.isEmpty()) {
                playerIn.setStackInHand(handIn, new ItemStack(ItemsAether.skyroot_poison_bucket));
            } else if (!playerIn.inventory.insertStack(new ItemStack(ItemsAether.skyroot_poison_bucket))) {
                playerIn.dropItem(new ItemStack(ItemsAether.skyroot_poison_bucket), false);
            }

            return ActionResult.SUCCESS;
        } else {
            return super.interactMob(playerIn, handIn);
        }
    }

    @Override
    public void takeKnockback(float strength, double xRatio, double zRatio) {
        if (this.getHealth() <= 0.0F) {
            super.takeKnockback(strength, xRatio, zRatio);
        }
    }

    @Override
    public void writeCustomDataToTag(CompoundTag compound) {
        super.writeCustomDataToTag(compound);

        compound.putInt("size", this.size);
    }

    @Override
    public void readCustomDataFromTag(CompoundTag compound) {
        super.readCustomDataFromTag(compound);

        this.size = compound.getInt("size");
    }

    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entityIn) {
        return null;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_GENERIC_BIG_FALL;
    }

    @Override
    public Identifier getLootTableId() {
        return null;//AetherLootTableList.ENTITIES_AECHOR_PLANT;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void pushAway(Entity entityIn) {

    }

    @Override
    protected void tickCramming() {

    }

}