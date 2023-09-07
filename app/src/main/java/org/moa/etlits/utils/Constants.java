package org.moa.etlits.utils;

public class Constants {
    public enum SyncType {
        CONFIG_DATA,
        ALL_DATA
    }

    public enum SyncStatus {
        IN_PROGRESS,
        STOPPED,
        COMPLETED,
        FAILED
    }

    public static final String USERNAME ="username";
    public static final String PASSWORD = "password";
}
