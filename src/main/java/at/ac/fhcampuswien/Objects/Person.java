package at.ac.fhcampuswien.Objects;

import java.util.ArrayList;
import java.util.List;

public abstract class Person {

    private final List<Card> holdingCards;
    private final String name;

    public Person(String name) {
        this.holdingCards=new ArrayList<>();
        this.name = name;
    }

    public void addCard(Card card) {
        this.holdingCards.add(card);
    }

    public void removeCard(int index){
        this.holdingCards.remove(index);
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

    public String getName() {
        return name;
    }
}