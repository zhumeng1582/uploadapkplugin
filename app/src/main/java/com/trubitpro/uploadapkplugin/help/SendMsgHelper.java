package com.trubitpro.uploadapkplugin.help;

import com.android.build.gradle.api.BaseVariant;
import com.trubitpro.uploadapkplugin.entry.lark.ElementsDTO;
import com.trubitpro.uploadapkplugin.entry.lark.LarkRequestBean;
import com.trubitpro.uploadapkplugin.entry.lark.TextDTO;
import com.trubitpro.uploadapkplugin.pramars.SendLarkParams;
import org.gradle.api.Project;
import java.util.ArrayList;
import java.util.List;
import groovy.json.JsonOutput;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public final class SendMsgHelper {
    public static void sendMsgToLark(BaseVariant variant, Project project, String dataDTO, String gitLog) {
        String defaultTitle = "测试包";
        String defaultText = "最新开发测试包已上传 ";
        String defaultClickText = "点我进行下载";
        String defaultLogTitle = "最近Git更新内容：\n ";
        SendLarkParams feishuParams = SendLarkParams.getLarkParamsConfig(project);
        String webHookHostUrl = feishuParams.webHookHostUrl;
        if (  isEmpty(webHookHostUrl)) {
            System.out.println("send to   lark failure：webHookHostUrl is empty");
            return;
        }
        String flavorStr =  getFlavorInfo(variant);
        String title = feishuParams.contentTitle;
        if (isEmpty(title)) {
            title = defaultTitle;
        }
        String titleStr = flavorStr  ;
        String text = feishuParams.contentText;
        if (  isEmpty(text)) {
            text = defaultText;
        }
        StringBuilder textStr = new StringBuilder("**").append(title).append( "\n").append(text).append("** ").append(dataDTO).append(" \n");
       LarkRequestBean  larkRequestBean = new LarkRequestBean();
        if ("interactive".equals(feishuParams.msgtype)) {
            LarkRequestBean.CardDTO cardDTO = new LarkRequestBean.CardDTO();

            larkRequestBean.setMsg_type("interactive");
              LarkRequestBean.CardDTO.ConfigDTO cardConfigBean = new   LarkRequestBean.CardDTO.ConfigDTO();
            cardConfigBean.setWide_screen_mode(true);
            cardConfigBean.setEnable_forward(true);
            cardDTO.setConfig(cardConfigBean);
              LarkRequestBean.CardDTO.HeaderDTO cardHeaderBean = new   LarkRequestBean.CardDTO.HeaderDTO();
            cardHeaderBean.setTemplate("green");
            TextDTO cardHeaderTitleBean = new TextDTO();
            cardHeaderTitleBean.setTag("plain_text");
            String rocket="\uD83D\uDE80\uD83D\uDE80\uD83D\uDE80\uD83D\uDE80\uD83D\uDE80\uD83D\uDE80\uD83D\uDE80\uD83D\uDE80\uD83D\uDE80\uD83D\uDE80\uD83D\uDE80\uD83D\uDE80\uD83D\uDE80\uD83D\uDE80\uD83D\uDE80\uD83D\uDE80\uD83D\uDE80\uD83D\uDE80 ";
            cardHeaderTitleBean.setContent(rocket +"\n"+ titleStr);
            cardHeaderBean.setTitle(cardHeaderTitleBean);
            cardDTO.setHeader(cardHeaderBean);
            List<ElementsDTO> elementsDTOList = new ArrayList<>();
            ElementsDTO elements1 = new ElementsDTO();
            elements1.setTag("div");
            TextDTO elements1TextBean = new TextDTO();
            elements1TextBean.setTag("lark_md");

            String QRCode="https://cli.im/api/qrcode/code?text="+dataDTO+"&mhid=skKRBVu8y54hMHYoI9dQMKo";

            textStr.append("![").append(  isEmpty(feishuParams.clickTxt) ? defaultClickText : feishuParams.clickTxt)
                    .append("]").append("(").append(dataDTO).append(")").append("");
//            textStr.append("![").append( "").append("(").append(dataDTO).append(")");

//            textStr.append("[查看下载二维码]").append("(").append(QRCode).append(")");
            if (feishuParams.isAtAll) {
                textStr.append(" \n").append("<at id=all></at>");
            }
            elements1TextBean.setContent(textStr.toString());
            elements1.setText(elements1TextBean);
            elementsDTOList.add(elements1);
            ElementsDTO elements2 = new ElementsDTO();

            elements2.setTag("hr");
            elementsDTOList.add(elements2);
            if (!  isEmpty(gitLog) && feishuParams.isSupportGitLog) {
                ElementsDTO elements3 = new ElementsDTO();
                elements3.setTag("div");
                TextDTO elements3TextBean = new TextDTO();
                elements3TextBean.setTag("lark_md");
                StringBuilder logStrBuilder = new StringBuilder("**").append(defaultLogTitle).append("**").append(gitLog);
                elements3TextBean.setContent(logStrBuilder.toString());
                elements3.setText(elements3TextBean);
                elementsDTOList.add(elements3);
                ElementsDTO elements4 = new ElementsDTO();
                elements4.setTag("hr");
                elementsDTOList.add(elements4);
            }
            ElementsDTO elements5 = new ElementsDTO();
            elements5.setTag("action");
            List<ElementsDTO> actionsList = new ArrayList<>();
            ElementsDTO actionBtnDownBtn = new ElementsDTO();
            actionBtnDownBtn.setTag("button");
            actionBtnDownBtn.setType("primary");
            actionBtnDownBtn.setUrl( dataDTO);
            TextDTO actionDownBtnText = new TextDTO();
            actionDownBtnText.setTag("plain_text");
            actionDownBtnText.setContent(  isEmpty(feishuParams.clickTxt) ? defaultClickText : feishuParams.clickTxt);
            actionBtnDownBtn.setText(actionDownBtnText);
            actionsList.add(actionBtnDownBtn);

            ElementsDTO actionBtnQRCodeBtn = new ElementsDTO();
            actionBtnQRCodeBtn.setTag("button");
            actionBtnQRCodeBtn.setType("primary");
            actionBtnQRCodeBtn.setUrl(QRCode);
            TextDTO actionQRCodeBtnText = new TextDTO();
            actionQRCodeBtnText.setTag("plain_text");
            actionQRCodeBtnText.setContent("查看下载二维码");
            actionBtnQRCodeBtn.setText(actionQRCodeBtnText);
            actionsList.add(actionBtnQRCodeBtn);
            elements5.setActions(actionsList);
            elementsDTOList.add(elements5);
            cardDTO.setElements(elementsDTOList);
            larkRequestBean.setCard(cardDTO);
        } else {
              LarkRequestBean.ContentDTO contentDTO = new   LarkRequestBean.ContentDTO();

            larkRequestBean.setMsg_type("post");
              LarkRequestBean.ContentDTO.PostDTO postDTO = new   LarkRequestBean.ContentDTO.PostDTO();

              LarkRequestBean.ContentDTO.PostDTO.ZhCnDTO.ContentBean contentBeanText = new   LarkRequestBean.ContentDTO.PostDTO.ZhCnDTO.ContentBean();
            contentBeanText.setTag("text");
            contentBeanText.setText(textStr.toString());
              LarkRequestBean.ContentDTO.PostDTO.ZhCnDTO.ContentBean contentBeanA = new   LarkRequestBean.ContentDTO.PostDTO.ZhCnDTO.ContentBean();
            contentBeanA.setTag("a");
            contentBeanA.setText(  isEmpty(feishuParams.clickTxt) ? defaultClickText : feishuParams.clickTxt);
            contentBeanA.setHref(  dataDTO);
            List<  LarkRequestBean.ContentDTO.PostDTO.ZhCnDTO.ContentBean> contentBeans = new ArrayList<>();
            contentBeans.add(contentBeanText);
            contentBeans.add(contentBeanA);
            if (feishuParams.isAtAll) {
                  LarkRequestBean.ContentDTO.PostDTO.ZhCnDTO.ContentBean contentBeanAt = new   LarkRequestBean.ContentDTO.PostDTO.ZhCnDTO.ContentBean();
                contentBeanAt.setTag("at");
                contentBeanAt.setUser_id("all");
                contentBeans.add(contentBeanAt);
            }
            List<List<  LarkRequestBean.ContentDTO.PostDTO.ZhCnDTO.ContentBean>> zhCnContentList = new ArrayList<>();
            zhCnContentList.add(contentBeans);
            if (!  isEmpty(gitLog) && feishuParams.isSupportGitLog) {
                List<  LarkRequestBean.ContentDTO.PostDTO.ZhCnDTO.ContentBean> contentGitLogBeans = new ArrayList<>();
                  LarkRequestBean.ContentDTO.PostDTO.ZhCnDTO.ContentBean contentGitLogBeanText = new   LarkRequestBean.ContentDTO.PostDTO.ZhCnDTO.ContentBean();
                contentGitLogBeanText.setTag("text");
                contentGitLogBeanText.setText("** " + defaultLogTitle + gitLog);
                contentGitLogBeans.add(contentGitLogBeanText);
                zhCnContentList.add(contentGitLogBeans);
            }

              LarkRequestBean.ContentDTO.PostDTO.ZhCnDTO zhCnDTO = new   LarkRequestBean.ContentDTO.PostDTO.ZhCnDTO();
            zhCnDTO.setTitle(titleStr.toString());
            zhCnDTO.setContent(zhCnContentList);
            postDTO.setZh_cn(zhCnDTO);
            contentDTO.setPost(postDTO);
            larkRequestBean.setContent(contentDTO);
        }


        /**
         * text	文本
         * post	富文本	发送富文本消息
         * image	图片	上传图片
         * share_chat	分享群名片	群名片
         * interactive	消息卡片	消息卡片消息
         */
        String json = JsonOutput.toJson(larkRequestBean);
        System.out.println("send to   lark request json：" + json);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), json);
        Request request = new Request.Builder()
                .addHeader("Connection", "Keep-Alive")
                .addHeader("Charset", "UTF-8")
                .url(webHookHostUrl)
                .post(requestBody)
                .build();
        try {
            Response response = HttpHelper.getOkHttpClient().newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String result = response.body().string();
                System.out.println("send to   lark result：" + result);
            } else {
                System.out.println("send to   lark failure");
            }
            System.out.println("*************** sendMsgToLark finish ***************");
        } catch (Exception e) {
            System.out.println("send to   lark failure " + e);
        }
    }

    private static String getFlavorInfo(BaseVariant variant) {
        String flavor =  isEmpty(variant.getName()) ? variant.getFlavorName() : variant.getName();
        return  isEmpty(flavor) ? "" : "(FlavorName：" + flavor + ")";
    }
    public static boolean isEmpty(CharSequence s) {
        if (s == null) {
            return true;
        } else {
            return s.length() == 0;
        }
    }

}