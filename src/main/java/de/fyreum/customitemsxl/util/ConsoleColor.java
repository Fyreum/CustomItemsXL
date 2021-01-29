package de.fyreum.customitemsxl.util;

public enum ConsoleColor {
    BLACK("\033[30m"),
    RED("\033[31m"),
    GREEN("\033[32m"),
    YELLOW("\033[33m"),
    BLUE("\033[34m"),
    PURPLE("\033[35m"),
    CYAN("\033[36m"),
    WHITE("\033[37m"),
    RESET("\033[0m"),
    BOLD("\033[1m"),
    ITALICS("\033[2m"),
    UNDERLINE("\033[4m"),
    CHECK_MARK("✓"),
    ERROR_MARK("✗");

    private final String ansiColor;

    ConsoleColor(String ansiColor) {
        this.ansiColor = ansiColor;
    }

    public String getAnsiColor() {
        return this.ansiColor;
    }

    @Override
    public String toString() {
        return this.ansiColor;
    }
}
