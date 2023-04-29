package com.owen2k6.chat;

import com.owen2k6.chat.account.Permissions;

public class Permissionworker {
    public static void main(String[] args) {
        System.out.println("Global Permission:"+ (Permissions.GLOBAL_ANNOUNCE.getValue() |
                Permissions.BAN.getValue() |
                Permissions.DELETE_ACCOUNTS.getValue() |
                Permissions.STOP_SERVER.getValue() |
                Permissions.SHOOT_OSAMA_BIN_LADEN.getValue()));
    }
}
