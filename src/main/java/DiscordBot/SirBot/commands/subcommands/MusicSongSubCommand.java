package DiscordBot.SirBot.commands.subcommands;

import java.awt.Color;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import DiscordBot.SirBot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MusicSongSubCommand extends SubCommand {
	private AudioPlayer player;

	public MusicSongSubCommand(Command parentCommand, AudioPlayer player) {
		super(parentCommand);
		this.player = player;
	}

	@Override
	public String getName() {
		return "song";
	}

	@Override
	public String getDescription() {
		return "tells you the current song";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		String mention = "<@" + e.getAuthor().getId() + ">";
		
		if(player.getPlayingTrack() == null) {
			parentCommand.sendErrorEmbed(e, new EmbedBuilder().setDescription(mention + ", there is no song currently playing.").build());
		} else if(msgArgs.length == 1) {
			AudioTrack track = player.getPlayingTrack();
			parentCommand.sendEmbed(e, new EmbedBuilder().setTitle("Now Playing").setDescription(track.getInfo().title).build());
		} else {
			parentCommand.sendErrorEmbed(e, new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
		}
	}

}
