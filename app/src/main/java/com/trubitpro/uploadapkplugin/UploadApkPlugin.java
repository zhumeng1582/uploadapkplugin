package com.trubitpro.uploadapkplugin;

import com.android.build.gradle.AppExtension;
import com.android.build.gradle.api.ApplicationVariant;
import com.google.gson.Gson;
import com.trubitpro.uploadapkplugin.entry.FlutterGitBean;
import com.trubitpro.uploadapkplugin.pramars.GitLogParams;
import com.trubitpro.uploadapkplugin.pramars.SendLarkParams;
import com.trubitpro.uploadapkplugin.pramars.TrubitProParams;
import com.trubitpro.uploadapkplugin.task.BuildFlutterTask;
import com.trubitpro.uploadapkplugin.task.OnlyUploadTask;

import org.gradle.api.DomainObjectSet;
import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class UploadApkPlugin   implements Plugin<Project> {
    int process = 0;
    @Override
    public void apply(Project target) {
        target.getExtensions().create(PluginConstants.TRUBIT_PRO_PARAMS_NAME, TrubitProParams.class);
        target.getExtensions().create(PluginConstants.GIT_LOG_PARAMS_NAME, GitLogParams.class);
        target.getExtensions().create(PluginConstants.UPLOAD_PARAMS_NAME, SendLarkParams.class);
        target.afterEvaluate(project1 -> {
            SendLarkParams sendLarkParams =  target.getExtensions().getByType(SendLarkParams.class);
            TrubitProParams trubitProParams =  target.getExtensions().getByType(TrubitProParams.class);


            AppExtension appExtension = ((AppExtension) project1.getExtensions().findByName(PluginConstants.ANDROID_EXTENSION_NAME));
            if (appExtension == null) {
                return;
            }
            if(sendLarkParams.isBuildFlutter){
                BuildFlutterTask.buildFlutter();
            }
            DomainObjectSet<ApplicationVariant> appVariants = appExtension.getApplicationVariants();
            for (ApplicationVariant applicationVariant : appVariants) {
                if (applicationVariant.getBuildType() != null) {
                    dependsOnTask(applicationVariant,  true, project1,trubitProParams.getGetHttpUpLoadUrl());
                }
            }
        });

    }


    private void dependsOnTask(ApplicationVariant applicationVariant, boolean  isInit, Project project1,String url) {
        String variantName =
                applicationVariant.getName().substring(0, 1).toUpperCase() + applicationVariant.getName().substring(1);
        if (variantName.isEmpty()) {
            variantName ="Release" ;
        }
        OnlyUploadTask uploadTask = project1.getTasks()
                .create(PluginConstants.TASK_EXTENSION_NAME + variantName, OnlyUploadTask.class);
        uploadTask.init(isInit?applicationVariant:null, project1,url);
       if (isInit){
           applicationVariant.getAssembleProvider().get().dependsOn(project1.getTasks().findByName("clean"));
            uploadTask.dependsOn(applicationVariant.getAssembleProvider().get());
       }
    }

}
