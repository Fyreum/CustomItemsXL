package de.fyreum.customitemsxl.command;

import de.erethon.commons.command.DRECommand;
import de.fyreum.customitemsxl.CustomItemsXL;
import de.fyreum.customitemsxl.recipe.IRecipe;
import de.fyreum.customitemsxl.serialization.ConfigSerializationError;
import de.fyreum.customitemsxl.util.Util;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class TestCommand extends DRECommand {

    CustomItemsXL plugin = CustomItemsXL.inst();

    public TestCommand() {
        setCommand("test");
        setMinArgs(2);
        setMaxArgs(Integer.MAX_VALUE);
        setPlayerCommand(true);
        setConsoleCommand(false);
        setHelp("/ci test [item|recipe|skill] [...]");
        setPermission("customitemsxl.cmd.test");
    }

    @Override
    public void onExecute(String[] args, CommandSender sender) {
        Player player = (Player) sender;
        try {
            if (args[1].equalsIgnoreCase("item")) {
                String[] argsCopy = Arrays.copyOfRange(args, 2, args.length);
                String argsString = Util.toString(argsCopy, " ", false);

                ItemStack deserialized = plugin.getItemFactory().deserialize(argsString);
                player.sendMessage(ChatColor.GREEN + "result: " + ChatColor.GRAY + ChatColor.stripColor(deserialized.toString()));

                player.getInventory().addItem(deserialized);
            } else if (args[1].equalsIgnoreCase("recipe")) {
                String[] argsCopy = Arrays.copyOfRange(args, 2, args.length);
                String argsString = Util.toString(argsCopy, " ", false);

                IRecipe recipe = plugin.getRecipeFactory().deserialize(CustomItemsXL.key("" + System.currentTimeMillis()), argsString);
                player.sendMessage(ChatColor.GREEN + "result: " + ChatColor.GRAY + ChatColor.stripColor(recipe.toString()));
            } else if (args[1].equalsIgnoreCase("skill")) {
                player.sendMessage("coming soon");
            } else {
                player.sendMessage(ChatColor.RED + this.getHelp());
            }
        } catch (ConfigSerializationError e) {
            player.sendMessage(ChatColor.RED + e.getMessage());
        }
    }
}
