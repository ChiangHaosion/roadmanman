package com.leetcode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

class LC_47 {


    public static void main(String[] args) {



        LC_47 lc47 = new LC_47();
        System.out.println(lc47.permuteUnique(new int[]{1, 1, 2}));
    }

    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> res = new LinkedList<>();
        boolean[] used = new boolean[nums.length];
        Arrays.fill(used, false);
        Arrays.sort(nums);
        bt(res, new LinkedList<>(), nums, used);

        return res;
    }

    void bt(List<List<Integer>> res, LinkedList<Integer> path, int[] nums, boolean[] used) {
        if (path.size() == nums.length) {
            res.add(new LinkedList<>(path));
            return;
        }


        for (int i = 0; i < nums.length; i++) {
            if (used[i]) {
                continue;
            }
            if (i>0&&nums[i]==nums[i-1]&&used[i-1]){
                continue;
            }
            used[i] = true;
            path.add(nums[i]);
            bt(res, path, nums, used);
            used[i] = false;
            path.removeLast();
        }
    }
}
