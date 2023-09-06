package com.trubitpro.uploadapkplugin.pramars;


import org.gradle.api.Project;

/**
 * 发送到飞书的消息参数
 */
public class SendLarkParams {

    public String webHookHostUrl;
    public String contentTitle;
    public String contentText;
    public String msgtype = "post";
    public boolean isAtAll = false;
    public String clickTxt = "点我进行下载";
    /**
     * 是否支持发送git记录
     */
    public boolean isSupportGitLog = true;
    
    public SendLarkParams() {

    }

    public SendLarkParams(String webHookHostUrl, String contentTitle, String contentText, String msgtype, boolean isAtAll, String clickTxt) {
        this.webHookHostUrl = webHookHostUrl;
        this.contentText = contentText;
        this.contentTitle = contentTitle;
        this.msgtype = msgtype;
        this.isAtAll = isAtAll;
        this.clickTxt = clickTxt;
    }

    public static SendLarkParams getLarkParamsConfig(Project project) {

        SendLarkParams extension = project.getExtensions().findByType(SendLarkParams.class);
        if (extension == null) {
            extension = new SendLarkParams();
        }
        return extension;
    }

}
