package com.cipherforge.registry;

import java.util.HashMap;
import java.util.Iterator;

import com.cipherforge.core.CipherAlgorithm;
import com.cipherforge.algorithms.CaesarCipher;
import com.cipherforge.algorithms.VigenereCipher;

public class CipherRegistry {
    private HashMap<String, CipherAlgorithm> ciphers;
    
    public CipherRegistry() {
        ciphers = new HashMap<String, CipherAlgorithm>();
        initializeCiphers();
    }
    
    private void initializeCiphers() {
        CipherAlgorithm caesar = new CaesarCipher();
        CipherAlgorithm vigenere = new VigenereCipher();
        
        ciphers.put("1", caesar);
        ciphers.put("2", vigenere);
    }
    
    public CipherAlgorithm getCipher(String key) {
        return ciphers.get(key);
    }
    
    public void listAvailableCiphers() {
        System.out.println("Available Cipher Algorithms:");
        System.out.println("1. Caesar Cipher");
        System.out.println("2. Vigenere Cipher");
    }
    
    public boolean isValidChoice(String choice) {
        return ciphers.containsKey(choice);
    }
    
    public void displayCipherNames() {
        Iterator<String> keyIterator = ciphers.keySet().iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            CipherAlgorithm cipher = ciphers.get(key);
            System.out.println(key + ": " + cipher.getName());
        }
    }
}
