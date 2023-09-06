package com.trubitpro.uploadapkplugin.help;

public interface ProgressListener {
    void onProgress(long currentBytes, long totalBytes);
}
