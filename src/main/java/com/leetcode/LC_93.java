package com.leetcode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author HaosionChiang
 * @Date 2023/5/21
 **/
public class LC_93 {
    public static void main(String[] args) {
        LC_93 lc93 = new LC_93();


        System.out.println(lc93.restoreIpAddresses("25525511135"));
    }

    public List<String> restoreIpAddresses(String s) {
        List<String> res = new ArrayList<String>();

        bt(res, new LinkedList<>(), s, 0);

        return res;
    }

    void bt(List<String> res, LinkedList<String> path, String s, int index) {

        if (index==s.length()&&path.size() == 4) {
            res.add(String.join(".", path));
            return;
        }


        for (int i = index; i < s.length(); i++) {

            String sub = s.substring(index, i+1);

            if (!isValid(s, index, i)) {
                continue;
            } else {
                path.add(sub);
                bt(res, path, s, i + 1);
                path.removeLast();
            }


        }

    }

    boolean isValid(String s, int start, int end) {
        if (start > end) {
            return false;
        }
        if (s.charAt(start) == '0' && start != end) { // 0开头的数字不合法
            return false;
        }
        int num = 0;
        for (int i = start; i <= end; i++) {
            if (s.charAt(i) > '9' || s.charAt(i) < '0') { // 遇到非数字字符不合法
                return false;
            }
            num = num * 10 + (s.charAt(i) - '0');
            if (num > 255) { // 如果大于255了不合法
                return false;
            }
        }
        return true;
    }
}
