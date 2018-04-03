package com.yitaqi.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述:
 * ${DESCRIPTION}
 *
 * @author xue
 * @create 2018-03-30 9:19
 */
public class Logs {

    private static Logs log = new Logs();
    private volatile FileWriter writer;
    private File file;
    private long fileMaxSize = 1024 * 20;

    public static void info(String info) {

        log.write(info);
    }

    public void write(String log) {
        try {
            writer = getFileWriter();
            writer.write(log + "\r\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileWriter getFileWriter() {

        try {
            if (writer == null) {
                synchronized (this) {
                    if (writer == null) {
                        file = openFile();
                        writer = new FileWriter(file, true);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // todo 考虑文件扩展
        return writer;
    }

    private File openFile() {
        String fileDir = System.getProperty("user.dir") + "/log/";
        File dir = new File(fileDir);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdir();
        }
        File file = new File(dir, "apm.log");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static void main(String[] args) throws InterruptedException {

        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(new Thread() {
                @Override
                public void run() {
                    Logs.info("4");
                }
            });
        }

        for (Thread thread : list) {
            thread.start();
        }
        for (Thread thread : list) {
            thread.join();

        }
    }
}
