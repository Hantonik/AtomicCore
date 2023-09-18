package hantonik.atomic.core.inventory.screen.widget.button

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

abstract class AtomicButton protected constructor(text: Component, protected val texture: ResourceLocation, protected val textureX: Int, protected val textureY: Int, builder: (Builder) -> Builder, onPress: OnPress) : Button(builder.invoke(builder(text, onPress))) {
    protected constructor(texture: ResourceLocation, textureX: Int, textureY: Int, builder: (Builder) -> Builder, onPress: OnPress) : this(Component.literal(""), texture, textureX, textureY, builder, onPress)

    override fun renderWidget(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTicks: Float) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader)
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F)
        RenderSystem.setShaderTexture(0, this.texture)
        RenderSystem.enableBlend()
        RenderSystem.defaultBlendFunc()
        RenderSystem.enableDepthTest()

        graphics.blit(this.texture, this.x, this.y, this.textureX, this.textureY + this.getButtonState() * this.height, this.width, this.height)
    }

    protected abstract fun getButtonState() : Int
}