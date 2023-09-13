package hantonik.atomic.core.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.Connection
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import kotlin.math.hypot

open class AtomicBlockEntity(type: BlockEntityType<out AtomicBlockEntity>, pos: BlockPos, state: BlockState) : BlockEntity(type, pos, state), IAtomicBlockEntityCallback, IAtomicPacketHandler {
    override fun getUpdatePacket(): Packet<ClientGamePacketListener> = ClientboundBlockEntityDataPacket.create(this)

    override fun onDataPacket(manager: Connection, packet: ClientboundBlockEntityDataPacket?) {
        packet ?: return

        if (packet.tag != null)
            this.load(packet.tag!!)
    }

    override fun getUpdateTag(): CompoundTag = this.saveWithoutMetadata()

    open fun markChunkUnsaved() {
        this.level ?: return

        if (this.level!!.hasChunkAt(this.worldPosition))
            this.level!!.getChunkAt(this.worldPosition).isUnsaved = true
    }

    open fun markDirty() = super.setChanged()

    open fun markDirtyAndDispatch() {
        this.markDirty()

        for (player in this.level!!.players())
            if (player is ServerPlayer)
                if (hypot(player.x - player.z, this.worldPosition.x + 0.5 - this.worldPosition.z + 0.5) < 64)
                    player.connection.send(this.updatePacket)
    }

    open fun playerWithinDistance(player: Player, distanceSq: Double) = !this.isRemoved && this.worldPosition.distToCenterSqr(player.position()) <= distanceSq

    override fun state(): BlockState = this.blockState

    override fun pos(): BlockPos = this.worldPosition

    override fun level() = this.level
}