package ml.peya.plugins;

import com.comphenix.protocol.events.*;
import com.comphenix.protocol.wrappers.*;
import ml.peya.plugins.Utils.*;
import net.minecraft.server.v1_12_R1.*;

import java.lang.reflect.*;

public class Packets
{
    public void onPacketReceiving(PacketEvent e)
    {
        try
        {
            PacketContainer packet = e.getPacket();

            PacketPlayInUseEntity entity = (PacketPlayInUseEntity) packet.getHandle();
            Field field = entity.getClass().getDeclaredField("a");
            field.setAccessible(true);
            int entityId = field.getInt(entity);
            if (e.getPacket().getEntityUseActions().readSafely(0) != EnumWrappers.EntityUseAction.ATTACK) return;
            DetectingList metas = PeyangSuperbAntiCheat.cheatMeta;
            for (CheatDetectNowMeta meta: metas.getMetas())
            {

                if (meta.getId() != entityId)
                    return;
                if (meta.getTarget().getUniqueId() == e.getPlayer().getUniqueId())
                    System.out.println(meta.addVL());
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}