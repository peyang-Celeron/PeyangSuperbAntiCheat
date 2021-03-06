// Compass Tracker 3000™
//
// © 2020 Peyang.
//
// This source code licensed by GNU General Public License 3.0.

package ml.peya.plugins.Gui.Items.Target;

import ml.peya.plugins.Gui.Item;
import ml.peya.plugins.Gui.*;
import ml.peya.plugins.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

public class CompassTracker3000_tm implements IItems
{
    @Override
    public void run(Player player, String target)
    {
        player.performCommand("tpto " + target);
    }

    @Override
    public ItemStack getItem(String target)
    {
        ItemStack item = new ItemStack(Material.COMPASS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(MessageEngine.get("item.compass"));

        meta.setLore(Item.getLore(this, target));

        item.setItemMeta(meta);
        return item;
    }

    @Override
    public boolean canSpace()
    {
        return false;
    }

    @Override
    public String getExecName()
    {
        return "TRACKER";
    }

    @Override
    public Type getType()
    {
        return Type.TARGET;
    }
}
