package org.test.app;


import android.content.Context;

import org.test.view.activity.MainActivity;
import org.test.view.fragment.TestPagePlainFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by BORIS on 26.07.2015.
 */
@Module(
        includes = {
                MainModule.class
        },
        injects = {
                MainActivity.class,
                MainApp.class,
                TestPagePlainFragment.class
        }
)
public class AppModule {

    private final MainApp mainApp;

    public AppModule(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @Provides
    @Singleton
    public MainApp provideApplication() {
        return mainApp;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return mainApp;
    }

}
