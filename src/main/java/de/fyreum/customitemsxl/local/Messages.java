package de.fyreum.customitemsxl.local;

import de.erethon.commons.chat.MessageUtil;
import de.erethon.commons.config.Message;
import de.erethon.commons.config.MessageHandler;
import de.fyreum.customitemsxl.CustomItemsXL;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

public enum Messages implements Message {

    CMD_DEBUG_HELP("cmd.debug.help"),
    CMD_DEBUG_INFO("cmd.debug.info"),
    CMD_DEBUG_CHANGED_MODE("cmd.debug.changedMode"),
    CMD_TEST_HELP("cmd.test.help"),
    CMD_TEST_ITEM_SUCCESS("cmd.test.item.success"),
    CMD_TEST_RECIPE_SUCCESS("cmd.test.recipe.success"),
    CMD_TEST_SKILL_SUCCESS("cmd.test.skill.success"),

    ;

    private final String path;

    Messages(String path) {
        this.path = path;
    }

    @Override
    public String getMessage() {
        if (this.getMessageHandler().getMessage(this) == null) {
            return "Invalid Message at " + getPath();
        }
        return this.getMessageHandler().getMessage(this);
    }
    @Override
    public String getMessage(String... args) {
        if (this.getMessageHandler().getMessage(this, args) == null) {
            return "Invalid Message at " + getPath();
        }
        return ChatColor.translateAlternateColorCodes('&', this.getMessageHandler().getMessage(this, args));
    }

    public void sendMessage(CommandSender sender, String... args) {
        if (this.getMessageHandler().getMessage(this, args) == null) {
            MessageUtil.sendMessage(sender, "Invalid Message at " + getPath());
        } else {
            MessageUtil.sendMessage(sender, this.getMessage(args));
        }
    }

    @Override
    public MessageHandler getMessageHandler() {
        return CustomItemsXL.inst().getMessageHandler();
    }

    @Override
    public String getPath() {
        return path;
    }
}
