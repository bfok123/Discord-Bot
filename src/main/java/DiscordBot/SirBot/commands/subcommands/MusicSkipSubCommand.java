package DiscordBot.SirBot.commands.subcommands;

import java.util.HashSet;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import DiscordBot.SirBot.commands.Command;
import DiscordBot.SirBot.music.TrackScheduler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MusicSkipSubCommand extends SubCommand {
	private HashSet<User> usersWhoVoted;
	private AudioPlayer player;
	private int skips;
	private TrackScheduler trackScheduler;
	
	public MusicSkipSubCommand(Command parentCommand, AudioPlayer player, TrackScheduler trackScheduler) {
		super(parentCommand);
		this.player = player;
		this.trackScheduler = trackScheduler;
		skips = 0;
		usersWhoVoted = new HashSet<User>();
	}

	@Override
	public String getName() {
		return "skip";
	}

	@Override
	public String getDescription() {
		return "vote to skip the current song, or use \"force\" to skip the current song or a given number of songs immediately";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		User user = e.getAuthor();
		AudioTrack currTrack = player.getPlayingTrack();
		String mention = "<@" + user.getId() + ">";
		
		if(msgArgs.length == 1) {
			if(player.getPlayingTrack() == null) {
				parentCommand.sendErrorEmbed(e, new EmbedBuilder().setDescription(mention + ", there is no song currently playing.").build());
				return;
			} else if(usersWhoVoted.contains(user)) {
				parentCommand.sendErrorEmbed(e, new EmbedBuilder().setDescription(mention + ", you have already voted to skip this song.").build());
				return;
			} else {
				skips++;
				usersWhoVoted.add(user);
				
				if(skips < 3) {
					parentCommand.sendEmbed(e, new EmbedBuilder().setTitle("Skip Vote").setDescription("There are currently " + skips + " out of 3 votes to skip this song.").build()); 
					return;
				} else if(skips == 3) {
					resetVotes();
					parentCommand.sendEmbed(e, new EmbedBuilder().setTitle("Song Skipped").setDescription(currTrack.getInfo().title).build());
					trackScheduler.nextTrack(player.getPlayingTrack(), true);
					return;
				}
			}
		} else if(msgArgs.length == 2) {
			if(msgArgs[1].equals("force")) {
				resetVotes();
				parentCommand.sendEmbed(e, new EmbedBuilder().setTitle("Song Force Skipped").setDescription(currTrack.getInfo().title).build());
				trackScheduler.nextTrack(player.getPlayingTrack(), true);
				return;
			}
		} else if(msgArgs.length == 3) {
			if(msgArgs[1].equals("force")) {
				try {
					int numTracks = Integer.parseInt(msgArgs[2]);
					AudioTrack[] skippedTracks = trackScheduler.nextTrackNum(numTracks);
					
					EmbedBuilder embedBuilder = new EmbedBuilder();
					for(int i = 0; i < skippedTracks.length; i++) {
						embedBuilder.appendDescription((i + 1) + ". " + skippedTracks[i].getInfo().title + "\n");
					}
					
					parentCommand.sendEmbed(e, embedBuilder.setTitle("Skipped " + numTracks + " Tracks").build());
					
					// this is handled here and not in the nextTrackNum() method so that the bot says "Now Playing" after "Skipped [num] Tracks"
					trackScheduler.nextTrack(null, false);
				} catch (Exception ex) {
					parentCommand.sendUsageEmbed(e, new EmbedBuilder().setDescription(mention + ", enter a number of songs to skip (must be a positive integer).").build());
				}
				return;
			}
		}
		parentCommand.sendErrorEmbed(e, new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
	}
	
	public void resetVotes() {
		usersWhoVoted.clear();
		skips = 0;
	}
	
}
