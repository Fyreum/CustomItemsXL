package de.fyreum.customitemsxl.logger;

import de.fyreum.customitemsxl.util.ConsoleColor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

public class ILogger {

    final String PREFIX = ConsoleColor.BLACK + "[" + ConsoleColor.BLUE + "CustomItemsXL" + ConsoleColor.BLACK + "] " + ConsoleColor.RESET;
    final String INFO_PREFIX = "[Info] ";
    final String DEBUG_PREFIX = "[Debug] ";
    final String WARN_PREFIX = "[WARN] ";
    final String ERROR_PREFIX = "[ERROR] ";
    // 1.16+ colors
    final ChatColor INFO_COLOR = ChatColor.of("#9fba84");
    final ChatColor DEBUG_COLOR = ChatColor.of("#897b8f");
    final ChatColor WARN_COLOR = ChatColor.of("#e3492d");
    final ChatColor ERROR_COLOR = ChatColor.of("#991800");

    private DebugMode mode = DebugMode.OFF;

    private String strip(String msg) {
        return ChatColor.stripColor(msg);
    }

    private void send(String msg) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + msg);
    }

    // logs

    public void log(String msg) {
        send(PREFIX + msg);
    }

    public void log(String path, String msg) {
        send(PREFIX + "file: " + path + " | " + msg);
    }

    public void log(String path, String id, String msg) {
        send(PREFIX + "file: " + path + " id: " + id + " | " + msg);
    }

    public void info(String msg) {
        send(ConsoleColor.CYAN + INFO_PREFIX + strip(msg) + ConsoleColor.RESET);
    }

    // debug

    public void debug(DebugMode mode, String msg) {
        if (this.mode.atLeast(mode)) {
            send(ConsoleColor.PURPLE + DEBUG_PREFIX + ConsoleColor.BLACK + strip(msg) + ConsoleColor.RESET);
        }
    }

    public void debug(DebugMode mode, String msg, DebugMode bMode, String bMsg) {
        if (this.mode.atLeast(mode)) {
            send(ConsoleColor.PURPLE + DEBUG_PREFIX + ConsoleColor.BLACK + strip(msg) + ConsoleColor.RESET);
        } else if (this.mode.atLeast(bMode)) {
            send(ConsoleColor.PURPLE + DEBUG_PREFIX + ConsoleColor.BLACK + strip(bMsg) + ConsoleColor.RESET);
        }
    }

    public void debug(DebugMode[] modes, String... msg) {
        if (modes.length > msg.length) {
            throw new IllegalArgumentException("There are not enough messages to debug");
        } else if (modes.length < msg.length) {
            throw new IllegalArgumentException("There are not enough modes to debug");
        }
        for (int i = 0; i < modes.length; i++) {
            if (this.mode.atLeast(modes[i])) {
                send(ConsoleColor.PURPLE + DEBUG_PREFIX + ConsoleColor.BLACK + strip(msg[i]) + ConsoleColor.RESET);
                return;
            }
        }
    }

    public void debugExact(DebugMode mode, String msg) {
        if (this.mode.equals(mode)) {
            send(ConsoleColor.PURPLE + DEBUG_PREFIX + ConsoleColor.BLACK + strip(msg) + ConsoleColor.RESET);
        }
    }

    // warns

    public void warn(String msg) {
        send(ConsoleColor.YELLOW + WARN_PREFIX + strip(msg));
    }

    public void warn(String path, String msg) {
        send(ConsoleColor.YELLOW + WARN_PREFIX + "file: " + path + " | " + strip(msg) + ConsoleColor.RESET);
    }

    public void warn(String path, String id, String msg) {
        send(ConsoleColor.YELLOW + WARN_PREFIX + "file: " + path + " id: " + id + " | " + strip(msg));
    }

    // errors

    public void error(String msg) {
        send(ConsoleColor.RED + ERROR_PREFIX + strip(msg) + ConsoleColor.RESET);
    }

    public void error(String path, String msg) {
        send(ConsoleColor.RED + ERROR_PREFIX + "file: " + path + " | " + strip(msg) + ConsoleColor.RESET);
    }

    public void error(String path, String id, String msg) {
        send(ConsoleColor.RED + ERROR_PREFIX + "file: " + path + " id: " + id + " | " + strip(msg) + ConsoleColor.RESET);
    }

    // configuration methods

    public void setDebugMode(DebugMode mode) {
        this.mode = mode;
    }

    // getter

    public DebugMode getDebugMode() {
        return mode;
    }
}
