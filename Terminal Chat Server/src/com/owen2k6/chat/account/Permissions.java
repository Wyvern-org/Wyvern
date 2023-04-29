package com.owen2k6.chat.account;

public enum Permissions
{
    GLOBAL_ANNOUNCE(0b1),
    BAN(0b10),
    DELETE_ACCOUNTS(0b100),
    STOP_SERVER(0b1000),
    SHOOT_OSAMA_BIN_LADEN(0b10000);

    Permissions(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }

    public static boolean hasPermission(int input, Permissions permission)
    {
        return (input & permission.getValue()) > 0;
    }

    private final int value;
}