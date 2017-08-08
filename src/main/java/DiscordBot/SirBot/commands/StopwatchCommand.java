package DiscordBot.SirBot.commands;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import DiscordBot.SirBot.commands.subcommands.StopwatchCreateSubCommand;
import DiscordBot.SirBot.commands.subcommands.StopwatchPauseSubCommand;
import DiscordBot.SirBot.commands.subcommands.StopwatchRemoveSubCommand;
import DiscordBot.SirBot.commands.subcommands.StopwatchResumeSubCommand;
import DiscordBot.SirBot.commands.subcommands.StopwatchTimeSubCommand;
import DiscordBot.SirBot.commands.subcommands.SubCommand;
import DiscordBot.SirBot.stopwatch.Stopwatch;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class StopwatchCommand extends Command {
	private HashMap<User, Stopwatch> stopwatches;
	private SubCommand[] subCommands;
	
	public StopwatchCommand() {
		stopwatches = new HashMap<User, Stopwatch>();
		subCommands = new SubCommand[5];
		
		subCommands[0] = new StopwatchCreateSubCommand(this, stopwatches);
		subCommands[1] = new StopwatchPauseSubCommand(this, stopwatches);
		subCommands[2] = new StopwatchRemoveSubCommand(this, stopwatches);
		subCommands[3] = new StopwatchResumeSubCommand(this, stopwatches);
		subCommands[4] = new StopwatchTimeSubCommand(this, stopwatches);
	}

	@Override
	public String getName() {
		return "stopwatch";
	}

	@Override
	public String getDescription() {
		return "Allows you to create a stopwatch that will notify you every hour.";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		if(msgArgs.length == 1) {
			sendHelpMessage(e);
		} else {
			String mention = "<@" + e.getAuthor().getId() + ">";
			
			// sub commands get msgArgs that start with their name
			String[] subCommandMsgArgs = new String[msgArgs.length - 1];
			for(int i = 0; i < subCommandMsgArgs.length; i++) {
				subCommandMsgArgs[i] = msgArgs[i + 1];
			}
			
			for(SubCommand subCommand : subCommands) {
				if(msgArgs[1].equals(subCommand.getName())) {
					subCommand.onCommand(e, subCommandMsgArgs);
					return;
				}
			}
			
			sendErrorEmbed(e, new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
		}
	}
	
	@Override
	public Message sendMessage(MessageReceivedEvent e, Message message) {
		return e.getChannel().sendMessage(new MessageBuilder().append(":stopwatch: ").append(message.getContent()).append(" :stopwatch:").build()).complete();
	}
	
	@Override
	public Message sendMessageWithMention(MessageReceivedEvent e, Message message) {
		return e.getChannel().sendMessage(new MessageBuilder().append(e.getAuthor()).append(": ").append(":stopwatch: ").append(message.getContent()).append(" :stopwatch:").build()).complete();
	}
	
	@Override
	public Message sendEmbed(MessageReceivedEvent e, MessageEmbed embed) {
		embed = new EmbedBuilder(embed).setColor(Color.WHITE).build();
		return e.getChannel().sendMessage(embed).complete();
	}
	
	@Override
	public Message sendUsageEmbed(MessageReceivedEvent e, MessageEmbed embed) {
		embed = new EmbedBuilder(embed).setTitle("Usage - Stopwatch").setColor(Color.YELLOW).build();
		return e.getChannel().sendMessage(embed).complete();
	}
	
	@Override
	public void sendHelpMessage(MessageReceivedEvent e) {
		EmbedBuilder builder = new EmbedBuilder();
		
		for(SubCommand subCommand : subCommands) {
			builder.appendDescription(" - " + subCommand.getName() + " : " + subCommand.getDescription() + "\n");
		}
		
		sendUsageEmbed(e, builder.build());
	}
}
