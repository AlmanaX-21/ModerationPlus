package me.almana.moderationplus.provider;

import com.hypixel.hytale.math.vector.Transform;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.protocol.packets.worldmap.MapMarker;
import com.hypixel.hytale.server.core.asset.type.gameplay.GameplayConfig;
import com.hypixel.hytale.server.core.asset.type.gameplay.WorldMapConfig;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.util.PositionUtil;
import me.almana.moderationplus.ModerationPlus;

import com.hypixel.hytale.server.core.universe.world.*;
import com.hypixel.hytale.server.core.universe.world.worldmap.*;
import com.hypixel.hytale.server.core.universe.world.worldmap.markers.*;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

// Confirmed Package via javap
import com.hypixel.hytale.server.core.universe.world.worldmap.markers.MapMarkerTracker;

public class VanishedPlayerIconMarkerProvider implements com.hypixel.hytale.server.core.universe.world.worldmap.WorldMapManager.MarkerProvider {

    private final ModerationPlus plugin;

    public VanishedPlayerIconMarkerProvider(ModerationPlus plugin) {
        this.plugin = plugin;
    }

    @Override
    public void update(@Nonnull World world, @Nonnull MapMarkerTracker tracker,
            int radius, int chunkX, int chunkZ) {
        WorldMapConfig worldMapConfig = world.getGameplayConfig().getWorldMapConfig();
        if (!worldMapConfig.isDisplayPlayers())
            return;

        int chunkViewRadiusSq = radius * radius;
        String currentWorldName = world.getName();
        
        for (me.almana.moderationplus.snapshot.VanishedPlayerSnapshot snapshot : plugin.getVanishedSnapshots().values()) {
            if (!snapshot.worldName().equals(currentWorldName)) continue;
            
            // Distance check
            Vector3d pos = snapshot.position();
            int otherChunkX = (int) pos.x >> 5;
            int otherChunkZ = (int) pos.z >> 5;

            int chunkDiffX = otherChunkX - chunkX;
            int chunkDiffZ = otherChunkZ - chunkZ;

            if (chunkDiffX * chunkDiffX + chunkDiffZ * chunkDiffZ > chunkViewRadiusSq) continue;
            
            String markerId = "Player-" + snapshot.uuid().toString();
            String markerLabel = "Player: " + snapshot.username();
            
            Transform t = new Transform(snapshot.position(), new com.hypixel.hytale.math.vector.Vector3f(0, snapshot.yaw(), 0));
            MapMarker marker = new MapMarker(markerId, markerLabel, "Player.png", PositionUtil.toTransformPacket(t), null);
            
            // Confirmed API: trySendMarker(int radius, int chunkX, int chunkZ, MapMarker marker)
            tracker.trySendMarker(radius, chunkX, chunkZ, marker);
        }
    }
}
