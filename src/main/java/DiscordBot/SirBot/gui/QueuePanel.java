package DiscordBot.SirBot.gui;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import DiscordBot.SirBot.music.TrackScheduler;

public class QueuePanel extends JPanel {
	private TrackScheduler trackScheduler;
	
	public QueuePanel(TrackScheduler trackScheduler) {
		this.trackScheduler = trackScheduler;
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
	}
	
}
