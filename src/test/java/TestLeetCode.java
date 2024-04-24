import org.junit.Test;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author HaosionChiang
 * @Date 2023/4/11
 **/
public class TestLeetCode {

    public int climbStairs(int n) {
        //dp[i]=dp[i-1]+dp[i-2]
        int[] dp = new int[n + 1];
        dp[1] = 1;
        dp[2] = 2;
        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }

    private static AtomicInteger count=new AtomicInteger(0);

    @Test
    public void test20230822() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                count.getAndIncrement();
            }
        });

      Thread t2=  new Thread(()->{
            for (int i = 0; i < 10; i++) {
                count.getAndIncrement();
            }
        });
      t1.start();
      t2.start();
      t1.join();
      t2.join();
      System.out.println(count);

    }


    @Test
    public void test(){
        climbStairs(1);
        System.out.println(Integer.toBinaryString(5));
        System.out.println(Integer.toBinaryString(5<<2));
    }
    public static void main(String[] args) {
        //lengthOfLongestSubstring("abcabcbb");
        lengthOfLongestSubstringv3("abcabcbb");
    }
    public static int lengthOfLongestSubstring(String s) {
        Set<Character> set = new HashSet<>();
        int left = 0, right = 0, maxLength = 0;
        while (right < s.length()) {
            //如果右指针指向的字符不在集合中，则扩展窗口，将字符添加到集合中
            char c = s.charAt(right);
            if (!set.contains(c)) {
                set.add(c);
                right++;
                maxLength = Math.max(maxLength, set.size()); //更新最长子串的长度
            }
            //如果右指针指向的字符已经在集合中，则缩小窗口，将左指针指向的字符从集合中移除
            else {
                set.remove(s.charAt(left));
                left++;
            }
            System.out.printf("window: [%d, %d)\n", left, right);

        }
        return maxLength;
    }

    public static int lengthOfLongestSubstringv2(String s) {
        Map<Character, Integer> window = new HashMap<>();

        int left = 0, right = 0;
        int res = 0; // 记录结果
        while (right < s.length()) {
            char c = s.charAt(right);
            right++;
            // 进行窗口内数据的一系列更新
            window.put(c, window.getOrDefault(c, 0) + 1);
            // 判断左侧窗口是否要收缩
            while (window.get(c) > 1) {
                char d = s.charAt(left);
                left++;
                // 进行窗口内数据的一系列更新
                window.put(d, window.get(d) - 1);
            }
            System.out.printf("window: [%d, %d)\n", left, right);
            // 在这里更新答案
            res = Math.max(res, right - left);
        }
        return res;
    }


    public static int lengthOfLongestSubstringv3(String s) {
        Map<Character, Integer> window = new HashMap<>();
        int maxres=0;

        int start=0;
        int end=0;

        while (end<s.length()){
            char c = s.charAt(end);
            if (window.containsKey(c)){
                //重复了
                start=Math.max(start,window.get(c)+1);
            }else{
                window.put(c,end);
            }
            maxres=Math.max(maxres,end-start+1);
            end++;
        }
        return maxres;
    }


}
