package DiscordBot.SirBot.gui;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import DiscordBot.SirBot.commands.MusicCommand;
import DiscordBot.SirBot.commands.subcommands.MusicSkipSubCommand;

public class ControlPanel extends JPanel {
	private MusicCommand musicCommand;
	private JButton backBtn, playpauseBtn, nextBtn;

	public ControlPanel(MusicCommand musicCommand) {
		this.musicCommand = musicCommand;
		backBtn = new JButton("Back");
		playpauseBtn = new JButton("Play");
		nextBtn = new JButton("Next");
		
		this.setLayout(new GridLayout(1, 3));
		this.add(backBtn);
		this.add(playpauseBtn);
		this.add(nextBtn);
	}
}
