# uploadapkplugin

Android. 上传测试服务器 并且自动发送飞书消息的gralde 插件



buildGitLogParams {
//是否发送消息是携带Git记录日志，如果配置了这块参数才会携带Git记录，消息里面可以单独设置是否携带Git日志数据
//获取以当前时间为基准至N天之前的Git记录（限定时间范围），不填或小于等于0为全部记录，会结合数量进行获取
gitLogHistoryDayTime = 3
//显示Git记录的最大数量，值范围1~50，不填默认是10条，最大数量50条
gitLogMaxCount = 20
}

buildTrubitProParams {
//上传服务器的接口地址 方便更改
httpUpLoadUrl = ""
//上传文件的key 默认大部分都是file
upLoadKey = "file"
}

buildLarkParams {
//飞书机器人的地址
webHookHostUrl = "https://open.feishu.cn/open-apis/bot/v2/hook/"+token
contentTitle = "TruBitPro Android"
contentText = "最新开发测试包已经上传至测试服务器, 可以下载使用了"
//富文本消息（post）、消息卡片（interactive），默认post
msgtype = "interactive"
//是否@全体群人员，默认false：isAtAll = true
isAtAll = false
clickTxt = "点击进行下载"
//是否单独支持发送Git记录数据，在配置了buildGitLogParams前提下有效，默认为true
isSupportGitLog = true
//是否编译flutter 项目  一般来说在同级目录里面 和当前项目同级即可默认false
isBuildFlutter = false

}

# 功能说明
##### 支持自定义接口地址上传自己服务器 
##### 支持是否编译flutter 项目并且打印log 和发送消息当前的flutter的分支和git log 信息
##### 当前Android项目并且打印log 和发送消息当前的项目的分支和git log 信息
##### 支持自定义各种机器人地址传入
