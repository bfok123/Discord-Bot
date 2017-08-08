package DiscordBot.SirBot.commands.subcommands;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import DiscordBot.SirBot.commands.Command;
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
		return "add a song to the queue by entering a URL";
	}

	@Override
	public void onCommand(final MessageReceivedEvent e, String[] msgArgs) {
		final String mention = "<@" + e.getAuthor().getId() + ">";
		
		if(msgArgs.length == 1) {
			parentCommand.sendUsageEmbed(e, new EmbedBuilder().setDescription(mention + ", enter a track or playlist URL.").build());
		} else if(msgArgs.length > 1) {
			if(!e.getGuild().getAudioManager().isConnected()) {
				parentCommand.sendErrorEmbed(e, new EmbedBuilder().setDescription(mention + ", the bot is not connected to a voice channel.").build());
				return;
			}
			
			String trackURL = e.getMessage().getContent().substring(15);
			
			playerManager.loadItem(trackURL, new AudioLoadResultHandler() {
				
				@Override
				public void loadFailed(FriendlyException arg0) {
					parentCommand.sendErrorEmbed(e, new EmbedBuilder().setDescription("Track load failed.").build());
				}

				@Override
				public void noMatches() {
					parentCommand.sendErrorEmbed(e, new EmbedBuilder().setDescription(mention + ", no matches found.").build());
				}

				@Override
				public void playlistLoaded(AudioPlaylist playlist) {
					parentCommand.sendEmbed(e, new EmbedBuilder().setTitle("Playlist Added").setDescription(playlist.getName()).build());
					for(AudioTrack track : playlist.getTracks()) {
						trackScheduler.queue(track);
					}
				}

				@Override
				public void trackLoaded(AudioTrack track) {
					parentCommand.sendEmbed(e, new EmbedBuilder().setTitle("Track Added").setDescription(track.getInfo().title).build());
					trackScheduler.queue(track);
				}
			});
		} else {
			parentCommand.sendErrorEmbed(e, new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
		}
	}

}
