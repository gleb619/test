package org.test.service;

import org.test.domain.Storage;
import org.test.settings.Settings;

import java.io.File;

/**
 * Created by BORIS on 23.09.2016.
 */
public class FolderService {

    private final Settings projectSettings;
    private final Storage storage;

    //TODO: Create business service interface, add storage there, don't save state in this service
    public FolderService(Settings project) {
        this.projectSettings = project;
        this.storage = new Storage();
        init();
    }

    private void init() {
        String separator = projectSettings.value(Settings.Code.FOLDER_SEPARATOR);
        String root = new StringBuilder()
                .append(projectSettings.value(Settings.Code.FOLDER_EXTERNAL_STORAGE))
                .append(separator)
                .append(projectSettings.value(Settings.Code.FOLDER_PROJECT))
                .toString();

        storage.setRootReference(root);
        storage.setPagesReference(root + separator);
        storage.setJsReference(webServerFolder("static/js", root));
        storage.setCssReference(webServerFolder("static/css", root));
        storage.setImagesReference(webServerFolder("static/img", root));
        storage.setFontsReference(webServerFolder("static/fonts", root));
        storage.setHttpCacheReference(webServerFolder("cache", root));
    }

    public String rootDir() {
        return storage.getRootReference();
    }

    public String pagesDir() {
        return storage.getPagesReference();
    }

    public String jsDir() {
        return storage.getJsReference();
    }

    public String cssDir() {
        return storage.getCssReference();
    }

    public String imagesDir() {
        return storage.getImagesReference();
    }

    public String fontsDir() {
        return storage.getFontsReference();
    }

    public String cacheDir() {
        return storage.getHttpCacheReference();
    }

    private String webServerFolder(String type, String root) {
        return new StringBuilder(root)
                .append(projectSettings.value(Settings.Code.FOLDER_SEPARATOR))
                .append(type)
                .append(projectSettings.value(Settings.Code.FOLDER_SEPARATOR))
                .toString();
    }

    public File[] filesInFolder(String path) {
        return new File(path).listFiles();
    }

    public File rootFolder() {
        return new File(storage.getRootReference());
    }

}
