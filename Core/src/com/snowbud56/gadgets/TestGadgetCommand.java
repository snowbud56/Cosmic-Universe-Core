package com.snowbud56.gadgets;

/*
 * Created by snowbud56 on May 28, 2019
 * Do not change or use this code without permission
 */

import com.snowbud56.command.CommandBase;
import com.snowbud56.gadgets.types.GadgetType;
import com.snowbud56.gadgets.types.KillEffectGadget;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import org.bukkit.entity.Player;

import java.util.List;

public class TestGadgetCommand extends CommandBase {

    public TestGadgetCommand() {
        super(Rank.ADMIN, "killeffect");
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length == 0) {
            p.sendMessage(Chat.prefix + "Test Gadget Commands:");
            p.sendMessage(Chat.help(aliasUsed, "<effect>", "Activates a kill effect on you.", Rank.ADMIN));
        } else {
            StringBuilder search = new StringBuilder();
            for (String msg : args) search.append(msg).append(" ");
            KillEffectGadget gadget = search(search.toString());
            if (gadget == null) {
                p.sendMessage(Chat.prefix + "That is not a valid kill effect!");
            } else
                gadget.activate(p);
        }
    }

    private KillEffectGadget search(String search) {
        List<Gadget> gadgets = GadgetManager.instance.getGadgetList(GadgetType.KILL_EFFECT);
        for (Gadget gadget : gadgets) {
            if (gadget.getName().toLowerCase().startsWith(search.toLowerCase())) {
                return (KillEffectGadget) gadget;
            }
        }
        return null;
    }
}
