package me.almana.moderationplus.storage;

public record Punishment(
        int id,
        int playerId,
        String type,
        String issuerUuid,
        String reason,
        long createdAt,
        long expiresAt,
        boolean active,
        String extraData) {
}
