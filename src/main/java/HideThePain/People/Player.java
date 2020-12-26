package HideThePain.People;


import HideThePain.Card;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private List<Card> holdingCards;
    private String playerName;
    private double balance;
    private double stake;

    public Player(String playerName, double balance) {
        this.holdingCards=new ArrayList<>();
        this.playerName = playerName;
        this.balance=balance;
        this.stake=0;
    }

    public double getStake() {
        return this.stake;
    }

    public void setStake(double stake) {
        this.stake = stake;
    }

    public void addCard(Card card) {
        this.holdingCards.add(card);
    }

    public void clearHoldingCards(){
        this.holdingCards.clear();
    }

    public List<Card> getHoldingCards() {
        return holdingCards;
    }

    public Card getCard(int index){
        return this.holdingCards.get(index);
    }

    public String getPlayerName() {
        return playerName;
    }

    public double getBalance() {
        return balance;
    }

    public void addBalance(double add){
        this.balance+=add;
    }
    public void removeBalance(double rem){
        this.balance-=rem;
    }

}
