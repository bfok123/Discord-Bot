package DiscordBot.SirBot.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import DiscordBot.SirBot.commands.MusicCommand;
import DiscordBot.SirBot.commands.subcommands.MusicRequestSubCommand;
import DiscordBot.SirBot.commands.subcommands.MusicSkipSubCommand;
import DiscordBot.SirBot.music.TrackScheduler;

public class MainFrame extends JFrame {
	private MusicCommand musicCommand;
	private TrackScheduler trackScheduler;
	private MusicRequestSubCommand requestCommand;
	
	public MainFrame(MusicCommand musicCommand, TrackScheduler trackScheduler, MusicRequestSubCommand requestCommand) {
		this.setTitle("Sir Bot Music Controller");
		this.setSize(500, 700);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		
		this.musicCommand = musicCommand;
		this.trackScheduler = trackScheduler;
		this.requestCommand = requestCommand;
		
		addComponents();
		
		this.setVisible(true);
	}
	
	public void addComponents() {
		GridBagConstraints c = new GridBagConstraints();
		
		ControlPanel controlPanel = new ControlPanel(musicCommand);
		c.gridx = 0;
		c.gridy = 0;
		c.weightx = 1;
		c.weighty = 0.1;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(controlPanel, c);
		
		QueuePanel queuePanel = new QueuePanel(trackScheduler);
		JScrollPane scrollPane = new JScrollPane(queuePanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 100;
		c.fill = GridBagConstraints.BOTH;
		this.add(scrollPane, c);
		
		JButton requestBtn = makeRequestButton();
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 1;
		c.weighty = 0.1;
		c.anchor = GridBagConstraints.LAST_LINE_END;
		c.fill = GridBagConstraints.HORIZONTAL;
		this.add(requestBtn, c);
	}
	
	public JButton makeRequestButton() {
		JButton button = new JButton("Request Song");
		
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String trackUrl = JOptionPane.showInputDialog("Enter a song URL");
				int addToBeginningInt = JOptionPane.showConfirmDialog(null, "Add to beginning of the queue?", "Music Controller", JOptionPane.YES_NO_OPTION);
				boolean addToBeginning = addToBeginningInt == JOptionPane.YES_OPTION;
				requestCommand.handleTrackRequest("admin", trackUrl, addToBeginning);
			}
		});
		
		return button;
	}
}
