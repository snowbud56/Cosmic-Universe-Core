package com.snowbud56.util;

/*
* Created by snowbud56 on March 31, 2018
* Do not change or use this code without permission
*/

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collection;

public class PacketUtil {

    public static void sendTitle(Collection<? extends Player> players, int fadein, int stay, int fadeout, String title, String subtitle) {
        PacketPlayOutTitle times = new PacketPlayOutTitle(fadein, stay, fadeout);
        PacketPlayOutTitle titlepacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a(JSONManager.getJSON(title)));
        PacketPlayOutTitle subtitlepacket = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a(JSONManager.getJSON(subtitle)));
        for (Player p : players) {
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(times);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(titlepacket);
            ((CraftPlayer) p).getHandle().playerConnection.sendPacket(subtitlepacket);
        }
    }

    public static void displayParticle(Location loc, EnumParticle particleType, int amount) {
        PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(particleType, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), 0F, 0F, 0F, 0, amount);
        for (Player target : Bukkit.getOnlinePlayers())
            ((CraftPlayer) target).getHandle().playerConnection.sendPacket(particles);
    }

    public static void displayParticle(Location loc, EnumParticle particleType, float speedX, float speedY, float speedZ, int amount) {
        PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(particleType, true, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ(), speedX, speedY, speedZ, 0, amount);
        for (Player target : Bukkit.getOnlinePlayers())
            ((CraftPlayer) target).getHandle().playerConnection.sendPacket(particles);
    }
}
