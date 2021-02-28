package com.snowbud56.warden.events;

/*
* Created by snowbud56 on March 17, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.warden.WAC;
import com.snowbud56.warden.checks.CheckResult;
import com.snowbud56.warden.checks.player.FastHeal;
import com.snowbud56.warden.checks.player.FastUse;
import com.snowbud56.warden.util.WACPlayer;
import net.minecraft.server.v1_8_R3.ItemFood;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class PlayerListener implements Listener {

    private static CheckResult result;

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        WACPlayer p = WAC.getPlayer(e.getPlayer());
        if (e.getPlayer().getItemInHand() == null || e.getPlayer().getItemInHand().getType() == Material.AIR) return;
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getPlayer().getItemInHand().getType() == Material.BOW && e.getPlayer().getInventory().contains(Material.ARROW)) {
                p.setBowStart();
                p.setBow(true);
            }
            if (CraftItemStack.asNMSCopy(e.getPlayer().getItemInHand()).getItem() instanceof ItemFood)
                p.setFoodStarting();
        }
    }

    @EventHandler
    public void onItemSwitch(PlayerItemHeldEvent e) {
        WAC.getPlayer(e.getPlayer()).setBow(false);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        WACPlayer u = WAC.getPlayer((Player) e.getEntity());
        CheckResult fasteat = FastUse.runFood(u);
        if (fasteat.failed()) {
            e.setCancelled(true);
            WAC.log(fasteat, u);
            u.clearFoodStarting();
        }
    }

    @EventHandler
    public void playerRegenHealth(EntityRegainHealthEvent e) {
        if (e.getEntityType() == EntityType.PLAYER) {
//            e.getEntity().sendMessage(Chat.prefix + "Last regen: " + (System.currentTimeMillis() - WAC.getPlayer((Player) e.getEntity()).getLastRegen()) + "ms ago");
            CheckResult regen = FastHeal.runCheck((Player) e.getEntity());
            if (regen.failed()) {
                e.setCancelled(true);
                e.setAmount(0);
                WAC.log(regen, WAC.getPlayer((Player) e.getEntity()));
            }
        }
    }

    @EventHandler
    public void onArrowShoot(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            WACPlayer p = WAC.getPlayer((Player) e.getEntity());
            CheckResult fastbow = FastUse.runBow(p);
            if (fastbow.failed()) {
                e.setCancelled(true);
                WAC.log(fastbow, p);
                p.clearBow();
            }
        }
    }
}
