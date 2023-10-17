package com.tzesh.tzebot.commands.music;

import com.tzesh.tzebot.commands.music.abstracts.AbstractMusicCommand;
import com.tzesh.tzebot.core.language.LanguageManager;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import static com.tzesh.tzebot.core.music.constants.MusicCommonConstants.BANNER_URL;
import static com.tzesh.tzebot.utils.InputControlHelper.isMusicChannelPresent;


/**
 * A class to create a music channel and set it up
 * @author Tzesh
 */
public class Channel<T extends GenericMessageEvent> extends AbstractMusicCommand<T> {

    @Override
    protected void initializePreRequisites() {
        boolean isArgsCorrect = args.size() <= 1;
        addPreRequisite(isArgsCorrect, "general.403", "general.403.description");
        if (!isArgsCorrect) return;

        boolean memberHasPermission = member.hasPermission(Permission.MANAGE_SERVER);
        addPreRequisite(memberHasPermission, "general.not_authorized", "general.not_authorized.description");
        if (!memberHasPermission) return;

        boolean selfMemberHasPermission = selfMember.hasPermission(Permission.MANAGE_SERVER);
        addPreRequisite(selfMemberHasPermission, "general.nonperm", "general.nonperm.manage_channel");
        if (!selfMemberHasPermission) return;

        boolean selfMemberHasPermissionMessageManage = selfMember.hasPermission(Permission.MESSAGE_MANAGE);
        addPreRequisite(selfMemberHasPermissionMessageManage, "general.nonperm", "general.nonperm.message_manage");
    }

    @Override
    public void handleCommand() {
        String arg = args.get(0);

        if (arg.equalsIgnoreCase(LanguageManager.getMessage("channel.create", this.guildChannel.getLanguage()))) {
            if (isMusicChannelPresent(commandContext)) {
                sendMessage(EmbedMessageBuilder.createErrorMessage("channel.already.setTitle", "channel.already.setDescription", user, this.guildChannel));
                return;
            }

            guild.createTextChannel(LanguageManager.getMessage("music.icon", this.guildChannel.getLanguage()) + LanguageManager.getMessage("music.name", this.guildChannel.getLanguage()))
                    .setTopic(LanguageManager.getMessage("channel.setTopic", this.guildChannel.getLanguage()))
                    .queue(textchannel -> {
                        this.guildChannel.setBoundedMusicChannelID(textchannel.getIdLong());
                        this.guildChannel.save();
                    });

            sendMessage(EmbedMessageBuilder.createSuccessMessage("channel.success.setTitle", "channel.success.setDescription", user, this.guildChannel));
            return;
        }

        if (arg.equalsIgnoreCase(LanguageManager.getMessage("channel.set", this.guildChannel.getLanguage()))) {
            if (channel.getIdLong() != this.guildChannel.getBoundedMusicChannelID()) {
                sendMessage(EmbedMessageBuilder.createErrorMessage("channel.wrongchannel.setTitle", "channel.wrongchannel.setDescription", user, this.guildChannel));
                return;
            }

            // delete the message
            message.delete().queue();

            // send the banner
            channel.sendMessage(BANNER_URL).queue();

            // send the embed message
            MessageEmbed musicChannelEmbedMessage = EmbedMessageBuilder.createMusicChannelEmbeddedMessage(guildChannel);
            channel.sendMessage(MessageCreateData.fromEmbeds(musicChannelEmbedMessage)).queue(message -> {
                EmbedMessageBuilder.initializeMusicChannelEmojiControls(message);

                this.guildChannel.setMusicChannelMessageID(message.getIdLong());
                this.guildChannel.save();
            });
        } else {
            sendMessage(
                    EmbedMessageBuilder.createErrorMessage("channel.noargs.setTitle", "channel.noargs.setDescription", user, this.guildChannel)
            );
        }
    }


    @Override
    public String getName(long guildID) {
        return LanguageManager.getMessage("channel.name", guildID);
    }

    @Override
    public String getHelp(long guildID) {
        return LanguageManager.getMessage("channel.gethelp", guildID);
    }
}
