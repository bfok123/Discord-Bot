package DiscordBot.SirBot.commands.subcommands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import DiscordBot.SirBot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MusicPauseSubCommand extends SubCommand {
	private AudioPlayer player;

	public MusicPauseSubCommand(Command parentCommand, AudioPlayer player) {
		super(parentCommand);
		
		this.player = player;
	}

	@Override
	public String getName() {
		return "pause";
	}

	@Override
	public String getDescription() {
		return "pause the current song";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {	
		String mention = "<@" + e.getAuthor().getId() + ">";
		
		if(msgArgs.length == 1) {
			if(player.getPlayingTrack() == null) {
				parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", there is no song currently playing.").build());
			} else if(!player.isPaused()){
				player.setPaused(true);
			} else if(player.isPaused()) {
				parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", the current song is already paused.").build());
			}
		} else {
			parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
		}
	}

}
