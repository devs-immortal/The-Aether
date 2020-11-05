package com.legacy.aether.client.particle;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ParticlePassiveWhirly extends AetherParticle {

    private float offsetSize;

    public ParticlePassiveWhirly(ClientWorld worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);

        this.velocityX = xSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.05000000074505806D;
        this.velocityY = ySpeedIn + (Math.random() * 2.0D - 1.0D) * 0.05000000074505806D;
        this.velocityZ = zSpeedIn + (Math.random() * 2.0D - 1.0D) * 0.05000000074505806D;
        float f = this.random.nextFloat() * 0.3F + 0.7F;
        this.colorRed = f;
        this.colorGreen = f;
        this.colorBlue = f;
        //this.size = this.random.nextFloat() * this.random.nextFloat() * 6.0F + 1.0F;
        //this.offsetSize = this.size;
        this.maxAge = (int) (8.0D / (Math.random() * 0.8D + 0.3D));
        this.maxAge = (int) ((float) this.maxAge * 2.5F);
        this.maxAge = Math.max(this.maxAge, 1);
        //this.maxAge = (int)(16.0D / ((double)this.rand.nextFloat() * 0.8D + 0.2D)) + 2;
    }

    @Override
    public void buildGeometry(VertexConsumer buffer, Camera context, float partialTicks) {
        float f = ((float) this.age + partialTicks) / (float) this.maxAge * 32.0F;
        f = MathHelper.clamp(f, 0.0F, 1.0F);
        //this.size = this.offsetSize * f;
        super.buildGeometry(buffer, context, partialTicks);
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        if (this.age++ >= this.maxAge) {
            this.markDead();
        }

        //this.setSpriteIndex(7 - this.age * 8 / this.maxAge);
        this.velocityY += 0.004D;
        this.setPos(this.velocityX, this.velocityY, this.velocityZ);
        this.velocityX *= 0.8999999761581421D;
        this.velocityY *= 0.8999999761581421D;
        this.velocityZ *= 0.8999999761581421D;

        if (this.onGround) {
            this.velocityX *= 0.699999988079071D;
            this.velocityZ *= 0.699999988079071D;
        }
    }

}