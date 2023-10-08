package com.trubitpro.uploadapkplugin.entry;

public class FlutterGitBean {

    private  String flutterGitBranch;

    private  String flutterGitLog;

    public FlutterGitBean(String flutterGitBranch, String flutterGitLog) {
        this.flutterGitBranch = flutterGitBranch;
        this.flutterGitLog = flutterGitLog;
    }

    public String getFlutterGitBranch() {
        return flutterGitBranch;
    }

    public void setFlutterGitBranch(String flutterGitBranch) {
        this.flutterGitBranch = flutterGitBranch;
    }

    public String getFlutterGitLog() {
        return flutterGitLog;
    }

    public void setFlutterGitLog(String flutterGitLog) {
        this.flutterGitLog = flutterGitLog;
    }
}
