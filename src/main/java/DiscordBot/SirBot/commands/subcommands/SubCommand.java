package DiscordBot.SirBot.commands.subcommands;

import DiscordBot.SirBot.commands.Command;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class SubCommand {
	public Command parentCommand;
	
	public abstract String getName();
	public abstract String getDescription();
	public abstract void onCommand(MessageReceivedEvent e, String[] msgArgs);
	
	public SubCommand(Command parentCommand) {
		this.parentCommand = parentCommand;
	}
}
