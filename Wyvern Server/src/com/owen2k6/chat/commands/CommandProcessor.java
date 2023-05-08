package com.owen2k6.chat.commands;

import com.owen2k6.chat.threads.ClientRedux;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CommandProcessor
{
    private final Map<String, Class<? extends CommandHandler>> handlerMap;

    public CommandProcessor()
    {
        handlerMap = new HashMap<>();
    }

    public void registerHandler(String commandLabel, Class<? extends CommandHandler> handlerClass)
    {
        handlerMap.put(commandLabel, handlerClass);
    }

    public void processCommand(ClientRedux sender, String input)
    {
        String[] parts = input.substring(1).split(" ");
        String commandLabel = parts[0];
        String[] arguments = Arrays.copyOfRange(parts, 1, parts.length);

        Class<? extends CommandHandler> handlerClass = handlerMap.get(commandLabel);

        if (handlerClass != null)
        {
            try
            {
                CommandHandler handler = handlerClass.getDeclaredConstructor().newInstance();
                handler.handleCommand(sender, arguments);
            } catch (Exception e) {
                System.err.println("Error processing command: " + e.getMessage());
            }
        } else {
            System.err.println("Unknown command: " + commandLabel);
        }
    }
}
