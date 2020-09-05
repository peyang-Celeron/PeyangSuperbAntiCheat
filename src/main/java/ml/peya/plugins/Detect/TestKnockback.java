package ml.peya.plugins.Detect;

import ml.peya.plugins.*;
import ml.peya.plugins.Enum.*;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.*;
import org.bukkit.entity.*;
import org.bukkit.metadata.*;
import org.bukkit.scheduler.*;

import static ml.peya.plugins.Utils.MessageEngine.get;
import static ml.peya.plugins.Utils.MessageEngine.pair;
import static ml.peya.plugins.Variables.cheatMeta;
import static ml.peya.plugins.Variables.config;

/**
 * ノックバックを確認するクラス。
 */
public class TestKnockback
{
    /**
     * 透明な矢をplayerに対して発射する。
     *
     * @param player プレイヤー。
     * @param type   判定タイプ(不使用)。
     * @param sender イベントsender。
     */
    public static void scan(Player player, DetectType type, CommandSender sender)
    {
        if (type == DetectType.AURA_BOT || type == DetectType.AURA_PANIC)
            return;

        Location location = player.getLocation();
        location.add(0, 1, 0);
        location.setPitch(0);
        location.add(location.getDirection().multiply(-0.1f));

        Arrow arrow = (Arrow) player.getWorld().spawnEntity(location, EntityType.ARROW);
        cheatMeta.add(player, arrow.getUniqueId(), arrow.getEntityId(), DetectType.ANTI_KB);
        arrow.setMetadata("testArrow-" + arrow.getUniqueId(), new FixedMetadataValue(PeyangSuperbAntiCheat.getPlugin(), player.getUniqueId()));
        Bukkit.getOnlinePlayers().parallelStream().map(hide -> ((CraftPlayer) hide).getHandle().playerConnection).forEachOrdered(connection -> connection.sendPacket(new PacketPlayOutEntityDestroy(arrow.getEntityId())));

        arrow.setVelocity(location.getDirection().multiply(32767f));

        cheatMeta.add(player, arrow.getUniqueId(), arrow.getEntityId(), type).setTesting(true);

        scanFinally(player, sender, arrow);
    }

    /**
     * 後始末をする。
     *
     * @param player プレイヤー。
     * @param sender イベントsender。
     * @param arrow  矢。
     */
    private static void scanFinally(Player player, CommandSender sender, Arrow arrow)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                sender.sendMessage(get(config.getBoolean("message.lynx") ? "message.testkb.normal": "message.textkb.lynx", pair("name", player.getName())));
                arrow.remove();
                cheatMeta.remove(arrow.getUniqueId());
            }
        }.runTaskLater(PeyangSuperbAntiCheat.getPlugin(), 20);
    }
}
