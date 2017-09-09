package DiscordBot.SirBot.commands.subcommands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import DiscordBot.SirBot.commands.Command;
import DiscordBot.SirBot.music.TrackScheduler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MusicQueueSubCommand extends SubCommand {
	private TrackScheduler trackScheduler;

	public MusicQueueSubCommand(Command parentCommand, TrackScheduler trackScheduler) {
		super(parentCommand);
		this.trackScheduler = trackScheduler;
	}

	@Override
	public String getName() {
		return "queue";
	}

	@Override
	public String getDescription() {
		return "display the next 10 tracks that are in the queue, or use \"clear\" to clear the queue";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		String mention = "<@" + e.getAuthor().getId() + ">";
		
		if(msgArgs.length == 1) {			
			EmbedBuilder builder = new EmbedBuilder();
			AudioTrack[] queue = trackScheduler.getQueueAsArray();
			
			if(queue.length == 0) {
				parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", there are no songs currently in queue.").build());
			} else {
				builder.setTitle("Queue (" + queue.length + " song(s))");
				
				for(int i = 0; i < queue.length && i < 10; i++) {
					builder.appendDescription("" + (i + 1) + ". " + queue[i].getInfo().title + "\n");
				}
			}
			
			parentCommand.sendEmbed(builder.build());
			return;
		} else if(msgArgs.length == 2) {
			AudioTrack[] queue = trackScheduler.getQueueAsArray();
			
			if(msgArgs[1].equals("clear")) {
				if(queue.length == 0) {
					parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", there are no songs currently in queue.").build());
					return;
				}
				int numOfSongsCleared = trackScheduler.clearQueue();
				parentCommand.sendEmbed(new EmbedBuilder().setTitle("Queue Cleared").setDescription(numOfSongsCleared + " songs removed.").build());
				return;
			}
		}
		parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
	}

}
