package com.cipherforge.ui;

import java.util.Scanner;
import java.util.InputMismatchException;
import com.cipherforge.registry.CipherRegistry;
import com.cipherforge.validation.KeyValidator;

public class InputManager {
    private final Scanner scanner;
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final String SEPARATOR = "=" + "=".repeat(50);
    
    public InputManager() {
        scanner = new Scanner(System.in);
    }
    
    public MenuOption getMenuChoice() throws InvalidInputException {
        int attempts = 0;
        
        while (attempts < MAX_RETRY_ATTEMPTS) {
            try {
                MenuOption.displayAll();
                
                if (!scanner.hasNextInt()) {
                    scanner.nextLine();
                    throw new InvalidInputException("Please enter a valid number.");
                }
                
                int choice = scanner.nextInt();
                scanner.nextLine();
                
                MenuOption option = MenuOption.fromValue(choice);
                if (option == null) {
                    throw new InvalidInputException("Invalid option. Please select 1-" + MenuOption.values().length);
                }
                
                return option;
                
            } catch (InvalidInputException e) {
                attempts++;
                System.err.println("Error: " + e.getMessage());
                
                if (attempts < MAX_RETRY_ATTEMPTS) {
                    System.out.println("Attempts remaining: " + (MAX_RETRY_ATTEMPTS - attempts));
                    System.out.println();
                } else {
                    throw new InvalidInputException("Maximum retry attempts exceeded. Please restart the application.");
                }
            } catch (InputMismatchException e) {
                attempts++;
                scanner.nextLine();
                System.err.println("Error: Please enter a valid number.");
                
                if (attempts < MAX_RETRY_ATTEMPTS) {
                    System.out.println("Attempts remaining: " + (MAX_RETRY_ATTEMPTS - attempts));
                    System.out.println();
                } else {
                    throw new InvalidInputException("Maximum retry attempts exceeded. Please restart the application.");
                }
            }
        }
        
        throw new InvalidInputException("Unexpected error in menu selection.");
    }
    
    public String getCipherChoice(CipherRegistry registry) throws InvalidInputException {
        int attempts = 0;
        
        while (attempts < MAX_RETRY_ATTEMPTS) {
            try {
                System.out.println("\n" + SEPARATOR);
                System.out.println("CIPHER SELECTION");
                System.out.println(SEPARATOR);
                registry.listAvailableCiphers();
                System.out.print("\nSelect cipher (1-2): ");
                
                if (!scanner.hasNextInt()) {
                    scanner.nextLine();
                    throw new InvalidInputException("Please enter a valid number (1 or 2).");
                }
                
                int choice = scanner.nextInt();
                scanner.nextLine();
                String choiceStr = String.valueOf(choice);
                
                if (!registry.isValidChoice(choiceStr)) {
                    throw new InvalidInputException("Invalid cipher selection. Please choose 1 or 2.");
                }
                
                return choiceStr;
                
            } catch (InvalidInputException e) {
                attempts++;
                System.err.println("Error: " + e.getMessage());
                
                if (attempts < MAX_RETRY_ATTEMPTS) {
                    System.out.println("Attempts remaining: " + (MAX_RETRY_ATTEMPTS - attempts));
                } else {
                    throw new InvalidInputException("Maximum retry attempts exceeded for cipher selection.");
                }
            } catch (InputMismatchException e) {
                attempts++;
                scanner.nextLine();
                System.err.println("Error: Please enter a valid number (1 or 2).");
                
                if (attempts < MAX_RETRY_ATTEMPTS) {
                    System.out.println("Attempts remaining: " + (MAX_RETRY_ATTEMPTS - attempts));
                } else {
                    throw new InvalidInputException("Maximum retry attempts exceeded for cipher selection.");
                }
            }
        }
        
        throw new InvalidInputException("Unexpected error in cipher selection.");
    }
    
