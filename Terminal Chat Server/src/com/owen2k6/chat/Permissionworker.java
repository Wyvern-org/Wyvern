package com.owen2k6.chat;

import com.owen2k6.chat.account.Permissions;

public class Permissionworker {
    public static void main(String[] args) {
        System.out.println("Operator Permissions:" + ( // legit every permission there is. no point in going higher.
                Permissions.GLOBAL_ANNOUNCE.getValue() |
                        Permissions.BAN.getValue() |
                        Permissions.DELETE_ACCOUNTS.getValue() |
                        Permissions.STOP_SERVER.getValue() |
                        Permissions.CREATE_SERVER.getValue() |
                        Permissions.DELETE_ANY_SERVER.getValue() |
                        Permissions.BAN_OWN_SERVER.getValue() |
                        Permissions.KICK_OWN_SERVER.getValue() |
                        Permissions.CREATE_OWN_ROLE.getValue() |
                        Permissions.DELETE_ANY_ROLE.getValue() |
                        Permissions.CREATE_CHANNELS.getValue()));

        System.out.println("Admin BASE Permissions:" + (
                Permissions.BAN.getValue() |
                        Permissions.GLOBAL_ANNOUNCE.getValue() |
                        Permissions.BAN.getValue() |
                        Permissions.DELETE_ANY_SERVER.getValue() |
                        Permissions.CREATE_SERVER.getValue() |
                        Permissions.BAN_OWN_SERVER.getValue() |
                        Permissions.KICK_OWN_SERVER.getValue() |
                        Permissions.CREATE_CHANNELS.getValue() |
                        Permissions.CREATE_OWN_ROLE.getValue() |
                        Permissions.DELETE_ANY_ROLE.getValue()));
        System.out.println("Approved User BASE Permissions:" + (
                Permissions.CREATE_SERVER.getValue() |
                        Permissions.BAN_OWN_SERVER.getValue() |
                        Permissions.KICK_OWN_SERVER.getValue() |
                        Permissions.CREATE_CHANNELS.getValue() |
                        Permissions.CREATE_OWN_ROLE.getValue() |
                        Permissions.DELETE_ANY_ROLE.getValue()));
    }
}
