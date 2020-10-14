package TzeBot.essentials;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface ICommandContext {

    Guild getGuild();

    GuildMessageReceivedEvent getEvent();

    default TextChannel getChannel() {
        return this.getEvent().getChannel();
    }

    default Message getMessage() {
        return this.getEvent().getMessage();
    }

    default Member getMember() {
        return this.getEvent().getMember();
    }

    default Member getSelfMember() {
        return this.getGuild().getSelfMember();
    }
}
