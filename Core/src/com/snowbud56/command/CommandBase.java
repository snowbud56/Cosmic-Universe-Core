package com.snowbud56.command;

/*
* Created by snowbud56 on January 10, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.player.Rank;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class CommandBase implements Command {

    private List<String> aliases;
    private Rank requiredRank;
    private Rank[] specificRank;
    protected String aliasUsed;

    public CommandBase(Rank requiredRank, String...aliases) {
        this.requiredRank = requiredRank;
        this.aliases = Arrays.asList(aliases);
    }

    public CommandBase(Rank requiredRank, Rank[] specificRank, String...aliases) {
        this.requiredRank = requiredRank;
        this.specificRank = specificRank;
        this.aliases = Arrays.asList(aliases);
    }

    @Override
    public Collection<String> getAliases() {
        return aliases;
    }

    @Override
    public Rank getRequiredRank() {
        return requiredRank;
    }

    @Override
    public void setAliasUsed(String alias) {
        this.aliasUsed = alias;
    }

    @Override
    public Rank[] getSpecificRanks() {
        return specificRank;
    }

    @Override
    public List<String> onTabComplete(Player p, String[] args) {
        return null;
    }
}
