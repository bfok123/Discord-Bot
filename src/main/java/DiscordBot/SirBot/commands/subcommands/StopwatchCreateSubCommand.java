package DiscordBot.SirBot.commands.subcommands;

import java.util.HashMap;
import java.util.TimerTask;

import DiscordBot.SirBot.commands.Command;
import DiscordBot.SirBot.stopwatch.Stopwatch;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class StopwatchCreateSubCommand extends SubCommand {
	private HashMap<User, Stopwatch> stopwatches;

	public StopwatchCreateSubCommand(Command parentCommand, HashMap<User, Stopwatch> stopwatches) {
		super(parentCommand);
		this.stopwatches = stopwatches;
	}

	@Override
	public String getName() {
		return "create";
	}

	@Override
	public String getDescription() {
		return "create and start a stopwatch, only one allowed per user";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		User user = e.getAuthor();
		String mention = "<@" + user.getId() + ">";
		
		if(msgArgs.length == 1) {
			boolean userHasStopwatch = stopwatches.containsKey(user);
			
			if(!userHasStopwatch) {
				final Stopwatch stopwatch = new Stopwatch(parentCommand, e);
				stopwatch.scheduleAtFixedRate(new TimerTask() {
	
					@Override
					public void run() {
						if(!stopwatch.isPaused()) {
							stopwatch.updateSeconds();
						}
					}
					
				}, 0, 1000);
				
				stopwatches.put(user, stopwatch);
				parentCommand.sendEmbed(new EmbedBuilder().setTitle("Stopwatch").setDescription(mention + ", you have started a stopwatch.").build());
			}
			else {
				parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", you already have a stopwatch.").build());
			}
		} else {
			parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
		}
	}

}
