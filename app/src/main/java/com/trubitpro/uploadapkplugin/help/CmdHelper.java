package com.trubitpro.uploadapkplugin.help;

import com.trubitpro.uploadapkplugin.pramars.GitLogParams;


import org.gradle.api.Project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class CmdHelper {
    private static final int LOG_MAX_COUNT = 50;
    private static final int LOG_MIN_COUNT = 10;
    private static final String GIT_LOG_BASIC_CMD = "git log --oneline --pretty=format:\"%an—>%s\" --no-merges";
    /**
     * %ai 是时间，格式：2022-08-31 23:18:48 +0800
     */

    public static String checkGetGitParamsWithLog(Project project) {
        GitLogParams gitLogParams = GitLogParams.getGitParamsConfig(project);
        if (gitLogParams == null) {
            return "";
        }
        return getGitLogByTimeAndCount(gitLogParams.gitLogHistoryDayTime, gitLogParams.gitLogMaxCount);
    }

    public static String getGitLogByTimeAndCount(int logDayTime, int logMaxCount) {
        StringBuilder logBuilder = new StringBuilder(GIT_LOG_BASIC_CMD);
        if (logDayTime >= 1) {
            logBuilder.append(" --since=").append(logDayTime).append("days");
            logMaxCount = LOG_MAX_COUNT;
        } else {
            if (logMaxCount <= 0) {
                logMaxCount = LOG_MIN_COUNT;
            } else if (logMaxCount > LOG_MAX_COUNT) {
                logMaxCount = LOG_MAX_COUNT;
            }
        }
        logBuilder.append(" --max-count=").append(logMaxCount);
        System.out.println("获取Git Log外部命令=========：\n" + logBuilder.toString());
        return exeCmd(logBuilder.toString(),true);
    }



    public static String exeCmd(String commandStr,boolean isFor) {
        BufferedReader bufferedReader = null;
        Process p = null;
        try {
            //java 调用外部命令
            p = Runtime.getRuntime().exec(commandStr);
            bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            if (isFor) {
                int count = 0;
                while ((line = bufferedReader.readLine()) != null) {
                    ++count;
                    stringBuilder.append(count).append(". ").append(line).append("\n ");
                }
            }else{
                line = bufferedReader.readLine();
                stringBuilder. append(line).append("\n ");
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (p != null) {
                try {
                    p.waitFor();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

}
