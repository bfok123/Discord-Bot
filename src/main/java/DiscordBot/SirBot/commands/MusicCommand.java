package DiscordBot.SirBot.commands;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import DiscordBot.SirBot.commands.subcommands.MusicBackSubCommand;
import DiscordBot.SirBot.commands.subcommands.MusicElapsedSubCommand;
import DiscordBot.SirBot.commands.subcommands.MusicJoinSubCommand;
import DiscordBot.SirBot.commands.subcommands.MusicLeaveSubCommand;
import DiscordBot.SirBot.commands.subcommands.MusicPauseSubCommand;
import DiscordBot.SirBot.commands.subcommands.MusicQueueSubCommand;
import DiscordBot.SirBot.commands.subcommands.MusicRequestSubCommand;
import DiscordBot.SirBot.commands.subcommands.MusicResumeSubCommand;
import DiscordBot.SirBot.commands.subcommands.MusicSkipSubCommand;
import DiscordBot.SirBot.commands.subcommands.MusicSongSubCommand;
import DiscordBot.SirBot.commands.subcommands.MusicVolumeSubCommand;
import DiscordBot.SirBot.commands.subcommands.SubCommand;
import DiscordBot.SirBot.music.AudioPlayerSendHandler;
import DiscordBot.SirBot.music.TrackScheduler;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.managers.AudioManager;

public class MusicCommand extends Command{
	private AudioManager manager;
	private AudioPlayerManager playerManager;
	private AudioPlayer player;
	private TrackScheduler trackScheduler;
	private SubCommand[] subCommands;
	private MessageReceivedEvent mostRecentEvent;

	public MusicCommand(AudioManager manager, AudioPlayerManager playerManager, AudioPlayer player) {
		this.manager = manager;
		this.playerManager = playerManager;
		this.player = player;
		trackScheduler = new TrackScheduler(player, this);
		player.addListener(trackScheduler);

		
		subCommands = new SubCommand[11];
		subCommands[0] = new MusicJoinSubCommand(this, manager, player);
		subCommands[1] = new MusicRequestSubCommand(this, playerManager, trackScheduler);
		subCommands[2] = new MusicSkipSubCommand(this, player, trackScheduler);
		subCommands[3] = new MusicPauseSubCommand(this, player);
		subCommands[4] = new MusicResumeSubCommand(this, player);
		subCommands[5] = new MusicVolumeSubCommand(this, player);
		subCommands[6] = new MusicQueueSubCommand(this, trackScheduler);
		subCommands[7] = new MusicSongSubCommand(this, player);
		subCommands[8] = new MusicElapsedSubCommand(this, player);
		subCommands[9] = new MusicLeaveSubCommand(this, player);
		subCommands[10] = new MusicBackSubCommand(this, trackScheduler);
	}
	
	// for help command
	public MusicCommand() {}
	
	@Override
	public String getName() {
		return "music";
	}

	@Override
	public String getDescription() {
		return "Play music in a voice channel.";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		String mention = "<@" + e.getAuthor().getId() + ">";
		mostRecentEvent = e;
		
		if(msgArgs.length == 1) {
			sendHelpMessage(e);
		} else {
			// sub commands get msgArgs that start with their name
			String[] subCommandMsgArgs = new String[msgArgs.length - 1];
			for(int i = 0; i < subCommandMsgArgs.length; i++) {
				subCommandMsgArgs[i] = msgArgs[i + 1];
			}
			
			for(SubCommand subCommand : subCommands) {
				if(msgArgs[1].equals(subCommand.getName())) {
					subCommand.onCommand(e, subCommandMsgArgs);
					return;
				}
			}
			
			sendErrorEmbed(e, new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
		}
	}
	
	@Override
	public Message sendMessage(MessageReceivedEvent e, Message message) {
		return e.getChannel().sendMessage(new MessageBuilder().append(":musical_note: ").append(message.getContent()).append(" :musical_note:").build()).complete();
	}
	
	@Override
	public Message sendEmbed(MessageReceivedEvent e, MessageEmbed embed) {
		embed = new EmbedBuilder(embed).setColor(Color.CYAN).build();
		return e.getChannel().sendMessage(embed).complete();
	}
	
	@Override
	public Message sendMessageWithMention(MessageReceivedEvent e, Message message) {
		return e.getChannel().sendMessage(new MessageBuilder().append(e.getAuthor()).append(": ").append(":musical_note: ").append(message.getContent()).append(" :musical_note:").build()).complete();
	}

	@Override
	public Message sendUsageEmbed(MessageReceivedEvent e, MessageEmbed embed) {
		embed = new EmbedBuilder(embed).setTitle("Usage - Music").setColor(Color.YELLOW).build();
		return e.getChannel().sendMessage(embed).complete();
	}
	
	@Override
	public void sendHelpMessage(MessageReceivedEvent e) {
		EmbedBuilder builder = new EmbedBuilder();
		
		for(SubCommand subCommand : subCommands) {
			builder.appendDescription(" - " + subCommand.getName() + " : " + subCommand.getDescription() + "\n");
		}
		
		sendUsageEmbed(e, builder.build());
	}
	
	public MessageReceivedEvent getMostRecentEvent() {
		return mostRecentEvent;
	}
	
	public void resetVotes() {
		MusicSkipSubCommand skipCommand = (MusicSkipSubCommand) subCommands[2];
		skipCommand.resetVotes();
	}
}
