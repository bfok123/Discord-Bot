package DiscordBot.SirBot.commands;

import java.awt.Color;

import DiscordBot.SirBot.App;
import DiscordBot.SirBot.blackjack.BlackjackManager;
import DiscordBot.SirBot.commands.subcommands.BlackjackDealSubCommand;
import DiscordBot.SirBot.commands.subcommands.BlackjackHitSubCommand;
import DiscordBot.SirBot.commands.subcommands.BlackjackJoinSubCommand;
import DiscordBot.SirBot.commands.subcommands.BlackjackLeaveSubCommand;
import DiscordBot.SirBot.commands.subcommands.BlackjackStandSubCommand;
import DiscordBot.SirBot.commands.subcommands.SubCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class BlackjackCommand extends Command {
	private SubCommand[] subCommands;
	private BlackjackManager blackjackManager;
	
	public BlackjackCommand() {
		blackjackManager = new BlackjackManager();
		subCommands = new SubCommand[5]; // hit, double down, split, stand, join, leave?
		subCommands[0] = new BlackjackHitSubCommand(this, blackjackManager);
		subCommands[1] = new BlackjackJoinSubCommand(this, blackjackManager);
		subCommands[2] = new BlackjackStandSubCommand(this, blackjackManager);
		subCommands[3] = new BlackjackDealSubCommand(this, blackjackManager);
		subCommands[4] = new BlackjackLeaveSubCommand(this, blackjackManager);
	}
	
	@Override
	public String getName() {
		return "blackjack";
	}

	@Override
	public String getDescription() {
		return "play blackjack";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		String mention = "<@" + e.getAuthor().getId() + ">";
		
		if(msgArgs.length == 1) {
			sendHelpMessage();
		} else {
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
			
			sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
		}
	}
	
	@Override
	public Message sendEmbed(MessageEmbed embed) {
		embed = new EmbedBuilder(embed).setColor(Color.ORANGE).build();
		return App.getBotTextChannel().sendMessage(embed).complete();
	}

	@Override
	public Message sendUsageEmbed(MessageEmbed embed) {
		embed = new EmbedBuilder(embed).setTitle("Usage - Blackjack").setColor(Color.YELLOW).build();
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
