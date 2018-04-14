package org.test.settings;

import android.os.Environment;

import org.test.test.BuildConfig;
import org.test.util.Objects;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by BORIS on 31.10.2015.
 */
public final class Settings implements Serializable {

    private final int httpConnectTimeout = 10000;
    private final int httpReadTimeout = 30000;
    private final long timeout = 100;
    private final long retry = 3;
    private final String url = "http://auctionexport.com";
    private final TimeUnit timeoutLatency = TimeUnit.SECONDS;
    private final Map<String, Object> data = new HashMap<>();
    private int httpPort = 8080;
    private String address = "127.0.0.1";
    private Double version = 1.0;

    public Settings() {
        data.put(Code.FOLDER_PROJECT.name(), "AuctionExport");
        data.put(Code.FOLDER_SEPARATOR.name(), File.separator);
        data.put(Code.FOLDER_EXTERNAL_STORAGE.name(), Environment.getExternalStorageDirectory().getAbsolutePath());
        data.put(Code.BUILD_DEBUG.name(), BuildConfig.DEBUG);
        data.put(Code.BRIDGE_NAME.name(), "AuctionExport");
        data.put(Code.TRIM_MODE.name(), Boolean.TRUE);
        data.put(Code.HIGH_PERFORMANCE_MODE.name(), Boolean.TRUE);
    }

    public int getHttpConnectTimeout() {
        return httpConnectTimeout;
    }

    public int getHttpReadTimeout() {
        return httpReadTimeout;
    }

    public long getRetry() {
        return retry;
    }

    public long getTimeout() {
        return timeout;
    }

    public String getUrl() {
        return url;
    }

    public TimeUnit getTimeoutLatency() {
        return timeoutLatency;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getVersion() {
        return version;
    }

    public void setVersion(Double version) {
        this.version = version;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void update(Code code, Object value) {
        data.put(code.name(), value);
    }

    public String value(Code code) {
        Object object = data.get(code.name());
        if (Objects.nonNull(object)) {
            return object.toString();
        } else {
            return null;
        }
    }

    public enum Code {

        FOLDER_PROJECT,
        FOLDER_SEPARATOR,
        FOLDER_EXTERNAL_STORAGE,
        BUILD_DEBUG,
        BRIDGE_NAME,
        FORCE_UPDATE,
        TRIM_MODE,
        HIGH_PERFORMANCE_MODE,

    }

}
