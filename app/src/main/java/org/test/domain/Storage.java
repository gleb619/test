package org.test.domain;

/**
 * Created by boris on 14.04.2018.
 */

public class Storage {

    private String rootReference;
    private String pagesReference;
    private String jsReference;
    private String cssReference;
    private String imagesReference;
    private String fontsReference;
    private String httpCacheReference;

    public String getRootReference() {
        return rootReference;
    }

    public void setRootReference(String rootReference) {
        this.rootReference = rootReference;
    }

    public String getPagesReference() {
        return pagesReference;
    }

    public void setPagesReference(String pagesReference) {
        this.pagesReference = pagesReference;
    }

    public String getJsReference() {
        return jsReference;
    }

    public void setJsReference(String jsReference) {
        this.jsReference = jsReference;
    }

    public String getCssReference() {
        return cssReference;
    }

    public void setCssReference(String cssReference) {
        this.cssReference = cssReference;
    }

    public String getImagesReference() {
        return imagesReference;
    }

    public void setImagesReference(String imagesReference) {
        this.imagesReference = imagesReference;
    }

    public String getFontsReference() {
        return fontsReference;
    }

    public void setFontsReference(String fontsReference) {
        this.fontsReference = fontsReference;
    }

    public String getHttpCacheReference() {
        return httpCacheReference;
    }

    public void setHttpCacheReference(String httpCacheReference) {
        this.httpCacheReference = httpCacheReference;
    }

}
