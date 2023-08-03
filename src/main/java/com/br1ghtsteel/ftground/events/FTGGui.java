package com.br1ghtsteel.ftground.events;

import java.util.Random;

import com.br1ghtsteel.ftground.FromTheGround;
import com.br1ghtsteel.ftground.core.EffectsInit;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FromTheGround.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public final class FTGGui {

	public static final ResourceLocation FTG_ICONS = new ResourceLocation(FromTheGround.MOD_ID,
			"textures/gui/ftg_icons.png");
	public static final ResourceLocation VANILLA_ICONS = new ResourceLocation("textures/gui/icons.png");

	private static float lastHealth = 0;
	private static long lastHealthTime = 0;
	private static long healthBlinkTime = 0;
	private static int displayHealth = 0;
	private static Random random = new Random();

	@SubscribeEvent
	public static void onGuiOverlay(RenderGuiOverlayEvent.Pre e) {
		if (e.getOverlay() == VanillaGuiOverlay.ARMOR_LEVEL.type()) {
			Minecraft minecraft = Minecraft.getInstance();
			Player player = minecraft.getCameraEntity() instanceof Player ? (Player) minecraft.getCameraEntity() : null;
			if (player != null && player.hasEffect(EffectsInit.CORROSION.get())) {
				ForgeGui gui = ((ForgeGui) minecraft.gui);
				if (!minecraft.options.hideGui && gui.shouldDrawSurvivalElements()) {
					RenderSystem.enableBlend();
					renderCorrodingArmorBar(e.getPoseStack(), e.getWindow().getGuiScaledWidth(),
							e.getWindow().getGuiScaledHeight(), player, gui);
					RenderSystem.disableBlend();
				}
				e.setCanceled(true);
			} else {
				e.setCanceled(false);
			}
		} else if (e.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type()) {
			Minecraft minecraft = Minecraft.getInstance();
			Player player = minecraft.getCameraEntity() instanceof Player ? (Player) minecraft.getCameraEntity() : null;
			if (player != null && player.hasEffect(EffectsInit.CORROSION.get())
					&& !player.hasEffect(MobEffects.WITHER) && player.getArmorValue() == 0) {
				ForgeGui gui = ((ForgeGui) minecraft.gui);
				if (!minecraft.options.hideGui && gui.shouldDrawSurvivalElements()) {
					RenderSystem.enableBlend();
					renderCorrodingHealthBar(e.getPoseStack(), e.getWindow().getGuiScaledWidth(),
							e.getWindow().getGuiScaledHeight(), player, gui);
					RenderSystem.disableBlend();
				}
				e.setCanceled(true);
			} else {
				e.setCanceled(false);
			}
		}
	}

	private static void renderCorrodingArmorBar(PoseStack poseStack, int width, int height, Player player,
			ForgeGui gui) {
		int left = width / 2 - 91;
		int top = height - gui.leftHeight;

		int level = player.getArmorValue();
		RenderSystem.setShaderTexture(0, FTG_ICONS);

		for (int i = 1; level > 0 && i < 20; i += 2) {
			if (i < level) {
				gui.blit(poseStack, left, top, 34, 9, 9, 9);
			} else if (i == level) {
				gui.blit(poseStack, left, top, 25, 9, 9, 9);
			} else if (i > level) {
				gui.blit(poseStack, left, top, 16, 9, 9, 9);
			}
			left += 8;
		}
		gui.leftHeight += 10;
		RenderSystem.setShaderTexture(0, VANILLA_ICONS);
	}

	private static void renderCorrodingHealthBar(PoseStack poseStack, int width, int height, Player player,
			ForgeGui gui) {
		int tickCount = gui.getGuiTicks();
		boolean highlight = healthBlinkTime > (long) tickCount && (healthBlinkTime - (long) tickCount) / 3L % 2L == 1L;
		int health = Mth.ceil(player.getHealth());

		if (health < lastHealth && player.invulnerableTime > 0) {
			lastHealthTime = Util.getMillis();
			healthBlinkTime = (long) (tickCount + 20);
		} else if (health > lastHealth && player.invulnerableTime > 0) {
			lastHealthTime = Util.getMillis();
			healthBlinkTime = (long) (tickCount + 10);
		}

		if (Util.getMillis() - lastHealthTime > 1000L) {
			lastHealth = health;
			displayHealth = health;
			lastHealthTime = Util.getMillis();
		}

		lastHealth = health;
		int healthLast = displayHealth;

		AttributeInstance attrMaxHealth = player.getAttribute(Attributes.MAX_HEALTH);
		float healthMax = Math.max((float) attrMaxHealth.getValue(), Math.max(healthLast, health));
		int absorb = Mth.ceil(player.getAbsorptionAmount());

		int healthRows = Mth.ceil((healthMax + absorb) / 2.0F / 10.0F);
		int rowHeight = Math.max(10 - (healthRows - 2), 3);

		random.setSeed((long) (tickCount * 312871));

		int left = width / 2 - 91;
		int top = height - gui.leftHeight;
		gui.leftHeight += (healthRows * rowHeight);
		if (rowHeight != 10)
			gui.leftHeight += 10 - rowHeight;

		int regen = -1;
		if (player.hasEffect(MobEffects.REGENERATION)) {
			regen = tickCount % Mth.ceil(healthMax + 5.0F);
		}

		RenderSystem.setShaderTexture(0, FTG_ICONS);
		renderHearts(gui, poseStack, player, left, top, rowHeight, regen, healthMax, health, healthLast, absorb,
				highlight);
		RenderSystem.setShaderTexture(0, VANILLA_ICONS);
	}

	protected static void renderHearts(ForgeGui gui, PoseStack poseStack, Player player, int left, int top,
			int rowHeight, int regen, float healthMax, int health, int healthLast, int absorb, boolean highlight) {
		int i = 9 * (player.level.getLevelData().isHardcore() ? 5 : 0);
		int j = Mth.ceil((double) healthMax / 2.0D);
		int k = Mth.ceil((double) absorb / 2.0D);
		int l = j * 2;

		for (int i1 = j + k - 1; i1 >= 0; --i1) {
			int j1 = i1 / 10;
			int k1 = i1 % 10;
			int l1 = left + k1 * 8;
			int i2 = top - j1 * rowHeight;
			if (health + absorb <= 4) {
				i2 += random.nextInt(2);
			}

			if (i1 < j && i1 == regen) {
				i2 -= 2;
			}

			// Empty hearts
			renderHeart(gui, poseStack, 0, l1, i2, i, highlight, false);
			int j2 = i1 * 2;
			boolean flag = i1 >= j;
			if (flag) {
				int k2 = j2 - l;
				if (k2 < absorb) {
					boolean flag1 = k2 + 1 == absorb;
					// Absorbing hearts
					renderHeart(gui, poseStack, 8, l1, i2, i, false, flag1);
				}
			}

			if (highlight && j2 < healthLast) {
				boolean flag2 = j2 + 1 == healthLast;
				// Corroded hearts
				renderHeart(gui, poseStack, 2, l1, i2, i, true, flag2);
			}

			if (j2 < health) {
				boolean flag3 = j2 + 1 == health;
				// Corroded hearts
				renderHeart(gui, poseStack, 2, l1, i2, i, false, flag3);
			}
		}
	}

	private static void renderHeart(ForgeGui gui, PoseStack posestack, int heartType, int x, int y, int v,
			boolean highlight, boolean bouncing) {
		gui.blit(posestack, x, y, getU(heartType, bouncing, highlight), v, 9, 9);
	}

	private static int getU(int base, boolean bouncing, boolean highlight) {
		int u;
		if (base == 0) {
			u = highlight ? 1 : 0;
		} else {
			int j = bouncing ? 1 : 0;
			int k = base == 2 && highlight ? 2 : 0;
			u = j + k;
		}

		return 16 + (base * 2 + u) * 9;
	}
}
