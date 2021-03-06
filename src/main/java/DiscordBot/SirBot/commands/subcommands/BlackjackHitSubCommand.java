package DiscordBot.SirBot.commands.subcommands;

import java.util.List;

import DiscordBot.SirBot.blackjack.BlackjackManager;
import DiscordBot.SirBot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class BlackjackHitSubCommand extends SubCommand {
	private BlackjackManager blackjackManager;
	
	public BlackjackHitSubCommand(Command parentCommand, BlackjackManager blackjackManager) {
		super(parentCommand);
		this.blackjackManager = blackjackManager;
	}

	@Override
	public String getName() {
		return "hit";
	}

	@Override
	public String getDescription() {
		return "request a hit - add a card to your hand";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		final String mention = "<@" + e.getAuthor().getId() + ">";
		
		if(msgArgs.length == 1) {
			String value = blackjackManager.recordHit(e.getAuthor());
			if(value != null && blackjackManager.waitingForPlayerDecisions()) {
				if(!value.contains("bust")) {
					EmbedBuilder result = new EmbedBuilder().setDescription(mention + ", you got a " + value);
					String hand = "";
					for(String c : blackjackManager.getUserHand(e.getAuthor()).getValues()) {
						hand += "| " + c + " |";
					}
					result.appendDescription("\nThis is your new hand: " + hand);
					parentCommand.sendEmbed(result.build());
				} else {
					String[] parts = value.split(" ");
					value = parts[0];
					EmbedBuilder result = new EmbedBuilder().setDescription(mention + ", you got a " + value);
					String hand = "";
					for(String c : blackjackManager.getUserHand(e.getAuthor()).getValues()) {
						hand += "| " + c + " |";
					}
					result.appendDescription("\nThis is your new hand: " + hand);
					result.appendDescription("\nYou busted!");
					parentCommand.sendEmbed(result.build());
					User user = blackjackManager.nextTurn();
					if(user != null) {
						parentCommand.sendEmbed(new EmbedBuilder().setDescription("Now " + user.getAsMention() + "'s turn").build());
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
				}
			} else if(value == null && !blackjackManager.waitingForPlayerDecisions()) {
					parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", cards must be dealt first.").build());
			} else {
				parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", it is not your turn.").build());
			}
		} else {
			parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
		}
	}

}
