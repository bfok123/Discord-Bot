package DiscordBot.SirBot.commands.subcommands;

import DiscordBot.SirBot.App;
import DiscordBot.SirBot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class TextChannelSetSubCommand extends SubCommand {

	public TextChannelSetSubCommand(Command parentCommand) {
		super(parentCommand);
	}

	@Override
	public String getName() {
		return "set";
	}

	@Override
	public String getDescription() {
		return "set the text channel that this bot responds in";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		String mention = "<@" + e.getAuthor().getId() + ">";
		
		if(msgArgs.length == 1) {
			parentCommand.sendUsageEmbed(new EmbedBuilder().setDescription(mention + ", enter a text channel name.").build());
		} else if(msgArgs.length == 2){
			if(msgArgs[1].equals(App.getBotTextChannel().getName())) {
				parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", the bot is already responding in that text channel.").build());
				return;
			}
			
			if(App.setBotTextChannel(msgArgs[1]) == null) {
				parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", that is not a valid text channel name.").build());
			}
			// no else because a successful setBotTextChannel is handled in App class
		} else {
			parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
		}
	}

}
