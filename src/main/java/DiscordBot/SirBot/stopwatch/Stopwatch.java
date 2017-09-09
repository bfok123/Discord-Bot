package DiscordBot.SirBot.stopwatch;

import java.util.Timer;

import DiscordBot.SirBot.commands.Command;
import DiscordBot.SirBot.commands.StopwatchCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Stopwatch extends Timer {
	private int hours, minutes, seconds;
	private boolean paused;
	private Command command;
	private MessageReceivedEvent e;
	
	public Stopwatch(Command command, MessageReceivedEvent e) {
		paused = false;
		hours = 0;
		minutes = 0;
		seconds = 0;
		this.command = command;
		this.e = e;
	}
	
	public void updateSeconds() {
		seconds++;
		if(seconds == 60) {
			updateMinutes();
			seconds = 0;
		}
	}
	
	public void updateMinutes() {
		minutes++;
		if(minutes == 60) {
			updateHours();
			minutes = 0;
		}
	}
	
	public void updateHours() {
		String mention = "<@" + e.getAuthor().getId() + ">";
		hours++;
		if(hours == 1) {
			command.sendEmbed(new EmbedBuilder().setTitle("Stopwatch").setDescription(mention + ", you have played for " + hours + " hour.").build());
		} else if(hours > 1) {
			command.sendEmbed(new EmbedBuilder().setTitle("Stopwatch").setDescription(mention + ", you have played for " + hours + " hours.").build());
		}
	}
	
	public void pause() {
		paused = true;
	}
	
	public void unpause() {
		paused = false;
	}
	
	public boolean isPaused() {
		return paused;
	}
	
	public int getSeconds() {
		return seconds;
	}
	
	public int getMinutes() {
		return minutes;
	}
	
	public int getHours() {
		return hours;
	}
}
