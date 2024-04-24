package com.interview;

import java.io.File;
import java.util.Stack;

/**
 * 大厂面试2
 *
 * 给定一个文件目录的路径,写一个函数统计这个目录下所有的文件数并返回
 *
 *
 * @Author HaosionChiang
 * @Date 2023/10/8
 **/
public class DCMS_2 {


    public static void main(String[] args) {
        String path="E:\\百度网盘下载";
        System.out.println(countFiles(path));
    }

    public static int countFiles(String path){
        File root=new File(path);
        if (!root.isFile()&&!root.isDirectory()){
            return 0;
        }
        if (root.isFile()){
            return 1;
        }
        Stack<File> stack =new Stack<>();

        stack.add(root);

        int cnt=0;
        while (!stack.isEmpty()){
            File pop = stack.pop();
            for (File listFile : pop.listFiles()) {
                if (listFile.isFile()){
                    cnt++;
                }else{
                    stack.add(listFile);
                }
            }
        }
        return cnt;
    }

}
