package de.fyreum.customitemsxl.command;

import de.erethon.commons.command.DRECommand;
import de.fyreum.customitemsxl.CustomItemsXL;
import de.fyreum.customitemsxl.local.Messages;
import de.fyreum.customitemsxl.logger.DebugMode;
import org.bukkit.command.CommandSender;

public class DebugCommand extends DRECommand {

    public DebugCommand() {
        setCommand("debug");
        setAliases("d");
        setMinArgs(0);
        setMaxArgs(1);
        setConsoleCommand(true);
        setPlayerCommand(true);
        setHelp("/ci debug [mode]");
        setPermission("itemsxs.cmd.debug");
    }

    @Override
    public void onExecute(String[] args, CommandSender sender) {
        if (args.length == 1) {
            Messages.CMD_DEBUG_INFO.sendMessage(sender, CustomItemsXL.LOGGER.getDebugMode().name());
        } else {
            DebugMode mode = DebugMode.getByString(args[1]);
            if (mode == null) {
                CustomItemsXL.LOGGER.debug(
                        DebugMode.TOO_MUCH,
                        "Couldn't find an matching " + DebugMode.class.getSimpleName() + ", sending help message from: " + Messages.CMD_DEBUG_HELP.getPath(),
                        DebugMode.EXTENDED,
                        "Couldn't find an matching " + DebugMode.class.getSimpleName()
                );
                Messages.CMD_DEBUG_HELP.sendMessage(sender);
                return;
            }
            CustomItemsXL.LOGGER.debug(
                    DebugMode.TOO_MUCH,
                    "Matching " + DebugMode.class.getSimpleName() + " found, changing debug mode from" + CustomItemsXL.LOGGER.getDebugMode().name() + " to " + mode.name(),
                    DebugMode.EXTENDED,
                    "Matching " + DebugMode.class.getSimpleName() + " found"
            );
            CustomItemsXL.LOGGER.setDebugMode(mode);
            Messages.CMD_DEBUG_CHANGED_MODE.sendMessage(sender, mode.name());
        }
    }
}
