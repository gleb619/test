package org.test.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import org.test.app.MainApp;
import org.test.service.helper.HelperRegister;
import org.test.service.http.WebServer;
import org.test.settings.Settings;
import org.test.test.R;
import org.test.util.AppUtil;
import org.test.util.Objects;
import org.test.view.activity.core.DefaultActivity;
import org.test.view.fragment.BugReportFragment;
import org.test.view.fragment.TestPagePlainFragment;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import dagger.ObjectGraph;

import static org.test.settings.Settings.Code.HIGH_PERFORMANCE_MODE;

public class MainActivity extends DefaultActivity {

    @Inject
    MainApp mainApp;
    @Inject
    AppUtil appUtil;
    @Inject
    Settings project;
    @Inject
    WebServer server;
    @Inject
    HelperRegister helperRegister;

    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.fragment_container)
    View coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            super.onAfterCreate();
            boolean isGranted = isStoragePermissionGranted();
            if (isGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                helperRegister.onWork(() -> onStart(savedInstanceState));
            } else {
                onStart(savedInstanceState);
            }
        } catch (Exception e) {
            Log.e(TAG, "onCreate: ", e);
        }
    }

    private void onStart(Bundle savedInstanceState) {
        if (Boolean.TRUE.toString().equals(project.value(HIGH_PERFORMANCE_MODE))) {
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                    WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }

        new Task()
                .execute();

        if (savedInstanceState == null) {
            if (Objects.nonNull(server) && server.wasStarted()) {
                selectItemData(Fragments.TEST_PAGE, true);
            } else {
                Toast.makeText(MainActivity.this, "Server hasn't been started yet", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    //TODO: Repair http server, move it to intentService.
    protected void onResume() {
        super.onResume();
        try {
            if (server != null) {
                server.start();
                if (!mainPageOpened()) {
                    selectItemData(Fragments.TEST_PAGE, true);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "ERROR: ", e);
        }
    }

    @Override
    //TODO: Repair http server, move it to intentService.
    protected void onPause() {
        super.onPause();
        if (server != null) {
            server.stop();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getVisibleFragment();

        if (fragment != null && fragment.isVisible()) {
            selectItemData(Fragments.TEST_PAGE, false);
        } else {
            super.onBackPressed();
        }
    }

    private void selectItemData(Fragments type, boolean clearBackStack) {
        Fragment fragment = null;
        switch (type) {
            case BUG_REPORT:
                fragment = new BugReportFragment();
                break;
            case TEST_PAGE:
                fragment = new TestPagePlainFragment();
                break;
            default:
                break;
        }

        Log.d(TAG, "selectItemData: fragment: " + fragment);

        if (fragment == null) {
            Log.e(TAG, "Error. Fragment is not created");
            return;
        }

        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment, type + "");
            if (clearBackStack) {
                transaction.addToBackStack(null);
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            transaction.commit();
        } catch (Exception e) {
            Log.e(TAG, "ERROR:", e);
        }
    }

    private boolean mainPageOpened() {
        android.app.Fragment fragment = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            fragment = getFragmentManager().findFragmentByTag(Fragments.TEST_PAGE.name());
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return (fragment != null && fragment.isVisible());
        }

        return true;
    }

    @Override
    public ObjectGraph getObjectGraph() {
        if (mainApp != null) {
            return mainApp.getObjectGraph();
        }

        return null;
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    class Task extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            String hasNetwork = appUtil.testData();
            if (Objects.nonNull(hasNetwork)) {
                Log.d(TAG, "doInBackground: Network is unreachable");
            }

            List<String> ips = Arrays.asList(
                    appUtil.ip4(),
                    appUtil.ip4_2(),
                    appUtil.ip4_3(),
                    appUtil.ip4_4(),
                    appUtil.ip4_5(),
                    appUtil.ip4_6(),
                    appUtil.ip4_7()
            );
            System.out.println("MainActivity.onCreate, available ips: " + ips);
            project.setAddress(appUtil.ip4_7());
            return ips.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
        }
    }

}
