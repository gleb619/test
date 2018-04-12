package org.test.service.helper;

import android.os.Environment;
import android.util.Log;

import org.test.service.FolderService;
import org.test.service.helper.core.Helper;
import org.test.settings.Settings;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by BORIS on 28.08.2016.
 */
public class FolderHelper implements Helper {

    private static final String TAG = AssetsHelper.class.getName() + ":";

    private final Settings projectSettings;
    private final FolderService folderService;

    public FolderHelper(Settings projectSettings, FolderService folderService) {
        this.projectSettings = projectSettings;
        this.folderService = folderService;
    }

    @Override
    public void onHelp() throws IOException {
        if (!isExternalStorageWritable() || !isExternalStorageReadable()) {
            Log.e(TAG, "Can't get access in file system");
        }

        List<String> folders = Arrays.asList(folderService.rootDir(),
                folderService.imagesDir(),
                folderService.pagesDir(),
                folderService.jsDir(),
                folderService.cssDir(),
                folderService.cacheDir());

        for (String folder : folders) {
            File file = new File(folder);
            if (file.exists()) continue;
            Log.d(TAG, "Prepare to create file " + file.getPath());
            file.mkdirs();
            Log.d(TAG, "File created " + file.getAbsolutePath());
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

}
