package com.snowbud56.npc.command;

/*
* Created by snowbud56 on February 07, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.command.CommandBase;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MobCommand extends CommandBase {

    public MobCommand() {
        super(Rank.ADMIN, "mob", "spawn");
    }

    @SuppressWarnings("deprecated")
    @Override
    public void execute(Player p, String[] args) {
        if (args.length == 0) {
            HashMap<EntityType, Integer> entMap = new HashMap<>();
            int count = 0;
            for (World world : Bukkit.getWorlds()) {
                for (Entity ent : world.getEntities()) {
                    if (!entMap.containsKey(ent.getType())) entMap.put(ent.getType(), 0);
                    entMap.put(ent.getType(), 1 + entMap.get(ent.getType()));
                    count++;
                }
            }
            p.sendMessage(Chat.prefix + "Listing Entities:");
            for (EntityType cur : entMap.keySet()) {
                p.sendMessage(Chat.prefix + cur.name() + ": " + entMap.get(cur));
            }
            p.sendMessage(Chat.prefix + "Total: " + count);
        } else {
            if (args[0].toLowerCase().equals("list")) {
                p.sendMessage(Chat.prefix + "Listing Entities:");
                for (EntityType type : EntityType.values()) {
                    p.sendMessage(Chat.prefix + type);
                }
                return;
            }
            EntityType type;
            try {
                type = EntityType.valueOf(args[0]);
            } catch (IllegalArgumentException e) {
                p.sendMessage(Chat.prefix + "Invalid mob!");
                return;
            }
            HashSet<String> argSet = new HashSet<>();
            for (int i = 1 ; i < args.length ; i++)
                if (args[i].length() > 0)
                    argSet.add(args[i]);
            int count = 1;
            HashSet<String> argHandle = new HashSet<>();
            for (String arg : argSet) {
                try {
                    int newCount = Integer.parseInt(arg);
                    if (newCount <= 0)
                        continue;
                    count = newCount;
                    p.sendMessage(Chat.prefix + "Amount: " + count);
                    argHandle.add(arg);
                    break;
                }
                catch (Exception e) {/*None*/}
            }
            for (String arg : argHandle)
                argSet.remove(arg);
            HashSet<Entity> entSet = new HashSet<>();
            for (int i = 0 ; i < count ; i++) {
                Entity e = p.getWorld().spawnEntity(p.getTargetBlock((Set<Material>) null, 100).getLocation().add(0, 1, 0), type);
                entSet.add(e);
            }
            for (String arg : argSet) {
                if (arg.length() == 0) continue;
                else if (arg.equalsIgnoreCase("baby") || arg.equalsIgnoreCase("b")) {
                    for (Entity ent : entSet) {
                        if (ent instanceof Ageable) ((Ageable) ent).setBaby();
                        else if (ent instanceof Zombie) ((Zombie) ent).setBaby(true);
                    }
                    p.sendMessage(Chat.prefix + "Baby: True");
                    argHandle.add(arg);
                } else if (arg.equalsIgnoreCase("age") || arg.equalsIgnoreCase("lock")) {
                    for (Entity ent : entSet)
                        if (ent instanceof Ageable) {
                            ((Ageable) ent).setAgeLock(true);
                            p.sendMessage(Chat.prefix + "Age: False");
                        }
                    argHandle.add(arg);
                } else if (arg.equalsIgnoreCase("angry") || arg.equalsIgnoreCase("a")) {
                    for (Entity ent : entSet)
                        if (ent instanceof Wolf) ((Wolf) ent).setAngry(true);
                    for (Entity ent : entSet)
                        if (ent instanceof Skeleton)
                            ((Skeleton) ent).setSkeletonType(Skeleton.SkeletonType.WITHER);
                    p.sendMessage(Chat.prefix + "Angry: True");
                    argHandle.add(arg);
                } else if (arg.toLowerCase().charAt(0) == 'p') {
                    try {
                        String prof = arg.substring(1, arg.length());
                        Villager.Profession profession = null;
                        for (Villager.Profession cur : Villager.Profession.values())
                            if (cur.name().toLowerCase().contains(prof.toLowerCase()))
                                profession = cur;
                        p.sendMessage(Chat.prefix + "Profession: " + profession.name());
                        for (Entity ent : entSet)
                            if (ent instanceof Villager)
                                ((Villager) ent).setProfession(profession);
                    } catch (Exception e) {
                        p.sendMessage(Chat.prefix + "Profession: Invalid [" + arg + "] on " + type.name());
                    }
                    argHandle.add(arg);
                } else if (arg.toLowerCase().charAt(0) == 's') {
                    try {
                        String size = arg.substring(1, arg.length());
                        p.sendMessage(Chat.prefix + "Size: " + Integer.parseInt(size));
                        for (Entity ent : entSet)
                            if (ent instanceof Slime)
                                ((Slime) ent).setSize(Integer.parseInt(size));
                    } catch (Exception e) {
                        p.sendMessage(Chat.prefix + "Size: Invalid [" + arg + "] on " + type.name());
                    }
                    argHandle.add(arg);
                } else if (arg.toLowerCase().charAt(0) == 'n' && arg.length() > 1) {
                    try {
                        String name = "";
                        for (char c : arg.substring(1, arg.length()).toCharArray()) {
                            if (c != '_') name += c;
                            else name += " ";
                        }

                        for (Entity ent : entSet) {
                            if (ent instanceof CraftLivingEntity) {
                                CraftLivingEntity cEnt = (CraftLivingEntity) ent;
                                cEnt.setCustomName(name);
                                cEnt.setCustomNameVisible(true);
                            }
                        }
                    } catch (Exception e) {
                        p.sendMessage(Chat.prefix + "Size: Invalid [" + arg + "] on " + type.name());
                    }
                    argHandle.add(arg);
                }
            }
            for (String arg : argHandle)
                argSet.remove(arg);
            for (String arg : argSet)
                p.sendMessage(Chat.prefix + "Unhandled: " + arg);
            p.sendMessage(Chat.prefix + "Spawned " + count + " " + type.getName() + ".");
        }
    }
}
