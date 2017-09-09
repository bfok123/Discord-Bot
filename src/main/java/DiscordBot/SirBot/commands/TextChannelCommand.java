package DiscordBot.SirBot.commands;

import java.awt.Color;

import DiscordBot.SirBot.App;
import DiscordBot.SirBot.commands.subcommands.SubCommand;
import DiscordBot.SirBot.commands.subcommands.TextChannelCurrentSubCommand;
import DiscordBot.SirBot.commands.subcommands.TextChannelSetSubCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class TextChannelCommand extends Command {
	private SubCommand[] subCommands = {new TextChannelSetSubCommand(this), new TextChannelCurrentSubCommand(this)};

	@Override
	public String getName() {
		return "textchannel";
	}

	@Override
	public String getDescription() {
		return "get or set the text channel that this bot responds in";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		String mention = "<@" + e.getAuthor().getId() + ">";
		
		if(msgArgs.length == 1) {
			sendHelpMessage();
		} else if(msgArgs.length >= 2){
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
		} else {
			sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
		}
	}
	
	@Override
	public Message sendEmbed(MessageEmbed embed) {
		embed = new EmbedBuilder(embed).setColor(Color.GREEN).build();
		return App.getBotTextChannel().sendMessage(embed).complete();
	}

	@Override
	public Message sendUsageEmbed(MessageEmbed embed) {
		embed = new EmbedBuilder(embed).setTitle("Usage - Text Channel").setColor(Color.YELLOW).build();
		return App.getBotTextChannel().sendMessage(embed).complete();
	}

	@Override
	public void sendHelpMessage() {
		EmbedBuilder builder = new EmbedBuilder();
		
		for(SubCommand subCommand : subCommands) {
			builder.appendDescription("- " + subCommand.getName() + " : " + subCommand.getDescription() + "\n");
		}
		
		sendUsageEmbed(builder.build());
	}

}
