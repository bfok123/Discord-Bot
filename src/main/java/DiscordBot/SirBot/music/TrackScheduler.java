package DiscordBot.SirBot.music;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import DiscordBot.SirBot.commands.MusicCommand;
import DiscordBot.SirBot.commands.subcommands.MusicSkipSubCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;

public class TrackScheduler extends AudioEventAdapter {
	private BlockingDeque<AudioTrack> queue;
	private BlockingDeque<AudioTrack> previousSongs;
	private AudioPlayer player;
	private MusicCommand command;
	
	public TrackScheduler(AudioPlayer player, MusicCommand command) {
		this.player = player;

		this.command = command;
		queue = new LinkedBlockingDeque<AudioTrack>();
		previousSongs = new LinkedBlockingDeque<AudioTrack>();
	}
	
	public void queue(AudioTrack track) {
		if(!player.startTrack(track, true)) {
			queue.offer(track);
		}
		
		if(player.getPlayingTrack() == null) {
			player.playTrack(queue.poll());
		}
	}
	
	// play next track
	public void nextTrack(AudioTrack currTrack, boolean addToPreviousSongs) {	
		if(addToPreviousSongs) {
			previousSongs.offerFirst(currTrack);
		}
		
		player.startTrack(queue.poll(), false);
		command.resetVotes();
	}
	
	// skip over a given number of tracks, return those tracks
	public AudioTrack[] nextTrackNum(int numTracks) {
		AudioTrack[] skippedTracks = new AudioTrack[numTracks];
		
		// first handle current playing song
		AudioTrack currTrack = player.getPlayingTrack();
		previousSongs.offerFirst(currTrack.makeClone());
		skippedTracks[0] = currTrack;
		
		// the rest of the songs to skip
		for(int i = 0; i < numTracks - 1; i++) {
			AudioTrack track = queue.poll();
			previousSongs.offerFirst(track.makeClone());
			skippedTracks[i + 1] = track;
		}
		
		return skippedTracks;
	}
	
	// play previous track, add current track to the queue again
	public void prevTrack() {
		AudioTrack currTrack = player.getPlayingTrack();
		
		queue.offerFirst(currTrack.makeClone());
		
		player.startTrack(previousSongs.poll(), false);
		command.resetVotes();
	}
	
	@Override
	public void onPlayerPause(AudioPlayer player) {
		command.sendEmbed(command.getMostRecentEvent(), new EmbedBuilder().setTitle("Song Paused").setDescription(player.getPlayingTrack().getInfo().title).build());

	}

	@Override
	public void onPlayerResume(AudioPlayer player) {
		command.sendEmbed(command.getMostRecentEvent(), new EmbedBuilder().setTitle("Song Resumed").setDescription(player.getPlayingTrack().getInfo().title).build());

	}

	@Override
	public void onTrackStart(AudioPlayer player, AudioTrack track) {
		command.sendEmbed(command.getMostRecentEvent(), new EmbedBuilder().setTitle("Now Playing").setDescription(track.getInfo().title).build());
	}

	@Override
	public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
		if (endReason.mayStartNext) {
			nextTrack(track, true);
		}

	    // endReason == FINISHED: A track finished or died by an exception (mayStartNext = true).
	    // endReason == LOAD_FAILED: Loading of a track failed (mayStartNext = true).
	    // endReason == STOPPED: The player was stopped.
	    // endReason == REPLACED: Another track started playing while this had not finished
	    // endReason == CLEANUP: Player hasn't been queried for a while, if you want you can put a
	    //                       clone of this back to your queue
	}

	@Override
	public void onTrackException(AudioPlayer player, AudioTrack track, FriendlyException exception) {
		command.sendErrorEmbed(command.getMostRecentEvent(), new EmbedBuilder().setDescription("The track threw an exception. Starting next track.").build());
		nextTrack(track, true);
	}

	@Override
	public void onTrackStuck(AudioPlayer player, AudioTrack track, long thresholdMs) {
		command.sendErrorEmbed(command.getMostRecentEvent(), new EmbedBuilder().setDescription("The track is stuck. Starting next track.").build());
		nextTrack(track, true);
	}
	
	public AudioTrack[] getQueueAsArray() {
		return queue.toArray(new AudioTrack[0]);
	}
	
	public AudioTrack[] getPreviousSongsAsArray() {
		return previousSongs.toArray(new AudioTrack[0]);
	}
	
	public int clearQueue() {
		int numOfSongsDeleted = queue.size();
		queue.clear();
		return numOfSongsDeleted;
	}
}
