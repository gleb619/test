package org.test.app;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import org.test.service.FolderService;
import org.test.service.ProjectInformationService;
import org.test.service.helper.AssetsHelper;
import org.test.service.helper.FolderHelper;
import org.test.service.helper.HelperRegister;
import org.test.service.helper.UnzipHelper;
import org.test.service.http.WebServer;
import org.test.settings.Settings;
import org.test.util.AppUtil;
import org.test.view.fragment.TestPagePlainFragment;

import java.io.IOException;
import java.io.Serializable;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by BORIS on 14.08.2015.
 */
@Module(
        injects = {
                TestPagePlainFragment.class
        },
        complete = false,
        library = true
)
public class MainModule implements Serializable {

    private static final String TAG = AssetsHelper.class.getName() + ":";

    @Provides
    @Singleton
    public Settings provideProjectSettings() {
        return new Settings();
    }

    @Provides
    @Singleton
    public HelperRegister provideHelperRegister(Context context, Settings project,
                                                FolderService folderService,
                                                ProjectInformationService informationService) {
        try {
            HelperRegister helperRegister = new HelperRegister(
                    new FolderHelper(project, folderService),
                    new AssetsHelper(context.getAssets(), folderService),
                    new UnzipHelper(folderService, project, informationService)
            );

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                helperRegister.onWork();
            }

            return helperRegister;
        } catch (IOException e) {
            Log.e(TAG, "Failed to help the project", e);
        }

        return null;
    }

    @Provides
    @Singleton
    public WebServer provideServer(Settings project, FolderService folderService, Context context) {
        return new WebServer(project, folderService, context.getAssets());
    }

    @Provides
    @Singleton
    public FolderService provideFolderService(Settings project) {
        return new FolderService(project);
    }

    @Provides
    @Singleton
    public ProjectInformationService provideDeviceInformationService(Context context) {
        return new ProjectInformationService(context);
    }

    @Provides
    public AppUtil provideAppUtil(Context context, Settings project) {
        return new AppUtil(context, project);
    }

}
