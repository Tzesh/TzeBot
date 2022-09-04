package TzeBot.commands;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.time.Instant;

import static TzeBot.essentials.LanguageManager.getMessage;


/**
 * @author Tzesh
 */
public class Support implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final long guildID = ctx.getGuild().getIdLong();

        EmbedBuilder support = new EmbedBuilder();
        support.setColor(0x00ff00);
        support.setTitle(getMessage("general.icon.tzebot", guildID) + " " + getMessage("support.channel", guildID), "https://discord.com/invite/CY4pGVv");
        support.setDescription(getMessage("support.setDescription", guildID)
                + "\n" + getMessage("support.setDescription2", guildID)
                + "\n" + getMessage("support.setDescription3", guildID)
                + "\n" + getMessage("general.icon.patreon", guildID) + "https://www.patreon.com/tzebot"
                + "\n" + getMessage("support.discordbots", guildID));
        support.addBlankField(true);
        support.addField("Discord Bot List", "https://top.gg/bot/700416851678855168", true);
        support.addBlankField(true);
        support.setFooter(getMessage("support.setFooter", guildID));
        support.setTimestamp(Instant.now());

        channel.sendMessage(MessageCreateData.fromEmbeds(support.build())).queue();
        support.clear();
    }

    @Override
    public String getName(long guildID) {
        return getMessage("support.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return getMessage("support.gethelp", guildID);
    }

}
