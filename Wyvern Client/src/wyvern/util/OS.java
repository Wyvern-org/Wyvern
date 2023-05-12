package wyvern.util;

public enum OS
{
    Windows,
    Mac,
    Linux,
    Unsupported;

    public static OS getOS()
    {
        String flag = System.getProperty("os.name").toLowerCase();

        if (flag.contains("win"))
            return OS.Windows;
        if (flag.contains("osx") || flag.contains("mac"))
            return OS.Mac;
        if (flag.contains("nix") || flag.contains("nux"))
            return OS.Linux;
        return OS.Unsupported;
    }
}