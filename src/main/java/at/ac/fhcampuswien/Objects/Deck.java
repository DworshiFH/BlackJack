package at.ac.fhcampuswien.Objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck{

    private List<Card> deck = new ArrayList<>();

    //6 sets of Cards

    //Karo
    static Card KA_2 = new Card("KA_2",2);
    static Card KA_3 = new Card("KA_3",3);
    static Card KA_4 = new Card("KA_4",4);
    static Card KA_5 = new Card("KA_5",5);
    static Card KA_6 = new Card("KA_6",6);
    static Card KA_7 = new Card("KA_7",7);
    static Card KA_8 = new Card("KA_8",8);
    static Card KA_9 = new Card("KA_9",9);
    static Card KA_10 = new Card("KA_10",10);
    static Card KA_J = new Card("KA_J",10);
    static Card KA_D = new Card("KA_D",10);
    static Card KA_K = new Card("KA_K",10);
    static Card KA_A = new Card("KA_A",11,true);

    //Pick
    static Card PI_2 = new Card("PI_2",2);
    static Card PI_3 = new Card("PI_3",3);
    static Card PI_4 = new Card("PI_4",4);
    static Card PI_5 = new Card("PI_5",5);
    static Card PI_6 = new Card("PI_6",6);
    static Card PI_7 = new Card("PI_7",7);
    static Card PI_8 = new Card("PI_8",8);
    static Card PI_9 = new Card("PI_9",9);
    static Card PI_10 = new Card("PI_10",10);
    static Card PI_J = new Card("PI_J",10);
    static Card PI_D = new Card("PI_D",10);
    static Card PI_K = new Card("PI_K",10);
    static Card PI_A = new Card("PI_A",11,true);

    //Kreuz
    static Card KR_2 = new Card("KR_2",2);
    static Card KR_3 = new Card("KR_3",3);
    static Card KR_4 = new Card("KR_4",4);
    static Card KR_5 = new Card("KR_5",5);
    static Card KR_6 = new Card("KR_6",6);
    static Card KR_7 = new Card("KR_7",7);
    static Card KR_8 = new Card("KR_8",8);
    static Card KR_9 = new Card("KR_9",9);
    static Card KR_10 = new Card("KR_10",10);
    static Card KR_J = new Card("KR_J",10);
    static Card KR_D = new Card("KR_D",10);
    static Card KR_K = new Card("KR_K",10);
    static Card KR_A = new Card("KR_A",11,true);

    //Herz
    static Card HE_2 = new Card("HE_2",2);
    static Card HE_3 = new Card("HE_3",3);
    static Card HE_4 = new Card("HE_4",4);
    static Card HE_5 = new Card("HE_5",5);
    static Card HE_6 = new Card("HE_6",6);
    static Card HE_7 = new Card("HE_7",7);
    static Card HE_8 = new Card("HE_8",8);
    static Card HE_9 = new Card("HE_9",9);
    static Card HE_10 = new Card("HE_10",10);
    static Card HE_J = new Card("HE_J",10);
    static Card HE_D = new Card("HE_D",10);
    static Card HE_K = new Card("HE_K",10);
    static Card HE_A = new Card("HE_A",11,true);

    static Card talon = new Card("Talon", 0);

    public static List<Card> makeDeck(){

        List<Card> deck = new ArrayList<>();

        deck.add(KA_2);
        deck.add(KA_3);
        deck.add(KA_4);
        deck.add(KA_5);
        deck.add(KA_6);
        deck.add(KA_7);
        deck.add(KA_8);
        deck.add(KA_9);
        deck.add(KA_10);
        deck.add(KA_J);
        deck.add(KA_D);
        deck.add(KA_K);
        deck.add(KA_A);

        deck.add(PI_2);
        deck.add(PI_3);
        deck.add(PI_4);
        deck.add(PI_5);
        deck.add(PI_6);
        deck.add(PI_7);
        deck.add(PI_8);
        deck.add(PI_9);
        deck.add(PI_10);
        deck.add(PI_J);
        deck.add(PI_D);
        deck.add(PI_K);
        deck.add(PI_A);

        deck.add(KR_2);
        deck.add(KR_3);
        deck.add(KR_4);
        deck.add(KR_5);
        deck.add(KR_6);
        deck.add(KR_7);
        deck.add(KR_8);
        deck.add(KR_9);
        deck.add(KR_10);
        deck.add(KR_J);
        deck.add(KR_D);
        deck.add(KR_K);
        deck.add(KR_A);

        deck.add(HE_2);
        deck.add(HE_3);
        deck.add(HE_4);
        deck.add(HE_5);
        deck.add(HE_6);
        deck.add(HE_7);
        deck.add(HE_8);
        deck.add(HE_9);
        deck.add(HE_10);
        deck.add(HE_J);
        deck.add(HE_D);
        deck.add(HE_K);
        deck.add(HE_A);

        List<Card> addDeck = new ArrayList<>(deck);

        for(int i=0; i<5 ;i++){ //loop until 5 because we already added 1 set of a deck.
            deck.addAll(addDeck);
        }

        for(int i = 0; i < 10; i++) //shuffle it many times so it becomes really random.
        Collections.shuffle(deck);

        Random rand = new Random();
        int x=rand.nextInt(252-60+1)+60;

        deck.add(x, talon);

        //for testing purposes only
        //deck.add(0, HE_A);
        //deck.add(1,PI_A);
        //deck.add(4,PI_3);


        return deck;
    }

    /*public static void resetAces(){
        KA_A.setValue(11);
        PI_A.setValue(11);
        KR_A.setValue(11);
        HE_A.setValue(11);
    }*/

}
