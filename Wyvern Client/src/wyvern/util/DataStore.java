package wyvern.util;

import java.util.HashMap;

public class DataStore
{
    private final HashMap<String, Object> objects = new HashMap<>();
    private final HashMap<String, String> strings = new HashMap<>();
    private final HashMap<String, Integer> ints = new HashMap<>();
    private final HashMap<String, Boolean> bools = new HashMap<>();

    public void setObject(String key, Object value)
    {
        objects.put(key, value);
    }

    public Object getObject(String key, Object defaultValue)
    {
        return objects.getOrDefault(key, defaultValue);
    }

    public void setString(String key, String value)
    {
        strings.put(key, value);
    }

    public String getString(String key, String defaultValue)
    {
        return strings.getOrDefault(key, defaultValue);
    }

    public void setInt(String key, int value)
    {
        ints.put(key, value);
    }

    public int getInt(String key, int defaultValue)
    {
        return ints.getOrDefault(key, defaultValue);
    }

    public void setBool(String key, boolean value)
    {
        bools.put(key, value);
    }

    public boolean getBool(String key, boolean defaultValue)
    {
        return bools.getOrDefault(key, defaultValue);
    }

    public boolean hasObject(String key)
    {
        return objects.containsKey(key);
    }

    public boolean hasString(String key)
    {
        return strings.containsKey(key);
    }

    public boolean hasInt(String key)
    {
        return ints.containsKey(key);
    }

    public boolean hasBool(String key)
    {
        return bools.containsKey(key);
    }
}
