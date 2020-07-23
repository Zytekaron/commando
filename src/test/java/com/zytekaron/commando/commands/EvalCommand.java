package com.zytekaron.commando.commands;

import com.zytekaron.commando.Command;
import com.zytekaron.commando.CommandContext;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class EvalCommand extends Command {
    
    public EvalCommand() {
        setName("eval");
        setAliases("e", "ev");
        setGuildOnly(false);
        setPermissions(Permission.MESSAGE_WRITE, Permission.ADMINISTRATOR);
    }
    
    @Override
    public boolean canUse(CommandContext context) {
        return context.getMessage().getAuthor().getIdLong() == 272659147974115328L;
    }
    
    @Override
    protected void run(CommandContext context) {
        Message message = context.getMessage();
        String prefix = context.getPrefix();
        String command = context.getCommand();
        List<String> args = context.getArgs();
        
        Binding binding = new Binding();
        binding.setProperty("context", context);
        binding.setProperty("ctx", context);
        binding.setProperty("message", message);
        binding.setProperty("prefix", prefix);
        binding.setProperty("command", command);
        binding.setProperty("args", args);
        
        GroovyShell shell = new GroovyShell(binding);
        
        try {
            String result = shell.evaluate(String.join(" ", args)).toString();
            MessageEmbed embed = new EmbedBuilder().setDescription(result).setColor(Color.GREEN).build();
            message.getChannel().sendMessage(embed).submit();
        } catch (Exception e) {
            e.printStackTrace();
            String stack = Arrays.stream(e.getStackTrace()).toString();
            stack = stack.substring(0, 1985);
            String text = "```groovy\n" + stack + "\n```";
            message.getChannel().sendMessage(text).submit();
        }
    }
}