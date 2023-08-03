package com.br1ghtsteel.ftground.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.RisingParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SulfurFlameParticle extends RisingParticle
{
	protected SulfurFlameParticle(ClientLevel worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpreadIn, double ySpreadIn, double zSpreadIn) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpreadIn, ySpreadIn, zSpreadIn);
	}
	
	public ParticleRenderType getRenderType() {
		return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
	}

    public void move(double x, double y, double z) {
    	this.setBoundingBox(this.getBoundingBox().move(x, y, z));
    	this.setLocationFromBoundingbox();
    }

   public float getQuadSize(float s) {
      float f = ((float)this.age + s) / (float)this.lifetime;
      return this.quadSize * (1.0F - f * f * 0.5F);
   }

   public int getLightColor(float l) {
      float f = ((float)this.age + l) / (float)this.lifetime;
      f = Mth.clamp(f, 0.0F, 1.0F);
      int i = super.getLightColor(l);
      int j = i & 255;
      int k = i >> 16 & 255;
      j += (int)(f * 15.0F * 16.0F);
      if (j > 240) {
         j = 240;
      }

      return j | k << 16;
   }

   @OnlyIn(Dist.CLIENT)
   public static class Provider implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet sprite;

      public Provider(SpriteSet sprites) {
         this.sprite = sprites;
      }

      public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
         SulfurFlameParticle flameparticle = new SulfurFlameParticle(level, x, y, z, dx, dy, dz);
         flameparticle.pickSprite(this.sprite);
         return flameparticle;
      }
   }
   
   @OnlyIn(Dist.CLIENT)
   public static class SmallFlameProvider implements ParticleProvider<SimpleParticleType> {
      private final SpriteSet sprite;

      public SmallFlameProvider(SpriteSet sprites) {
         this.sprite = sprites;
      }

      public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
         SulfurFlameParticle flameparticle = new SulfurFlameParticle(level, x, y, z, dx, dy, dz);
         flameparticle.pickSprite(this.sprite);
         flameparticle.scale(0.5F);
         return flameparticle;
      }
   }
}
