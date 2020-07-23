package com.zytekaron.commando;

import com.zytekaron.commando.commands.EvalCommand;
import com.zytekaron.commando.commands.PingCommand;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Main {
    private static final List<Long> blacklist = List.of(123L, 456L);
    private static JDA jda;
    
    public static void main(String[] args) {
        try {
            jda = new JDABuilder().setToken("BOT TOKEN").build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    
        // Create the Commando and give it our JDA instance
        Commando commando = new Commando(jda);
        
        // Register all the commands
        commando.registerCommands(
                new PingCommand(),
                new EvalCommand()
        );
        
        // Whether or not to run this command
        // Useful for blacklists, ratelimits, or similar systems
        // Default: __ -> true
        commando.setCommandPredicate((message, command) -> {
            if (blacklist.contains(message.getAuthor().getIdLong())) return false;
            if (blacklist.contains(message.getGuild().getIdLong())) return false;
            return true;
        });
        
        // Suppliers for prefixes (in order) given Message context
        // Recommended to put database operations at the end
        // Default: [ () -> "!" ]
        commando.setPrefixSupplier(message -> {
            List<Supplier<String>> prefixes = new ArrayList<>();
            prefixes.add(() -> "!");
            prefixes.add(() -> "./");
            return prefixes;
        });
        // commando.setPrefixSupplier(message -> List.of(() -> ".")); for a single prefix
    
        // Set a command pre-processing function (called before the command is executed)
        // Default: none
        commando.setCommandPreProcess(message -> {
            // Make sure there are database entries for the author
            // Set up any data that is required in more than one command
        });
    
        // Set a command post-processing function (called after the command is executed)
        // Default: none
        commando.setCommandPostProcess(message -> {
            // Whatever you need to do after the command has been processed
        });
        
        // Set a fallback handler for messages that
        // - don't start with a prefix / contain a command
        // - don't pass the CommandPredicate
        // Default: none
        commando.setFallback(message -> {});
        
        // Message updates within this many seconds will also trigger commands
        // Set to 0 to disable
        // Default: 30
        commando.setUpdateTime(30);
        
        // Start or stop this commando
        // Required to run
        commando.setEnabled(true);
        
        // Remove a specific command
        commando.unregisterCommand("ping");
    }
}