package DiscordBot.SirBot.commands.subcommands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;

import DiscordBot.SirBot.commands.Command;
import DiscordBot.SirBot.commands.MusicCommand;
import DiscordBot.SirBot.music.AudioPlayerSendHandler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.VoiceChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

public class MusicJoinSubCommand extends SubCommand {
	private AudioManager manager;
	private AudioPlayerSendHandler sendHandler;
	private AudioPlayer player;

	public MusicJoinSubCommand(Command parentCommand, AudioManager manager, AudioPlayer player) {
		super(parentCommand);
		this.manager = manager;
		this.player = player;
		sendHandler = new AudioPlayerSendHandler(player);
	}

	@Override
	public String getName() {
		return "join";
	}

	@Override
	public String getDescription() {
		return "get the bot to join a voice channel in the server";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		String mention = "<@" + e.getAuthor().getId() + ">";
		
		if(msgArgs.length == 1) {
			 parentCommand.sendUsageEmbed(new EmbedBuilder().setDescription(mention + ", enter a voice channel name.").build());
		} else if(msgArgs.length > 1) {
			String channelName = e.getMessage().getContent().substring(12);
			boolean voiceChannelExists = !e.getGuild().getVoiceChannelsByName(channelName, false).isEmpty();
			
			if(voiceChannelExists) {
				manager = e.getGuild().getAudioManager();
				manager.setSendingHandler(sendHandler);
				manager.openAudioConnection(getVoiceChannelByName(e.getGuild(), channelName));
				
				parentCommand.sendEmbed(new EmbedBuilder().setTitle("Joined Channel").setDescription(channelName).build());
				player.setPaused(false);
			} else if(!voiceChannelExists) {
				parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", there are no voice channels in this server that match that name.").build());
			}
		} else {
			parentCommand.sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
		}
	}
	
	public VoiceChannel getVoiceChannelByName(Guild guild, String name) {
		VoiceChannel channel = guild.getVoiceChannelsByName(name, false).get(0);
		return channel;
	}

}
