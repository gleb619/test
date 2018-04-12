package org.test.view.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.test.app.MainApp;
import org.test.service.FolderService;
import org.test.settings.Settings;
import org.test.test.R;
import org.test.util.AppUtil;
import org.test.view.fragment.core.InjectableFragment;

import javax.inject.Inject;

import butterknife.Bind;

import static org.test.settings.Settings.Code.HIGH_PERFORMANCE_MODE;

/**
 * A placeholder fragment containing a simple view.
 */
public class TestPagePlainFragment extends InjectableFragment {

    @Inject
    MainApp mainApp;
    @Inject
    Settings project;
    @Inject
    AppUtil appUtil;
    @Inject
    FolderService folderService;

    @Bind(R.id.page_test_browser)
    WebView webView;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(false);

        webView.canGoBack();
        webView.canGoForward();
        webView.clearHistory();
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        }
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setAppCacheEnabled(true);

        if (Boolean.TRUE.toString().equals(project.value(HIGH_PERFORMANCE_MODE))) {
            webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            }

            webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        }

        webView.loadUrl("http://127.0.0.1:8080/");
    }

    @Override
    protected void onViewInjected() {

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public Context getContext() {
        return mainApp;
    }

    @Override
    public int resolveLayoutId() {
        return R.layout.fragment_test_page_plain;
    }

    private class WebViewClient extends android.webkit.WebViewClient {
    }
}
