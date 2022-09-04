package TzeBot.moderation;

import TzeBot.essentials.Config;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;

import java.util.List;

public class VoteRole {

    public static void onReactionRemove(MessageReactionRemoveEvent event) {
        if (Config.VOTEROLES.containsKey(event.getMessageIdLong())) {
            List<Long> roleIDs = Config.VOTEROLES.get(event.getMessageIdLong());
            switch (roleIDs.size()) {
                case 2:
                    if (event.getReaction().getEmoji().getFormatted().equals("1️⃣")) {
                        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(0))).queue();
                    }
                    if (event.getReaction().getEmoji().getFormatted().equals("2️⃣")) {
                        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(1))).queue();
                    }
                    break;
                case 3:
                    if (event.getReaction().getEmoji().getFormatted().equals("1️⃣")) {
                        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(0))).queue();
                    }
                    if (event.getReaction().getEmoji().getFormatted().equals("2️⃣")) {
                        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(1))).queue();
                    }
                    if (event.getReaction().getEmoji().getFormatted().equals("3️⃣")) {
                        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(2))).queue();
                    }
                    break;
                case 4:
                    if (event.getReaction().getEmoji().getFormatted().equals("1️⃣")) {
                        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(0))).queue();
                    }
                    if (event.getReaction().getEmoji().getFormatted().equals("2️⃣")) {
                        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(1))).queue();
                    }
                    if (event.getReaction().getEmoji().getFormatted().equals("3️⃣")) {
                        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(2))).queue();
                    }
                    if (event.getReaction().getEmoji().getFormatted().equals("4️⃣")) {
                        event.getGuild().removeRoleFromMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(3))).queue();
                    }
                    break;
                default:
                    break;
            }
        }
    }

    public static void onReactionAdd(MessageReactionAddEvent event) {
        if (Config.VOTEROLES.containsKey(event.getMessageIdLong())) {
            List<Long> roleIDs = Config.VOTEROLES.get(event.getMessageIdLong());
            switch (roleIDs.size()) {
                case 2:
                    if (event.getReaction().getEmoji().getFormatted().equals("1️⃣")) {
                        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(0))).queue();
                    }
                    if (event.getReaction().getEmoji().equals("2️⃣")) {
                        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(1))).queue();
                    }
                    break;
                case 3:
                    if (event.getReaction().getEmoji().getFormatted().equals("1️⃣")) {
                        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(0))).queue();
                    }
                    if (event.getReaction().getEmoji().getFormatted().equals("2️⃣")) {
                        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(1))).queue();
                    }
                    if (event.getReaction().getEmoji().equals("3️⃣")) {
                        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(2))).queue();
                    }
                    break;
                case 4:
                    if (event.getReaction().getEmoji().getFormatted().equals("1️⃣")) {
                        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(0))).queue();
                    }
                    if (event.getReaction().getEmoji().getFormatted().equals("2️⃣")) {
                        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(1))).queue();
                    }
                    if (event.getReaction().getEmoji().getFormatted().equals("3️⃣")) {
                        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(2))).queue();
                    }
                    if (event.getReaction().getEmoji().getFormatted().equals("4️⃣")) {
                        event.getGuild().addRoleToMember(event.getMember(), event.getGuild().getRoleById(roleIDs.get(3))).queue();
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
