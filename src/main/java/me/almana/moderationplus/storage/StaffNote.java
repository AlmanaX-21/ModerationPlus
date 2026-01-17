package me.almana.moderationplus.storage;

public record StaffNote(
        int id,
        int playerId,
        String issuerUuid,
        String message,
        long createdAt) {
}
