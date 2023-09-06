package com.trubitpro.uploadapkplugin;

import com.trubitpro.uploadapkplugin.task.BaseTask;

import java.io.File;

public class test {


    public static void main(String[] args) {
        BaseTask baseTask=new BaseTask();
        File baseFile=new File("/Users/wuao/AndroidStudioProjects/MyApplication3/app/build/outputs/apk/Facebook/debug/app-Facebook-debug.apk");
        baseTask .uploadPgyQuickWay(baseFile);
    }
}
