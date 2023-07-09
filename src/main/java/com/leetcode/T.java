package com.leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author HaosionChiang
 * @Date 2023/5/21
 **/
public class T {
    /**
     * 回溯三部曲
     * <p>
     * 1. 确定递归函数参数
     * 2. 确定递归终止条件
     * 3. 确定单层循环逻辑
     */
    public static void main(String[] args) {
        T t = new T();
        System.out.println(t.partition("aab"));
//        System.out.println(t.ishw(Arrays.asList("aba")));
    }

    public List<List<String>> partition(String s) {
        List<List<String>> res = new ArrayList<>();
        backTracking(res, new LinkedList<>(), s, 0);
        return res;
    }

    void backTracking(List<List<String>> res, LinkedList<String> path, String s, int index) {
        if (index >= s.length()) {
            res.add(new ArrayList<>(path));
            return;
        }

        for (int i = index; i < s.length(); i++) {

            if (ishw(s,index,i)) {
                String substring = s.substring(index, i + 1);
                path.add(substring);
            } else {
                continue;
            }
            backTracking(res, path, s, i + 1);
            path.removeLast();
        }
    }


    boolean ishw(String s,int start,int end) {

        boolean res = true;
        for (int i = start, j =end; i <= j; i++, j--) {
            if (s.charAt(i) != s.charAt(j)) {
                res = false;
                break;
            }
        }
        return res;
    }
}
