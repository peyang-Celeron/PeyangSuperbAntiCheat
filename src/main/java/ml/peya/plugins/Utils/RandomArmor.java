package ml.peya.plugins.Utils;

import jdk.internal.dynalink.beans.*;
import ml.peya.plugins.*;
import net.citizensnpcs.api.ai.tree.*;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.trait.*;
import net.citizensnpcs.api.trait.trait.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import java.util.*;

public class RandomArmor
{
    public static ItemStack getHelmet()
    {
        ArrayList<Material> helmets = new ArrayList<>();
        helmets.add(Material.CHAINMAIL_HELMET);
        helmets.add(Material.DIAMOND_HELMET);
        helmets.add(Material.GOLD_HELMET);
        helmets.add(Material.GOLD_HELMET);
        helmets.add(Material.IRON_HELMET);
        helmets.add(Material.LEATHER_HELMET);
        helmets.add(Material.AIR);


        return getRandomItems(helmets);
    }

    public static ItemStack getChestPlate()
    {
        ArrayList<Material> chestPlates = new ArrayList<>();
        chestPlates.add(Material.CHAINMAIL_CHESTPLATE);
        chestPlates.add(Material.DIAMOND_CHESTPLATE);
        chestPlates.add(Material.GOLD_CHESTPLATE);
        chestPlates.add(Material.IRON_CHESTPLATE);
        chestPlates.add(Material.LEATHER_CHESTPLATE);
        chestPlates.add(Material.AIR);

        return getRandomItems(chestPlates);
    }

    public static ItemStack getLeggings()
    {
        ArrayList<Material> leggings = new ArrayList<>();
        leggings.add(Material.LEATHER_LEGGINGS);
        leggings.add(Material.CHAINMAIL_LEGGINGS);
        leggings.add(Material.DIAMOND_LEGGINGS);
        leggings.add(Material.GOLD_LEGGINGS);
        leggings.add(Material.IRON_LEGGINGS);
        leggings.add(Material.AIR);

        return getRandomItems(leggings);
    }

    public static ItemStack getBoots()
    {
        ArrayList<Material> boots = new ArrayList<>();
        boots.add(Material.CHAINMAIL_BOOTS);
        boots.add(Material.DIAMOND_BOOTS);
        boots.add(Material.GOLD_BOOTS);
        boots.add(Material.IRON_BOOTS);
        boots.add(Material.LEATHER_BOOTS);
        boots.add(Material.AIR);

        return getRandomItems(boots);
    }

    public static ItemStack getSwords()
    {
        ArrayList<Material> swords = new ArrayList<>();
        swords.add(Material.DIAMOND_SWORD);
        swords.add(Material.STONE_SWORD);
        swords.add(Material.GOLD_SWORD);
        swords.add(Material.IRON_SWORD);
        swords.add(Material.WOOD_SWORD);
        swords.add(Material.DIAMOND_AXE);
        swords.add(Material.GOLD_AXE);
        swords.add(Material.IRON_AXE);
        swords.add(Material.STONE_AXE);
        swords.add(Material.WOOD_AXE);
        swords.add(Material.AIR);

        return getRandomItems(swords);
    }

    public static ItemStack getRandomItems(ArrayList<Material> items)
    {
        Random random = new Random();
        ItemStack stack = new ItemStack(items.get(random.nextInt(items.size() - 1)), 1);
        if (stack.getType() == Material.LEATHER_BOOTS || stack.getType() == Material.LEATHER_CHESTPLATE || stack.getType() == Material.LEATHER_HELMET || stack.getType() == Material.LEATHER_LEGGINGS)
        {
            LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
            meta.setColor(Color.fromRGB(random.nextInt(254),
                    random.nextInt(254),
                    random.nextInt(254)));
            stack.setItemMeta(meta);
        }

        return stack;
    }

    public static void setRandomArmor(NPC npc )
    {
        Equipment equipment = npc.getTrait(Equipment.class);
        equipment.set(Equipment.EquipmentSlot.HELMET, getHelmet());
        equipment.set(Equipment.EquipmentSlot.CHESTPLATE, getChestPlate());
        equipment.set(Equipment.EquipmentSlot.LEGGINGS, getLeggings());
        equipment.set(Equipment.EquipmentSlot.BOOTS, getBoots());

        equipment.set(Equipment.EquipmentSlot.HAND, getSwords());


    }
}