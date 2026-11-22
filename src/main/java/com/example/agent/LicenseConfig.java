package com.nimah79.agent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Resolves the fake license contact info and dates from environment
 * variables (preferred) or JVM system properties, falling back to the
 * original hardcoded defaults.
 *
 * Environment variables (JVM flags work too, e.g.
 * -DNEXUS_LICENSE_CONTACT_NAME=Jane):
 *
 *   NEXUS_LICENSE_CONTACT_NAME       contact name
 *   NEXUS_LICENSE_CONTACT_COMPANY    contact company
 *   NEXUS_LICENSE_CONTACT_EMAIL      contact email address
 *   NEXUS_LICENSE_CONTACT_TELEPHONE  contact telephone
 *   NEXUS_LICENSE_CONTACT_COUNTRY    contact country
 *   NEXUS_LICENSE_EFFECTIVE_DATE     effective date, format yyyy-MM-dd
 *   NEXUS_LICENSE_EXPIRATION_DATE    expiration date, format yyyy-MM-dd
 */
public final class LicenseConfig {

    public static final String ENV_CONTACT_NAME = "NEXUS_LICENSE_CONTACT_NAME";
    public static final String ENV_CONTACT_COMPANY = "NEXUS_LICENSE_CONTACT_COMPANY";
    public static final String ENV_CONTACT_EMAIL = "NEXUS_LICENSE_CONTACT_EMAIL";
    public static final String ENV_CONTACT_TELEPHONE = "NEXUS_LICENSE_CONTACT_TELEPHONE";
    public static final String ENV_CONTACT_COUNTRY = "NEXUS_LICENSE_CONTACT_COUNTRY";
    public static final String ENV_EFFECTIVE_DATE = "NEXUS_LICENSE_EFFECTIVE_DATE";
    public static final String ENV_EXPIRATION_DATE = "NEXUS_LICENSE_EXPIRATION_DATE";

    private static final String PREFIX = "[NexusLicenseAgent] ";

    /* Defaults: the original hardcoded license values. */
    private static final String DEFAULT_CONTACT_NAME = "nimah79";
    private static final String DEFAULT_CONTACT_COMPANY = "nimah79";
    private static final String DEFAULT_CONTACT_EMAIL = "info@nimah79.ir";
    private static final String DEFAULT_CONTACT_TELEPHONE = "11011001110";
    private static final String DEFAULT_CONTACT_COUNTRY = "Iran";
    private static final String DEFAULT_EFFECTIVE_DATE = "2015-01-01";
    private static final String DEFAULT_EXPIRATION_DATE = "2038-01-01";

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    private static final String CONTACT_NAME = resolve(ENV_CONTACT_NAME, DEFAULT_CONTACT_NAME);
    private static final String CONTACT_COMPANY = resolve(ENV_CONTACT_COMPANY, DEFAULT_CONTACT_COMPANY);
    private static final String CONTACT_EMAIL = resolve(ENV_CONTACT_EMAIL, DEFAULT_CONTACT_EMAIL);
    private static final String CONTACT_TELEPHONE = resolve(ENV_CONTACT_TELEPHONE, DEFAULT_CONTACT_TELEPHONE);
    private static final String CONTACT_COUNTRY = resolve(ENV_CONTACT_COUNTRY, DEFAULT_CONTACT_COUNTRY);
    private static final long EFFECTIVE_DATE = resolveDate(ENV_EFFECTIVE_DATE, DEFAULT_EFFECTIVE_DATE);
    private static final long EXPIRATION_DATE = resolveDate(ENV_EXPIRATION_DATE, DEFAULT_EXPIRATION_DATE);

    static {
        System.out.println(PREFIX + "License contact info: name=" + CONTACT_NAME
            + ", company=" + CONTACT_COMPANY
            + ", email=" + CONTACT_EMAIL
            + ", telephone=" + CONTACT_TELEPHONE
            + ", country=" + CONTACT_COUNTRY
            + ", effectiveDate=" + new Date(EFFECTIVE_DATE)
            + ", expirationDate=" + new Date(EXPIRATION_DATE));
    }

    private LicenseConfig() {
    }

    public static String contactName() {
        return CONTACT_NAME;
    }

    public static String contactCompany() {
        return CONTACT_COMPANY;
    }

    public static String contactEmail() {
        return CONTACT_EMAIL;
    }

    public static String contactTelephone() {
        return CONTACT_TELEPHONE;
    }

    public static String contactCountry() {
        return CONTACT_COUNTRY;
    }

    /** Effective date as epoch millis, for {@code new java.util.Date(long)}. */
    public static long effectiveDate() {
        return EFFECTIVE_DATE;
    }

    /** Expiration date as epoch millis, for {@code new java.util.Date(long)}. */
    public static long expirationDate() {
        return EXPIRATION_DATE;
    }

    /**
     * Environment variable first, JVM system property as fallback,
     * default when neither is set or the value is blank.
     */
    private static String resolve(String envName, String defaultValue) {
        String value = System.getenv(envName);
        if (isBlank(value)) {
            value = System.getProperty(envName);
        }
        if (isBlank(value)) {
            return defaultValue;
        }
        return value.trim();
    }

    private static long resolveDate(String envName, String defaultDateString) {
        String value = resolve(envName, defaultDateString);
        try {
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            format.setLenient(false);
            return format.parse(value).getTime();
        } catch (ParseException e) {
            System.err.println(PREFIX + "Invalid date for " + envName + ": '" + value
                + "' (expected " + DATE_FORMAT + "), using default " + defaultDateString);
            SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
            format.setLenient(false);
            try {
                return format.parse(defaultDateString).getTime();
            } catch (ParseException ignored) {
                return new Date(2038 - 1900, 1, 1).getTime();
            }
        }
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
