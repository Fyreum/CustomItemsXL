package de.fyreum.customitemsxl.command;

import de.erethon.commons.command.DRECommand;
import de.erethon.commons.command.DRECommandCache;
import de.fyreum.customitemsxl.CustomItemsXL;
import de.fyreum.customitemsxl.recipe.IRecipe;
import de.fyreum.customitemsxl.serialization.file.RootFile;
import de.fyreum.customitemsxl.serialization.roots.Root;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CICommandCache extends DRECommandCache implements TabCompleter {

    public static final String LABEL = "customitemsxl";
    CustomItemsXL plugin;

    public DebugCommand debugCommand = new DebugCommand();
    public ItemCommand itemCommand = new ItemCommand();
    public TestCommand testCommand = new TestCommand();
    public RecipeCommand recipeCommand = new RecipeCommand();
    public ReloadCommand reloadCommand = new ReloadCommand();

    public CICommandCache(CustomItemsXL plugin) {
        super(LABEL, plugin);
        this.plugin = plugin;

        addCommand(debugCommand);
        addCommand(itemCommand);
        addCommand(testCommand);
        addCommand(recipeCommand);
        addCommand(reloadCommand);
    }

    @Override
    public void register(JavaPlugin plugin) {
        super.register(plugin);
        plugin.getCommand(CICommandCache.LABEL).setTabCompleter(this);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command unused1, @NotNull String unused2, @NotNull String[] args) {
        List<String> cmds = new ArrayList<>();
        for (DRECommand cmd : getCommands()) {
            if (cmd.getPermission() == null || cmd.getPermission().isEmpty() || sender.hasPermission(cmd.getPermission())) {
                cmds.add(cmd.getCommand());
            }
        }
        List<String> completes = new ArrayList<>();
        String cmd = args[0];

        if(args.length == 1) {
            for(String string : cmds) {
                if(string.toLowerCase().startsWith(cmd)) completes.add(string);
            }
            return completes;
        } else {
            int length = args.length;
            String sub = args[1];

            if (itemCommand.getCommand().equalsIgnoreCase(cmd) || itemCommand.getAliases().contains(cmd)) {
                if (length == 2) {
                    if ("add".startsWith(sub.toLowerCase())) completes.add("add");
                    if ("addHand".startsWith(sub.toLowerCase())) completes.add("addHand");
                    if ("delete".startsWith(sub.toLowerCase())) completes.add("delete");
                    if ("give".startsWith(sub.toLowerCase())) completes.add("give");
                    return completes;
                }
                if (length == 3) {
                    if (itemCommand.isAddCommand(sub) | itemCommand.isAddHandCommand(sub)) {
                        completes.add("<name>");
                        return completes;
                    }
                    if (itemCommand.isGiveCommand(sub) | itemCommand.isDeleteCommand(sub)) {
                        for (Root<ItemStack> root : plugin.getRootFileManager().getItemRoots()) {
                            String id = root.getId();
                            if (id.toLowerCase().startsWith(args[2].toLowerCase())) {
                                completes.add(id);
                            }
                        }
                        return completes;
                    }
                }
                if (length == 4 && (itemCommand.isAddCommand(sub) | itemCommand.isAddHandCommand(sub))) {
                    for (RootFile<Root<ItemStack>> file : plugin.getRootFileManager().getItemRootFiles()) {
                        String f = file.getFile().getName().replace(".yml", "");
                        if (f.toLowerCase().startsWith(args[3].toLowerCase())) {
                            completes.add(f);
                        }
                    }
                    return completes;
                }
                if (length > 4 && (itemCommand.isAddCommand(sub) | itemCommand.isAddHandCommand(sub))) {
                    completes.add("{...}");
                    return completes;
                }
            }
            if (recipeCommand.getCommand().equalsIgnoreCase(cmd) || recipeCommand.getAliases().contains(cmd)) {
                if (length == 2) {
                    if ("add".startsWith(sub.toLowerCase())) completes.add("add");
                    if ("editor".startsWith(sub.toLowerCase())) completes.add("editor");
                    if ("delete".startsWith(sub.toLowerCase())) completes.add("delete");
                    return completes;
                }
                if (length == 3) {
                    if (recipeCommand.isAddCommand(sub)) {
                        completes.add("<name>");
                        return completes;
                    }
                    if (recipeCommand.isEditorCommand(sub) | recipeCommand.isDeleteCommand(sub)) {
                        for (Root<IRecipe> root : plugin.getRootFileManager().getRecipeRoots()) {
                            String id = root.getId();
                            if (id.toLowerCase().startsWith(args[2].toLowerCase())) {
                                completes.add(id);
                            }
                        }
                        return completes;
                    }
                }
                if (length == 4 && (recipeCommand.isAddCommand(sub) | recipeCommand.isEditorCommand(sub))) {
                    for (RootFile<Root<IRecipe>> file : plugin.getRootFileManager().getRecipeRootFiles()) {
                        String f = file.getFile().getName().replace(".yml", "");
                        if (f.toLowerCase().startsWith(args[3].toLowerCase())) {
                            completes.add(f);
                        }
                    }
                    return completes;
                }
                if (length > 4 && recipeCommand.isAddCommand(sub)) {
                    completes.add("{...}");
                    return completes;
                }
            }
            if (testCommand.getCommand().equalsIgnoreCase(cmd) || testCommand.getAliases().contains(cmd)) {
                if (length == 2) {
                    if ("item".startsWith(sub.toLowerCase())) completes.add("item");
                    if ("recipe".startsWith(sub.toLowerCase())) completes.add("recipe");
                    // if ("skill".startsWith(sub.toLowerCase())) completes.add("skill");
                    return completes;
                }
                completes.add("{...}");
                return completes;
            }
        }
        return completes;
    }
}
