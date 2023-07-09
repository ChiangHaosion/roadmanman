package com.leetcode;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author HaosionChiang
 * @Date 2023/5/21
 **/
public class H {

    public void priorityTest(){
        PriorityQueue<String> priority = new PriorityQueue<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.charAt(0)==o2.charAt(0)){
                    return Integer.parseInt(o1.substring(1,o1.length()))-Integer.parseInt(o2.substring(1,o2.length()));
                }else {
                    if (o1.startsWith("v")){
                        return -1;
                    }
                    return 1;
                }
            }
        });
        PriorityQueue<String> priority1 = new PriorityQueue<>();
        priority.offer("a11");
        priority.offer("a2");
        priority.offer("v1");

        while (!priority.isEmpty()){
            System.out.println(priority.poll());
        }

    }




    public int[] searchRange(int[] nums, int target) {

        int[] res = {-1, -1};
        int left = 0;
        int right = nums.length - 1;

        int i = 0;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] < target) {
                left = mid + 1;
            } else if (nums[mid] > target) {
                right = mid - 1;
            } else {
                res[i++] = mid;
            }
        }

        return res;
    }


    public String minWindow(String s, String t) {
        Map<Character, Integer> needs = new HashMap<>();
        for (int m = 0; m < t.length(); m++) {
            needs.put(t.charAt(m), needs.getOrDefault(t.charAt(m), 0) + 1);
        }
        int valid = 0; // 窗口中满足需要的字符个数
        int i = 0;
        int minLen = Integer.MAX_VALUE;
        int start=0;
        Map<Character, Integer> window = new HashMap<>();
        for (int j = 0; j < s.length(); ) {
            char c=t.charAt(j);
            j++;
            if (needs.containsKey(c)) {
                window.put(c, needs.getOrDefault(c, 0) + 1);
                if (window.get(c).equals(needs.get(c))) {
                    valid++;
                }
            }
            //左侧是否收缩
            while (needs.size() == valid) {
                if (j - i < minLen) {
                    minLen = j - i;
                    start=i;
                }
                char d=s.charAt(i);
                i++;
                if (needs.containsKey(d)){
                    if (window.get(d).equals(needs.get(d))){
                        valid--;
                    }
                    window.put(d,window.get(d)-1);
                }

            }


        }
        return minLen==Integer.MAX_VALUE?"":s.substring(start,minLen);


    }
    public int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> map=new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i],map.getOrDefault(nums[i],0)+1);
        }

        PriorityQueue pq = new PriorityQueue(Comparator.comparingInt(map::get));

        for (Integer integer : map.keySet()) {
            pq.offer(integer);
            if (pq.size()>k){
                pq.poll();
            }

        }
        int[] result = new int[k];
        for (int i = k - 1; i >= 0; i--) {
            result[i] = (int) pq.poll();
        }
        return result;


    }
    public String removeDuplicates(String s) {
        Stack stack = new Stack();
        stack.push(s.charAt(0));
        for (int i = 1; i < s.length(); i++) {
            if (!stack.isEmpty()&&stack.peek().equals(s.charAt(i))) {
                stack.pop();
            } else {
                stack.push(s.charAt(i));
            }
        }
        String str = "";

        while (!stack.isEmpty()) {
            str = str+stack.pop();
        }
        return str;

    }
    public static void main(String[] args) {
        H h = new H();


        System.out.println(h.removeDuplicates("abbaca"));


//        PriorityQueue pq = new PriorityQueue(Comparator.comparingInt(e -> Integer.parseInt((String) e)));
//        h.priorityTest();
//        h.searchRange(new int[]{2, 3, 4, 8, 8, 9}, 8);

//        h.minWindow("AESGSKLJBASCANBC","ABC");
//
//        List<List<Integer>> res1 = new LinkedList<>();
//
//        List<List<Integer>> collect = res1.stream().sorted().distinct().collect(Collectors.toList());
//
//        System.out.println(h.letterCombinations("123"));
    }

    public List<String> letterCombinations(String digits) {
        //返回值
        List<String> res = new ArrayList<String>();
        digits = digits.replaceAll("0|1|#|\\*", "");
        Map<String, String> map = new HashMap<String, String>();
        map.put("2", "abc");
        map.put("3", "def");
        map.put("4", "ghi");
        map.put("5", "jkl");
        map.put("6", "mno");
        map.put("7", "pqrs");
        map.put("8", "tuv");
        map.put("9", "wxyz");
        int[] r121 = new int[]{-1, -1};
        backTracking(res, new StringBuilder(), digits, map, 0);
        return res;


    }

    void backTracking(List<String> res, StringBuilder sb, String digits, Map<String, String> map, int index) {
        if (index == digits.length()) {
            res.add(sb.toString());
            return;
        }
        String str = map.get(String.valueOf(digits.charAt(index)));

        for (int i = 0; i < str.length(); i++) {
            sb.append(str.charAt(i));
            backTracking(res, sb, digits, map, index + 1);
            sb.deleteCharAt(sb.length() - 1);
        }
    }

}
