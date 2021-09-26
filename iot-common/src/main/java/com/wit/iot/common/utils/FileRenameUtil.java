package com.wit.iot.common.utils;


import java.io.File;

/**
 * 文件批量重命名方法
 */
public class FileRenameUtil {

    private final static String path = "F:\\hadoop+jvm学习【20210922\\【javazx.com-A021】徐老师大数据\\[大数据]徐老师大数据-1511-IT18-解压版\\资料与书籍\\Public_PDF";
    private final static String key = "[IT十八掌www.it18zhang.com]";

    public static void batchRenameFile(String path){
        File file = new File(path);
        String name = file.getPath();
        String replace = name.replace(key, "");
        file.renameTo(new File(replace));

        File[] files = file.listFiles();
        if(files == null){
            return;
        }

        for (File f: files) {
            batchRenameFile(f.getPath());
        }
    }


    public static void main(String[] args) {
        batchRenameFile(path);
    }
}
