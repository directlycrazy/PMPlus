package kyoku.cloud.pmplus.Commands;

import kyoku.cloud.pmplus.PMPlus;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReplyCommand implements CommandExecutor {

    PMPlus plugin;

    public ReplyCommand(PMPlus pmPlus) {
        plugin = pmPlus;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("pmplus.reply")) {
                if (plugin.mM.getReplyTarget((Player) sender) == null) {
                    String noreplytarget = ChatColor.translateAlternateColorCodes('&', PMPlus.plugin.getConfig().getString("Messages.NoReplyTarget"));
                    noreplytarget = noreplytarget.replace("%sender%", ((Player) sender).getDisplayName());
                    sender.sendMessage(noreplytarget);
                    return true;
                }
                if (args.length > 0) {
                    Player recipient = plugin.mM.getReplyTarget((Player) sender);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < args.length; i++) {
                        sb.append(args[i]).append(" ");
                    }
                    sb.setLength(sb.length() - 1);
                    String message = sb.toString();

                    // send the message to the recipient
                    String msg = ChatColor.translateAlternateColorCodes('&', PMPlus.plugin.getConfig().getString("Messages.MessageToSender"));
                    msg = msg.replace("%sender%", ((Player) sender).getDisplayName());
                    msg = msg.replace("%message%", message);
                    msg = msg.replace("%recipient%", recipient.getDisplayName());
                    sender.sendMessage(msg);

                    // send message to players with social spy
                    String msgto = ChatColor.translateAlternateColorCodes('&', PMPlus.plugin.getConfig().getString("Messages.MessageToRecipient"));
                    msgto = msgto.replace("%sender%", ((Player) sender).getDisplayName());
                    msgto = msgto.replace("%message%", message);
                    msgto = msgto.replace("%recipient%", recipient.getDisplayName());
                    sender.sendMessage(msgto);

                    // send message to players with social spy
                    for (Player spy : SocialSpyCommand.getSpies()) {
                        if (!spy.getName().equals(sender.getName()) && !spy.getName().equals(recipient.getName())) {
                            String msgspy = ChatColor.translateAlternateColorCodes('&', PMPlus.plugin.getConfig().getString("SocialSpyFormat"));
                            msgspy = msgspy.replace("%sender%", ((Player) sender).getDisplayName());
                            msgspy = msgspy.replace("%message%", message);
                            msgspy = msgspy.replace("%receiver%", recipient.getDisplayName());
                            spy.sendMessage(msgspy);
                        }
                    }
                } else {
                    String invalidargsreply = ChatColor.translateAlternateColorCodes('&', PMPlus.plugin.getConfig().getString("ReplyCommandUsage"));
                    invalidargsreply = invalidargsreply.replace("%sender%", ((Player) sender).getDisplayName());
                    sender.sendMessage(invalidargsreply);
                }
            }
        } else {
            Bukkit.getServer().getLogger().info(ChatColor.RED + "Only players can use this command.");
        }
        return true;
    }
}
