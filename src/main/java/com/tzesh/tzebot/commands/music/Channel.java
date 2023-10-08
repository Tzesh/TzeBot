package com.tzesh.tzebot.commands.music;

import com.tzesh.tzebot.commands.music.abstracts.AbstractMusicCommand;
import com.tzesh.tzebot.core.inventory.Inventory;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import com.tzesh.tzebot.core.music.enums.MusicEmoteUnicodes;
import com.tzesh.tzebot.core.LanguageManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import java.util.HashMap;

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

        if (arg.equalsIgnoreCase(LanguageManager.getMessage("channel.create", guildID))) {
            if (isMusicChannelPresent(commandContext)) {
                sendMessage(EmbedMessageBuilder.createErrorMessage("channel.already.setTitle", "channel.already.setDescription", user, guildID));
                return;
            }

            guild.createTextChannel(LanguageManager.getMessage("music.icon", guildID) + LanguageManager.getMessage("music.name", guildID))
                    .setTopic(LanguageManager.getMessage("channel.setTopic", guildID))
                    .queue(textchannel -> {
                        Inventory.INITIALIZED_MUSIC_CHANNELS.put(guildID, textchannel.getIdLong());
                    });

            sendMessage(EmbedMessageBuilder.createSuccessMessage("channel.success.setTitle", "channel.success.setDescription", user, guildID));
            return;
        }

        if (arg.equalsIgnoreCase(LanguageManager.getMessage("channel.set", guildID))) {
            if (channel.getIdLong() != Inventory.INITIALIZED_MUSIC_CHANNELS.get(guildID)) {
                sendMessage(EmbedMessageBuilder.createErrorMessage("channel.wrongchannel.setTitle", "channel.wrongchannel.setDescription", user, guildID));
                return;
            }

            // delete message
            message.delete().queue();

            // set channel
            channel.sendMessage("https://raw.githubusercontent.com/Tzesh/TzeBot/master/banner.PNG").queue();

            MessageEmbed musicChannelEmbedMessage = EmbedMessageBuilder.createMusicChannelEmbeddedMessage(guildID);

            channel.sendMessage(MessageCreateData.fromEmbeds(musicChannelEmbedMessage)).queue(message -> {
                message.addReaction(MusicEmoteUnicodes.nowPlaying.getUnicode()).queue();
                message.addReaction(MusicEmoteUnicodes.stop.getUnicode()).queue();
                message.addReaction(MusicEmoteUnicodes.skip.getUnicode()).queue();
                message.addReaction(MusicEmoteUnicodes.loop.getUnicode()).queue();
                message.addReaction(MusicEmoteUnicodes.shuffle.getUnicode()).queue();
                message.addReaction(MusicEmoteUnicodes.next.getUnicode()).queue();
                message.addReaction(MusicEmoteUnicodes.previous.getUnicode()).queue();
                message.addReaction(MusicEmoteUnicodes.volumedown.getUnicode()).queue();
                message.addReaction(MusicEmoteUnicodes.volumeup.getUnicode()).queue();
                message.addReaction(MusicEmoteUnicodes.queue.getUnicode()).queue();

                HashMap<Long, Long> IDs = new HashMap<>();
                IDs.put(channel.getIdLong(), message.getIdLong());
                Inventory.EMOJI_CONTROLLED_MUSIC_CHANNELS.put(guildID, IDs);
                Inventory.INITIALIZED_MUSIC_CHANNELS.put(guildID, channel.getIdLong());
            });
        } else {
            sendMessage(
                    EmbedMessageBuilder.createErrorMessage("channel.noargs.setTitle", "channel.noargs.setDescription", user, guildID)
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
