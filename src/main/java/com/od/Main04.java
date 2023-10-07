package com.od;


import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

/**
 * 字符消消乐
 *
 *
 *
 *
 *
 */
public class Main04 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String str = scanner.nextLine();
        Stack<Character> stack=new Stack<>();


        for (int i = 0; i < str.toCharArray().length; i++) {
            while (!stack.isEmpty() &&
                    i<str.toCharArray().length&&
                    stack.peek()==str.toCharArray()[i]){
                stack.pop();
                i++;
            }

            if (i<str.toCharArray().length){
                stack.add(str.toCharArray()[i]);
            }
        }

        System.out.println(stack.size());

    }
}
