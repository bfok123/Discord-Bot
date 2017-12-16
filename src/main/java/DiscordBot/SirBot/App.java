package DiscordBot.SirBot;

import java.awt.Color;
import java.util.List;

import javax.security.auth.login.LoginException;
import javax.swing.JOptionPane;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import DiscordBot.SirBot.commands.BlackjackCommand;
import DiscordBot.SirBot.commands.HelpCommand;
import DiscordBot.SirBot.commands.InsultCommand;
import DiscordBot.SirBot.commands.MusicCommand;
import DiscordBot.SirBot.commands.TextChannelCommand;
import DiscordBot.SirBot.commands.StopwatchCommand;
import DiscordBot.SirBot.gui.MainFrame;
import DiscordBot.SirBot.music.TrackScheduler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.exceptions.RateLimitedException;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.managers.AudioManager;

/**
 * Hello world!
 *
 */
public class App
{
	private static AudioManager manager;
	private static AudioPlayerManager playerManager;
	public static JDA jda;
	private static TextChannel botTextChannel;
	private static AudioPlayer player;
	
	public static void main( String[] args )
    {
        try {
			String channelName = JOptionPane.showInputDialog("Enter a text channel name for the bot to send messages to:");
        	
			jda = new JDABuilder(AccountType.BOT).setToken(Info.TOKEN).buildBlocking();
			
			setBotTextChannel(channelName);
			
			playerManager = new DefaultAudioPlayerManager();
			AudioSourceManagers.registerRemoteSources(playerManager);
			AudioSourceManagers.registerLocalSource(playerManager);
			player = playerManager.createPlayer();
			HelpCommand helpCommand = new HelpCommand();
			jda.addEventListener(helpCommand);
			jda.addEventListener(helpCommand.registerCommand(new StopwatchCommand()));
			jda.addEventListener(helpCommand.registerCommand(new InsultCommand()));
			jda.addEventListener(helpCommand.registerCommand(new MusicCommand(manager, playerManager, player)));
			jda.addEventListener(helpCommand.registerCommand(new TextChannelCommand()));
			jda.addEventListener(helpCommand.registerCommand(new BlackjackCommand()));
			
			// make MainFrame
			
		} catch (LoginException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (RateLimitedException e) {
			e.printStackTrace();
		}
    }
	
	public static TextChannel setBotTextChannel(String channelName) {		
		List<TextChannel> textChannels = jda.getTextChannelsByName(channelName, false);
	
		// if there are no text channels that have the given name
		if(textChannels.isEmpty()) {
			// if the current botTextChannel is null (the user just started the bot)
			if(botTextChannel == null) {
				JOptionPane.showMessageDialog(null, "\"" + channelName + "\"" + " is not a valid text channel name.");
				System.exit(0);
			}
		} else {
			TextChannel textChannel = jda.getTextChannelsByName(channelName, false).get(0);
			
			// if bot is already in a text channel
			if(botTextChannel != null) {
				// notify in old text channel that the text channel will be changed
				botTextChannel.sendMessage(new EmbedBuilder().setColor(Color.GREEN).setTitle("Text Channel").setDescription("Setting to \"" + channelName + "\"").build()).complete();
				textChannel.sendMessage(new EmbedBuilder().setColor(Color.GREEN).setTitle("Text Channel").setDescription("Responses will now be sent to this text channel.").build()).complete();
			} else {
				textChannel.sendMessage(new EmbedBuilder().setColor(Color.GREEN).setTitle("Text Channel").setDescription("Responses will be sent to this text channel.").build()).complete();
			}
			
			botTextChannel = textChannel;
			return textChannel;
		}
		return null;
	}
	
	public static TextChannel getBotTextChannel() {
		return botTextChannel;
	}
}
