package hantonik.atomic.core.inventory.screen.widget.button;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public abstract class AtomicButton extends Button {
    private final ResourceLocation texture;
    private final int textureX;
    private final int textureY;

    protected AtomicButton(ResourceLocation texture, int textureX, int textureY, Function<Builder, Builder> builder, OnPress onPress) {
        this(Component.literal(""), texture, textureX, textureY, builder, onPress);
    }

    protected AtomicButton(Component text, ResourceLocation texture, int textureX, int textureY, Function<Builder, Builder> builder, OnPress onPress) {
        super(builder.apply(builder(text, onPress)));

        this.texture = texture;
        this.textureX = textureX;
        this.textureY = textureY;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, this.texture);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        graphics.blit(this.texture, this.getX(), this.getY(), this.textureX, this.textureY + this.getButtonState() * this.height, this.width, this.height);
    }

    protected abstract int getButtonState();
}
