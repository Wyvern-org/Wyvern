package com.owen2k6.chat.event;

public abstract class AbstractEvent
{
    private boolean cancelled;

    public final boolean isCancelled()
    {
        return cancelled;
    }

    public final void setCancelled(boolean flag)
    {
        cancelled = flag;
    }
}
