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


    //是否编译flutter项目
    public boolean isBuildFlutter= false;


    //当前用户的sudo 密码 如果是isBuildFlutter ==true 必须要sudo 密码 不然无法执行文件权限 会导致flutter 打包失败

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

    public String getWebHookHostUrl() {
        return webHookHostUrl;
    }

    public void setWebHookHostUrl(String webHookHostUrl) {
        this.webHookHostUrl = webHookHostUrl;
    }

    public String getContentTitle() {
        return contentTitle;
    }

    public void setContentTitle(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    public String getContentText() {
        return contentText;
    }

    public void setContentText(String contentText) {
        this.contentText = contentText;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public boolean isAtAll() {
        return isAtAll;
    }

    public void setAtAll(boolean atAll) {
        isAtAll = atAll;
    }

    public String getClickTxt() {
        return clickTxt;
    }

    public void setClickTxt(String clickTxt) {
        this.clickTxt = clickTxt;
    }

    public boolean isSupportGitLog() {
        return isSupportGitLog;
    }

    public void setSupportGitLog(boolean supportGitLog) {
        isSupportGitLog = supportGitLog;
    }

    public boolean isBuildFlutter() {
        return isBuildFlutter;
    }

    public void setBuildFlutter(boolean buildFlutter) {
        isBuildFlutter = buildFlutter;
    }
}
