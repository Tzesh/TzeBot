package TzeBot.commands.moderation;

import TzeBot.essentials.CommandContext;
import TzeBot.essentials.Config;
import TzeBot.essentials.ICommand;
import TzeBot.essentials.LanguageManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Tzesh
 */
public class VoteRole implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        LinkedList<Long> roleIDs = new LinkedList<>();
        final TextChannel channel = ctx.getChannel();
        final List<String> args = ctx.getArgs();
        final Member member = ctx.getMember();
        final long guildID = ctx.getGuild().getIdLong();
        final Member selfmember = ctx.getGuild().getSelfMember();

        if (!member.hasPermission(Permission.MANAGE_SERVER)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("general.not_authorized", guildID));
            error.setDescription(LanguageManager.getMessage("general.not_authorized.description", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(error.build()).queue();
            return;
        }
        if (!selfmember.hasPermission(Permission.MANAGE_ROLES)) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("general.nonperm", guildID));
            error.setDescription(LanguageManager.getMessage("general.nonperm.manage_roles", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(error.build()).queue();
        }

        if (args.isEmpty() || ctx.getMessage().getMentionedRoles().size() < 2) {
            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xff3923);
            error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("general.403", guildID));
            error.setDescription(LanguageManager.getMessage("general.403.description", guildID));
            error.setTimestamp(Instant.now());

            channel.sendMessage(error.build()).queue();
        } else {
            String space = " ";
            String res = String.join(space, args);
            String[] variables = res.split(" : ");
            for (int i = 0; i < ctx.getMessage().getMentionedRoles().size(); i++) {
                roleIDs.add(i, ctx.getMessage().getMentionedRoles().get(i).getIdLong());
                if (!ctx.getSelfMember().canInteract(ctx.getMessage().getMentionedRoles().get(i))) {
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("general.hierarchy", guildID));
                    error.setDescription(LanguageManager.getMessage("general.hierarchy.setDescription", guildID));
                    error.setTimestamp(Instant.now());

                    channel.sendMessage(error.build()).queue();
                    return;
                }
            }
            switch (variables.length) {
                case 5:
                    if (roleIDs.size() != 2) {
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("vote.error.setTitle", guildID));
                        error.setDescription(LanguageManager.getMessage("vote.error.setDescription", guildID));
                        error.setTimestamp(Instant.now());

                        channel.sendMessage(error.build()).queue();
                        return;
                    }

                    EmbedBuilder vote3 = new EmbedBuilder();
                    vote3.setColor(0x0087ff);
                    vote3.setTitle(LanguageManager.getMessage("general.icon.vote", guildID) + variables[0] + LanguageManager.getMessage("general.icon.question", guildID));
                    vote3.setDescription(LanguageManager.getMessage("general.icon.1", guildID) + " " + variables[1]
                            + "\n" + LanguageManager.getMessage("general.icon.2", guildID) + " " + variables[3]);
                    vote3.setFooter(LanguageManager.getMessage("vote.setFooter", guildID));

                    channel.sendMessage(vote3.build()).queue(message -> {
                        message.addReaction(LanguageManager.getMessage("general.icon.1", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.2", guildID)).queue();
                        Config.VOTEROLES.put(message.getIdLong(), roleIDs);
                    });
                    break;
                case 7:
                    if (roleIDs.size() != 3) {
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("vote.error.setTitle", guildID));
                        error.setDescription(LanguageManager.getMessage("vote.error.setDescription", guildID));
                        error.setTimestamp(Instant.now());

                        channel.sendMessage(error.build()).queue();
                        return;
                    }
                    EmbedBuilder vote4 = new EmbedBuilder();
                    vote4.setColor(0x0087ff);
                    vote4.setTitle(LanguageManager.getMessage("general.icon.vote", guildID) + variables[0] + LanguageManager.getMessage("general.icon.question", guildID));
                    vote4.setDescription(LanguageManager.getMessage("general.icon.1", guildID) + " " + variables[1]
                            + "\n" + LanguageManager.getMessage("general.icon.2", guildID) + " " + variables[2]
                            + "\n" + LanguageManager.getMessage("general.icon.3", guildID) + " " + variables[3]);
                    vote4.setFooter(LanguageManager.getMessage("vote.setFooter", guildID));

                    channel.sendMessage(vote4.build()).queue(message -> {
                        message.addReaction(LanguageManager.getMessage("general.icon.1", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.2", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.3", guildID)).queue();
                        Config.VOTEROLES.put(message.getIdLong(), roleIDs);
                    });
                    break;
                case 9:
                    if (roleIDs.size() != 4) {
                        EmbedBuilder error = new EmbedBuilder();
                        error.setColor(0xff3923);
                        error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("vote.error.setTitle", guildID));
                        error.setDescription(LanguageManager.getMessage("vote.error.setDescription", guildID));
                        error.setTimestamp(Instant.now());

                        channel.sendMessage(error.build()).queue();
                        return;
                    }
                    EmbedBuilder vote5 = new EmbedBuilder();
                    vote5.setColor(0x0087ff);
                    vote5.setTitle(LanguageManager.getMessage("general.icon.vote", guildID) + variables[0] + LanguageManager.getMessage("general.icon.question", guildID));
                    vote5.setDescription(LanguageManager.getMessage("general.icon.1", guildID) + " " + variables[1]
                            + "\n" + LanguageManager.getMessage("general.icon.2", guildID) + " " + variables[2]
                            + "\n" + LanguageManager.getMessage("general.icon.3", guildID) + " " + variables[3]
                            + "\n" + LanguageManager.getMessage("general.icon.4", guildID) + " " + variables[4]);
                    vote5.setFooter(LanguageManager.getMessage("vote.setFooter", guildID));

                    channel.sendMessage(vote5.build()).queue(message -> {
                        message.addReaction(LanguageManager.getMessage("general.icon.1", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.2", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.3", guildID)).queue();
                        message.addReaction(LanguageManager.getMessage("general.icon.4", guildID)).queue();
                        Config.VOTEROLES.put(message.getIdLong(), roleIDs);
                    });
                    break;
                default:
                    EmbedBuilder error = new EmbedBuilder();
                    error.setColor(0xff3923);
                    error.setTitle(LanguageManager.getMessage("general.icon.error", guildID) + LanguageManager.getMessage("vote.error.setTitle", guildID));
                    error.setDescription(LanguageManager.getMessage("vote.error.setDescription", guildID));
                    error.setTimestamp(Instant.now());

                    channel.sendMessage(error.build()).queue();
                    break;
            }
        }
    }

    @Override
    public String getName(long guildID) {
        return LanguageManager.getMessage("voterole.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageManager.getMessage("voterole.gethelp", guildID);
    }
}
