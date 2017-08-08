package DiscordBot.SirBot;

import javax.security.auth.login.LoginException;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;

import DiscordBot.SirBot.commands.HelpCommand;
import DiscordBot.SirBot.commands.InsultCommand;
import DiscordBot.SirBot.commands.MusicCommand;
import DiscordBot.SirBot.commands.StopwatchCommand;
import DiscordBot.SirBot.music.TrackScheduler;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
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
	private static AudioPlayer player;
	
	public static void main( String[] args )
    {
        try {
			JDA jda = new JDABuilder(AccountType.BOT).setToken(Info.TOKEN).buildBlocking();
			
			playerManager = new DefaultAudioPlayerManager();
			AudioSourceManagers.registerRemoteSources(playerManager);
			AudioSourceManagers.registerLocalSource(playerManager);
			player = playerManager.createPlayer();
			
			jda.addEventListener(new StopwatchCommand());
			jda.addEventListener(new InsultCommand());
			jda.addEventListener(new HelpCommand());
			jda.addEventListener(new MusicCommand(manager, playerManager, player));
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
}
