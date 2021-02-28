package com.snowbud56.disguise;

/*
* Created by snowbud56 on January 08, 2018
* Do not change or use this code without permission
*/

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.util.UUIDTypeAdapter;
import com.snowbud56.Core;
import com.snowbud56.disguise.events.PlayerNicknameEvent;
import com.snowbud56.player.PlayerManager;
import com.snowbud56.player.Rank;
import com.snowbud56.util.Chat;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;

public class PlayerDisguise {

    private Player player;
    private String name, skin, value = "", signature = "";
    private Rank rank;
    private EntityPlayer entityPlayer;
    private Boolean activated = false;

//    public PlayerDisguise(Player p, Rank rank, String name, String value, String signature) {
//        this.player = p;
//        this.rank = rank;
//        this.name = name;
//        this.value = value;
//        this.signature = signature;
//    }

    public PlayerDisguise(Player p) {
        this.player = p;
    }
    public PlayerDisguise() {}

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public Rank getRank() {
        return rank;
    }

    public Boolean isActivated() {
        return activated;
    }

    public void activate() {
        PlayerNicknameEvent event = new PlayerNicknameEvent(player);
        Bukkit.getServer().getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            player.sendMessage(Chat.prefix + "Your nickname failed to apply because nicknames have been disabled on this server!");
            return;
        }
        this.activated = true;
        WorldServer worldServer = ((CraftWorld) this.player.getWorld()).getHandle();
        GameProfile gp = new GameProfile(player.getUniqueId(), name);
        if (value.equals("") && signature.equals("") && skin != null) {
            UUID uuid = Bukkit.getOfflinePlayer(skin).getUniqueId();
            try {
                HttpsURLConnection connection = (HttpsURLConnection) new URL(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", UUIDTypeAdapter.fromUUID(uuid))).openConnection();
                if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {
                    String reply = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
                    value = reply.split("\"value\":\"")[1].split("\"")[0];
                    signature = reply.split("\"signature\":\"")[1].split("\"")[0];
                    gp.getProperties().clear();
                    gp.getProperties().put(name, new Property("textures", value, signature));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            gp.getProperties().clear();
            gp.getProperties().put("textures", new Property("textures", value, signature));
        }
        this.entityPlayer = new EntityPlayer(((CraftServer) Bukkit.getServer()).getServer(), worldServer, gp, new PlayerInteractManager(worldServer));
        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) this.player).getHandle()));
            connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, this.entityPlayer));
        }
//        try {
//            PreparedStatement checkStats = Core.getConnection().prepareStatement("select * from disguises where uuid = '" + player.getUniqueId().toString() + "'");
//            ResultSet set = checkStats.executeQuery();
//            if (set.next()) {
//                PreparedStatement updateInfo = Core.getConnection().prepareStatement("update disguises set activated = 'true', name = '" + name + "', value = '" + value + "', signature = '" + signature + "', rank = '" + rank + "' where uuid = '" + player.getUniqueId().toString() + "'");
//                updateInfo.executeUpdate();
//            } else {
//                PreparedStatement addInfo = Core.getConnection().prepareStatement("insert into disguises (activated, uuid, name, value, signature, rank) values ('true', '" + player.getUniqueId().toString() + "', '" + name + "', '" + value + "', '" + signature + "', '" + rank + "')");
//                addInfo.executeUpdate();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            Team team = p.getScoreboard().getTeam(player.getName());
            if (team == null) team = p.getScoreboard().registerNewTeam(player.getName());
            if (team.hasEntry(player.getName())) team.removeEntry(player.getName());
        }
        this.player.setDisplayName(this.name);
        Location loc = this.player.getLocation().clone();
        Location newLoc = Bukkit.getWorlds().get(2).getSpawnLocation();
        new BukkitRunnable() {
            public void run() {
                player.teleport(newLoc);
            }
        }.runTaskLater(Core.getPlugin(), 1L);
        new BukkitRunnable() {
            Player p = player;
            EntityPlayer eh = entityPlayer;
            @Override
            public void run() {
                p.teleport(loc);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Scoreboard board = player.getScoreboard();
                    Team team1 = board.getTeam(p.getName());
                    if (team1 == null) team1 = board.registerNewTeam(p.getName());
                    team1.setPrefix(rank.getTag(false, true, true) + "§f");
                    team1.addEntry(eh.displayName);
                }
            }
        }.runTaskLater(Core.getPlugin(), 4L);
    }

    public void deactivate() {
//        try {
//            PreparedStatement checkStats = Core.getConnection().prepareStatement("select * from disguises where uuid = '" + player.getUniqueId().toString() + "'");
//            ResultSet set = checkStats.executeQuery();
//            if (set.next()) {
//                PreparedStatement updateInfo = Core.getConnection().prepareStatement("update disguises set activated = 'false' where uuid = '" + player.getUniqueId().toString() + "'");
//                updateInfo.executeUpdate();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        this.activated = false;
        Location original = this.player.getLocation().clone();
        for (Player player : Bukkit.getOnlinePlayers()) {
            Scoreboard board = player.getScoreboard();
            Team team = board.getTeam(this.player.getName());
            if (team == null) team = board.registerNewTeam(this.player.getName());
            if (team.hasEntry(this.entityPlayer.displayName)) team.removeEntry(this.entityPlayer.displayName);
            if (!team.hasEntry(this.player.getName())) team.addEntry(this.player.getName());
            team.setPrefix(PlayerManager.getPlayer(this.player).getRank().getTag(false, true, true) + "§f");
        }
        PacketPlayOutPlayerInfo remove = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, this.entityPlayer);
        PacketPlayOutPlayerInfo add = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ((CraftPlayer) this.player).getHandle());
        PacketPlayOutEntityDestroy remove2 = new PacketPlayOutEntityDestroy(this.entityPlayer.getId());
        PacketPlayOutNamedEntitySpawn add2 = new PacketPlayOutNamedEntitySpawn(((CraftPlayer) this.player).getHandle());
        this.player.setDisplayName(this.player.getName());
        for (Player p : Bukkit.getOnlinePlayers()) {
            PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
            connection.sendPacket(remove);
            if (p != this.player) connection.sendPacket(remove2);
            connection.sendPacket(add);
            if (p != this.player) connection.sendPacket(add2);
        }
        if (this.player.isOnline()) {
            Location newLoc = Bukkit.getWorlds().get(2).getSpawnLocation();
            this.player.teleport(newLoc);
            new BukkitRunnable() {
                Player p = player;
                @Override
                public void run() {
                    p.teleport(original);
                }
            }.runTaskLater(Core.getPlugin(), 3L);
        }
        this.entityPlayer = null;
        this.player = null;
    }

    public void update(Player player, Player target) {
        target.hidePlayer(player);
        WorldServer worldServer = ((CraftWorld) player.getWorld()).getHandle();
        GameProfile gp = new GameProfile(player.getUniqueId(), name);
        gp.getProperties().clear();
        gp.getProperties().put("textures", new Property("textures", value, signature));
        EntityPlayer ep = new EntityPlayer(((CraftServer) Bukkit.getServer()).getServer(), worldServer, gp, new PlayerInteractManager(worldServer));
        PlayerConnection connection = ((CraftPlayer) target).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) player).getHandle()));
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ep));
        Scoreboard board = player.getScoreboard();
        Team team = board.getTeam(player.getName());
        if (team == null) team = board.registerNewTeam(player.getName());
        team.setPrefix(rank.getTag(false, true, true) + "§f");
        team.addEntry(ep.displayName);
        target.showPlayer(player);
    }
}
