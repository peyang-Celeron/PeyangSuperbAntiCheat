package ml.peya.plugins.Commands.CmdPub;

import org.bukkit.*;
import org.bukkit.command.*;

public class Help
{
    public static void run(CommandSender sender, String label)
    {
        sender.sendMessage(ChatColor.AQUA + "-----" +
                ChatColor.GREEN + "[" +
                ChatColor.BLUE + "PeyangSuperbAntiCheat" +
                ChatColor.GREEN + "]" +
                ChatColor.AQUA + "-----");
        if (sender.hasPermission("psr.admin"))
        {
            sender.sendMessage(ChatColor.AQUA + "/" + label + " view");
            sender.sendMessage(ChatColor.GREEN + "レポートされた対処されてない問題を、");
            sender.sendMessage(ChatColor.GREEN + "重大度順に表示します");

            sender.sendMessage(ChatColor.AQUA + "/" + label + " test " + ChatColor.DARK_AQUA + "<PlayerName>");
            sender.sendMessage(ChatColor.GREEN + "プレイヤーが Killauraを使用しているかチェックします。");
            return;
        }

        sender.sendMessage(ChatColor.AQUA + "/report(wdr) " + ChatColor.DARK_AQUA + "<PlayerName>");
        sender.sendMessage(ChatColor.GREEN + "プレイヤーをレポートします。");
    }
}