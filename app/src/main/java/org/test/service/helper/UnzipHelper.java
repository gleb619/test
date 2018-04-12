package org.test.service.helper;

import android.util.Log;

import org.test.service.FolderService;
import org.test.service.ProjectInformationService;
import org.test.service.helper.core.Helper;
import org.test.settings.Settings;
import org.test.util.Objects;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by BORIS on 14.08.2016.
 */
public class UnzipHelper implements Helper {

    private static final String TAG = UnzipHelper.class.getName() + ":";

    private final FolderService folderService;
    private final Settings settings;
    private final ProjectInformationService informationService;

    public UnzipHelper(FolderService folderService, Settings settings, ProjectInformationService informationService) {
        this.folderService = folderService;
        this.settings = settings;
        this.informationService = informationService;
    }

    @Override
    public void onHelp() throws IOException {
        File[] files = folderService.filesInFolder(folderService.cacheDir());
        if (Objects.isNull(files)) {
            return;
        }

        for (File file : files) {
            boolean allowed = checkVersion(file.getName());
            if (allowed) {
                Log.d(TAG, "onHelp: Prepare to unzip file: " + file.getName());
                unzip(file, folderService.rootFolder());
                Log.d(TAG, "onHelp: Successfully unzipped file, prepare to delete: " + file.getName());
                file.delete();
            } else {
                Log.d(TAG, "onHelp: Version of project already installed");
            }
        }
    }

    private boolean checkVersion(String fileName) {
        String[] fileInfo = parseName(fileName);
        if (fileInfo.length != 2) return false;

        Double fileVersion = Double.valueOf(fileInfo[1]);
        Double installedVersion = informationService.currentInstalledVersion();
        int diff = fileVersion.compareTo(installedVersion);
        String isDebug = settings.value(Settings.Code.BUILD_DEBUG);
        boolean output = diff > 0;
        if ("true".equals(isDebug) && !output) {
            output = (diff >= 0);
        }
        if (output) {
            Log.d(TAG, "checkVersion: install new version of project: " + fileVersion);
            informationService.installVersion(fileVersion);
        }

        return output;
    }

    private String[] parseName(String fileName) {
        return fileName
                .replace(".zip", "")
                .split("_");
    }

    public void unzip(File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    fout.close();
                }
            }
        } finally {
            zis.close();
        }
    }

}
