package tk.zytekaron.commando;

import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class CommandContext {
    private Commando commando;
    private Message message;
    private String prefix;
    private String command;
    private List<String> args;
    
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