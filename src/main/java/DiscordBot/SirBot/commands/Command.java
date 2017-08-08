package DiscordBot.SirBot.commands;

import java.awt.Color;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/*
 * Every command will have a name, description, and onMessageReceived method
 */

public abstract class Command extends ListenerAdapter {
	public abstract String getName();
	public abstract String getDescription();
	public abstract void onCommand(MessageReceivedEvent e, String[] msgArgs);
	
	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		String[] msgArgs = e.getMessage().getContent().split(" ");
		
		// send "help" message for this command if user only types first argument
		if(commandCalled(msgArgs)) {
			onCommand(e, msgArgs);
		}
	}
	
	// determines if this command was called by a user
	public boolean commandCalled(String[] msgArgs) {
		if(msgArgs[0].equals("!" + getName())) return true;
		
		return false;
	}
	
	public Message sendMessage(MessageReceivedEvent e, String message) {
		return sendMessage(e, new MessageBuilder().append(message).build());
	}
	
	public Message sendMessage(MessageReceivedEvent e, Message message) {
		return e.getChannel().sendMessage(message).complete();
	}
	
	public Message sendEmbed(MessageReceivedEvent e, MessageEmbed embed) {
		return e.getChannel().sendMessage(embed).complete();
	}
	
	// cannot call sendEmbed because it is overridden differently in each command (for different colors)
	public Message sendErrorEmbed(MessageReceivedEvent e, MessageEmbed embed) {
		embed = new EmbedBuilder(embed).setTitle("Error").setColor(Color.RED).build();
		return e.getChannel().sendMessage(embed).complete();
	}
	
	public Message sendUsageEmbed(MessageReceivedEvent e, MessageEmbed embed) {
		embed = new EmbedBuilder(embed).setTitle("Usage").setColor(Color.YELLOW).build();
		return e.getChannel().sendMessage(embed).complete();
	}
	
	public Message sendMessageWithMention(MessageReceivedEvent e, String message) {
		return sendMessageWithMention(e, new MessageBuilder().append(message).build());
	}
	
	public Message sendMessageWithMention(MessageReceivedEvent e, Message message) {
		return e.getChannel().sendMessage(new MessageBuilder().append(e.getAuthor()).append(": ").append(message.getContent()).build()).complete();
	}
	
	// help message will only display name and description of command by default, have to add sub-commands
	// in child classes
	public void sendHelpMessage(MessageReceivedEvent e) {
		sendMessage(e, new MessageBuilder().append(e.getAuthor()).append(": " + getName().toUpperCase() + ": " + getDescription()).build());
	}
}
