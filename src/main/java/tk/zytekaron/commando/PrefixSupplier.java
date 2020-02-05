package tk.zytekaron.commando;

import net.dv8tion.jda.api.entities.Message;

import java.util.List;
import java.util.function.Supplier;

public interface PrefixSupplier {
    List<Supplier<String>> getPrefixes(Message message);
}