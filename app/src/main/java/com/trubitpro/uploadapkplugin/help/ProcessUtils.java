package com.trubitpro.uploadapkplugin.help;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

public class ProcessUtils {
    /**
     * @param timeout 超时时长
     * @param fileDir 所运行程序路径
     * @param command 程序所要执行的命令
     * 运行一个外部命令，返回状态.若超过指定的超时时间，抛出TimeoutException
     */
    public static int executeProcess(final long timeout, File fileDir, final String command)
            throws IOException, InterruptedException, TimeoutException {
        Process process = Runtime.getRuntime().exec(command, null, fileDir);
        Worker worker = new Worker(process);
        worker.start();
        try {
            worker.join(timeout);
            if (worker.exit != null){
                return worker.exit;
            } else{
                throw new TimeoutException();
            }
        } catch (InterruptedException ex) {
            worker.interrupt();
            Thread.currentThread().interrupt();
            throw ex;
        }
        finally {
            process.destroy();
        }
    }

private static class Worker extends Thread {
    private final Process process;
    private Integer exit;

    private Worker(Process process) {
        this.process = process;
    }

    @Override
    public void run() {
        InputStream errorStream = null;
        InputStream inputStream = null;
        try {
            errorStream = process.getErrorStream();
            inputStream = process.getInputStream();
            readStreamInfo(errorStream, inputStream);
            exit = process.waitFor();
            process.destroy();
            if (exit == 0) {
                System.out.println("子进程正常完成");
            } else {
                System.out.println("子进程异常结束");
            }
        } catch (InterruptedException ignore) {
            return;
        }
    }
}

    /**
     * 读取RunTime.exec运行子进程的输入流 和 异常流
     * @param inputStreams 输入流
     */
    public static void readStreamInfo(InputStream... inputStreams){
        ExecutorService executorService = Executors.newFixedThreadPool(inputStreams.length);
        for (InputStream in : inputStreams) {
            executorService.execute(new MyThread(in));
        }
        executorService.shutdown();
    }
    public static class MyThread implements Runnable {

        private InputStream in;
        public MyThread(InputStream in){
            this.in = in;
        }

        @Override
        public void run() {
            try{
                BufferedReader br = new BufferedReader(new InputStreamReader(in, "GBK"));
                String line = null;
                while((line = br.readLine())!=null){
                    System.out.println(" inputStream: " + line);
                }
            }catch (IOException e){
                e.printStackTrace();
            }finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}