package com.owen2k6.chat.account;

public enum Permissions
{
    GLOBAL_ANNOUNCE(0b1),
    BAN(0b10),
    DELETE_ACCOUNTS(0b100),
    STOP_SERVER(0b1000),
    CREATE_SERVER(0b10000),
    DELETE_ANY_SERVER(0b100000),
    CREATE_CHANNELS(0b1000000),
    BAN_OWN_SERVER(0b10000000),
    KICK_OWN_SERVER(0b100000000),
    CREATE_OWN_ROLE(0b1000000000),
    DELETE_ANY_ROLE(0b10000000000);


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