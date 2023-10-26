package wyvern.util;

public class AuthDataStore
{
    private static boolean loggedIn = false;
    private static String username;
    private static String jwt;

    public static void login(String username, String jwt)
    {
        loggedIn = true;
        AuthDataStore.username = username;
        AuthDataStore.jwt = jwt;
    }

    public static boolean isLoggedIn()
    {
        return loggedIn;
    }

    public static String getUsername()
    {
        return username;
    }

    public static String getJWT()
    {
        return jwt;
    }
}
