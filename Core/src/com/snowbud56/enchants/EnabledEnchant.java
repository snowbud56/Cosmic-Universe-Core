package com.snowbud56.enchants;

/*
* Created by snowbud56 on February 04, 2018
* Do not change or use this code without permission
*/

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class EnabledEnchant extends Enchantment {
    public EnabledEnchant() {
        super(101);
    }

    @Override
    public String getName() {
        return "Enabled";
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getStartLevel() {
        return 1;
    }

    @Override
    public EnchantmentTarget getItemTarget() {
        return null;
    }

    @Override
    public boolean conflictsWith(Enchantment enchantment) {
        return false;
    }

    @Override
    public boolean canEnchantItem(ItemStack itemStack) {
        return true;
    }
}
