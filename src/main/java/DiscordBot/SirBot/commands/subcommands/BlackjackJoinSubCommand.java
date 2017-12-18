package DiscordBot.SirBot.commands.subcommands;

import DiscordBot.SirBot.blackjack.BlackjackManager;
import DiscordBot.SirBot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class BlackjackJoinSubCommand extends SubCommand {
	private BlackjackManager blackjackManager;
	
	public BlackjackJoinSubCommand(Command parentCommand, BlackjackManager blackjackManager) {
		super(parentCommand);
		this.blackjackManager = blackjackManager;
	}
	
	@Override
	public String getName() {
		return "join";
	}

	@Override
	public String getDescription() {
		return "join the current blackjack game";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		String mention = "<@" + e.getAuthor().getId() + ">";
		
		if(msgArgs.length == 1) {
			if(blackjackManager.isPlaying(e.getAuthor())) {
				parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", you are already playing.").build());
			} else {
				if(blackjackManager.waitingForPlayerDecisions()) {
					parentCommand.sendEmbed(new EmbedBuilder().setTitle("New Player").setDescription(mention + ", you will be added to the game after the current round.").build());
				} else {
					parentCommand.sendEmbed(new EmbedBuilder().setTitle("New Player").setDescription(mention + ", you joined the game.").build());
				}
				blackjackManager.addUser(e.getAuthor());
			}
		} else {
			parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());

		}
	}

}
