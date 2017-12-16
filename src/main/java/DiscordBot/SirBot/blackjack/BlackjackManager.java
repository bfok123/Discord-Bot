package DiscordBot.SirBot.blackjack;

import java.util.*;

import net.dv8tion.jda.core.entities.User;

public class BlackjackManager {
	public static final String[] values = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
	
	private Map<User, BlackjackHand> hands;
	private List<User> users;
	private int currPlayerIndex;
	private List<Card> deck;
	private boolean waitingForPlayerDecisions;
	private List<User> queueToJoin;
	private List<User> queueToLeave;
	
	public BlackjackManager() {
		queueToJoin = new LinkedList<User>();
		queueToLeave = new LinkedList<User>();
		deck = new LinkedList<Card>();
		buildDeck();
		hands = new HashMap<User, BlackjackHand>();
		users = new ArrayList<User>();
		currPlayerIndex = 0;
		waitingForPlayerDecisions = false;
	}
	
	public void buildDeck() {
		for(String cardVal : values) {
			for(int i = 0; i < 4; i++) {
				deck.add(new Card(cardVal));
			}
		}
		Collections.shuffle(deck);
	}
	
	public void dealCards() {
		currPlayerIndex = 0;
		for(User user : users) {
			if(deck.isEmpty()) {
				buildDeck();
			}
			Card curr1 = deck.remove(0);
			Card curr2 = deck.remove(0);
			hands.put(user, new BlackjackHand(curr1, curr2));
		}
		waitingForPlayerDecisions = true;
	}
	
	// returns true if a player has a blackjack (only use right after dealing cards)
	public boolean playerHasBlackjack() {
		for(User user : users) {
			if(hands.get(user).getValue() == 21) {
				waitingForPlayerDecisions = false; // ends round, deal to start new round
				return true;
			}
		}
		return false;
	}
	
	public boolean isPlaying(User user) {
		return users.contains(user);
	}
	
	public void addUsersInQueue() {
		Iterator<User> itr = queueToJoin.iterator();
		while(itr.hasNext()) {
			User curr = itr.next();
			users.add(curr);
			itr.remove();
		}
	}
	
	public void removeUsersInQueue() {
		Iterator<User> itr = queueToLeave.iterator();
		while(itr.hasNext()) {
			User curr = itr.next();
			removeUser(curr);
			itr.remove();
		}
	}
	
	public void addUser(User user) {
		if(waitingForPlayerDecisions) {
			queueToJoin.add(user);
		} else {
			users.add(user);
		}
	}
	
	// if not waiting for player decisions, remove given user
	public void removeUser(User user) {
		if(waitingForPlayerDecisions) {
			queueToLeave.add(user);
		} else {
			users.remove(user);
			hands.remove(user);
		}
	}
	
	public BlackjackHand getUserHand(User user) {
		return hands.get(user);
	}
	
	public User getUserTurn() {
		return users.get(currPlayerIndex);
	}
	
	public List<User> getUsers() {
		return Collections.unmodifiableList(users);
	}
	
	public boolean waitingForPlayerDecisions() {
		return waitingForPlayerDecisions;
	}
	
	// returns null if user can't hit, value if user can hit, or "<value> bust" if user busts after hit
	public String recordHit(User user) {
		// if it is given User's turn
		if(!users.isEmpty() && waitingForPlayerDecisions && user.equals(users.get(currPlayerIndex))) {
			Card removed = deck.remove(0);
			hands.get(user).addCard(removed);
			if(deck.isEmpty()) {
				buildDeck();
				System.out.println("rebuild deck");
			}
			if(hands.get(user).bust()) {
				return removed.getValue() + " bust";
			}
			return removed.getValue();	
		} else {
			return null;
		}
	}
	
	public void recordStand(User user) {
		if(user.equals(users.get(currPlayerIndex)) && waitingForPlayerDecisions) {
			nextTurn();
		}
	}
	
	// switches turn to next player, returns next player - null if at end of list of players (must deal to start next round)
	public User nextTurn() {
		currPlayerIndex++;
		if(currPlayerIndex >= users.size()) {
			waitingForPlayerDecisions = false;
			return null;
		}
		return users.get(currPlayerIndex);
	}
	
	public List<User> determineWinners() {
		List<User> contenders = new ArrayList<User>();
		// contenders to be winner are players who have not busted
		for(User user : users) {
			if(!hands.get(user).bust()) {
				contenders.add(user);
			}
		}
		// if everyone busted, there is no winner
		if(contenders.isEmpty()) {
			return null;
		} else {
			List<User> winners = new ArrayList<User>();
			winners.add(users.get(0));
			User currWinner = users.get(0);
			int minDiffTo21 = Math.abs(hands.get(currWinner).getValue() - 21);
			for(User user : contenders) {
				int currDiffTo21 = Math.abs(hands.get(user).getValue() - 21);
				if(currDiffTo21 < minDiffTo21) {
					winners.clear();
					winners.add(user);
					minDiffTo21 = currDiffTo21;
				}
			}
			return winners;
		}
	}
	
}
