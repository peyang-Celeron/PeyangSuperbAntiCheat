package ml.peya.plugins.Gui.Events;

import ml.peya.plugins.Gui.*;
import ml.peya.plugins.*;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.scheduler.*;

import java.util.*;

public class Drop implements Listener
{
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDrop(PlayerDropItemEvent e)
    {
        if (Item.canGuiItem(e.getItemDrop().getItemStack()))
            return;
        e.setCancelled(true);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                Arrays.stream(e.getPlayer().getInventory().getContents()).parallel().filter(stack -> !Item.canGuiItem(stack)).forEachOrdered(stack -> stack.setAmount(0));
                this.cancel();
            }
        }.runTask(PeyangSuperbAntiCheat.getPlugin());
        PeyangSuperbAntiCheat.tracker.remove(e.getPlayer().getName());
        e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(MessageEngine.get("item.tracking.noTarget")));
        e.getPlayer().sendMessage(MessageEngine.get("item.stopTarget"));
    }
}
