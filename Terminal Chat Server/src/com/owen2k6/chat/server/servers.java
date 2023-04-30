package com.owen2k6.chat.server;

import com.owen2k6.chat.account.user;

import java.util.List;
import java.util.UUID;

public class servers {
    public String name;
    public String id;
    public List<UUID> members;
    public UUID owner;
    public List<UUID> banned;
    public String serverinvitecode;

}
