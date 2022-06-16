package hantonik.atomiccore.inventory.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import hantonik.atomiccore.utils.Localizable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract class BasicContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {
    protected ResourceLocation bgTexture;
    protected int bgWidth;
    protected int bgHeight;

    public BasicContainerScreen(T container, Inventory inventory, Component title, ResourceLocation bgTexture, int xSize, int ySize) {
        this(container, inventory, title, bgTexture, xSize, ySize, 256, 256);
    }

    public BasicContainerScreen(T container, Inventory inventory, Component title, ResourceLocation bgTexture, int xSize, int ySize, int bgWidth, int bgHeight) {
        super(container, inventory, title);

        this.bgTexture = bgTexture;
        this.bgWidth = bgWidth;
        this.bgHeight = bgHeight;
        this.imageWidth = xSize;
        this.imageHeight = ySize;
    }

    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(stack);
        super.render(stack, mouseX, mouseY, partialTicks);
        this.renderTooltip(stack, mouseX, mouseY);
    }

    protected void renderDefaultBg(PoseStack stack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.bgTexture);

        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        blit(stack, x, y, 0, 0, this.imageWidth, this.imageHeight, this.bgWidth, this.bgHeight);
    }

    protected String text(String key, Object... args) {
        return Localizable.of(key).args(args).buildString();
    }
}
