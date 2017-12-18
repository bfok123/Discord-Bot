package DiscordBot.SirBot.commands.subcommands;

import DiscordBot.SirBot.blackjack.BlackjackManager;
import DiscordBot.SirBot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class BlackjackLeaveSubCommand extends SubCommand {
	private BlackjackManager blackjackManager;

	public BlackjackLeaveSubCommand(Command parentCommand, BlackjackManager blackjackManager) {
		super(parentCommand);
		this.blackjackManager = blackjackManager;
	}

	@Override
	public String getName() {
		return "leave";
	}

	@Override
	public String getDescription() {
		return "leave the game in between rounds";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		String mention = "<@" + e.getAuthor().getId() + ">";
		
		if(msgArgs.length == 1) {
			if(blackjackManager.isPlaying(e.getAuthor())) {
				if(blackjackManager.waitingForPlayerDecisions()) {
					parentCommand.sendEmbed(new EmbedBuilder().setTitle("Player Leaving").setDescription(mention + ", you will be removed from the game after the current round.").build());
				} else {
					parentCommand.sendEmbed(new EmbedBuilder().setTitle("Player Left").setDescription(mention + ", you have left the game.").build());
				}
				blackjackManager.removeUser(e.getAuthor());
			} else {
				parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", you are not currently in the game.").build());
			}
		} else {
			parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
		}
	}

}
