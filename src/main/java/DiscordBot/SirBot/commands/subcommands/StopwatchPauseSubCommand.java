package DiscordBot.SirBot.commands.subcommands;

import java.util.HashMap;

import DiscordBot.SirBot.commands.Command;
import DiscordBot.SirBot.stopwatch.Stopwatch;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class StopwatchPauseSubCommand extends SubCommand {
	private HashMap<User, Stopwatch> stopwatches;

	public StopwatchPauseSubCommand(Command parentCommand, HashMap<User, Stopwatch> stopwatches) {
		super(parentCommand);
		this.stopwatches = stopwatches;
	}

	@Override
	public String getName() {
		return "pause";
	}

	@Override
	public String getDescription() {
		return "pauses your stopwatch";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		User user = e.getAuthor();
		String mention = "<@" + user.getId() + ">";
		
		if(msgArgs.length == 1) {
			boolean userHasStopwatch = stopwatches.containsKey(user);
			
			if(userHasStopwatch) {
				Stopwatch stopwatch = stopwatches.get(e.getAuthor());
				
				if(stopwatch.isPaused()) {
					parentCommand.sendErrorEmbed(e, new EmbedBuilder().setDescription(mention + ", your stopwatch is already paused.").build());
				} else {
					stopwatch.pause();
					parentCommand.sendEmbed(e, new EmbedBuilder().setTitle("Stopwatch").setDescription(mention + ", you have paused your stopwatch at: " + stopwatch.getHours() + " hour(s), " + stopwatch.getMinutes() +
							       " minute(s), and " + stopwatch.getSeconds() + " second(s).").build());
				}
			} else {
				parentCommand.sendErrorEmbed(e, new EmbedBuilder().setDescription(mention + ", you do not have a stopwatch.").build());
			}
		} else {
			parentCommand.sendErrorEmbed(e, new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
		}
	}

}
