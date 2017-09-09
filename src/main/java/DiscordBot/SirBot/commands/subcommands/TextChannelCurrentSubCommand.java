package DiscordBot.SirBot.commands.subcommands;

import DiscordBot.SirBot.App;
import DiscordBot.SirBot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class TextChannelCurrentSubCommand extends SubCommand {

	public TextChannelCurrentSubCommand(Command parentCommand) {
		super(parentCommand);
	}

	@Override
	public String getName() {
		return "current";
	}

	@Override
	public String getDescription() {
		return "tells you the current text channel that this bot is currently responding in";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		String mention = "<@" + e.getAuthor().getId() + ">";
		
		if(msgArgs.length == 1) {
			parentCommand.sendEmbed(new EmbedBuilder().setTitle("Current Text Channel").setDescription(App.getBotTextChannel().getName()).build());
		} else {
			parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
		}
	}

}
