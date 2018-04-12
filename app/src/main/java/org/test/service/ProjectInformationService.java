package org.test.service;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

/**
 * Created by BORIS on 18.09.2016.
 */
public class ProjectInformationService {

    private static final String PREF_UNIQUE_ID = "UNIQUE_ID";
    private static final String PREF_PROJECT_INSTALLED_VERSION = "PROJECT_INSTALLED_VERSION";
    private final Context context;
    private String uniqueID = null;

    public ProjectInformationService(Context context) {
        this.context = context;
    }

    public synchronized String id() {
        if (uniqueID == null) {
            uniqueID = generateId();
        }

        return uniqueID;
    }

    public Double currentInstalledVersion() {
        SharedPreferences sharedPreferences = get(PREF_PROJECT_INSTALLED_VERSION);
        String versionInString = sharedPreferences.getString(PREF_PROJECT_INSTALLED_VERSION, "1.0");

        return Double.valueOf(versionInString);
    }

    public void installVersion(Double version) {
        SharedPreferences sharedPreferences = get(PREF_PROJECT_INSTALLED_VERSION);
        set(sharedPreferences, PREF_PROJECT_INSTALLED_VERSION, version.toString());
    }

    private String generateId() {
        String uniqueID;
        SharedPreferences sharedPrefs = get(PREF_UNIQUE_ID);
        uniqueID = sharedPrefs.getString(PREF_UNIQUE_ID, null);
        if (uniqueID == null) {
            uniqueID = generateUID();
            set(sharedPrefs, uniqueID, PREF_UNIQUE_ID);
        }

        return uniqueID;
    }

    private void set(SharedPreferences sharedStore, String value, String key) {
        SharedPreferences.Editor editor = sharedStore.edit();
        editor.putString(key, value);
        editor.commit();
    }

    private SharedPreferences get(String key) {
        return context.getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    private String generateUID() {
        return UUID.randomUUID().toString().substring(0, 7).replace("-", "");
    }

}
