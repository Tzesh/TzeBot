package com.tzesh.tzebot.commands.abstracts;

import com.tzesh.tzebot.core.channel.abstracts.GuildChannel;
import com.tzesh.tzebot.core.command.CommandContextImpl;
import com.tzesh.tzebot.core.inventory.Inventory;
import com.tzesh.tzebot.utils.EmbedMessageBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.GenericMessageEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An abstract class for comments
 * @author tzesh
 */
public abstract class AbstractCommand<T extends GenericMessageEvent> implements Command<T> {
    private Map<Boolean, MessageEmbed> preRequisites = new HashMap<>();
    protected CommandContextImpl<T> commandContext;
    protected TextChannel channel;
    protected List<String> args;
    protected Member member;
    protected User user;
    protected Member selfMember;
    protected long guildID;
    protected Guild guild;
    protected Message message;
    protected String rawMessage;
    protected String prefix;
    protected GuildChannel guildChannel;

    protected abstract void initializePreRequisites();

    protected abstract void handleCommand();

    protected boolean canHandle() {
        if (!canTalk()) {
            sendMessage(EmbedMessageBuilder.createErrorMessage("general.cannot_talk.setTitle", "", user, this.guildChannel));
            return false;
        }

        // initialize pre-requisites
        initializePreRequisites();

        // check pre-requisites
        for (Map.Entry<Boolean, MessageEmbed> entry : preRequisites.entrySet()) {
            // if the condition is false, send the message and return false
            if (!entry.getKey()) {
                // send the message
                sendMessage(entry.getValue());

                // return false
                return false;
            }
        }

        // return true if all pre-requisites are met
        return true;
    }

    @Override
    public void handle(CommandContextImpl<T> commandContext) {
        this.initializeVariables(commandContext);

        if (canHandle()) {
            this.handleCommand();
        }
    }

    protected void initializeVariables(CommandContextImpl<T> commandContext) {
        this.commandContext = commandContext;
        this.channel = commandContext.getChannel();
        this.args = commandContext.getArgs();
        this.selfMember = commandContext.getSelfMember();
        this.guild = commandContext.getGuild();
        this.guildID = guild.getIdLong();
        this.guildChannel = Inventory.get(guildID);
        this.prefix = guildChannel.getPrefix();
        this.rawMessage = String.join(" ", args);
        if (commandContext.getEvent() instanceof MessageReceivedEvent) {
            this.message = ((MessageReceivedEvent) commandContext.getEvent()).getMessage();
            this.member = ((MessageReceivedEvent) commandContext.getEvent()).getMember();
            this.user = ((MessageReceivedEvent) commandContext.getEvent()).getAuthor();
        } else if (commandContext.getEvent() instanceof MessageReactionAddEvent) {
            this.member = ((MessageReactionAddEvent) commandContext.getEvent()).getMember();
            this.user = ((MessageReactionAddEvent) commandContext.getEvent()).getUser();
        }
    }

    protected boolean canTalk() {
        return this.channel.canTalk();
    }

    protected void addPreRequisite(boolean condition, String titleKey, String descriptionKey) {
        this.preRequisites.put(condition, EmbedMessageBuilder.createErrorMessage(titleKey, descriptionKey, user, this.guildChannel));
    }

    protected void addPreRequisite(boolean condition, String titleKey) {
        this.preRequisites.put(condition, EmbedMessageBuilder.createErrorMessage(titleKey, "", user, this.guildChannel));
    }

    protected void sendMessage(MessageEmbed message) {
        this.channel.sendMessage(MessageCreateData.fromEmbeds(message)).queue();
    }

    private String getClassName() {
        return this.getClass().getSimpleName();
    }
}
