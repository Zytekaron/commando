package tk.zytekaron.commando;

import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class CommandContext {
    private Message message;
    private String prefix;
    private String command;
    private List<String> args;
    
    public CommandContext(Message message, String prefix, String command, List<String> args) {
        this.message = message;
        this.prefix = prefix;
        this.command = command;
        this.args = args;
    }
    
    public Message getMessage() {
        return message;
    }
    
    public void setMessage(Message message) {
        this.message = message;
    }
    
    public String getPrefix() {
        return prefix;
    }
    
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
    public String getCommand() {
        return command;
    }
    
    public void setCommand(String command) {
        this.command = command;
    }
    
    public List<String> getArgs() {
        return args;
    }
    
    public void setArgs(List<String> args) {
        this.args = args;
    }
}