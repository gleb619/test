package org.test.service.helper;

import android.content.res.AssetManager;
import android.util.Log;

import org.test.service.FolderService;
import org.test.service.helper.core.Helper;
import org.test.util.Objects;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Created by BORIS on 14.08.2016.
 */
public class AssetsHelper implements Helper {

    private static final String TAG = AssetsHelper.class.getName() + ":";

    private final AssetManager assetManager;
    private final FolderService folderService;

    public AssetsHelper(AssetManager assetManager, FolderService folderService) {
        this.assetManager = assetManager;
        this.folderService = folderService;
    }

    @Override
    public void onHelp() throws IOException {
        String[] files = assetManager.list("");
        if (Objects.isNull(files)) {
            return;
        }

        for (String fileName : files) {
            boolean test = hasExtension(fileName);
            if (!test) continue;
            Log.d(TAG, String.format("Work with %s from assets", fileName));
            File file = createFile(fileName);
            if (Objects.nonNull(file)) {
                moveFile(file);
            }
        }
    }

    private void moveFile(File file) {
        InputStream in = null;
        OutputStream out = null;

        try {
            in = assetManager.open(file.getName());
            out = new FileOutputStream(file);
            copyFile(in, out);
            Log.d(TAG, String.format("Successfully replaced file from assets, new path is %s", file.getCanonicalPath()));
        } catch (IOException e) {
            Log.e(TAG, String.format("Failed to copy asset file: %s", file.getName()), e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                Log.e(TAG, String.format("FATAL ERROR#Failed to copy asset file: %s", file.getName()), e);
            }
        }
    }

    private File createFile(String filename) {
        File outFile;
        String extension = parseExtension(filename);

        switch (extension) {
            case Variables.JS:
                outFile = new File(folderService.jsDir(), filename);
                break;
            case Variables.CSS:
                outFile = new File(folderService.cssDir(), filename);
                break;
            case Variables.ZIP:
                outFile = new File(folderService.cacheDir(), filename);
                break;
            default:
                outFile = null;
                Log.w(TAG, "Unknown type of filename, file will be skipped, filename: " + filename);
        }

        if (Objects.nonNull(outFile)) {
            try {
                outFile.createNewFile();
            } catch (IOException e) {
                Log.d(TAG, String.format("ERROR#fileName: %s, ", filename), e);
                outFile = null;
            }
        }

        return outFile;
    }

    private boolean hasExtension(String fileName) {
        return fileName.lastIndexOf('.') > -1;
    }

    private String parseExtension(String fileName) {
        String extension;
        int i = fileName.lastIndexOf('.');
        int p = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (i > p) {
            extension = fileName.substring(i + 1);
        } else {
            extension = "";
        }

        return extension;
    }

    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    private interface Variables extends Serializable {

        String JS = "js";
        String CSS = "css";
        String ZIP = "zip";

    }

}
