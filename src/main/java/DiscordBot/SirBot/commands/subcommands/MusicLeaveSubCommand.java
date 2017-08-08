package DiscordBot.SirBot.commands.subcommands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import DiscordBot.SirBot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

public class MusicLeaveSubCommand extends SubCommand {
	private AudioPlayer player;

	public MusicLeaveSubCommand(Command parentCommand, AudioPlayer player) {
		super(parentCommand);
		this.player = player;
	}

	@Override
	public String getName() {
		return "leave";
	}

	@Override
	public String getDescription() {
		return "make the bot leave the voice channel that it is currently in";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		AudioManager manager = e.getGuild().getAudioManager();
		String mention = "<@" + e.getAuthor().getId() + ">";
		
		if(msgArgs.length == 1) {
			if(!manager.isConnected()) {
				parentCommand.sendErrorEmbed(e, new EmbedBuilder().setDescription(mention + ", the bot is not connected to a voice channel.").build());
			} else if(manager.isConnected()) {
				VoiceChannel channel = manager.getConnectedChannel();
				manager.setSendingHandler(null);
				manager.closeAudioConnection();
				parentCommand.sendEmbed(e, new EmbedBuilder().setTitle("Left Channel").setDescription(channel.getName()).build());
				player.setPaused(true);
			}
		} else {
			parentCommand.sendErrorEmbed(e, new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
		}
	}

}
