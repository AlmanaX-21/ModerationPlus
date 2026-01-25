package me.almana.moderationplus.system;

import com.hypixel.hytale.component.ArchetypeChunk;
import com.hypixel.hytale.component.CommandBuffer;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.component.query.Query;
import com.hypixel.hytale.component.system.tick.EntityTickingSystem;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import me.almana.moderationplus.ModerationPlus;
import me.almana.moderationplus.snapshot.VanishedPlayerSnapshot;

import javax.annotation.Nonnull;
import java.util.UUID;

public class VanishSnapshotSystem extends EntityTickingSystem<EntityStore> {

    private final ModerationPlus plugin;
    private final Query<EntityStore> query;

    public VanishSnapshotSystem(ModerationPlus plugin) {
        this.plugin = plugin;
        this.query = Query.and(
                Player.getComponentType(),
                UUIDComponent.getComponentType(),
                TransformComponent.getComponentType()
        );
    }

    @Nonnull
    @Override
    public Query<EntityStore> getQuery() {
        return query;
    }

    @Override
    public void tick(float dt, int index, @Nonnull ArchetypeChunk<EntityStore> archetypeChunk,
                     @Nonnull Store<EntityStore> store, @Nonnull CommandBuffer<EntityStore> commandBuffer) {

        UUIDComponent uuidComponent = archetypeChunk.getComponent(index, UUIDComponent.getComponentType());
        if (uuidComponent == null) return;

        UUID uuid = uuidComponent.getUuid();

        // Check if player is vanished using local plugin state
        if (!plugin.isVanished(uuid)) {
            // Remove snapshot if it exists (player unvanished)
            plugin.getVanishedSnapshots().remove(uuid);
            return;
        }

        Player player = archetypeChunk.getComponent(index, Player.getComponentType());
        TransformComponent transform = archetypeChunk.getComponent(index, TransformComponent.getComponentType());

        if (player == null || transform == null) return;

        String worldName = player.getWorld().getName();
        Vector3d position = transform.getPosition();
        // Defensive copy of position to ensure thread safety (Vector3d might be mutable or shared)
        Vector3d safePos = new Vector3d(position);
        float yaw = transform.getRotation().getYaw();

        VanishedPlayerSnapshot snapshot = new VanishedPlayerSnapshot(
                uuid,
                player.getDisplayName(),
                worldName,
                safePos,
                yaw
        );

        plugin.getVanishedSnapshots().put(uuid, snapshot);
    }

    @Override
    public boolean isParallel(int archetypeChunkSize, int taskCount) {
        // Run sequentially to ensure thread-safety of the ConcurrentHashMap puts, 
        // though ConcurrentHashMap is safe, strictly parallel systems might need care.
        // Sequential is safer and overhead is low.
        return false;
    }
}
