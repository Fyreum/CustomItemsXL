package de.fyreum.customitemsxl.command;

import de.erethon.commons.chat.MessageUtil;
import de.erethon.commons.command.DRECommand;
import de.fyreum.customitemsxl.CustomItemsXL;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends DRECommand {

    public ReloadCommand() {
        setCommand("reload");
        setAliases("rl");
        setMinArgs(0);
        setMaxArgs(0);
        setConsoleCommand(true);
        setPlayerCommand(true);
        setHelp("/ci reload");
        setPermission("itemsxs.cmd.reload");
    }

    @Override
    public void onExecute(String[] args, CommandSender sender) {
        MessageUtil.sendMessage(sender, "&aReloading...");
        CustomItemsXL.inst().reload();
        MessageUtil.sendMessage(sender, "&2Finished reload!");
    }
}
