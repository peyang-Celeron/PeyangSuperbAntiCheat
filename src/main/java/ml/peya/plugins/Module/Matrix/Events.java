package ml.peya.plugins.Module.Matrix;

import me.rerere.matrix.api.*;
import me.rerere.matrix.api.events.*;
import ml.peya.plugins.Detect.*;
import ml.peya.plugins.Enum.*;
import ml.peya.plugins.*;
import org.bukkit.event.*;

public class Events implements Listener
{
    @EventHandler
    public static void vl(PlayerViolationEvent e)
    {
        if (e.getHackType() != HackType.KILLAURA)
            return;
        if (e.getViolations() == 10)
        {
            DetectConnection.scan(e.getPlayer(), DetectType.AURA_BOT, null, false);
            Variables.matrix.setViolations(e.getPlayer(), HackType.KILLAURA, 0);
        }
    }
}
