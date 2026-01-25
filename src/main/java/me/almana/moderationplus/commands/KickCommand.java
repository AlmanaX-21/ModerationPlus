package me.almana.moderationplus.commands;

import com.hypixel.hytale.server.core.Message;
import java.awt.Color;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgumentType;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import java.util.concurrent.Executor;

import me.almana.moderationplus.ModerationPlus;
import me.almana.moderationplus.storage.Punishment;
import me.almana.moderationplus.storage.StorageManager.PlayerData;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class KickCommand extends AbstractCommand {

    private final ModerationPlus plugin;
    private final RequiredArg<String> playerArg;
    private final RequiredArg<String> reasonArg;

    public KickCommand(ModerationPlus plugin) {
        super("kick", "Kick a player from the server");
        this.plugin = plugin;
        this.requirePermission("moderation.kick");
        this.playerArg = withRequiredArg("player", "Player to kick", (ArgumentType<String>) ArgTypes.STRING);
        this.reasonArg = withRequiredArg("reason", "Kick reason", (ArgumentType<String>) ArgTypes.STRING);
        setAllowsExtraArguments(true);
    }

    @Override
    public CompletableFuture<Void> execute(CommandContext ctx) {
        CommandSender sender = ctx.sender();
        String targetName = ctx.get(playerArg);

        String fullInput = ctx.getInputString();
        String reason = "Kicked by an operator.";
        String cmdPrefix = "kick " + targetName;
        int idx = fullInput.toLowerCase().indexOf(cmdPrefix.toLowerCase());
        if (idx != -1 && fullInput.length() > idx + cmdPrefix.length()) {
            reason = fullInput.substring(idx + cmdPrefix.length()).trim();
        } else {
            reason = ctx.get(reasonArg);
        }
        if (reason == null || reason.isEmpty())
            reason = "Kicked by an operator.";

        String issuerName = (sender instanceof Player) ? sender.getDisplayName() : "Console";
        UUID targetUuid = plugin.getStorageManager().getUuidByUsername(targetName);
        if (targetUuid == null) {
            ctx.sendMessage(Message.raw("Player '" + targetName + "' not found in database.").color(Color.RED));
            return CompletableFuture.completedFuture(null);
        }

        if (sender instanceof Player && targetUuid.equals(sender.getUuid())) {
            ctx.sendMessage(Message.raw("You cannot kick yourself.").color(Color.RED));
            return CompletableFuture.completedFuture(null);
        }

        me.almana.moderationplus.service.ExecutionContext context = new me.almana.moderationplus.service.ExecutionContext(
                (sender instanceof Player) ? sender.getUuid() : UUID.nameUUIDFromBytes("CONSOLE".getBytes()),
                issuerName,
                me.almana.moderationplus.service.ExecutionContext.ExecutionSource.COMMAND);

        plugin.getModerationService().kick(targetUuid, targetName, reason, context).thenAccept(success -> {
            if (success) {
                ctx.sendMessage(Message.raw("Kicked " + targetName).color(Color.GREEN));
            } else {
                ctx.sendMessage(Message.raw("Failed to kick " + targetName + " (player offline or bypassed).")
                        .color(Color.RED));
            }
        });

        return CompletableFuture.completedFuture(null);
    }
}
