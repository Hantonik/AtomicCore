package hantonik.atomic.core.inventory.screen

import com.mojang.blaze3d.systems.RenderSystem
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
import net.minecraft.client.renderer.GameRenderer
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.player.Inventory
import net.minecraft.world.inventory.AbstractContainerMenu

abstract class AtomicContainerScreen<T : AbstractContainerMenu>(menu: T, inventory: Inventory, title: Component, protected val bgTexture: ResourceLocation, bgWidth: Int, bgHeight: Int, protected val imgWidth: Int, protected val imgHeight: Int) : AbstractContainerScreen<T>(menu, inventory, title) {
    constructor(menu: T, inventory: Inventory, title: Component, bgTexture: ResourceLocation, bgWidth: Int, bgHeight: Int) : this(menu, inventory, title, bgTexture, bgWidth, bgHeight, 256, 256)

    init {
        super.imageWidth = bgWidth
        super.imageHeight = bgHeight
    }

    override fun render(graphics: GuiGraphics, mouseX: Int, mouseY: Int, partialTicks: Float) {
        this.renderBackground(graphics)

        super.render(graphics, mouseX, mouseY, partialTicks)

        this.renderTooltip(graphics, mouseX, mouseY)
    }

    override fun renderBg(graphics: GuiGraphics, partialTicks: Float, mouseX: Int, mouseY: Int) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader)
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F)
        RenderSystem.setShaderTexture(0, this.bgTexture)

        graphics.blit(this.bgTexture, this.guiLeft, this.guiTop, 0.0F, 0.0F, super.imageWidth, super.imageHeight, this.imgWidth, this.imgHeight)
    }
}