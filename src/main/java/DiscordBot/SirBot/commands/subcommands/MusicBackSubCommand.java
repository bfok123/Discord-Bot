package DiscordBot.SirBot.commands.subcommands;

import DiscordBot.SirBot.commands.Command;
import DiscordBot.SirBot.music.TrackScheduler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MusicBackSubCommand extends SubCommand {
	private TrackScheduler trackScheduler;

	public MusicBackSubCommand(Command parentCommand, TrackScheduler trackScheduler) {
		super(parentCommand);
		this.trackScheduler = trackScheduler;
	}

	@Override
	public String getName() {
		return "back";
	}

	@Override
	public String getDescription() {
		return "play the previous song";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		String mention = "<@" + e.getAuthor().getId() + ">";
		
		if(msgArgs.length == 1) {
			int previousSongsLength = trackScheduler.getPreviousSongsAsArray().length;
			
			if(previousSongsLength > 0) {
				parentCommand.sendEmbed(e, new EmbedBuilder().setTitle("Previous Song").setDescription(mention + " has requested to play the previous song.").build());
				trackScheduler.prevTrack();
			} else {
				parentCommand.sendErrorEmbed(e, new EmbedBuilder().setDescription(mention + ", there are no previous songs to play.").build());
			}
		} else {
			parentCommand.sendErrorEmbed(e, new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
		}
	}

}
