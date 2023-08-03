package com.br1ghtsteel.ftground.entity_types;

import javax.annotation.Nullable;

import com.br1ghtsteel.ftground.core.BlocksInit;
import com.br1ghtsteel.ftground.core.EntityTypesInit;
import com.br1ghtsteel.ftground.util.AnfoExplosionDamageCalculator;
import com.mojang.math.Vector3f;

import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class ExplodingBlock extends Entity {

	private static final EntityDataAccessor<Integer> DATA_FUSE_ID = SynchedEntityData.defineId(ExplodingBlock.class,
			EntityDataSerializers.INT);
	private static final EntityDataAccessor<Integer> DATA_STYLE_ID = SynchedEntityData.defineId(ExplodingBlock.class,
			EntityDataSerializers.INT);
	private BlockState blockstate = BlocksInit.ANFO_CHARGE.get().defaultBlockState();
	private float explosionPower = 4;
	private boolean isVolatile = false;
	@Nullable
	private LivingEntity owner;

	public ExplodingBlock(EntityType<? extends ExplodingBlock> entityType, Level level) {
		super(entityType, level);
		this.blocksBuilding = true;
	}

	public ExplodingBlock(Level level, double x, double y, double z, float explosionPower, int style,
			BlockState blockstate, @Nullable LivingEntity livingEntity) {
		this(EntityTypesInit.EXPLODING_BLOCK.get(), level);
		this.setPos(x, y, z);
		if (style == 1) {
			double d0 = level.random.nextDouble() * (double) ((float) Math.PI * 2F);
			this.setDeltaMovement(-Math.sin(d0) * 0.02D, (double) 0.2F, -Math.cos(d0) * 0.02D);
		} else {
			double d0 = level.random.nextDouble() / 10f;
			this.setDeltaMovement(0D, (double) d0, 0D);
		}
		this.xo = x;
		this.yo = y;
		this.zo = z;
		this.explosionPower = explosionPower;
		this.blockstate = blockstate;
		this.owner = livingEntity;
		this.setStyle(style);
	}

	protected void defineSynchedData() {
		this.entityData.define(DATA_FUSE_ID, 80);
		this.entityData.define(DATA_STYLE_ID, 1);
	}

	protected Entity.MovementEmission getMovementEmission() {
		return Entity.MovementEmission.NONE;
	}

	public boolean isPickable() {
		return !this.isRemoved();
	}

	public void tick() {
		if (!this.isNoGravity()) {
			this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
		}

		this.move(MoverType.SELF, this.getDeltaMovement());
		this.setDeltaMovement(this.getDeltaMovement().scale(0.98D));
		if (this.onGround) {
			this.setDeltaMovement(this.getDeltaMovement().multiply(0.7D, -0.5D, 0.7D));
		}

		int i = this.getFuse() - 1;
		this.setFuse(i);
		if (i <= 0) {
			this.discard();
			if (!this.level.isClientSide) {
				this.explode();
			}
		} else {
			this.updateInWaterStateAndDoFluidPushing();
			if (this.level.isClientSide) {
				switch (getStyle()) {
					case 1:
						this.level.addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 0.0D,
								0.0D, 0.0D);
						break;
					case 2:
						displayDustParticles(new Vector3f(1f, 1f, 1f), 3);
						break;
					case 3:
						displayDustParticles(new Vector3f(232f / 255f, 157f / 255f, 149f / 255f), 3);
						break;
				}
			}
		}
	}

	private void displayDustParticles(Vector3f colour, int count) {
		for (int i = 0; i < count; i++) {
			float dx = this.level.random.nextFloat() * 1.6f - 0.8f;
			float dy = this.level.random.nextFloat() * 1.6f - 0.8f;
			float dz = this.level.random.nextFloat() * 1.6f - 0.8f;

			this.level.addParticle(new DustParticleOptions(colour, 1.0f), this.getX() + dx, this.getY() + 0.5f + dy,
					this.getZ() + dz, 0.0D, 0.0D, 0.0D);
		}
	}

	protected void explode() {
		if (this.explosionPower < 0) {
			this.level.explode(this, (DamageSource) null, AnfoExplosionDamageCalculator.EXPLOSION_DAMAGE_CALCULATOR,
					this.getX(), this.getY(0.0625D), this.getZ(), Mth.abs(this.explosionPower), false,
					Explosion.BlockInteraction.BREAK);
		}
		this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), this.explosionPower,
				Explosion.BlockInteraction.BREAK);
	}

	protected void addAdditionalSaveData(CompoundTag tag) {
		tag.put("BlockState", NbtUtils.writeBlockState(this.blockstate));
		tag.putShort("Particle", (short) this.getStyle());
		tag.putFloat("Power", this.explosionPower);
		tag.putShort("Fuse", (short) this.getFuse());
		tag.putShort("IsVolatile", this.isVolatile ? (short) 1 : (short) 0);
	}

	protected void readAdditionalSaveData(CompoundTag tag) {
		if (tag.contains("BlockState")) {
			this.setBlockstate(NbtUtils.readBlockState(tag.getCompound("BlockState")));
		}
		this.setStyle(tag.getShort("Particle"));
		this.explosionPower = tag.getFloat("Power");
		this.setFuse(tag.getShort("Fuse"));
		this.isVolatile = tag.getBoolean("IsVolatile");
	}

	@Nullable
	public LivingEntity getOwner() {
		return this.owner;
	}

	protected float getEyeHeight(Pose pose, EntityDimensions entityDimensions) {
		return 0.15F;
	}

	public void setFuse(int fuse) {
		this.entityData.set(DATA_FUSE_ID, fuse);
	}

	public int getFuse() {
		return this.entityData.get(DATA_FUSE_ID);
	}

	public void setStyle(int style) {
		this.entityData.set(DATA_STYLE_ID, style);
	}

	public int getStyle() {
		return this.entityData.get(DATA_STYLE_ID);
	}

	public boolean shouldBurstOnExplode() {
		return this.entityData.get(DATA_STYLE_ID) == 1;
	}

	public void setBlockstate(BlockState blockstate) {
		this.blockstate = blockstate;
	}

	public BlockState getBlockstate() {
		return this.blockstate;
	}

	public void setVolatile(boolean isVolatile) {
		this.isVolatile = isVolatile;
	}

	public Packet<?> getAddEntityPacket() {
		return new ClientboundAddEntityPacket(this, Block.getId(this.getBlockstate()));
	}

	@Override
	public void recreateFromPacket(ClientboundAddEntityPacket packet) {
		super.recreateFromPacket(packet);
		this.blockstate = Block.stateById(packet.getData());
		this.blocksBuilding = true;
		double d0 = packet.getX();
		double d1 = packet.getY();
		double d2 = packet.getZ();
		this.setPos(d0, d1, d2);
	}
}
