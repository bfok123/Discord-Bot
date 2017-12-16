package DiscordBot.SirBot.commands.subcommands;

import java.util.List;

import DiscordBot.SirBot.blackjack.BlackjackManager;
import DiscordBot.SirBot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class BlackjackStandSubCommand extends SubCommand {
	private BlackjackManager blackjackManager;
	
	public BlackjackStandSubCommand(Command parentCommand, BlackjackManager blackjackManager) {
		super(parentCommand);
		this.blackjackManager = blackjackManager;
	}
	
	@Override
	public String getName() {
		return "stand";
	}

	@Override
	public String getDescription() {
		return "take no more cards, move on to the next player";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		final String mention = "<@" + e.getAuthor().getId() + ">";
		
		if(msgArgs.length == 1) {
			System.out.println(blackjackManager.waitingForPlayerDecisions());
			// if it is this player's turn
			if(blackjackManager.waitingForPlayerDecisions() && blackjackManager.getUserTurn().equals(e.getAuthor())) {
				blackjackManager.recordStand(e.getAuthor());
				User user = blackjackManager.nextTurn();
				if(user != null) {
					parentCommand.sendEmbed(new EmbedBuilder().setDescription("Now " + user.getAsMention() + "'s turn.").build());
				} else {
					List<User> winners = blackjackManager.determineWinners();
					if(winners != null) {
						if(winners.size() == 1) {
							parentCommand.sendEmbed(new EmbedBuilder().setDescription(winners.get(0).getAsMention() + " wins the round with "
																			  	      + blackjackManager.getUserHand(winners.get(0)).getValue() + "!").build());
						} else {
							EmbedBuilder winnersMessage = new EmbedBuilder();
							User firstWinner = winners.get(0);
							winnersMessage.appendDescription(firstWinner.getAsMention() + " ");
							for(int i = 1; i < winners.size() - 1; i++) {
								winnersMessage.appendDescription(", " + winners.get(i) + " ");
							}
							winnersMessage.appendDescription("and " + winners.get(winners.size() - 1).getAsMention() + " ");
							winnersMessage.appendDescription("win the round with " + blackjackManager.getUserHand(firstWinner).getValue());
						}
					} else {
						parentCommand.sendEmbed(new EmbedBuilder().setDescription("Everyone busted, no winner.").build());
					}
					parentCommand.sendEmbed(new EmbedBuilder().setDescription("Deal cards to start new round.").build());
				}
			} else if(!blackjackManager.waitingForPlayerDecisions()){
				parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", cards must be dealt first.").build());
			} else {
				parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", it is not your turn.").build());
			}
		} else {
			parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
		}
	}

}
