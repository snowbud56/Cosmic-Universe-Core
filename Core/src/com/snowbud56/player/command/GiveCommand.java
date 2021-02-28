package com.snowbud56.player.command;

/*
* Created by snowbud56 on February 07, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.command.CommandBase;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import com.snowbud56.util.ItemFactory;
import com.snowbud56.util.ItemUtil;
import com.snowbud56.util.PlayersUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GiveCommand extends CommandBase {

    public GiveCommand() {
        super(Rank.ADMIN, "give");
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(Chat.prefix + "Usage: /give <player | all> <item name> <amount> [enchantments]");
        } else if (args.length == 1)
            give(player, player.getDisplayName(), args[0], "1", "");

        else if (args.length == 2)
            give(player, args[0], args[1], "1", "");

        else if (args.length == 3)
            give(player, args[0], args[1], args[2], "");

        else
            give(player, args[0], args[1], args[2], args[3]);
    }

    private void give(Player player, String target, String itemNames, String amount, String enchants) {
        LinkedList<Map.Entry<Material, Byte>> itemList = ItemUtil.matchItem(player, itemNames, true);
        if (itemList.isEmpty())
            return;
        List<String> giveList = new ArrayList<>();
        if (target.equalsIgnoreCase("all")) {
            for (Player cur : Bukkit.getOnlinePlayers())
                giveList.add(cur.getName());
        }
        else {
            giveList = PlayersUtil.findPlayer(player, target, false);
            if (giveList.size() != 1) {
                player.sendMessage(Chat.playerNotFound(target, giveList));
                return;
            }
            Player tName = Bukkit.getPlayer(giveList.get(0));
            giveList.clear();
            giveList.add(tName.getDisplayName());
        }
        int count = 1;
        try {
            count = Integer.parseInt(amount);
            if (count < 1) {
                player.sendMessage(Chat.prefix + "Invalid amount! Defaulting to 1.");
                count = 1;
            }
        }
        catch (Exception e) {
            player.sendMessage(Chat.prefix + "Invalid amount! Defaulting to 1.");
        }
        HashMap<Enchantment, Integer> enchs = new HashMap<>();
        if (enchants.length() > 0) {
            for (String cur : enchants.split(",")) {
                try {
                    String[] tokens = cur.split(":");
                    enchs.put(Enchantment.getByName(tokens[0]), Integer.parseInt(tokens[1]));
                }
                catch (Exception e) {
                    player.sendMessage(Chat.prefix + "Invalid enchantment: " + Chat.cRed + cur);
                }
            }
        }
        String givenList = giveList.toString().replace("[", "").replace("]", "");
        List<Player> glist = new ArrayList<>();
        for (String p : giveList) glist.add(Bukkit.getPlayer(p));
        for (Map.Entry<Material, Byte> curItem : itemList) {
            for (Player cur : glist) {
                ItemStack stack = new ItemFactory(curItem.getKey()).amount(count).data(curItem.getValue()).unbreakable(true).buildItem();
                stack.addUnsafeEnchantments(enchs);
                cur.getInventory().addItem(stack);
                cur.updateInventory();
                cur.sendMessage(Chat.prefix + "You have received " + Chat.cRed + count + " " + curItem.getKey().name() + Chat.cGray + " from " + Chat.cRed + player.getDisplayName());
            }
            if (target.equalsIgnoreCase("all"))
                player.sendMessage(Chat.prefix + "You gave " + Chat.cRed + count + " " + curItem.getKey().name() + Chat.cGray + " to " + Chat.cRed + "ALL" + Chat.cGray + ".");
            else if (giveList.size() > 1)
                player.sendMessage(Chat.prefix + "You gave " + Chat.cRed + count + " " + curItem.getKey().name() + Chat.cGray + " to " + Chat.cRed + givenList + Chat.cGray + ".");
            else
                player.sendMessage(Chat.prefix + "You gave " + Chat.cRed + count + " " + curItem.getKey().name() + Chat.cGray + " to " + Chat.cRed + glist.get(0).getDisplayName() + Chat.cGray + ".");
        }
    }
}
