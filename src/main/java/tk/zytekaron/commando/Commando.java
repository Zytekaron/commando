package tk.zytekaron.commando;

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
import java.util.function.Supplier;

public class Commando extends ListenerAdapter {
    private Map<String, Command> commands = new HashMap<>();
    private Map<String, String> mappings = new HashMap<>();
    private PrefixSupplier prefixSupplier = message -> List.of(() -> "!");
    private BiPredicate<Message, Command> commandPredicate = null;
    private JDA jda;
    private boolean mentionPrefix = true;
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
        if (message.getContentRaw().equals("")) return;
        
        List<Supplier<String>> suppliers = new ArrayList<>();
        if (mentionPrefix) {
            suppliers.add(() -> "<@" + jda.getSelfUser().getId() + ">");
            suppliers.add(() -> "<@!" + jda.getSelfUser().getId() + ">");
        }
        suppliers.addAll(prefixSupplier.getPrefixes(message));
        
        for (Supplier<String> supplier : suppliers) {
            try {
                String prefix = supplier.get();
                if (prefix == null) continue;
                if (!message.getContentRaw().startsWith(prefix.toLowerCase())) continue;

                String content = message.getContentRaw().substring(prefix.length()).trim();
                List<String> args = new ArrayList<>(Arrays.asList(content.split(" ")));
                String command = args.get(0);
                args.remove(0);

                Command cmd = getCommand(command);
                if (cmd != null) {
                    if (commandPredicate != null && !commandPredicate.test(message, cmd)) {
                        continue;
                    }
                    // fixme actually make permissions useful
                    try {
                        cmd.execute(this, message, prefix, command, args);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
    
    public void useMentionPrefix(boolean useMentionPrefix) {
        this.mentionPrefix = useMentionPrefix;
    }
    
    public void setUpdateTime(int seconds) {
        this.updateTime = seconds;
    }
    
    public void setCommandPredicate(BiPredicate<Message, Command> predicate) {
        this.commandPredicate = predicate;
    }
    
    public void setPrefixSupplier(PrefixSupplier supplier) {
        this.prefixSupplier = supplier;
    }
}