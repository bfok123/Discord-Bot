package DiscordBot.SirBot.commands.subcommands;

import java.text.DecimalFormat;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import DiscordBot.SirBot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MusicElapsedSubCommand extends SubCommand {
	private AudioPlayer player;

	public MusicElapsedSubCommand(Command parentCommand, AudioPlayer player) {
		super(parentCommand);
		this.player = player;
	}

	@Override
	public String getName() {
		return "elapsed";
	}

	@Override
	public String getDescription() {
		return "find out how much time has elapsed in the current song";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		String mention = "<@" + e.getAuthor().getId() + ">";
		
		if(msgArgs.length == 1) {
			AudioTrack track = player.getPlayingTrack();
			
			if(track == null) {
				parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", there is no song currently playing.").build());
			} else if(track != null) {
				String timeElapsed = new DecimalFormat("#0.00").format(track.getPosition() / (1000.0 * 60.0));
				String totalTime = new DecimalFormat("#0.00").format(track.getDuration() / (1000.0 * 60.0));
				
				String[] duration = totalTime.split("\\.");
				String durationMinutes = duration[0];
				String durationSeconds = duration[1];
				
				String[] elapsed = timeElapsed.split("\\.");
				String elapsedMinutes = elapsed[0];
				String elapsedSeconds = elapsed[1];
				
				parentCommand.sendEmbed(new EmbedBuilder().setTitle("Elapsed").setDescription(elapsedMinutes + ":" + elapsedSeconds + " / " + durationMinutes + ":" + durationSeconds).build());
			}
		} else {
			parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
		}
	}

}
