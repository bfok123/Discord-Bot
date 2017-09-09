package DiscordBot.SirBot.commands.subcommands;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import DiscordBot.SirBot.commands.Command;
import DiscordBot.SirBot.commands.MusicCommand;
import DiscordBot.SirBot.music.TrackScheduler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

public class MusicRequestSubCommand extends SubCommand {
	private AudioPlayerManager playerManager;
	private TrackScheduler trackScheduler;

	public MusicRequestSubCommand(Command parentCommand, AudioPlayerManager playerManager, TrackScheduler trackScheduler) {
		super(parentCommand);
		
		this.playerManager = playerManager;
		this.trackScheduler = trackScheduler;
	}

	@Override
	public String getName() {
		return "request";
	}

	@Override
	public String getDescription() {
		return "add a song to the end of the queue by entering a URL, or use \"first\" to add to the beginning of the queue";
	}

	@Override
	public void onCommand(final MessageReceivedEvent e, String[] msgArgs) {
		final String mention = "<@" + e.getAuthor().getId() + ">";
		
		if(msgArgs.length == 1) {
			parentCommand.sendUsageEmbed(new EmbedBuilder().setDescription(mention + ", enter a track or playlist URL.").build());
		} else if(msgArgs.length > 1) {
			if(!e.getGuild().getAudioManager().isConnected()) {
				parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", the bot is not connected to a voice channel.").build());
				return;
			}
			
			final boolean addToBeginning = msgArgs[1].equals("first") ? true : false;
			
			String trackUrl = addToBeginning ? e.getMessage().getContent().substring(21) : e.getMessage().getContent().substring(15);
			
			handleTrackRequest(mention, trackUrl, addToBeginning);
		} else {
			parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
		}
	}
	
	public void handleTrackRequest(final String mention, String trackUrl, final boolean addToBeginning) {
		playerManager.loadItem(trackUrl, new AudioLoadResultHandler() {
			
			@Override
			public void loadFailed(FriendlyException arg0) {
				parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription("Track load failed.").build());
			}

			@Override
			public void noMatches() {
				parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", no matches found.").build());
			}

			@Override
			public void playlistLoaded(AudioPlaylist playlist) {
				if(addToBeginning) parentCommand.sendEmbed(new EmbedBuilder().setTitle("Playlist Added to Beginning").setDescription(playlist.getName()).build());
				else if(!addToBeginning) parentCommand.sendEmbed(new EmbedBuilder().setTitle("Playlist Added").setDescription(playlist.getName()).build());
				
				for(AudioTrack track : playlist.getTracks()) {
					trackScheduler.queue(track, addToBeginning);
				}
			}

			@Override
			public void trackLoaded(AudioTrack track) {
				if(addToBeginning) parentCommand.sendEmbed(new EmbedBuilder().setTitle("Track Added to Beginning").setDescription(track.getInfo().title).build());
				else if(!addToBeginning) parentCommand.sendEmbed(new EmbedBuilder().setTitle("Track Added").setDescription(track.getInfo().title).build());
				
				trackScheduler.queue(track, addToBeginning);
			}
		});
	}

}
