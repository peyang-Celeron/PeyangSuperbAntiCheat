package ml.peya.plugins.Utils;

import org.bukkit.entity.*;

import java.util.*;

public class CheatDetectNowMeta
{
    private  Player target;
    private  UUID uuids;
    private int VL;
    private boolean canNPC = false;

    public CheatDetectNowMeta(Player target, UUID uuids)
    {
        this.target = target;
        this.uuids = uuids;
    }

    public boolean isCanNPC()
    {
        return canNPC;
    }

    public void setCanNPC(boolean canNPC)
    {
        this.canNPC = canNPC;
    }

    public Player getTarget()
    {
        return target;
    }

    public UUID getUuids()
    {
        return uuids;
    }

    public int addVL()
    {
        VL++;
        return VL;
    }

    public int removeVL()
    {
        VL--;
        return VL;
    }

    public int getVL()
    {
        return VL;
    }
}