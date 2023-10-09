package com.trubitpro.uploadapkplugin.task;



import com.android.build.gradle.api.BaseVariant;
import com.google.gson.Gson;
import com.trubitpro.uploadapkplugin.PluginConstants;
import com.trubitpro.uploadapkplugin.entry.FlutterGitBean;
import com.trubitpro.uploadapkplugin.entry.LarkResult;
import com.trubitpro.uploadapkplugin.help.CmdHelper;
import com.trubitpro.uploadapkplugin.help.HttpHelper;
import com.trubitpro.uploadapkplugin.help.ProcessUtils;
import com.trubitpro.uploadapkplugin.help.ProgressListener;
import com.trubitpro.uploadapkplugin.help.ProgressRequestBody;
import com.trubitpro.uploadapkplugin.help.SendMsgHelper;
import com.trubitpro.uploadapkplugin.pramars.GitLogParams;
import com.trubitpro.uploadapkplugin.pramars.SendLarkParams;
import com.trubitpro.uploadapkplugin.pramars.TrubitProParams;


import org.gradle.api.DefaultTask;
import org.gradle.api.Project;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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


    public FlutterGitBean flutterGit;


    public void init(BaseVariant variant, Project project) {
        this.mVariant = variant;
        this.mTargetProject = project;
        setDescription(PluginConstants.TASK_DES);
        setGroup(PluginConstants.TASK_GROUP_NAME);
    }


    /**
     * 上传apk 到自己服务器地址
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
                    System.out.println("\n 上传APK进度-------"+progress+"%------------");

                }
            }
        });

        TrubitProParams trubitProParams = TrubitProParams.getTrubitProParamsConfig(mTargetProject);
        String url="https://test-api.trubit.com/member-api/api/v1/uploadApp";
        String fileKey="file";

        if (!trubitProParams.getHttpUpLoadUrl().isEmpty()){
            url=trubitProParams.getHttpUpLoadUrl();
        }
        if (!trubitProParams.getUpLoadKey().isEmpty()){
            fileKey=trubitProParams.getUpLoadKey();
        }
        MultipartBody mBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(fileKey , name , progressRequestBody)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(mBody)
                .build();
        System.out.println("\n******************* TrubitPro 上传: Start *******************");

        try {
            Response response = HttpHelper.getOkHttpClient().newCall(request).execute();
            if (response.isSuccessful()) {
                System.out.println("\n******************* TrubitPro 上传: finish *******************");

                String result = response.body().string();
                System.out.println("\n TrubitPro  =======   result: " + result);
                if (!result.isEmpty()) {
                    LarkResult larkResult = new Gson().fromJson(result, LarkResult.class);
                    if (larkResult.getCode() != 0 ) {
                        System.out.println("\n TrubitPro=======upload error : " + larkResult.getMsg());
                        return;
                    }
                    if (larkResult.getData() != null) {
                        String apkDownUrl = larkResult.getData();
                        flutterGit=new FlutterGitBean("","");
                        System.out.println("\n 上传成功，应用链接: " + apkDownUrl);
                        String gitLog = CmdHelper.checkGetGitParamsWithLog(mTargetProject);
                        String gitBranch = CmdHelper.exeCmd( "git symbolic-ref --short HEAD",false);
                        System.out.println("\n Android Project Git branch    ：\n" + gitBranch);
                        System.out.println("\n Android Project Git log 日志列表：\n" + gitLog);
                        SendLarkParams larkParams = SendLarkParams.getLarkParamsConfig(mTargetProject);
                        GitLogParams gitLogParams = GitLogParams.getGitParamsConfig(mTargetProject);

                        List<String> commend2 = new ArrayList<>();
                        commend2.add("bash");
                        commend2.add("-c");
                        commend2.add("cd ../mexo_flutter_module && git symbolic-ref --short HEAD");
                        List<String> commend3 = new ArrayList<>();
                        commend3.add("bash");
                        commend3.add("-c");
                        StringBuilder sb = new StringBuilder();
                        sb.append("cd ../mexo_flutter_module && git log --oneline --pretty=format:\"%an—>%s\" --no-merges --since=")
                                .append(gitLogParams.gitLogHistoryDayTime).append("days --max-count=").append(gitLogParams.gitLogMaxCount);
                        commend3.add(sb.toString());
                        try {
                            String flutterBranch  = ProcessUtils.exeCmd(commend2);
                            String flutterLog  = ProcessUtils.exeCmd(commend3);
                            flutterGit.setFlutterGitLog(flutterLog);
                            flutterGit.setFlutterGitBranch(flutterBranch);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        if (larkParams.isBuildFlutter){
                            System.out.println("\n Flutter Project  Git branch    ：\n" + flutterGit.getFlutterGitBranch());
                            System.out.println("\n Flutter Project  Git log 日志列表：\n" + flutterGit.getFlutterGitLog());
                        }
                        SendMsgHelper.sendMsgToLark(mVariant, mTargetProject, larkResult.getData(), gitLog,gitBranch,flutterGit);
                    } else {
                        System.out.println("\n  TrubitPro=========buildInfo: upload pgy result error : data is empty");
                    }
                }
            } else {
                System.out.println(response.toString());
                System.out.println("TrubitPro ======= request 上传 call failed");
            }
        } catch (Exception e) {
            System.out.println("TrubitPro  ======= request  上传 call failed " + e);
        }
    }





}