package com.trubitpro.uploadapkplugin;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.api.ApplicationVariant;
import com.android.build.gradle.api.BaseVariant;
import com.trubitpro.uploadapkplugin.help.CmdHelper;
import com.trubitpro.uploadapkplugin.help.ProcessUtils;
import com.trubitpro.uploadapkplugin.pramars.GitLogParams;
import com.trubitpro.uploadapkplugin.pramars.SendLarkParams;
import com.trubitpro.uploadapkplugin.pramars.UploadPgyParams;
import com.trubitpro.uploadapkplugin.task.OnlyUploadTask;

import org.gradle.api.Action;
import org.gradle.api.DomainObjectSet;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Locale;

import kotlin.jvm.internal.Intrinsics;

public class UploadApkPlugin   implements Plugin<Project> {
    @Override
    public void apply(Project target) {
        target.getExtensions().create(PluginConstants.GIT_LOG_PARAMS_NAME, GitLogParams.class);

        SendLarkParams uploadParams = (SendLarkParams)target.getExtensions().create(PluginConstants.UPLOAD_PARAMS_NAME, SendLarkParams.class, new Object[0]);

        target.afterEvaluate(project1 -> {
            AppExtension appExtension = ((AppExtension) project1.getExtensions().findByName(PluginConstants.ANDROID_EXTENSION_NAME));
            if (appExtension == null) {
                return;
            }
            DomainObjectSet<ApplicationVariant> appVariants = appExtension.getApplicationVariants();
            for (ApplicationVariant applicationVariant : appVariants) {
                if (applicationVariant.getBuildType() != null) {
                    dependsOnTask(applicationVariant,  true, project1);
                }
            }
        });
        OnlyUploadTask uploadTask = target.getTasks()
                .create(PluginConstants.TASK_EXTENSION_NAME_ONLY_UPLOAD, OnlyUploadTask.class);
        uploadTask.init(  null, target);
    }


    private void dependsOnTask(ApplicationVariant applicationVariant, boolean  isInit, Project project1) {
        String variantName =
                applicationVariant.getName().substring(0, 1).toUpperCase() + applicationVariant.getName().substring(1);
        if (variantName.isEmpty()) {
            variantName ="Release" ;
        }
        //创建我们，上传到蒲公英的task任务
        OnlyUploadTask uploadTask = project1.getTasks()
                .create(PluginConstants.TASK_EXTENSION_NAME + variantName, OnlyUploadTask.class);
        uploadTask.init( isInit?applicationVariant:null, project1);
        //依赖关系 。上传依赖打包，打包依赖clean。
       if (isInit){
//           applicationVariant.getAssembleProvider().get().dependsOn(project1.getTasks().findByName("clean"));
           uploadTask.dependsOn(applicationVariant.getAssembleProvider().get());
       }
    }

}
