package com.cipherforge.engine;

import java.util.logging.Logger;
import java.util.logging.Level;

import com.cipherforge.core.CipherAlgorithm;
import com.cipherforge.core.CipherResult;
import com.cipherforge.registry.CipherRegistry;
import com.cipherforge.ui.InputManager;
import com.cipherforge.ui.MenuOption;
import com.cipherforge.ui.InvalidInputException;

public class Engine {
    private CipherRegistry registry;
    private InputManager inputManager;
    private static final Logger logger = Logger.getLogger(Engine.class.getName());
    private static final String HEADER_SEPARATOR = "=".repeat(60);
    private static final String SECTION_SEPARATOR = "-".repeat(40);
    
    public Engine() {
        registry = new CipherRegistry();
        inputManager = new InputManager();
        
        logger.setLevel(Level.SEVERE);
    }
    
    public void start() {
        displayWelcomeMessage();
        
        boolean continueRunning = true;
        int sessionCount = 0;
        
        while (continueRunning) {
            try {
                MenuOption choice = inputManager.getMenuChoice();
                continueRunning = handleMenuChoice(choice);
                sessionCount++;
                
            } catch (InvalidInputException e) {
                handleInputError(e);
                continueRunning = inputManager.confirmAction("Would you like to try again?");
                
            } catch (Exception e) {
                handleUnexpectedError(e);
                continueRunning = inputManager.confirmAction("An unexpected error occurred. Would you like to continue?");
            }
        }
        
        displayGoodbyeMessage(sessionCount);
        cleanup();
    }
    
    private boolean handleMenuChoice(MenuOption choice) {
        try {
            switch (choice) {
                case ENCRYPT_MESSAGE:
                    return handleEncryption();
                    
                case VIEW_ALGORITHMS:
                    displayAlgorithmInfo();
                    return true;
                    
                case HELP:
                    inputManager.displayHelp();
                    return true;
                    
                case EXIT:
                    return false;
                    
                default:
                    System.err.println("Unknown menu option: " + choice);
                    return true;
            }
            
        } catch (InvalidInputException e) {
            handleInputError(e);
            return inputManager.confirmAction("Would you like to return to the main menu?");
            
        } catch (Exception e) {
            handleUnexpectedError(e);
            return inputManager.confirmAction("An error occurred. Would you like to continue?");
        }
    }
    
    private boolean handleEncryption() throws InvalidInputException {
        try {
            String cipherChoice = inputManager.getCipherChoice(registry);
            CipherAlgorithm selectedCipher = registry.getCipher(cipherChoice);
            
            if (selectedCipher == null) {
                throw new InvalidInputException("Selected cipher is not available.");
            }
            
            String plaintext = inputManager.getPlaintext();
            
            String key = inputManager.getKey(selectedCipher.getName());
            
            System.out.println("\n" + SECTION_SEPARATOR);
            System.out.println("Processing encryption...");
            System.out.println(SECTION_SEPARATOR);
            
            CipherResult result = selectedCipher.encrypt(plaintext, key);
            
            displayResult(result);
            
            boolean encryptAnother = inputManager.confirmAction("Would you like to encrypt another message?");
            if (encryptAnother) {
                return handleEncryption();
            } else {
                return true;
            }
            
        } catch (InvalidInputException e) {
            throw e;
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error during encryption", e);
            throw new InvalidInputException("An unexpected error occurred during encryption: " + e.getMessage());
        }
    }
    
    private void displayAlgorithmInfo() {
        System.out.println("\n" + HEADER_SEPARATOR);
        System.out.println("AVAILABLE CIPHER ALGORITHMS");
        System.out.println(HEADER_SEPARATOR);
        
        registry.displayCipherNames();
        
        System.out.println("\nDetailed Information:");
        System.out.println();
        
        System.out.println("1. Caesar Cipher:");
        System.out.println("   Type: Substitution cipher");
        System.out.println("   Method: Shifts each letter by a fixed number");
        System.out.println("   Key: Integer (-25 to 25)");
        System.out.println("   Security: Low (easily breakable)");
        System.out.println("   Use case: Educational purposes");
        System.out.println();
        
        System.out.println("2. Vigenere Cipher:");
        System.out.println("   Type: Polyalphabetic substitution cipher");
        System.out.println("   Method: Uses keyword for variable shifts");
        System.out.println("   Key: Alphabetic string (1-20 characters)");
        System.out.println("   Security: Medium (stronger than Caesar)");
        System.out.println("   Use case: Historical cryptography");
        System.out.println();
        
        System.out.println("Press Enter to continue...");
        try {
            System.in.read();
        } catch (Exception e) {
        }
    }
    
    private void displayResult(CipherResult result) {
        System.out.println("\n" + HEADER_SEPARATOR);
        System.out.println("ENCRYPTION RESULT");
        System.out.println(HEADER_SEPARATOR);
        
        System.out.println("Algorithm: " + result.getAlgorithm());
        System.out.println("Key: " + maskSensitiveKey(result.getKey()));
        System.out.println("Status: " + (result.isSuccess() ? "SUCCESS" : "FAILED"));
        
        if (result.isSuccess()) {
            System.out.println();
            System.out.println("Original Text: " + truncateForDisplay(result.getCiphertext().length() > 100 ? "..." : ""));
            System.out.println("Encrypted Text: " + result.getCiphertext());
            System.out.println();
            System.out.println("Character Count: " + result.getCiphertext().length());
            
        } else {
            System.out.println();
            System.out.println("Encryption failed!");
            System.out.println("Reason: Invalid key format for selected cipher");
            System.out.println("Please check your key and try again.");
        }
        
        System.out.println(HEADER_SEPARATOR);
    }
    
    private String maskSensitiveKey(String key) {
        if (key == null || key.length() <= 3) {
            return key;
        }
        
        StringBuilder masked = new StringBuilder();
        masked.append(key.charAt(0));
        for (int i = 1; i < key.length() - 1; i++) {
            masked.append('*');
        }
        masked.append(key.charAt(key.length() - 1));
        
        return masked.toString();
    }
    
    private String truncateForDisplay(String text) {
        if (text.length() <= 100) {
            return text;
        }
        return text.substring(0, 97) + "...";
    }
    
    private void handleInputError(InvalidInputException e) {
        System.err.println("\nInput Error: " + e.getMessage());
        logger.log(Level.WARNING, "Input validation error", e);
    }
    
    private void handleUnexpectedError(Exception e) {
        System.err.println("\nUnexpected Error: " + e.getMessage());
        System.err.println("Please report this issue if it persists.");
        logger.log(Level.SEVERE, "Unexpected application error", e);
    }
    
    private void displayWelcomeMessage() {
        System.out.println("\nWelcome to The Cipher Forge");
        System.out.println("Terminal Cryptography Simulator");
    }
    
    private void displayGoodbyeMessage(int sessionCount) {
        System.out.println("\nThank you for using The Cipher Forge!");
    }
    
    private void cleanup() {
        try {
            if (inputManager != null) {
                inputManager.close();
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error during cleanup", e);
        }
    }
}
