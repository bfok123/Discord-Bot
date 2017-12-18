package DiscordBot.SirBot.commands.subcommands;

import java.util.List;

import DiscordBot.SirBot.blackjack.BlackjackHand;
import DiscordBot.SirBot.blackjack.BlackjackManager;
import DiscordBot.SirBot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class BlackjackDealSubCommand extends SubCommand {
	private BlackjackManager blackjackManager;
	
	public BlackjackDealSubCommand(Command parentCommand, BlackjackManager blackjackManager) {
		super(parentCommand);
		this.blackjackManager = blackjackManager;
	}

	@Override
	public String getName() {
		return "deal";
	}

	@Override
	public String getDescription() {
		return "start play by dealing the cards to all users who have joined";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		final String mention = "<@" + e.getAuthor().getId() + ">";
		
		if(msgArgs.length == 1) {
			blackjackManager.addUsersInQueue();
			blackjackManager.removeUsersInQueue();
			if(blackjackManager.getUsers().isEmpty()) {
				parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription("No players have joined the game.").build());
			} else {
				if(!blackjackManager.waitingForPlayerDecisions()) {
					blackjackManager.dealCards();
					EmbedBuilder result = new EmbedBuilder();
					for(User user : blackjackManager.getUsers()) {
						result.appendDescription(mention + ", this is your hand:\n");
						BlackjackHand hand = blackjackManager.getUserHand(user);
						for(String c : hand.getValues()) {
							result.appendDescription(" +---+ ");
						}
						result.appendDescription("\n");
						for(String c : hand.getValues()) {
							if(c.equals("Q")) {
								result.appendDescription(" | " + c + "  |  ");
							} else if(c.equals("10")) {
								result.appendDescription(" | " + c + "  |  ");
							} else if(c.equals("J")) {
								result.appendDescription(" |  " + c + "   |  ");
							} else {
								result.appendDescription(" |  " + c + "  |  ");
							}
						}
						result.appendDescription("\n");
						for(String c : hand.getValues()) {
							result.appendDescription(" +---+ ");
						}
					}
					parentCommand.sendEmbed(result.setTitle("Cards Dealt").build());
					if(blackjackManager.playerHasBlackjack()) {
						blackjackManager.determineWinners();
						List<User> winners = blackjackManager.determineWinners();
						if(winners.size() == 1) {
							parentCommand.sendEmbed(new EmbedBuilder().setTitle("Results").setDescription(winners.get(0).getAsMention() + " wins the round with "
																				  	  + "a blackjack!").build());
						} else {
							EmbedBuilder winnersMessage = new EmbedBuilder();
							User firstWinner = winners.get(0);
							winnersMessage.appendDescription(firstWinner.getAsMention() + " ");
							for(int i = 1; i < winners.size() - 1; i++) {
								winnersMessage.appendDescription(", " + winners.get(i) + " ");
							}
							winnersMessage.appendDescription("and " + winners.get(winners.size() - 1).getAsMention() + " ");
							winnersMessage.appendDescription("win the round with blackjacks!");
							parentCommand.sendEmbed(winnersMessage.setTitle("Results").build());
						}
						parentCommand.sendEmbed(new EmbedBuilder().setDescription("Deal cards to start new round.").build());
					}
				} else {
					parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription("Waiting for player decisions...").build());
				}
			}
		} else {
			parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
		}
	}

}
