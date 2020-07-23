package com.zytekaron.commando;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Command {
    private String name;
    private String description;
    private String usage;
    private List<String> aliases = new ArrayList<>();
    private List<Permission> permissions = new ArrayList<>();
    private boolean guildOnly = false;
    
    public void execute(Commando commando, Message message, String prefix, String command, List<String> args) {
        CommandContext context = new CommandContext(commando, message, prefix, command, args);
        if (!message.isFromGuild() && guildOnly) {
            noDirectMessage(context);
            return;
        }
        if (!canUse(context)) {
            noPermission(context);
            return;
        }
        run(context);
    }
    
    protected boolean canUse(CommandContext context) {
        return true;
    }
    
    protected void noDirectMessage(CommandContext context) {
        String prefix = context.getPrefix();
        String command = context.getCommand();
        context.getMessage().getChannel().sendMessage(prefix + command + " must be used in a server!").queue();
    }
    
    protected void noPermission(CommandContext context) {
        String prefix = context.getPrefix();
        String command = context.getCommand();
        context.getMessage().getChannel().sendMessage("You don't have permission to use " + prefix + command).queue();
    }
    
    protected abstract void run(CommandContext context);
    
    public String getName() {
        return name;
    }
    
    protected void setName(String name) {
        this.name = name.toLowerCase();
    }
    
    public String getDescription() {
        return description;
    }
    
    protected void setDescription(String description) {
        this.description = description;
    }
    
    public String getUsage() {
        return usage;
    }
    
    protected void setUsage(String usage) {
        this.usage = usage;
    }
    
    public List<String> getAliases() {
        return aliases;
    }
    
    private void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }
    
    protected void setAliases(String... aliases) {
        setAliases(Arrays.asList(aliases));
    }
    
    public List<Permission> getPermissions() {
        return permissions;
    }
    
    private void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }
    
    protected void setPermissions(Permission... permissions) {
        setPermissions(Arrays.asList(permissions));
    }
    
    public boolean isGuildOnly() {
        return guildOnly;
    }
    
    protected void setGuildOnly(boolean guildOnly) {
        this.guildOnly = guildOnly;
    }
}