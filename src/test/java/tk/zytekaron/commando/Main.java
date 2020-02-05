package tk.zytekaron.commando;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import tk.zytekaron.commando.commands.EvalCommand;
import tk.zytekaron.commando.commands.PingCommand;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Main {
    private static JDA jda;
    private static List<Long> blacklist = List.of();
    
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
        commando.setCommandPredicate(message -> !blacklist.contains(message.getAuthor().getIdLong()));
        
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
        
        // Enable the mention prefix (@Bot#1234 help)
        // Default: true
        commando.useMentionPrefix(true);
        
        // Message updates within this many seconds will also trigger commands
        // Default: 30
        commando.setUpdateTime(30);
        
        // Start or stop this commando
        // Required to run
        commando.setEnabled(true);
        
        // Remove a specific command
        commando.unregisterCommand("n/a");
    }
}