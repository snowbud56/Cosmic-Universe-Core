package com.snowbud56.util;

/*
* Created by snowbud56 on January 09, 2018
* Do not change or use this code without permission
*/

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftMetaBook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.List;

public class BookUtil {

    @SuppressWarnings("unchecked")
    public static void openBook(Player p, String[] jsonpages) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) book.getItemMeta();
        List<IChatBaseComponent> pages;
        try {
            pages = (List<IChatBaseComponent>) CraftMetaBook.class.getDeclaredField("pages").get(bookMeta);
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
            return;
        }
        for (String text : jsonpages) {
            IChatBaseComponent page = IChatBaseComponent.ChatSerializer.a(text);
            pages.add(page);
        }
        bookMeta.setTitle("Custom Book");
        bookMeta.setAuthor("Cosmic Universe");
        book.setItemMeta(bookMeta);
        openBook(p, book);
    }

    private static void openBook(Player p, ItemStack book) {
        int slot = p.getInventory().getHeldItemSlot();
        ItemStack old = p.getInventory().getItem(slot);
        p.getInventory().setItem(slot, book);
        ByteBuf buf = Unpooled.buffer(256);
        buf.setByte(0, (byte)0);
        buf.writerIndex(1);
        PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("MC|BOpen", new PacketDataSerializer(buf));
        ((CraftPlayer)p).getHandle().playerConnection.sendPacket(packet);
        p.getInventory().setItem(slot, old);
    }
}
