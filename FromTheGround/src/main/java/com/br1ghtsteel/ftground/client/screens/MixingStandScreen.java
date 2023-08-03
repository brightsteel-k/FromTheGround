package com.br1ghtsteel.ftground.client.screens;

import com.br1ghtsteel.ftground.FromTheGround;
import com.br1ghtsteel.ftground.menus.MixingStandMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class MixingStandScreen extends AbstractContainerScreen<MixingStandMenu> {
	private static final ResourceLocation MIXING_STAND_LOCATION = new ResourceLocation(FromTheGround.MOD_ID,
			"textures/gui/container/mixing_stand.png");

	private static final int[] SPINNER_COORDS_X = new int[] { 51, 60, 69 };
	private static final int[] SPINNER_COORDS_Y = new int[] { 32, 36, 32 };
	private static final int[] GAUGE_COORDS_X = new int[] { 39, 72, 130 };
	private static final int[] GAUGE_COORDS_Y = new int[] { 51, 58, 51 };

	public MixingStandScreen(MixingStandMenu menu, Inventory inventory, Component component) {
		super(menu, inventory, component);
	}

	protected void init() {
		super.init();
		this.titleLabelX = (this.imageWidth - this.font.width(this.title)) / 2;
	}

	public void render(PoseStack poseStack, int x, int y, float f) {
		this.renderBackground(poseStack);
		super.render(poseStack, x, y, f);
		this.renderTooltip(poseStack, x, y);
	}

	protected void renderBg(PoseStack poseStack, float f, int x, int y) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, MIXING_STAND_LOCATION);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(poseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);

		int tick = this.menu.getMixingTicks();
		if (tick > 0) {
			int j1 = (int) (28.0F * (1.0F - (float) tick / 300.0F));
			if (j1 > 0) {
				this.blit(poseStack, i + 98, j + 16, 176, 0, 9, j1);
			}
		}
		boolean[] currentMixtures = calculateCurrentMixtures(this.menu.getCurrentMixtures());
		for (int m = 0; m < 3; m++) {
			// Spinners
			if (!currentMixtures[m]) {
				this.blit(poseStack, i + SPINNER_COORDS_X[m], j + SPINNER_COORDS_Y[m], 187, 1, 6, 9);
			} else {
				int j1 = ((tick / 2) + (m % 2)) % 3;
				this.blit(poseStack, i + SPINNER_COORDS_X[m], j + SPINNER_COORDS_Y[m], 187, 10 * j1 + 1, 6, 9);
			}
			
			// Acidity levels
			double a = calculateAcidityLevel(this.menu.getTotalAcidityOfSlot(m));
			if (a < 0) continue;
			int y1 = 1;
			if (a >= 100) {
				a = Math.max(Math.min((double)(a - 100) / 4d, 1), 0);
				y1 = 18;
			}
			int j1 = (int)(16 * (1 - a));
			this.blit(poseStack, i + GAUGE_COORDS_X[m], j + GAUGE_COORDS_Y[m] + j1, 194, y1 + j1, 5, (int)(16 * a));
		}
	}

	private static boolean[] calculateCurrentMixtures(int mixtures) {
		boolean[] result = new boolean[3];
		for (int i = 0; i < 3; i++) {
			result[i] = mixtures % 2 == 1;
			mixtures /= 2;
		}
		return result;
	}
	
	private static double calculateAcidityLevel(int totalAcidity) {
		if (totalAcidity >= 100 || totalAcidity < 0) {
			return totalAcidity;
		}
		if (totalAcidity == 62) {
			return 1d;
		} else if (totalAcidity >= 30) {
			return (double)(totalAcidity - 30) / 32d;
		} else if (totalAcidity >= 14) {
			return (double)(totalAcidity - 14) / 16d;
		} else if (totalAcidity >= 6) {
			return (double)(totalAcidity - 6) / 8d;
		} else if (totalAcidity >= 2) {
			return (double)(totalAcidity - 2) / 4d;
		} else {
			return Math.max(Math.min(totalAcidity / 2d, 1), 0);
		}
	}
}
