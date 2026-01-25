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
import com.hypixel.hytale.server.core.modules.accesscontrol.ban.TimedBan;
import com.hypixel.hytale.server.core.modules.accesscontrol.provider.HytaleBanProvider;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.Universe;
import com.hypixel.hytale.server.core.universe.world.World;
import java.util.concurrent.Executor;

import me.almana.moderationplus.ModerationPlus;
import me.almana.moderationplus.storage.Punishment;
import me.almana.moderationplus.storage.StorageManager.PlayerData;
import me.almana.moderationplus.utils.TimeUtils;
import java.util.UUID;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TempBanCommand extends AbstractCommand {

    private final ModerationPlus plugin;
    private final RequiredArg<String> playerArg;
    private final RequiredArg<String> durationArg;
    private final RequiredArg<String> reasonArg;

    public TempBanCommand(ModerationPlus plugin) {
        super("tempban", "Ban a player temporarily");
        this.plugin = plugin;
        this.requirePermission("moderation.tempban");
        this.playerArg = withRequiredArg("player", "Player to ban", (ArgumentType<String>) ArgTypes.STRING);
        this.durationArg = withRequiredArg("duration", "Duration (e.g. 5m, 1h)",
                (ArgumentType<String>) ArgTypes.STRING);
        this.reasonArg = withRequiredArg("reason", "Ban reason", (ArgumentType<String>) ArgTypes.STRING);
        setAllowsExtraArguments(true);
    }

    @Override
    public CompletableFuture<Void> execute(CommandContext ctx) {
        CommandSender sender = ctx.sender();

        String targetName = ctx.get(playerArg);
        String durationStr = ctx.get(durationArg);


        String fullInput = ctx.getInputString();
        String reason = "Banned by an operator.";
        int dIdx = fullInput.toLowerCase().indexOf(" " + durationStr.toLowerCase() + " ");
        if (dIdx != -1) {

            String sub = fullInput.substring(dIdx + durationStr.length() + 2).trim();
            if (!sub.isEmpty())
                reason = sub;
        } else {
            reason = ctx.get(reasonArg);
        }

        String issuerName = (sender instanceof Player) ? sender.getDisplayName() : "Console";

        long duration;
        try {
            duration = TimeUtils.parseDuration(durationStr);
        } catch (IllegalArgumentException e) {
            ctx.sendMessage(Message.raw("Invalid duration format. Use 5m, 1h, 1d, etc.").color(Color.RED));
            return CompletableFuture.completedFuture(null);
        }


        UUID targetUuid = plugin.getStorageManager().getUuidByUsername(targetName);

        if (targetUuid == null) {
            ctx.sendMessage(Message.raw("Cannot resolve UUID for " + targetName).color(Color.RED));
            return CompletableFuture.completedFuture(null);
        }

        me.almana.moderationplus.service.ExecutionContext context = new me.almana.moderationplus.service.ExecutionContext(
                (sender instanceof Player) ? sender.getUuid() : UUID.nameUUIDFromBytes("CONSOLE".getBytes()),
                issuerName,
                me.almana.moderationplus.service.ExecutionContext.ExecutionSource.COMMAND);

        plugin.getModerationService().tempBan(targetUuid, targetName, reason, duration, context).thenAccept(success -> {
            if (success) {
                ctx.sendMessage(
                        Message.raw("Temp-banned " + targetName + " for " + TimeUtils.formatDuration(duration))
                                .color(Color.GREEN));
            } else {
                ctx.sendMessage(Message.raw("Failed to temp-ban " + targetName + " (likely bypassed or already banned).")
                        .color(Color.RED));
            }
        });

        return CompletableFuture.completedFuture(null);
    }
}

