package com.zytekaron.commando;

import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class CommandContext {
    private final Commando commando;
    private final Message message;
    private final String prefix;
    private final String command;
    private final List<String> args;
    
    public CommandContext(Commando commando, Message message, String prefix, String command, List<String> args) {
        this.commando = commando;
        this.message = message;
        this.prefix = prefix;
        this.command = command;
        this.args = args;
    }
    
    public Commando getCommando() {
        return commando;
    }
    
    public Message getMessage() {
        return message;
    }
    
    public String getPrefix() {
        return prefix;
    }
    
    public String getCommand() {
        return command;
    }
    
    public List<String> getArgs() {
        return args;
    }
}