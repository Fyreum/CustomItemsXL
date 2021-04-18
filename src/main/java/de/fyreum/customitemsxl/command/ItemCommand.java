package de.fyreum.customitemsxl.command;

import de.erethon.commons.chat.MessageUtil;
import de.erethon.commons.command.DRECommand;
import de.fyreum.customitemsxl.CustomItemsXL;
import de.fyreum.customitemsxl.serialization.ConfigSerializationError;
import de.fyreum.customitemsxl.util.Util;
import de.fyreum.customitemsxl.util.Validate;
import de.fyreum.customitemsxl.util.ValidationException;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ItemCommand extends DRECommand {

    CustomItemsXL plugin = CustomItemsXL.inst();

    public ItemCommand() {
        setCommand("item");
        setAliases("i");
        setMinArgs(2);
        setMaxArgs(Integer.MAX_VALUE);
        setConsoleCommand(false);
        setPlayerCommand(true);
        setHelp("/ci item [add|addHand|delete|give] [(key)] [(file)] ({...})");
        setPermission("customitemsxl.cmd.item");
    }

    @Override
    public void onExecute(String[] args, CommandSender sender) {
        Player player = (Player) sender;
        try {
            String cmd = args[1];
            if (isAddCommand(cmd)) {
                Validate.senderHasPermission(player, "customitemsxl.cmd.item.add");

                Validate.minLength(args, 5, getRedHelp());

                String key = args[2];

                String[] argsCopy = Arrays.copyOfRange(args, 4, args.length);
                String argsString = Util.toString(argsCopy, " ");

                ItemStack item = plugin.getItemFactory().deserialize(argsString);
                plugin.getRootFileManager().addItem(args[3], key, item);

                MessageUtil.sendMessage(player, ChatColor.GREEN + "Added: " + ChatColor.GRAY + ChatColor.stripColor(item.toString()));
            } else if (isAddHandCommand(cmd)) {
                Validate.senderHasPermission(player, "customitemsxl.cmd.item.addHand");

                Validate.length(args, 4, getRedHelp());

                String key = args[2];
                ItemStack item = player.getInventory().getItemInMainHand();

                Validate.check(!item.getType().equals(Material.AIR), "Cannot add an empty item");

                plugin.getRootFileManager().addItem(args[3].replace(".yml", "") + ".yml", key, item);

                MessageUtil.sendMessage(player, ChatColor.GREEN + "Added: " + ChatColor.GRAY + ChatColor.stripColor(item.toString()));
            } else if (isGiveCommand(cmd)) {
                Validate.senderHasPermission(player, "customitemsxl.cmd.item.give");

                Validate.length(args, 3, 4, getRedHelp());

                ItemStack item = Validate.notNull(plugin.getRootFileManager().getMatchingItem(args[2]), "Item not found");
                Player target = args.length != 4 ? player : Validate.notNull(Bukkit.getPlayer(args[3]), "Player not found");

                target.getInventory().addItem(item);
            } else if (isDeleteCommand(cmd)) {
                Validate.senderHasPermission(player, "customitemsxl.cmd.item.delete");

                Validate.length(args, 3, getRedHelp());

                String id = args[2];

                ItemStack item = plugin.getRootFileManager().getMatchingItem(id);
                plugin.getRootFileManager().removeItem(id);

                MessageUtil.sendMessage(player, ChatColor.GREEN + "deleted: " + ChatColor.GRAY + item);
            } else {
                MessageUtil.sendMessage(player, getRedHelp());
            }
        } catch (ConfigSerializationError | ValidationException e) {
            MessageUtil.sendMessage(player, ChatColor.RED + e.getMessage());
        }
    }

    private String getRedHelp() {
        return ChatColor.RED + this.getHelp();
    }

    public boolean isAddCommand(String s) {
        return s.equalsIgnoreCase("add") | s.equalsIgnoreCase("a");
    }

    public boolean isAddHandCommand(String s) {
        return s.equalsIgnoreCase("addHand");
    }

    public boolean isDeleteCommand(String s) {
        return s.equalsIgnoreCase("delete") | s.equalsIgnoreCase("d");
    }

    public boolean isGiveCommand(String s) {
        return s.equalsIgnoreCase("give") | s.equalsIgnoreCase("get");
    }
}
