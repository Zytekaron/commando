package tk.zytekaron.commando.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import tk.zytekaron.commando.Command;
import tk.zytekaron.commando.CommandContext;

public class PingCommand extends Command {
    
    public PingCommand() {
        super();
        setName("ping");
        setAliases("pong");
        setGuildOnly(true);
        setPermissions(Permission.MESSAGE_WRITE);
    }
    
    @Override
    protected void run(CommandContext context) {
        MessageEmbed embed = new EmbedBuilder()
                .setTitle("Pong!")
                .setDescription("That's all you get, nerd!")
                .build();
        context.getMessage().getChannel().sendMessage(embed).queue();
    }
}