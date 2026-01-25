package me.almana.moderationplus.snapshot;

import com.hypixel.hytale.math.vector.Vector3d;
import java.util.UUID;

public record VanishedPlayerSnapshot(
    UUID uuid,
    String username,
    String worldName,
    Vector3d position,
    float yaw
) {}
