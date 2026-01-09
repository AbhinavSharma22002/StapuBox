package com.management.venue.utility;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.management.venue.exceptions.StapuBoxException;
import com.management.venue.logger.BaseLogger;

import jakarta.annotation.PostConstruct;

@Service
public class EncryptionService extends BaseLogger {

    @Value("${stapu.encryption.secret-key}")
    private String salt;

    private final int MIN_HASH_LENGTH = 10;
    private Hashids hashids;

    @PostConstruct
    public void init() {
        // Alphanumeric alphabet as requested
        this.hashids = new Hashids(salt, MIN_HASH_LENGTH, "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890");
    }

    /**
     * Encodes a String value (numeric) into an Alphanumeric Hash.
     * Use this to turn database PKs into "pretty" IDs.
     */
    public String encode(String value) {
        if (value == null || value.isEmpty()) return null;
        try {
            long numericValue = Long.parseLong(value);
            return hashids.encode(numericValue);
        } catch (NumberFormatException e) {
            log.error("Failed to encode: Value {} is not a valid number", value);
            return null; 
        }
    }

    /**
     * Decodes an Alphanumeric Hash back into its original String value.
     */
    public String decode(String hash) {
        if (hash == null || hash.isEmpty()) return null;
        try {
            long[] numbers = hashids.decode(hash);
            if (numbers.length > 0) {
                return String.valueOf(numbers[0]);
            }
            throw new StapuBoxException("Invalid ID format", "400", null);
        } catch (Exception e) {
            log.error("Failed to decode hash: {}", hash);
            throw new StapuBoxException("Failed to decode ID", "400", e);
        }
    }
}