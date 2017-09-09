package DiscordBot.SirBot.commands;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import DiscordBot.SirBot.App;
import DiscordBot.SirBot.commands.subcommands.SubCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HelpCommand extends Command{
	private List<Command> commands = new ArrayList<>();
	
	public Command registerCommand(Command command) {
		commands.add(command);
		return command;
	}
	
	@Override
	public Message sendUsageEmbed(MessageEmbed embed) {
		embed = new EmbedBuilder(embed).setTitle("Help").setColor(Color.YELLOW).build();
		return App.getBotTextChannel().sendMessage(embed).complete();
	}
	
	@Override
	public void sendHelpMessage() {
		EmbedBuilder builder = new EmbedBuilder();
		
		for(Command command : commands) {
			builder.appendDescription("- " + command.getName() + " : " + command.getDescription() + "\n");
		}
		
		sendUsageEmbed(builder.build());
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
			sendHelpMessage();
		} else {
			sendMessage(new MessageBuilder().append(e.getAuthor()).append(": That is not a valid command.").build());
		}
	}

}
