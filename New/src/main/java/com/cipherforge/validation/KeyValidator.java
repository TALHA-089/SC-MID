package com.cipherforge.validation;

public class KeyValidator {
    
    public static boolean isValidVigenereKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            return false;
        }
        
        for (int i = 0; i < key.length(); i++) {
            char ch = key.charAt(i);
            if (!Character.isLetter(ch)) {
                return false;
            }
        }
        
        return true;
    }
    
    public static boolean isValidCaesarKey(String key) {
        if (key == null || key.trim().isEmpty()) {
            return false;
        }
        
        try {
            Integer.parseInt(key);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
