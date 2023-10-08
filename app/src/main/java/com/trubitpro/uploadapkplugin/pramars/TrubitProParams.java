package com.trubitpro.uploadapkplugin.pramars;

import org.gradle.api.Project;

public class TrubitProParams {




    /**
     * 上传的接口地址 方便切换
     */
    private  String httpUpLoadUrl;

    /**
     * 上传的key 一般来说默认是file
     */
    private  String upLoadKey;


    public static TrubitProParams getTrubitProParamsConfig(Project project) {

        TrubitProParams extension = project.getExtensions().findByType(TrubitProParams.class);
        if (extension == null) {
            extension = new TrubitProParams();
        }
        return extension;
    }


    public String getHttpUpLoadUrl() {
        return httpUpLoadUrl;
    }

    public void setHttpUpLoadUrl(String httpUpLoadUrl) {
        this.httpUpLoadUrl = httpUpLoadUrl;
    }

    public String getUpLoadKey() {
        return upLoadKey;
    }

    public void setUpLoadKey(String upLoadKey) {
        this.upLoadKey = upLoadKey;
    }



}
