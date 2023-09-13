package hantonik.atomic.core.inventory.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class AtomicContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    protected final ResourceLocation bgTexture;
    protected final int imgWidth;
    protected final int imgHeight;

    public AtomicContainerScreen(T menu, Inventory inventory, Component title, ResourceLocation bgTexture, int bgWidth, int bgHeight) {
        this(menu, inventory, title, bgTexture, bgWidth, bgHeight, 256, 256);
    }

    public AtomicContainerScreen(T menu, Inventory inventory, Component title, ResourceLocation bgTexture, int bgWidth, int bgHeight, int imgWidth, int imgHeight) {
        super(menu, inventory, title);

        super.imageWidth = bgWidth;
        super.imageHeight = bgHeight;

        this.bgTexture = bgTexture;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);

        super.render(graphics, mouseX, mouseY, partialTicks);

        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.bgTexture);

        graphics.blit(this.bgTexture, this.getGuiLeft(), this.getGuiTop(), 0, 0, super.imageWidth, super.imageHeight, this.imgWidth, this.imgHeight);
    }
}
