package DiscordBot.SirBot.commands.subcommands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import DiscordBot.SirBot.commands.Command;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class MusicVolumeSubCommand extends SubCommand {
	private AudioPlayer player;

	public MusicVolumeSubCommand(Command parentCommand, AudioPlayer player) {
		super(parentCommand);
		this.player = player;
	}

	@Override
	public String getName() {
		return "volume";
	}

	@Override
	public String getDescription() {
		return "set the volume of the music (0 to 100)";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		if(msgArgs.length == 1) {
			parentCommand.sendUsageEmbed(e, new EmbedBuilder().setDescription("Enter a volume level from 0 to 100.").build());
		} else if(msgArgs.length == 2) {
			int volume = Integer.parseInt(msgArgs[1]);
			if(volume < 0) volume = 0;
			else if(volume > 100) volume = 100;
			player.setVolume(volume);
			parentCommand.sendEmbed(e, new EmbedBuilder().setTitle("Volume").setDescription("Set to " + volume).build());
		} else {
			parentCommand.sendErrorEmbed(e, new EmbedBuilder().setDescription("That is not a valid command.").build());
		}
	}

}
