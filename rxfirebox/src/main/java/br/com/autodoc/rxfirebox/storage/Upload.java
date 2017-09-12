package br.com.autodoc.rxfirebox.storage;

import android.net.Uri;

public class Upload {

    private final double progress;
    private final Uri session;
    private final Uri url;

    public Upload(double progress, Uri session, Uri url) {
        this.progress = progress;
        this.session = session;
        this.url = url;
    }

    public double getProgress() {
        return progress;
    }

    public Uri getSession() {
        return session;
    }

    public Uri getUrl() {
        return url;
    }
}
