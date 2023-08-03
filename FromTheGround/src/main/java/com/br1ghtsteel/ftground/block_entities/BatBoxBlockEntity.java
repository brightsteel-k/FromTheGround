package com.br1ghtsteel.ftground.block_entities;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import com.br1ghtsteel.ftground.blocks.BatBoxBlock;
import com.br1ghtsteel.ftground.core.BlockEntityTypesInit;
import com.br1ghtsteel.ftground.core.BlocksInit;
import com.br1ghtsteel.ftground.entity_types.Bat;
import com.google.common.collect.Lists;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class BatBoxBlockEntity extends BlockEntity {

	public static final String MIN_OCCUPATION_TICKS = "MinOccupationTicks";
	public static final String ENTITY_DATA = "EntityData";
	public static final String TICKS_IN_HIVE = "TicksInHive";
	public static final String BEES = "Bees";
	private static final List<String> IGNORED_BAT_TAGS = Arrays.asList("Air", "ArmorDropChances", "ArmorItems", "Brain",
			"CanPickUpLoot", "DeathTime", "FallDistance", "FallFlying", "Fire", "HandDropChances", "HandItems",
			"HurtByTimestamp", "HurtTime", "LeftHanded", "Motion", "NoGravity", "OnGround", "PortalCooldown", "Pos",
			"Rotation", "CannotEnterHiveTicks", "TicksSincePollination", "CropsGrownSincePollination", "HivePos",
			"Passengers", "Leash", "UUID");
	public static final int MAX_OCCUPANTS = 3;
	private static final int MIN_TICKS_BEFORE_REENTERING_HIVE = 400;
	private final List<BatBoxBlockEntity.BatData> stored = Lists.newArrayList();

	public BatBoxBlockEntity(BlockPos blockpos, BlockState blockstate) {
		super(BlockEntityTypesInit.BAT_BOX.get(), blockpos, blockstate);
	}

	public void setChanged() {
		if (this.isFireNearby()) {
			this.emptyAllLivingFromBox((Player) null, this.level.getBlockState(this.getBlockPos()),
					BatBoxBlockEntity.BatReleaseStatus.EMERGENCY);
		}

		super.setChanged();
	}

	public boolean isFireNearby() {
		if (this.level == null) {
			return false;
		} else {
			for (BlockPos blockpos : BlockPos.betweenClosed(this.worldPosition.offset(-1, -1, -1),
					this.worldPosition.offset(1, 1, 1))) {
				if (this.level.getBlockState(blockpos).getBlock() instanceof FireBlock) {
					return true;
				}
			}

			return false;
		}
	}

	public boolean isEmpty() {
		return this.stored.isEmpty();
	}

	public boolean isFull() {
		return this.stored.size() == 3;
	}

	public void emptyAllLivingFromBox(@Nullable Player player, BlockState blockstate,
			BatBoxBlockEntity.BatReleaseStatus status) {
		List<Entity> list = this.releaseAllOccupants(blockstate, status);
		if (player != null) {
			for (Entity entity : list) {
				if (entity instanceof Bat) {
					Bat bat = (Bat) entity;
					if (player.position().distanceToSqr(entity.position()) <= 16.0D) {
						bat.setStayOutOfBoxCountdown(level.random.nextInt(400) + MIN_TICKS_BEFORE_REENTERING_HIVE);
					}
				}
			}
		}

	}

	private List<Entity> releaseAllOccupants(BlockState blockstate, BatBoxBlockEntity.BatReleaseStatus status) {
		List<Entity> list = Lists.newArrayList();
		this.stored.removeIf((p_58766_) -> {
			return releaseOccupant(this.level, this.worldPosition, blockstate, p_58766_, list, status);
		});
		if (!list.isEmpty()) {
			super.setChanged();
		}

		return list;
	}

	public void addOccupant(Entity entity) {
		this.addOccupantWithPresetTicks(entity, 0);
	}

	public int getOccupantCount() {
		return this.stored.size();
	}

	public static int getGuanoLevel(BlockState blockstate) {
		return blockstate.getValue(BatBoxBlock.GUANO_LEVEL);
	}

	public void addOccupantWithPresetTicks(Entity entity, int ticks) {
		if (this.stored.size() < 3) {
			entity.stopRiding();
			entity.ejectPassengers();
			CompoundTag compoundtag = new CompoundTag();
			entity.save(compoundtag);
			this.storeBat(compoundtag, ticks);
			if (this.level != null) {
				BlockPos blockpos = this.getBlockPos();
				this.level.playSound((Player) null, (double) blockpos.getX(), (double) blockpos.getY(),
						(double) blockpos.getZ(), SoundEvents.BEEHIVE_ENTER, SoundSource.BLOCKS, 1.0F, 1.0F);
				this.level.gameEvent(GameEvent.BLOCK_CHANGE, blockpos,
						GameEvent.Context.of(entity, this.getBlockState()));
			}

			entity.discard();
			super.setChanged();
		}
	}

	public void storeBat(CompoundTag tag, int ticksInBox) {
		this.stored.add(new BatBoxBlockEntity.BatData(tag, ticksInBox, level.random.nextInt(1800) + 600));
	}

	private static boolean releaseOccupant(Level level, BlockPos boxPos, BlockState blockstate,
			BatBoxBlockEntity.BatData batData, @Nullable List<Entity> occupants,
			BatBoxBlockEntity.BatReleaseStatus status) {
		if ((level.isDay() || level.isRaining()) && status != BatBoxBlockEntity.BatReleaseStatus.EMERGENCY) {
			return false;
		} else {
			CompoundTag compoundtag = batData.entityData.copy();
			removeIgnoredBatTags(compoundtag);
			compoundtag.put("BoxPos", NbtUtils.writeBlockPos(boxPos));
			compoundtag.putBoolean("NoGravity", true);
			Direction direction = blockstate.getValue(BeehiveBlock.FACING);
			BlockPos blockpos = boxPos.relative(direction);
			boolean flag = !level.getBlockState(blockpos).getCollisionShape(level, blockpos).isEmpty();
			if (flag && status != BatBoxBlockEntity.BatReleaseStatus.EMERGENCY) {
				return false;
			} else {
				Entity entity = EntityType.loadEntityRecursive(compoundtag, level, (e) -> {
					return e;
				});
				if (entity != null) {
					if (!(entity instanceof Bat)) {
						return false;
					} else {
						Bat bat = (Bat) entity;

						if (status == BatBoxBlockEntity.BatReleaseStatus.GUANO_DELIVERED) {
							if (blockstate.is(BlocksInit.BAT_BOX.get())) {
								int i = getGuanoLevel(blockstate);
								if (i < BatBoxBlock.MAX_GUANO_LEVELS) {
									int j = level.random.nextInt(100) == 0 ? 2 : 1;
									if (i + j > BatBoxBlock.MAX_GUANO_LEVELS) {
										--j;
									}

									level.setBlockAndUpdate(boxPos,
											blockstate.setValue(BatBoxBlock.GUANO_LEVEL, Integer.valueOf(i + j)));
								}
							}

							setBatReleaseData(batData.ticksInBox, bat);
							if (occupants != null) {
								occupants.add(bat);
							}

							float f = entity.getBbWidth();
							double d3 = flag ? 0.0D : 0.55D + (double) (f / 2.0F);
							double d0 = (double) boxPos.getX() + 0.5D + d3 * (double) direction.getStepX();
							double d1 = (double) boxPos.getY() + 0.5D - (double) (entity.getBbHeight() / 2.0F);
							double d2 = (double) boxPos.getZ() + 0.5D + d3 * (double) direction.getStepZ();
							entity.moveTo(d0, d1, d2, entity.getYRot(), entity.getXRot());
						}

						level.playSound((Player) null, boxPos, SoundEvents.BEEHIVE_EXIT, SoundSource.BLOCKS, 1.0F,
								1.0F);
						level.gameEvent(GameEvent.BLOCK_CHANGE, boxPos,
								GameEvent.Context.of(entity, level.getBlockState(boxPos)));
						return level.addFreshEntity(entity);
					}
				} else {
					return false;
				}
			}
		}
	}

	static void removeIgnoredBatTags(CompoundTag tag) {
		for (String s : IGNORED_BAT_TAGS) {
			tag.remove(s);
		}

	}

	private static void setBatReleaseData(int flag, Bat bat) {
		int i = bat.getAge();
		if (i < 0) {
			bat.setAge(Math.min(0, i + flag));
		} else if (i > 0) {
			bat.setAge(Math.max(0, i - flag));
		}

		bat.setInLoveTime(Math.max(0, bat.getInLoveTime() - flag));
	}

	private static void tickOccupants(Level level, BlockPos blockpos, BlockState blockstate,
			List<BatBoxBlockEntity.BatData> occupants) {
		boolean flag = false;

		BatBoxBlockEntity.BatData batData;
		for (Iterator<BatBoxBlockEntity.BatData> iterator = occupants.iterator(); iterator
				.hasNext(); ++batData.ticksInBox) {
			batData = iterator.next();
			if (batData.ticksInBox > batData.minOccupationTicks) {
				BatBoxBlockEntity.BatReleaseStatus status = level.random.nextBoolean()
						? BatBoxBlockEntity.BatReleaseStatus.GUANO_DELIVERED
						: BatBoxBlockEntity.BatReleaseStatus.BAT_RELEASED;
				if (releaseOccupant(level, blockpos, blockstate, batData, (List<Entity>) null, status)) {
					flag = true;
					iterator.remove();
				}
			}
		}

		if (flag) {
			setChanged(level, blockpos, blockstate);
		}

	}

	public static void serverTick(Level level, BlockPos blockpos, BlockState blockstate,
			BatBoxBlockEntity blockEntity) {
		tickOccupants(level, blockpos, blockstate, blockEntity.stored);
		if (!blockEntity.stored.isEmpty() && level.getRandom().nextDouble() < 0.005D) {
			double d0 = (double) blockpos.getX() + 0.5D;
			double d1 = (double) blockpos.getY();
			double d2 = (double) blockpos.getZ() + 0.5D;
			level.playSound((Player) null, d0, d1, d2, SoundEvents.BEEHIVE_WORK, SoundSource.BLOCKS, 1.0F, 1.0F);
		}
	}

	public void load(CompoundTag tag) {
		super.load(tag);
		this.stored.clear();
		ListTag listtag = tag.getList("Bats", 10);

		for (int i = 0; i < listtag.size(); ++i) {
			CompoundTag compoundtag = listtag.getCompound(i);
			BatBoxBlockEntity.BatData batData = new BatBoxBlockEntity.BatData(compoundtag.getCompound("EntityData"),
					compoundtag.getInt("TicksInBox"), compoundtag.getInt("MinOccupationTicks"));
			this.stored.add(batData);
		}
	}

	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.put("Bats", this.writeBats());
	}

	public ListTag writeBats() {
		ListTag listtag = new ListTag();

		for (BatBoxBlockEntity.BatData batData : this.stored) {
			CompoundTag compoundtag = batData.entityData.copy();
			compoundtag.remove("UUID");
			CompoundTag compoundtag1 = new CompoundTag();
			compoundtag1.put("EntityData", compoundtag);
			compoundtag1.putInt("TicksInBox", batData.ticksInBox);
			compoundtag1.putInt("MinOccupationTicks", batData.minOccupationTicks);
			listtag.add(compoundtag1);
		}

		return listtag;
	}

	static class BatData {
		final CompoundTag entityData;
		int ticksInBox;
		final int minOccupationTicks;

		BatData(CompoundTag tag, int ticks, int minOccupationTicks) {
			BatBoxBlockEntity.removeIgnoredBatTags(tag);
			this.entityData = tag;
			this.ticksInBox = ticks;
			this.minOccupationTicks = minOccupationTicks;
		}
	}

	public static enum BatReleaseStatus {
		GUANO_DELIVERED, BAT_RELEASED, EMERGENCY;
	}
}
