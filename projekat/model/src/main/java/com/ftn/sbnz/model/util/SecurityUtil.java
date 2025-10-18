package com.ftn.sbnz.model.util;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SecurityUtil {

    private static final String DOMAINS_FILE_PATH = "/temporary_domains.txt";
    private static Set<String> temporaryDomains = Collections.emptySet();

    public SecurityUtil() {}

    static {
        Set<String> domains = new HashSet<>();
        try (InputStream inputStream = SecurityUtil.class.getResourceAsStream(DOMAINS_FILE_PATH)) {

            if (inputStream == null) {
                System.err.println("ERROR: Could not find the temporary domains file: " + DOMAINS_FILE_PATH);
                // In a real application, you might use a proper logger or throw an exception
                // to halt application startup if this file is critical.
            } else {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Normalize the domain: trim whitespace and convert to lowercase
                        String domain = line.trim().toLowerCase();
                        if (!domain.isEmpty()) {
                            domains.add(domain);
                        }
                    }
                    System.out.println("Successfully loaded " + domains.size() + " temporary email domains.");
                }
            }
        } catch (IOException e) {
            System.err.println("ERROR: Failed to read the temporary domains file.");
            e.printStackTrace();
        }

        // Make the set unmodifiable to ensure it's thread-safe and can't be changed at runtime.
        temporaryDomains = Collections.unmodifiableSet(domains);
    }



    public boolean isMailTemporary(String email){
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        int atIndex = email.lastIndexOf('@');
        // Check if '@' exists and is not the last character
        if (atIndex < 0 || atIndex == email.length() - 1) {
            return false;
        }

        // Extract domain, convert to lowercase for case-insensitive comparison
        String domain = email.substring(atIndex + 1).toLowerCase();
        System.out.println("Domain: " + domain);
        System.out.println("Checking if it is temporary email domain: " + temporaryDomains.contains(domain));
        return temporaryDomains.contains(domain);
    }

    public Instant getShortBlockExpiration() {
        // Vraća Instant 30 minuta u budućnosti
        return Instant.now().plus(6, ChronoUnit.HOURS);
    }
}
