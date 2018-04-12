package org.test.service.http;

import android.content.res.AssetManager;
import android.util.Log;

import org.test.service.FolderService;
import org.test.settings.Settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by BORIS on 16.08.2016.
 */
public class WebServer extends NanoHTTPD {

    private static final String TAG = WebServer.class.getName() + ":";

    private final String originLocation;
    private final FolderService folderService;
    private final AssetManager assetManager;


    public WebServer(Settings project, FolderService folderService, AssetManager assetManager) {
        super(project.getHttpPort());

        this.folderService = folderService;
        this.originLocation = project.getUrl() + "/";
        this.assetManager = assetManager;
    }

    private static String readFileExtension(String fileName, String separator) {
        int index = fileName.lastIndexOf(separator) + 1;
        int length = fileName.length();
        String output = fileName.substring(index, length);

        return output;
    }

    private static void modifyResponse(Response response) {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "*");
        response.addHeader("Access-Control-Allow-Methods", "*");
        response.addHeader("Access-Control-Allow-Credentials", "false");
    }

    private static InputStream io(String filePathLocation) {
        try {
            File file = new File(filePathLocation);
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "io: ", e);
        }

        return null;
    }

    private String readName(String fileName) {
        if (fileName.equals(Variables.ROOT)) {
            fileName = folderService.pagesDir() + "index.html";
        }

        String extension = readFileExtension(fileName, File.separator);

        return extension;
    }

    @Override
    public Response serve(IHTTPSession session) {
        String location = readFolder(session.getUri()) + readName(session.getUri());

        if (location.contains(Variables.FAVICON)) {
            return newFixedLengthResponse(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "Not Found");
        }

        String mimeType = readMimeType(session.getUri());
        Response response = newChunkedResponse(Response.Status.OK, mimeType, io(location));
        String message = String.format("uri: %s, mimeType: %s, serve: %s", session.getUri(), mimeType, location);
        Log.d(TAG, message);
        modifyResponse(response);

        return response;
    }

    public boolean in(String text, String... objects) {
        return Arrays.asList(objects, text).contains(text);
    }

    private String readFolder(String fileName) {
        if (fileName.equals(Variables.ROOT)) {
            return folderService.rootDir() + Variables.ROOT;
        }

        String extension = readFileExtension(fileName, Variables.DOT);
        switch (extension) {
            case Variables.HTML:
                return folderService.pagesDir();
            case Variables.CSS:
                return folderService.cssDir();
            case Variables.JS:
                return folderService.jsDir();
            case Variables.TTF:
            case Variables.WOFF:
            case Variables.WOFF2:
                return folderService.fontsDir();
            case Variables.PNG:
            case Variables.JPG:
                return folderService.imagesDir();
        }

        return folderService.rootDir();
    }

    private String readMimeType(String fileName) {
        if (fileName.equals(Variables.ROOT)) {
            return MIME_HTML;
        }

        String extension = readFileExtension(fileName, Variables.DOT);
        switch (extension) {
            case Variables.HTML:
                return Variables.MIME_HTML;
            case Variables.CSS:
                return Variables.MIME_CSS;
            case Variables.JS:
                return Variables.MIME_JS;
            case Variables.TTF:
                return Variables.MIME_FONT3;
            case Variables.WOFF:
                return Variables.MIME_FONT;
            case Variables.WOFF2:
                return Variables.MIME_FONT2;
            case Variables.PNG:
                return Variables.MIME_PNG;
            case Variables.JPG:
                return Variables.MIME_JPG;
        }

        return MIME_PLAINTEXT;
    }

    public interface Variables {

        String DOT = ".";
        String ROOT = "/";

        String FAVICON = "favicon.ico";
        String MIME_CSS = "text/css";
        String MIME_JS = "application/javascript";
        String MIME_FONT = "application/x-font-woff";
        String MIME_FONT2 = "application/font-woff2";
        String MIME_FONT3 = "application/x-font-ttf";
        String MIME_PNG = "image/png";
        String MIME_JPG = "image/jpg";
        String MIME_HTML = "text/html";

        String TTF = "ttf";
        String WOFF = "woff";
        String WOFF2 = "woff2";
        String PNG = "png";
        String JPG = "jpg";
        String HTML = "html";
        String CSS = "css";
        String JS = "js";

    }

}
