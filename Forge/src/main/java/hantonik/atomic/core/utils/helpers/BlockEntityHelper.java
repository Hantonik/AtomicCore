package hantonik.atomic.core.utils.helpers;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Some helpful code for block entities. <p>
 *
 * @author Hantonik
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BlockEntityHelper {
    public static void dispatchToNearbyPlayers(BlockEntity entity) {
        var level = entity.getLevel();

        if (level == null)
            return;

        var packet = entity.getUpdatePacket();

        if (packet == null)
            return;

        var players = level.players();
        var pos = entity.getBlockPos();

        for (var player : players) {
            if (player instanceof ServerPlayer serverPlayer)
                if (isPlayerNearby(serverPlayer.getX(), serverPlayer.getZ(), pos.getX() + 0.5, pos.getZ() + 0.5))
                    serverPlayer.connection.send(packet);
        }
    }

    public static void dispatchToNearbyPlayers(Level level, int x, int y, int z) {
        var entity = level.getBlockEntity(new BlockPos(x, y, z));

        if (entity != null)
            dispatchToNearbyPlayers(entity);
    }

    private static boolean isPlayerNearby(double x1, double z1, double x2, double z2) {
        return Math.hypot(x1 - x2, z1 - z2) < 64;
    }
}