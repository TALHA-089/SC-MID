package com.cipherforge.algorithms;

import com.cipherforge.core.CipherAlgorithm;
import com.cipherforge.core.CipherResult;
import com.cipherforge.validation.KeyValidator;

public class VigenereCipher extends CipherAlgorithm {
    
    public VigenereCipher() {
        super("Vigenere Cipher");
    }
    
    @Override
    public CipherResult encrypt(String plaintext, String key) {
        if (!KeyValidator.isValidVigenereKey(key)) {
            return new CipherResult("", name, key, false);
        }
        
        StringBuilder result = new StringBuilder();
        String upperKey = key.toUpperCase();
        int keyIndex = 0;
        
        for (int i = 0; i < plaintext.length(); i++) {
            char ch = plaintext.charAt(i);
            
            if (Character.isUpperCase(ch)) {
                int shift = upperKey.charAt(keyIndex % upperKey.length()) - 'A';
                int shifted = ((ch - 'A' + shift) % 26);
                result.append((char) ('A' + shifted));
                keyIndex++;
            } else if (Character.isLowerCase(ch)) {
                int shift = upperKey.charAt(keyIndex % upperKey.length()) - 'A';
                int shifted = ((ch - 'a' + shift) % 26);
                result.append((char) ('a' + shifted));
                keyIndex++;
            } else {
                result.append(ch);
            }
        }
        
        return new CipherResult(result.toString(), name, key, true);
    }
}
