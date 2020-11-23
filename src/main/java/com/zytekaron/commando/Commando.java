package com.zytekaron.commando;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Commando extends ListenerAdapter {
    private final Map<String, Command> commands = new HashMap<>();
    private final Map<String, String> mappings = new HashMap<>();
    private PrefixSupplier prefixSupplier = message -> List.of(() -> "!");
    private BiPredicate<Message, Command> commandPredicate = null;
    private Consumer<Message> commandPreProcess = null;
    private Consumer<Message> commandPostProcess = null;
    private Consumer<Message> fallback;
    private final JDA jda;
    private int updateTime = 30;
    
    public Commando(JDA jda) {
        this.jda = jda;
    }
    
    public void registerCommands(Command... cmds) {
        for (Command command : cmds) {
            registerCommand(command);
        }
    }
    
    private void registerCommand(Command command) {
        String name = command.getName();
        commands.put(name, command);
        for (String alias : command.getAliases()) {
            if (mappings.containsKey(alias)) {
                throw new CommandoException("Duplicate command alias '" + alias + "' (found in '" + mappings.get(alias) + "' and '" + name + "'");
            }
            mappings.put(alias, name);
        }
    }
    
    public void unregisterCommand(String name) {
        Command command = commands.get(name);
        if (command == null) return;
        commands.remove(name);
        for (String alias : command.getAliases()) {
            mappings.remove(alias);
        }
    }
    
    @Override
    public void onGenericMessage(@NotNull GenericMessageEvent event) {
        Message message;
        if (event instanceof MessageReceivedEvent) {
            message = ((MessageReceivedEvent) event).getMessage();
        } else if (event instanceof MessageUpdateEvent) {
            if (updateTime == 0) return;
            message = ((MessageUpdateEvent) event).getMessage();
            OffsetDateTime now = OffsetDateTime.now();
            Duration diff = Duration.between(message.getTimeCreated(), now);
            if (diff.toSeconds() > updateTime) return;
        } else {
            return;
        }
        execute(message);
    }
    
    private void execute(Message message) {
        if (message.getAuthor().isBot()) return;
        if (message.getContentRaw().isEmpty()) return;
        
        for (Supplier<String> supplier : prefixSupplier.getPrefixes(message)) {
            try {
                // Get a prefix
                String prefix = supplier.get();
                if (prefix == null) continue;
                // Continue checking if the prefix isn't here
                if (!message.getContentRaw().startsWith(prefix.toLowerCase())) continue;

                // Prepare data
                String content = message.getContentRaw().substring(prefix.length()).trim();
                List<String> args = new ArrayList<>(Arrays.asList(content.split(" +")));
                String command = args.remove(0).toLowerCase();

                // Fetch the command
                Command cmd = getCommand(command);
                if (cmd != null) {
                    // Check if this command can be used
                    if (commandPredicate != null && !commandPredicate.test(message, cmd)) {
                        // If not, break the loop and continue to fallback
                        break;
                    }
                    // Call the pre-processing function, if one exists
                    if (commandPreProcess != null) {
                        commandPreProcess.accept(message);
                    }
                    // Attempt to run the command
                    try {
                        cmd.execute(this, message, prefix, command, args);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Call the post-processing function, if one exists
                    if (commandPostProcess != null) {
                        commandPostProcess.accept(message);
                    }
                    // Return, so the fallback isn't called once the command is run
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // If there was no command, process this message normally
        if (fallback != null) {
            fallback.accept(message);
        }
    }
    
    public Command getCommand(String name) {
        String query = mappings.getOrDefault(name, name);
        return commands.get(query.toLowerCase());
    }
    
    public void setEnabled(boolean enabled) {
        if (enabled) {
            jda.addEventListener(this);
        } else {
            jda.removeEventListener(this);
        }
    }
    
    public Map<String, Command> getCommands() {
        return commands;
    }
    
    public Map<String, String> getMappings() {
        return mappings;
    }
    
    public void setUpdateTime(int seconds) {
        this.updateTime = seconds;
    }
    
    public void setPrefixSupplier(PrefixSupplier supplier) {
        this.prefixSupplier = supplier;
    }
    
    public void setCommandPredicate(BiPredicate<Message, Command> predicate) {
        this.commandPredicate = predicate;
    }
    
    public void setCommandPreProcess(Consumer<Message> commandPreProcess) {
        this.commandPreProcess = commandPreProcess;
    }
    
    public void setCommandPostProcess(Consumer<Message> commandPostProcess) {
        this.commandPostProcess = commandPostProcess;
    }
    
    public void setFallback(Consumer<Message> fallback) {
        this.fallback = fallback;
    }
}