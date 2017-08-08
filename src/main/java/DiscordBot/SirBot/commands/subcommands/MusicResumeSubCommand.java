package DiscordBot.SirBot.commands.subcommands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import DiscordBot.SirBot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MusicResumeSubCommand extends SubCommand {
	private AudioPlayer player;

	public MusicResumeSubCommand(Command parentCommand, AudioPlayer player) {
		super(parentCommand);
		this.player = player;
	}

	@Override
	public String getName() {
		return "resume";
	}

	@Override
	public String getDescription() {
		return "resume the current song";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		String mention = "<@" + e.getAuthor().getId() + ">";
		
		if(msgArgs.length == 1) {
			if(player.getPlayingTrack() == null) {
				parentCommand.sendErrorEmbed(e, new EmbedBuilder().setDescription(mention + ", there is no song currently playing.").build());
			} else if(!e.getGuild().getAudioManager().isConnected()) {
				parentCommand.sendErrorEmbed(e, new EmbedBuilder().setDescription(mention + ", the bot is not connected to a voice channel.").build());
			} else if(!player.isPaused()) {
				parentCommand.sendErrorEmbed(e, new EmbedBuilder().setDescription(mention + ", the song: \"" + player.getPlayingTrack().getInfo().title + "\" is already playing.").build());
			} else if(player.isPaused()) {
				player.setPaused(false);
			}
		} else {
			parentCommand.sendErrorEmbed(e, new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
		}
	}

}