    public String getPlaintext() throws InvalidInputException {
        System.out.println("\n" + SEPARATOR);
        System.out.println("TEXT INPUT");
        System.out.println(SEPARATOR);
        System.out.print("Enter text to encrypt: ");
        
        String input = scanner.nextLine().trim();
        
        if (input.isEmpty()) {
            throw new InvalidInputException("Text cannot be empty. Please enter some text to encrypt.");
        }
        
        if (input.length() > 1000) {
            throw new InvalidInputException("Text is too long. Maximum length is 1000 characters.");
        }
        
        return input;
    }
    
    public String getKey(String cipherName) throws InvalidInputException {
        int attempts = 0;
        
        while (attempts < MAX_RETRY_ATTEMPTS) {
            try {
                System.out.println("\n" + SEPARATOR);
                System.out.println("KEY INPUT");
                System.out.println(SEPARATOR);
                
                String prompt;
                if (cipherName.contains("Caesar")) {
                    prompt = "Enter shift value (integer between -25 and 25): ";
                } else if (cipherName.contains("Vigenere")) {
                    prompt = "Enter keyword (letters only, 1-20 characters): ";
                } else {
                    prompt = "Enter key: ";
                }
                
                System.out.print(prompt);
                String key = scanner.nextLine().trim();
                
                if (key.isEmpty()) {
                    throw new InvalidInputException("Key cannot be empty.");
                }
                
                if (cipherName.contains("Caesar")) {
                    if (!KeyValidator.isValidCaesarKey(key)) {
                        throw new InvalidInputException("Invalid Caesar key. Please enter an integer between -25 and 25.");
                    }
                    
                    int shiftValue = Integer.parseInt(key);
                    if (shiftValue < -25 || shiftValue > 25) {
                        throw new InvalidInputException("Shift value must be between -25 and 25.");
                    }
                    
                } else if (cipherName.contains("Vigenere")) {
                    if (!KeyValidator.isValidVigenereKey(key)) {
                        throw new InvalidInputException("Invalid Vigenere key. Please use only letters (a-z, A-Z).");
                    }
                    
                    if (key.length() > 20) {
                        throw new InvalidInputException("Vigenere key is too long. Maximum length is 20 characters.");
                    }
                }
                
                return key;
                
            } catch (InvalidInputException e) {
                attempts++;
                System.err.println("Error: " + e.getMessage());
                
                if (attempts < MAX_RETRY_ATTEMPTS) {
                    System.out.println("Attempts remaining: " + (MAX_RETRY_ATTEMPTS - attempts));
                } else {
                    throw new InvalidInputException("Maximum retry attempts exceeded for key input.");
                }
            } catch (NumberFormatException e) {
                attempts++;
                System.err.println("Error: Please enter a valid integer for Caesar cipher.");
                
                if (attempts < MAX_RETRY_ATTEMPTS) {
                    System.out.println("Attempts remaining: " + (MAX_RETRY_ATTEMPTS - attempts));
                } else {
                    throw new InvalidInputException("Maximum retry attempts exceeded for key input.");
                }
            }
        }
        
        throw new InvalidInputException("Unexpected error in key input.");
    }
    
    public boolean confirmAction(String message) {
        System.out.print("\n" + message + " (y/n): ");
        String response = scanner.nextLine().trim().toLowerCase();
        return response.equals("y") || response.equals("yes");
    }
    
    public void displayHelp() {
        System.out.println("\n" + SEPARATOR);
        System.out.println("HELP INFORMATION");
        System.out.println(SEPARATOR);
        System.out.println("The Cipher Forge - Terminal Cryptography Simulator");
        System.out.println();
        System.out.println("Available Ciphers:");
        System.out.println("Caesar Cipher: Shifts each letter by a fixed number of positions");
        System.out.println("  Key: Integer between -25 and 25");
        System.out.println("  Example: HELLO with key 3 becomes KHOOR");
        System.out.println();
        System.out.println("Vigenere Cipher: Uses a keyword to shift letters variably");
        System.out.println("  Key: Letters only, 1-20 characters");
        System.out.println("  Example: HELLO with key KEY becomes RIJVS");
        System.out.println();
        System.out.println("Features:");
        System.out.println("Preserves non-alphabetic characters");
        System.out.println("Maintains original case");
        System.out.println("Input validation and error handling");
        System.out.println("Maximum text length: 1000 characters");
        System.out.println();
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
    
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
