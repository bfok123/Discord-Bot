package DiscordBot.SirBot.commands;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import DiscordBot.SirBot.App;
import DiscordBot.SirBot.Info;
import DiscordBot.SirBot.commands.subcommands.SubCommand;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class InsultCommand extends Command {
	private String joshGASM = Info.CustomEmoji.joshGASM.code;
	private String anthonyWOW_AMAZING = Info.CustomEmoji.anthonyWOW_AMAZING.code;
	private String oliverIM_DONE_WITH_EVERYTHING = Info.CustomEmoji.oliverIM_DONE_WITH_EVERYTHING.code;
	private String brandonUR_ON_MY_LIST = Info.CustomEmoji.brandonUR_ON_MY_LIST.code;
	
	private String[] insults = {"is a cotton-headed ninnymuggins! " + joshGASM + joshGASM,
			                    "sure is fat! " + joshGASM,
			                    "smells worse than Brandon's farts! " + joshGASM + brandonUR_ON_MY_LIST + joshGASM,
			                    "is a fat weeb! " + anthonyWOW_AMAZING + anthonyWOW_AMAZING,
			                    "is a little annoying little child! " + joshGASM + joshGASM,
			                    "is the best at being an idiot, and their big ugly face is as dumb as a butt! " + joshGASM + joshGASM + joshGASM,
			                    "has the salt content of the Dead Sea! " + joshGASM + joshGASM,
			                    "is almost as bad as Oli at Overwatch! " + joshGASM + oliverIM_DONE_WITH_EVERYTHING + joshGASM};
	private HashMap<User, CooldownTimer> cooldownTimers;
	
	public InsultCommand() {
		cooldownTimers = new HashMap<User, CooldownTimer>();
	}
	
	@Override
	public String getName() {
		return "insult";
	}

	@Override
	public String getDescription() {
		return "insult a given person or user in the server";
	}

	@Override
	public void onCommand(MessageReceivedEvent e, String[] msgArgs) {
		User user = e.getAuthor();
		String mention = "<@" + user.getId() + ">";
		
		if(msgArgs.length == 1) {
			sendHelpMessage();
			return;
		} else if(msgArgs.length >= 2) {
			MessageBuilder builder = new MessageBuilder();
			String victimName = e.getMessage().getContent().substring(8);
			
			// if it is a mention
			if(containsMention(victimName)) {
				User victimUser = getUserByNameOrNickname(e.getGuild(), victimName.substring(1));
				if(victimUser == null) {
					sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", that user does not exist.").build());
					return;
				} else {
					builder.append(victimUser);
				}
			// if it is not a mention
			} else {
				builder.append(victimName);
			}
			
			// check the cooldown timer of the user
			if(cooldownTimers.containsKey(user)) {
				CooldownTimer timer = cooldownTimers.get(e.getAuthor());
				
				if(!timer.isOver()) {
					sendEmbed(new EmbedBuilder().setTitle("Insult Cooldown").setDescription(mention + ", you have " + timer.getCooldown() + " second(s) remaining until you can insult another person.").setColor(Color.YELLOW).build());
					return;
				} else if(timer.isOver()) {
					timer.resetCooldown();
				}
			// if user does not have a cooldown timer, make one
			} else {
				final CooldownTimer timer = new CooldownTimer();
				cooldownTimers.put(user, timer);
				timer.scheduleAtFixedRate(new TimerTask() {

					@Override
					public void run() {
						timer.reduceCooldown();
					}
					
				}, 0, 1000);
			}
									
			int randomInsultIndex = (int) (Math.random() * insults.length);
			builder.append(" " + insults[randomInsultIndex]);
			
			sendMessage(builder.build());
		} else {
			sendErrorEmbed(new EmbedBuilder().setDescription(mention + ", that is not a valid command.").build());
		}
	}
	
	@Override
	public Message sendUsageEmbed(MessageEmbed embed) {
		embed = new EmbedBuilder(embed).setTitle("Usage - Insult").setColor(Color.YELLOW).build();
		return App.getBotTextChannel().sendMessage(embed).complete();
	}
	
	@Override
	public void sendHelpMessage() {
		EmbedBuilder builder = new EmbedBuilder();
		
		builder.setDescription("Enter any name or mention a user in the server to insult");
		
		sendUsageEmbed(builder.build());
	}
	
	/*
	 * Checks to see if an insult victim is a mention or not, ie: @Brandon
	 */
	public boolean containsMention(String victimName) {
		// if victimName starts with an "@" symbol
		if(victimName.startsWith("@")) {
			return true;
		} else {
			return false;
		}
	}
	
	/*
	 * Checks if a given username or nickname exists in the Guild, return null if it does not exist
	 */
	public User getUserByNameOrNickname(Guild guild, String name) {
		// first check if user has a nickname
		if(!guild.getMembersByNickname(name, false).isEmpty()) {
			return guild.getMembersByNickname(name, false).get(0).getUser();
		// if no nickname, check their name
		} else if(!guild.getMembersByName(name, false).isEmpty()) {
			return guild.getMembersByName(name, false).get(0).getUser();
		// if user does not exist in the guild
		} else {
			return null;
		}
	}
	
	private class CooldownTimer extends Timer {
		private int cooldown; // cooldown in seconds
		private boolean paused;
		
		public CooldownTimer() {
			cooldown = 5;
			paused = false;
		}
		
		public boolean isOver() {
			if(cooldown == 0) {
				return true;
			}
			return false;
		}
		
		public int getCooldown() {
			return cooldown;
		}
		
		public void reduceCooldown() {
			// can't pause a timer, so only lower cooldown if it is not paused
			if(!paused) {
				cooldown--;	
			}
			if(cooldown == 0) {
				paused = true;
			}
		}
		
		public void resetCooldown() {
			cooldown = 5;
			paused = false;
		}
	}

}
