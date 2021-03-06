package ml.peya.plugins.Commands.CmdPub;

import ml.peya.plugins.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import org.bukkit.scheduler.*;

import java.util.*;

public class Help
{
    public static void run(CommandSender sender, String label)
    {
        final boolean[] flag = {false};
        sender.sendMessage(MessageEngine.get("base.prefix"));

        ArrayList<String> nodes = sender instanceof Player ? getPlayerNodes() : getNodes();

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for (String node : nodes)
                {
                    if (sender.hasPermission("psac." + node))
                    {
                        sender.sendMessage(MessageEngine.get("command.help." + node, MessageEngine.hsh("label", label)));
                        flag[0] = true; //ここFlagの都合で短縮不可
                    }
                }

                if ((sender.hasPermission("psac.drop") || sender.hasPermission("psac.show")) && sender instanceof Player)
                    sender.sendMessage(MessageEngine.get("command.help.mngIdWarning"));


                if (!flag[0])
                    sender.sendMessage(MessageEngine.get("error.psac.notPage"));
            }
        }.runTaskAsynchronously(PeyangSuperbAntiCheat.getPlugin());

    }

    private static ArrayList<String> getNodes()
    {
        ArrayList<String> nodes = new ArrayList<>();
        nodes.add("report");
        nodes.add("view");
        nodes.add("aurapanic");
        nodes.add("aurabot");
        nodes.add("show");
        nodes.add("drop");
        nodes.add("kick");
        nodes.add("bans");
        nodes.add("testkb");
        return nodes;
    }

    private static ArrayList<String> getPlayerNodes()
    {
        ArrayList<String> nodes = getNodes();
        nodes.add("pull");
        nodes.add("target");
        nodes.add("silentteleport");
        nodes.add("tracking");
        return nodes;
    }
}
