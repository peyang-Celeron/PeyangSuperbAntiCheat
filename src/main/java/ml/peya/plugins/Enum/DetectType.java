package ml.peya.plugins.Enum;

import org.bukkit.command.*;

public enum DetectType
{
    AURA_BOT("AuraBot"),
    AURA_PANIC("AuraPanic"),
    ANTI_KB("TestKB");

    private final String name;
    private int count;
    private CommandSender sender;

    DetectType(String name)
    {
        count = 5;
        sender = null;
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public int getPanicCount()
    {
        return count;
    }

    public void setPanicCount(int time)
    {
        this.count = time;
    }

    public CommandSender getSender()
    {
        return sender;
    }

    public void setSender(CommandSender sender)
    {
        this.sender = sender;
    }

}
