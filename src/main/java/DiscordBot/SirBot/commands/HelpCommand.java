package DiscordBot.SirBot.commands;

import java.awt.Color;

import DiscordBot.SirBot.commands.subcommands.SubCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HelpCommand extends Command{
	private Command[] commands = {new StopwatchCommand(), new InsultCommand(), new MusicCommand()};
	
	@Override
	public Message sendUsageEmbed(MessageReceivedEvent e, MessageEmbed embed) {
		embed = new EmbedBuilder(embed).setTitle("Help").setColor(Color.YELLOW).build();
		return e.getChannel().sendMessage(embed).complete();
	}
	
	@Override
	public void sendHelpMessage(MessageReceivedEvent e) {
		EmbedBuilder builder = new EmbedBuilder();
		
		for(Command command : commands) {
			builder.appendDescription(" - " + command.getName() + " : " + command.getDescription() + "\n");
		}
		
		sendUsageEmbed(e, builder.build());
	}
	
	@Override
	public String getName() {
		return "help";
	}

	@Override
	public String getDescription() {
		return "A list of all available commands.";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		if(msgArgs.length == 1) {
			sendHelpMessage(e);
		} else {
			sendMessage(e, new MessageBuilder().append(e.getAuthor()).append(": That is not a valid command.").build());
		}
	}

}
