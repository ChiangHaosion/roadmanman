package com.interview._多线程;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserSessionManager {
    // 使用ThreadLocal存储用户信息
    private static final ThreadLocal<UserInfo> userSession = ThreadLocal.withInitial(UserInfo::new);

    // 设置当前线程的用户信息
    public static void setCurrentUser(UserInfo user) {
        userSession.set(user);
    }

    // 获取当前线程的用户信息
    public static UserInfo getCurrentUser() {
        return userSession.get();
    }

    // 清除当前线程的用户信息
    public static void clearCurrentUser() {
        userSession.remove();
    }

    public static void main(String[] args) {
        // 模拟用户登录
        UserInfo user = new UserInfo("john_doe", "John Doe");
        setCurrentUser(user);

        // 在不同的方法中获取用户信息
        displayUserInfo();
        // ... 其他操作

        // 模拟用户注销
        clearCurrentUser();
    }

    public static void displayUserInfo() {
        UserInfo currentUser = getCurrentUser();
        System.out.println("Current User: " + currentUser.getUsername());
    }
}


@AllArgsConstructor
@NoArgsConstructor
@Data
class UserInfo {
    private String username;
    private String fullName;
}
