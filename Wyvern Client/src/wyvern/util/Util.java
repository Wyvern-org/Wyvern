package wyvern.util;

import java.util.Objects;

public class Util
{
    public static String getLinuxHomeDirectory()
    {
        String linuxHomeDir = System.getenv("user.home");
        // $HOME environment variable should exist, but handle the situation when
        // it doesn't exist and/or the user executes program as root.
        String linux_home = System.getenv("HOME");
        if (linux_home == null)
        {
            String linux_user = System.getenv("USER");
            if (Objects.equals(linux_user, "root"))
                linuxHomeDir = "/root";
            else
                linuxHomeDir = "/home/" + linux_user;
        } else
            linuxHomeDir = linux_home;
        return linuxHomeDir;
    }

    public static String getInstallDirectory()
    {
        switch (OS.getOS())
        {
            default:
                System.out.println("Unknown operating system (assuming Windows).");
                return backslashes(System.getProperty("user.home") + "/AppData/Roaming/.wyvern/");
            case Windows:
                return backslashes(System.getProperty("user.home") + "/AppData/Roaming/.wyvern/");
            case Mac:
                return "~/Library/Application Support/wyvern/";
            case Linux:
                return getLinuxHomeDirectory() + "/.wyvern/";
            case Unsupported:
                System.out.println("Unsupported operating system (assuming Linux).");
                return getLinuxHomeDirectory() + "/.wyvern/";
        }
    }

    /**
     * hacky path fix for windows, but i don't think it's necessary anymore
     * @param input
     * @return
     */
    private static String backslashes(String input)
    {
        return input.replaceAll("/", "\\\\");
    }
}
