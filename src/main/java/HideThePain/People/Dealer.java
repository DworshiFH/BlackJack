package HideThePain.People;

import HideThePain.Card;

import java.util.ArrayList;
import java.util.List;

public class Dealer {

    private List<Card> holdingCards;
    private static final String dealerName="Dealer";

    public Dealer() {
        this.holdingCards=new ArrayList<>();
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
}
