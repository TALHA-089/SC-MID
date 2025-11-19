package com.cipherforge.ui;

public enum MenuOption {
    ENCRYPT_MESSAGE(1, "Encrypt a message"),
    VIEW_ALGORITHMS(2, "View available algorithms"),
    HELP(3, "Show help information"),
    EXIT(4, "Exit application");
    
    private final int value;
    private final String description;
    
    MenuOption(int value, String description) {
        this.value = value;
        this.description = description;
    }
    
    public int getValue() {
        return value;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static MenuOption fromValue(int value) {
        for (MenuOption option : MenuOption.values()) {
            if (option.value == value) {
                return option;
            }
        }
        return null;
    }
    
    public static void displayAll() {
        System.out.println("\nMain Menu");
        for (MenuOption option : MenuOption.values()) {
            System.out.println(option.value + ". " + option.description);
        }
        System.out.print("\nSelect an option (1-" + MenuOption.values().length + "): ");
    }
}
