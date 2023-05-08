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

// so why am i looking at the code for registering?
// to see how it works in order to make the server system work.
// servers are sorta like accounts. see server directory and the account directory.
// how do you want the server system to work?
// User makes new server, they get access and are owner. they can add users with invite codes or some shit idk
// ah
// speaking of which
// smooth brain moment i misread what you said
// the client is just an input system. the server is everything else
// What i mean is the clientside is just an input and the server contains all the clients code.
// ik i mean i misread line 40
// ah
// TODO: letting you know that register user would be of a similar method to creating a server and channel and group system.
// i am really not following what you want me to do with the server making system
// see how registering a user works, making a server would be the same method but with some shit changed over
// ima ask mod on keeping the servers alive but thats all you need to do.
// so what should happen
// it asks for the name of the server and then the system will create a server json file using gson (info present in registering a user)
// k, i was going to say "so what should happen is" and explain what i thought you meant but got distracted
// ah lol
// that works too tho
// so there should be a first time setup for that stuff right
// legit the command will be /new server {servername}
// thats about it.
// so when you download and start up the server app files it doesnt make a server?
// ?
// so when you download the server files and run the server app, it doesnt even start the server until you run teh command?
// (ofc the server would start automatically after because the .json would be saved)
// It wont make a server by default. the users have to make their own servers
// wait are the server and client bundled together?
// The term server is for like "discord server" not the actual server host.
// they are not bundled together.
//
// so you do make a server from the client
// ye with /new server (name) when its finished
// then why didnt you say so when i asked back on line 48 originally (before i changed it)
// i didnt understand your question fully lmao
// Ok i understand now, i think i can do that