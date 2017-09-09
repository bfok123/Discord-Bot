package DiscordBot.SirBot.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

/*
 * A single track in the QueuePanel
 */
public class TrackPanel extends JPanel implements MouseListener {
	private AudioTrack track;
	private QueuePanel parentPanel;
	
	public TrackPanel(AudioTrack track, QueuePanel parentPanel) {
		this.parentPanel = parentPanel;
		this.track = track;
		
		this.addMouseListener(this);
		this.setPreferredSize(parentPanel.getSize()); // to fill the width of the parent panel
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Segoe UI", Font.PLAIN, 30));
		
		g.drawString(track.getInfo().title, 5, 30);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getClickCount() == 2) {
			// skip to this song
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
}
