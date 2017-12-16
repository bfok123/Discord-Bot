package DiscordBot.SirBot.blackjack;

import java.util.ArrayList;
import java.util.List;

public class BlackjackHand {
	public static final int MAX_VALUE = 21;

	private List<Card> cards;
	private int value;
	
	public BlackjackHand(Card c1, Card c2) {
		cards = new ArrayList<Card>();
		cards.add(c1);
		cards.add(c2);
		calculateValue();
	}
	
	public boolean bust() {
		return getValue() > MAX_VALUE;
	}
	
	public List<String> getValues() {
		List<String> result = new ArrayList<String>();
		for(Card c : cards) {
			result.add(c.getValue());
		}
		return result;
	}
	
	public void clearHand() {
		cards.clear();
		value = 0;
	}
	
	public void addCard(Card card) {
		cards.add(card);
		calculateValue();
	}
	
	public void calculateValue() {
		value = 0;
		// have to calculate value of aces at end (to see if they are 1 or 11)
		List<Card> aces = new ArrayList<Card>();
		for(Card c : cards) {
			String val = c.getValue();
			if(val.equals("J") || val.equals("Q") || val.equals("K")) {
				value += 10;
			} else if(val.matches("[1-9]|10")) {
				value += Integer.parseInt(val);
			} else {
				aces.add(c);
			}
		}
		for(Card ace : aces) {
			if(value + 11 > MAX_VALUE) {
				value += 1;
			} else {
				value += 11;
			}
		}
	}
	
	public int getValue() {
		calculateValue();
		System.out.println(value);
		return value;
	}
}
