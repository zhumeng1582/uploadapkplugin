package com.trubitpro.uploadapkplugin.task;



import com.android.build.gradle.api.BaseVariant;
import com.google.gson.Gson;
import com.trubitpro.uploadapkplugin.PluginConstants;
import com.trubitpro.uploadapkplugin.entry.LarkResult;
import com.trubitpro.uploadapkplugin.entry.PgyCOSTokenResult;
import com.trubitpro.uploadapkplugin.help.CmdHelper;
import com.trubitpro.uploadapkplugin.help.HttpHelper;
import com.trubitpro.uploadapkplugin.help.ProgressListener;
import com.trubitpro.uploadapkplugin.help.ProgressRequestBody;
import com.trubitpro.uploadapkplugin.help.SendMsgHelper;


import org.gradle.api.DefaultTask;
import org.gradle.api.Project;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Android-ZX
 * 2021/9/3.
 */
public class BaseTask extends DefaultTask {

    protected BaseVariant mVariant;
    protected Project mTargetProject;

    public void init(BaseVariant variant, Project project) {
        this.mVariant = variant;
        this.mTargetProject = project;
        setDescription(PluginConstants.TASK_DES);
        setGroup(PluginConstants.TASK_GROUP_NAME);
    }


    /**
     * 快速上传方式 获取上传的 token
     * @param apkFile
     */
    public void uploadPgyQuickWay(File apkFile) {
        //builder

        RequestBody fileBody = RequestBody.create(MediaType.parse("*/*"), apkFile);
        String name = apkFile.getName();          //文件名称
        try {
            name = URLEncoder.encode(name, "UTF-8");                 //文件名称编码，防止出现中文乱码
        } catch (UnsupportedEncodingException e1) {
            //TODO
        }
        final int[] progressInit = {0};
        ProgressRequestBody progressRequestBody=new ProgressRequestBody(fileBody, new ProgressListener() {
            @Override
            public void onProgress(long currentBytes, long totalBytes) {
                int progress = (int) (currentBytes * 100 / totalBytes);
                if (progress==100||progress- progressInit[0] >8){
                    progressInit[0] =progress;
                    System.out.println("上传APK进度-------"+progress+"%------------");

                }
            }
        });
        MultipartBody mBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file" , name , progressRequestBody)
                .build();
        Request request = new Request.Builder()
                .url("https://test-api.trubit.com/member-api/api/v1/uploadApp")
                .post(mBody)
                .build();
        try {
            Response response = HttpHelper.getOkHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                String result = response.body().string();
                System.out.println("TrubitPro   ---   result: " + result);
                if (!result.isEmpty()) {
                    LarkResult larkResult = new Gson().fromJson(result, LarkResult.class);
                    if (larkResult.getCode() != 0 ) {
                        System.out.println("TrubitPro  --- upload error : " + larkResult.getMsg());
                        return;
                    }
                    if (larkResult.getData() != null) {
                        String apkDownUrl = larkResult.getData();
                        System.out.println("上传成功，应用链接: " + apkDownUrl);
                        String gitLog = CmdHelper.checkGetGitParamsWithLog(mTargetProject);
                        String gitBranch = CmdHelper.exeCmd( "git symbolic-ref --short HEAD",false);
                        System.out.println("TrubitPro --- gitBranch====="+gitBranch);


                        SendMsgHelper.sendMsgToLark(mVariant, mTargetProject, larkResult.getData(), gitLog,gitBranch);
                    } else {
                        System.out.println("TrubitPro --- buildInfo: upload pgy result error : data is empty");
                    }
                }
            } else {
                System.out.println(response.toString());
                System.out.println("TrubitPro ---- request 上传 call failed");
            }
            System.out.println("******************* TrubitPro: finish *******************");
        } catch (Exception e) {
            System.out.println("TrubitPro ---- request  上传 call failed " + e);
        }
    }





}