package org.test.service.helper;

import android.os.AsyncTask;
import android.util.Log;

import org.test.service.helper.core.Helper;
import org.test.view.activity.core.DefaultActivity;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BORIS on 14.08.2016.
 */
public class HelperRegister implements Serializable {

    protected static final String TAG = DefaultActivity.class.getName() + ":";

    private final List<Helper> helpers = new ArrayList<>();

    public HelperRegister(Helper... helpers) {
        for (Helper helper : helpers) {
            this.helpers.add(helper);
        }
    }

    public HelperRegister onWork() throws IOException {
        for (Helper helper : helpers) {
            helper.onHelp();
        }

        return this;
    }

    public HelperRegister onWork(Runnable runnable) {
        new Task(runnable).execute();

        return this;
    }

    class Task extends AsyncTask<Void, Void, Void> {

        private Runnable runnable;

        public Task(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                for (Helper helper : helpers) {
                    helper.onHelp();
                }
            } catch (Exception e) {
                Log.e(TAG, "doInBackground: ", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            runnable.run();
        }
    }


}
