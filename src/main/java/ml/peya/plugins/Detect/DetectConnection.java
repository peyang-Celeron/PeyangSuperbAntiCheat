package ml.peya.plugins.Detect;

import ml.peya.plugins.DetectClasses.*;
import ml.peya.plugins.Enum.*;
import ml.peya.plugins.Moderate.*;
import ml.peya.plugins.*;
import ml.peya.plugins.Utils.*;
import net.minecraft.server.v1_12_R1.*;
import org.apache.commons.lang3.tuple.*;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.*;

import java.sql.*;
import java.util.*;

import static ml.peya.plugins.PeyangSuperbAntiCheat.network;

public class DetectConnection
{
    public static CheatDetectNowMeta spawnWithArmor(Player player, DetectType type)
    {
        EntityPlayer uuid = NPC.spawn(player, type);
        CheatDetectNowMeta meta = PeyangSuperbAntiCheat.cheatMeta.add(player, uuid.getUniqueID(), uuid.getId(), type);
        meta.setCanTesting(true);
        return meta;
    }

    public static void scan(Player player, DetectType type, CommandSender sender)
    {
        if (type == DetectType.ANTI_KB)
        {
            TestKnockback.scan(player, type, sender);
            return;
        }

        CheatDetectNowMeta meta = spawnWithArmor(player, type);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                meta.setCanTesting(false);

                double vl = meta.getVL();
                double seconds = PeyangSuperbAntiCheat.cheatMeta.getMetaByPlayerUUID(player.getUniqueId()).getSeconds();

                if (PeyangSuperbAntiCheat.learnCount > PeyangSuperbAntiCheat.learnCountLimit && network.commit(Pair.of(vl, seconds)) > 0.0)
                {
                    learn(vl, seconds);

                    if (kick(player)) return;
                }
                if (PeyangSuperbAntiCheat.learnCount < PeyangSuperbAntiCheat.learnCountLimit && PeyangSuperbAntiCheat.banLeft <= meta.getVL())
                {
                    learn(vl, seconds);

                    if (kick(player)) return;
                }

                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        String name = player.getDisplayName() + (player.getDisplayName().equals(player.getName()) ? "": (" (" + player.getName() + ") "));

                        switch (type)
                        {
                            case AURA_BOT:
                                if (sender == null)
                                    Bukkit.getOnlinePlayers().parallelStream().filter(np -> np.hasPermission("psac.aurabot")).forEachOrdered(np -> np.spigot().sendMessage(TextBuilder.textTestRep(name, meta.getVL(), PeyangSuperbAntiCheat.banLeft).create()));
                                else
                                    sender.spigot().sendMessage(TextBuilder.textTestRep(name, meta.getVL(), PeyangSuperbAntiCheat.banLeft).create());
                                break;

                            case AURA_PANIC:
                                if (sender == null)
                                    Bukkit.getOnlinePlayers().parallelStream().filter(np -> np.hasPermission("psac.aurapanic")).forEachOrdered(np -> np.spigot().sendMessage(TextBuilder.textPanicRep(name, meta.getVL()).create()));
                                else
                                    sender.spigot().sendMessage(TextBuilder.textPanicRep(name, meta.getVL()).create());
                                break;
                        }

                        PeyangSuperbAntiCheat.cheatMeta.remove(meta.getUUIDs());
                        this.cancel();
                    }
                }.runTaskLater(PeyangSuperbAntiCheat.getPlugin(), 10);
                this.cancel();
            }

            private void learn(double vl, double seconds)
            {
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        ArrayList<Triple<Double, Double, Double>> arr = new ArrayList<>();
                        arr.add(Triple.of(vl, seconds, seconds / meta.getVL()));
                        PeyangSuperbAntiCheat.learnCount++;
                        network.learn(arr, 1000);

                        this.cancel();
                    }
                }.runTask(PeyangSuperbAntiCheat.getPlugin());
            }
        }.runTaskLater(PeyangSuperbAntiCheat.getPlugin(), 20 * PeyangSuperbAntiCheat.config.getInt("npc.seconds"));
    }

    private static boolean kick(Player player)
    {
        ArrayList<String> reason = new ArrayList<>();
        try (Connection connection = PeyangSuperbAntiCheat.eye.getConnection();
             Statement statement = connection.createStatement();
             Statement statement1 = connection.createStatement())
        {
            if (WatchEyeManagement.isInjection(player.getName()))
                return false;
            ResultSet rs = statement.executeQuery("SeLeCt * FrOm WaTcHeYe WhErE ID='" + player.getName() + "'");
            while (rs.next())
            {
                ResultSet set = statement1.executeQuery("SeLeCt * FrOm WaTcHrEaSon WhErE MNGID='" +
                        rs.getString("MNGID") + "'");
                while (set.next())
                    reason.add(Objects.requireNonNull(CheatTypeUtils.getCheatTypeFromString(set.getString("REASON"))).getText());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            ReportUtils.errorNotification(ReportUtils.getStackTrace(e));
        }

        ArrayList<String> realReason = new ArrayList<>(new HashSet<>(reason));

        KickUtil.kickPlayer(player, (String.join(", ", realReason).equals("") ? "KillAura": "Report: " + String.join(", ", realReason)), true, false);
        return true;
    }
}
