package DiscordBot.SirBot.commands;

import java.awt.Color;

import DiscordBot.SirBot.App;
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
	public abstract void sendHelpMessage();
	
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
	
	public Message sendMessage(String message) {
		return sendMessage(new MessageBuilder().append(message).build());
	}
	
	public Message sendMessage(Message message) {
		return App.getBotTextChannel().sendMessage(message).complete();
	}
	
	public Message sendEmbed(MessageEmbed embed) {
		return App.getBotTextChannel().sendMessage(embed).complete();
	}
	
	// cannot call sendEmbed because it is overridden differently in each command (for different colors)
	public Message sendErrorEmbed(MessageEmbed embed) {
		embed = new EmbedBuilder(embed).setTitle("Error").setColor(Color.RED).build();
		return App.getBotTextChannel().sendMessage(embed).complete();
	}
	
	public Message sendUsageEmbed(MessageEmbed embed) {
		embed = new EmbedBuilder(embed).setTitle("Usage").setColor(Color.YELLOW).build();
		return App.getBotTextChannel().sendMessage(embed).complete();
	}
}
