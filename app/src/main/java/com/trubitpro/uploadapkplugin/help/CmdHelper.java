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
    public static final String gitLogCmd = GIT_LOG_BASIC_CMD + " --since=\"2022-8-30\" --until=\"2022-9-1\"";
    /**
     * %ai 是时间，格式：2022-08-31 23:18:48 +0800
     */
    public static final String gitLogCmd2 = "git log --oneline --pretty=format:\"%ai,%an:%s\" --no-merges --since=2days";
    public static final String gitLogCmd3 = GIT_LOG_BASIC_CMD + " --max-count=10";

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
        System.out.println("获取Git log 外部命令：\n" + logBuilder.toString());
        return exeCmd(logBuilder.toString(),true);
    }


    public static boolean exeBuildFlutterAar(){

        BufferedReader bufferedReader = null;
        Process p = null;
        String cdFir="cd ../mexo_flutter_module&&pwd&&flutter clear&&flutter build aar";
        try {
            p = Runtime.getRuntime().exec(cdFir );
            System.out.println("cdFir Start");
            Print(p,cdFir);
            int exitVal = p.waitFor();
            System.out.println("cdFir End==="+exitVal);
            if (exitVal==0){
                return true;
            }

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
        return false;


    }


    private  static void   Print(Process  p, String s){
        BufferedReader   bufferedReader=null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            int count = 0;
            while ((line = bufferedReader.readLine()) != null) {
                ++count;
                stringBuilder.append(count).append(". ").append(line).append("\n ");
            }
            System.out.println( s+" result：\n" + stringBuilder);
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
    }

    public static String exeCmd(String commandStr,boolean isFor) {
        BufferedReader bufferedReader = null;
        Process p = null;
        try {
            //java 调用外部命令
            p = Runtime.getRuntime().exec(commandStr);
            //Charset.forName("UTF-8")
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
            System.out.println("ExeCmd result：\n" + stringBuilder);
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
