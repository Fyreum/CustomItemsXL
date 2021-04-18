package de.fyreum.customitemsxl.command;

import de.erethon.commons.chat.MessageUtil;
import de.erethon.commons.command.DRECommand;
import de.fyreum.customitemsxl.CustomItemsXL;
import de.fyreum.customitemsxl.recipe.IRecipe;
import de.fyreum.customitemsxl.recipe.editor.RecipeEditor;
import de.fyreum.customitemsxl.serialization.ConfigSerializationError;
import de.fyreum.customitemsxl.serialization.file.RootFile;
import de.fyreum.customitemsxl.serialization.roots.Root;
import de.fyreum.customitemsxl.util.Util;
import de.fyreum.customitemsxl.util.Validate;
import de.fyreum.customitemsxl.util.ValidationException;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class RecipeCommand extends DRECommand {

    CustomItemsXL plugin = CustomItemsXL.inst();

    public RecipeCommand() {
        setCommand("recipe");
        setAliases("r");
        setMinArgs(1);
        setMaxArgs(Integer.MAX_VALUE);
        setConsoleCommand(false);
        setPlayerCommand(true);
        setHelp("/ci recipe [add|delete|editor] [(key)] [(file)] [...]");
        setPermission("customitemsxl.cmd.recipe");
    }

    @Override
    public void onExecute(String[] args, CommandSender sender) {
        Player player = (Player) sender;
        try {
            String cmd = args[1];
            if (isAddCommand(cmd)) {
                Validate.senderHasPermission(player, "customitemsxl.cmd.recipe.add");

                Validate.minLength(args, 5, getRedHelp());

                String k = args[2];
                NamespacedKey key = CustomItemsXL.key(k);

                String[] argsCopy = Arrays.copyOfRange(args, 4, args.length);
                String argsString = Util.toString(argsCopy, " ");

                IRecipe recipe = plugin.getRecipeFactory().deserialize(key, argsString);
                plugin.addRecipe(recipe);
                plugin.getRootFileManager().addRecipe(args[3].replace(".yml", "") + ".yml", k, recipe);

                MessageUtil.sendMessage(player, ChatColor.GREEN + "result: " + ChatColor.GRAY + ChatColor.stripColor(recipe.toString()));
            } else if (isEditorCommand(cmd)) {
                Validate.senderHasPermission(player, "customitemsxl.cmd.recipe.editor");

                Validate.length(args, 3, 5);

                String id = args[2];
                for (RootFile<Root<IRecipe>> rootFile : plugin.getRootFileManager().getRecipeRootFiles()) {
                    for (Root<IRecipe> root : rootFile.getRoots()) {
                        if (root.getId().equalsIgnoreCase(id)) {
                            RecipeEditor.startSession(rootFile.getFile().getName(), root.getDeserialized(), player);
                            return;
                        }
                    }
                }

                Validate.length(args, 4, 5);
                String file = args[3];

                RecipeEditor.startSession(id, file.replace(".yml", "") + ".yml", player);
            } else if (isDeleteCommand(cmd)) {
                Validate.senderHasPermission(player, "customitemsxl.cmd.recipe.delete");

                Validate.length(args, 3, getRedHelp());

                String id = args[2];

                IRecipe recipe = plugin.getRootFileManager().getMatchingRecipe(id);
                plugin.getRootFileManager().removeRecipe(id);

                MessageUtil.sendMessage(player, ChatColor.GREEN + "deleted: " + ChatColor.GRAY + recipe);
            } else {
                MessageUtil.sendMessage(player, getRedHelp());
            }
        } catch (ConfigSerializationError | ValidationException e) {
            MessageUtil.sendMessage(player, getRedHelp());
        }
    }

    private String getRedHelp() {
        return ChatColor.RED + this.getHelp();
    }

    public boolean isAddCommand(String s) {
        return s.equalsIgnoreCase("add") | s.equalsIgnoreCase("a");
    }

    public boolean isDeleteCommand(String s) {
        return s.equalsIgnoreCase("delete") | s.equalsIgnoreCase("d");
    }

    public boolean isEditorCommand(String s) {
        return s.equalsIgnoreCase("editor") | s.equalsIgnoreCase("e");
    }
}
