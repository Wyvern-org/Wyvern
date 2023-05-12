package wyvern.util;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * written by ChatGPT. feel free to change anything in here
 */
public class ColorMap
{
    public static final Map<Character, Color> MINECRAFT_COLORS = new HashMap<>();

    static {
        MINECRAFT_COLORS.put('0', Color.rgb(0, 0, 0)); // black
        MINECRAFT_COLORS.put('1', Color.rgb(0, 0, 170)); // dark blue
        MINECRAFT_COLORS.put('2', Color.rgb(0, 170, 0)); // dark green
        MINECRAFT_COLORS.put('3', Color.rgb(0, 170, 170)); // dark aqua
        MINECRAFT_COLORS.put('4', Color.rgb(170, 0, 0)); // dark red
        MINECRAFT_COLORS.put('5', Color.rgb(170, 0, 170)); // dark purple
        MINECRAFT_COLORS.put('6', Color.rgb(255, 170, 0)); // gold
        MINECRAFT_COLORS.put('7', Color.rgb(170, 170, 170)); // gray
        MINECRAFT_COLORS.put('8', Color.rgb(85, 85, 85)); // dark gray
        MINECRAFT_COLORS.put('9', Color.rgb(85, 85, 255)); // blue
        MINECRAFT_COLORS.put('a', Color.rgb(85, 255, 85)); // green
        MINECRAFT_COLORS.put('b', Color.rgb(85, 255, 255)); // aqua
        MINECRAFT_COLORS.put('c', Color.rgb(255, 85, 85)); // red
        MINECRAFT_COLORS.put('d', Color.rgb(255, 85, 255)); // light purple
        MINECRAFT_COLORS.put('e', Color.rgb(255, 255, 85)); // yellow
        MINECRAFT_COLORS.put('f', Color.rgb(255, 255, 255)); // white
    }

    public static Color getColorFromMinecraftCode(char code) {
        return MINECRAFT_COLORS.getOrDefault(code, Color.BLACK);
    }

}
