package com.trubitpro.uploadapkplugin.task;


import com.android.build.gradle.api.BaseVariantOutput;
import com.trubitpro.uploadapkplugin.help.ProcessUtils;
import com.trubitpro.uploadapkplugin.help.SystemLogUitls;

import org.gradle.api.GradleException;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Android-ZX
 * 2021/9/3.
 */
public class BuildFlutterTask {

    public static void buildFlutter() {

        SystemLogUitls systemLogUitls=new SystemLogUitls();
        System.out.println(systemLogUitls.BuildFlutterStart);
        List<String> commend = new ArrayList<>();
        commend.add("bash");
        commend.add("-c");
        commend.add("cd ../mexo_flutter_module && pwd && flutter clean && flutter pub get ");
        try {
           ProcessUtils.exec(commend);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(systemLogUitls.BuildFlutterEnd);

    }
}