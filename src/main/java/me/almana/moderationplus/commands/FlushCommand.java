package me.almana.moderationplus.commands;

import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.CommandSender;
import java.awt.Color;
import java.util.concurrent.CompletableFuture;
import me.almana.moderationplus.ModerationPlus;

public class FlushCommand extends AbstractCommand {

    private final ModerationPlus plugin;

    public FlushCommand(ModerationPlus plugin) {
        super("flushdb", "Manually flush the database");
        this.plugin = plugin;
    }

    @Override
    public CompletableFuture<Void> execute(CommandContext ctx) {
        CommandSender sender = ctx.sender();
        if (!sender.hasPermission("moderation.flush")) {
            ctx.sendMessage(Message.raw("You do not have permission to use this command.").color(Color.RED));
            return CompletableFuture.completedFuture(null);
        }

        ctx.sendMessage(Message.raw("Flushing database...").color(Color.YELLOW));

        return CompletableFuture.runAsync(() -> {
            try {
                plugin.getStorageManager().flush();
                ctx.sendMessage(Message.raw("Database flushed successfully.").color(Color.GREEN));
            } catch (Exception e) {
                ctx.sendMessage(Message.raw("Error flushing database: " + e.getMessage()).color(Color.RED));
                e.printStackTrace();
            }
        });
    }
}
