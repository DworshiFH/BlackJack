package at.ac.fhcampuswien;



import at.ac.fhcampuswien.Objects.Card;
import at.ac.fhcampuswien.Objects.Dealer;
import at.ac.fhcampuswien.Objects.Player;

import java.util.List;

public class GameMethods {
    private static boolean hasTalon=false;

    public static void giveCardToPlayer(Player P, List<Card> deck) {
        Card card=deck.get(0);
        if(card.getID().equals("Talon")){ //Talon Checker
            hasTalon=true;
            deck.remove(0);
            card=deck.get(0);
        }
        card.setImageView(AppController.CardTextureAssigner(card, P.getHoldingCards().size(),false));
        P.addCard(card);
        deck.remove(0);
    }

    public static boolean talonFound(){
        return hasTalon;
    }
    public static void resetTalon(){
        hasTalon=false;
    }

    public static void giveCardToDealer(Dealer D, List<Card> deck) {
        Card card=deck.get(0);
        if(card.getID().equals("Talon")){ //Talon Checker
            hasTalon=true;
            deck.remove(0);
            card=deck.get(0);
        }
        card.setImageView(AppController.CardTextureAssigner(card, D.getHoldingCards().size(),true));
        D.addCard(card);
        deck.remove(0);
    }
    public static void Hit(Player P, List<Card> deck) {
        Card card=deck.get(0);
        card.setImageView(AppController.CardTextureAssigner(card, P.getHoldingCards().size(),false));
        P.addCard(card);
        deck.remove(0);
    }

    public static void setStake(Player P, int amount){
        P.setStake(amount);
        P.removeBalance(amount);
    }

    //implement Split

    public static Player Split(Player P, List<Card> deck) {
        boolean hasTalon=false;
        List<Card> cards=P.getHoldingCards();

        if(cards.size()==2){

            if(cards.get(0).getValue()==cards.get(1).getValue()){
                Player ret=new Player(P.getPlayerName()+" Split", P.getStake());
                ret.setStake(P.getStake());

                List <Card> temp=P.getHoldingCards();

                ret.addCard(temp.get(0));
                temp.remove(0);

                P.clearHoldingCards();
                giveCardToPlayer(P,temp);

                giveCardToPlayer(P, deck);
                giveCardToPlayer(ret, deck);

                return ret;
            }
        }
        return null;
    }

    public static void DoubleDown(Player P, List<Card> deck) {
        P.removeBalance(P.getStake());
        P.setStake(P.getStake()*2);
        giveCardToPlayer(P,deck);
    }

    public static void BlackJackPayout(Player P){
        P.addBalance(P.getStake()*2.5);
        P.setStake(0);
    }
    public static void winPayout(Player P){
        P.addBalance(P.getStake()*2);
        P.setStake(0);
    }
    public static void pushPayout(Player P){
        P.addBalance(P.getStake()*1);
        P.setStake(0);
    }
    public static void lostPayout(Player P){
        P.setStake(0);
    }

    public static boolean win(Player P, Dealer D){
        boolean ret;

        if(DealerHasOverdrawn(D)){
            ret=true;
        }else{
            int PcardValue=0;
            for(int i=0; i<P.getHoldingCards().size();i++){
                PcardValue+=P.getHoldingCards().get(i).getValue();
            }

            int DcardValue=0;
            for(int i=0; i<D.getHoldingCards().size();i++){
                DcardValue+=D.getHoldingCards().get(i).getValue();
            }

            ret= PcardValue > DcardValue;
        }

        return ret;
    }
    public static boolean push(Player P, Dealer D){

        boolean ret;
        int PcardValue=0;
        for(int i=0; i<P.getHoldingCards().size();i++){
            PcardValue+=P.getHoldingCards().get(i).getValue();
        }
        int DcardValue=0;
        for(int i=0; i<D.getHoldingCards().size();i++){
            DcardValue+=D.getHoldingCards().get(i).getValue();
        }

        ret= PcardValue == DcardValue;
        return ret;
    }

    public static boolean lost(Player P, Dealer D){
        boolean ret;
        if(PlayerHasOverdrawn(P)){
            ret=true;
        }else{

            int PcardValue=PlayerValueCalculator(P);
            int DcardValue=DealerValueCalculator(D);

            ret= PcardValue < DcardValue;
        }
        return ret;
    }

    public static boolean PlayerHasOverdrawn(Player P){
        boolean ret;
        int PcardValue=0;
        for(int i=0; i<P.getHoldingCards().size();i++){
            PcardValue+=P.getHoldingCards().get(i).getValue();
        }
        if(PcardValue>21){
            ret=true;
            for(int i=0; i<P.getHoldingCards().size();i++){
                if(P.getHoldingCards().get(i).getValue()==11){
                    PcardValue-=10;
                    ret=false;
                }
            }
        }else{
            ret=false;
        }
        if(PcardValue>21){
            ret=true;
        }
        return ret;
    }
    public static boolean DealerHasOverdrawn(Dealer D){
        boolean ret;
        int DcardValue=0;
        for(int i=0; i<D.getHoldingCards().size();i++){
            DcardValue+=D.getHoldingCards().get(i).getValue();
        }
        ret= DcardValue > 21;
        return ret;
    }


    public static int PlayerValueCalculator(Player P){
        int PcardValue=0;
        for(int i=0; i<P.getHoldingCards().size();i++){
            PcardValue+=P.getHoldingCards().get(i).getValue();
        }
        if(PcardValue>21){
            for(int i=0; i<P.getHoldingCards().size();i++){ //checks for As
                if(P.getHoldingCards().get(i).getValue()==11){
                    PcardValue-=10;
                    break;
                }
            }
        }
        return PcardValue;
    }
    public static int DealerValueCalculator(Dealer D){
        int PcardValue=0;
        for(int i=0; i<D.getHoldingCards().size();i++){
            PcardValue+=D.getHoldingCards().get(i).getValue();
        }
        if(PcardValue>21){
            for(int i=0; i<D.getHoldingCards().size();i++){ //checks for As
                if(D.getHoldingCards().get(i).getValue()==11){
                    PcardValue-=10;
                    break;
                }
            }
        }
        return PcardValue;
    }
}






/*    public static void main(String[] args){

        boolean playing=true;
        boolean isBlackJack=false;

        boolean push=false;
        boolean talonFound=false;


        boolean win=false;
        boolean lost=false;



        List<Card> deck = new ArrayList<>();
        deck=Deck.makeDeck();

        Dealer dealer=new Dealer();

        Player player1=new Player("Kurti",1000);
        //Player player2=new Player("Hansi",1000);

        do{
            if(hasTalon){
                deck=Deck.makeDeck();
                hasTalon=false;
            }

            //set your Stakes;
            setStake(player1);

            //Dealer and Players get Cards
            giveCardToPlayer(player1, deck);

            //get Texture out of Card-Object and make ImageView out of it

            giveCardToDealer(dealer, deck);
            giveCardToPlayer(player1, deck);
            giveCardToDealer(dealer, deck);

            //check for BlackJack
            if(player1.getCard(0).getValue()==11 && player1.getCard(1).getValue()==10){
                BlackJack(player1, dealer);
                continue;
            }else if(player1.getCard(1).getValue()==11 && player1.getCard(0).getValue()==10){
                BlackJack(player1, dealer);
                continue;
            }

            //check for possibility of Hit Split Double Down

            int dealerCardValue=0;
            for(int i=0; i<dealer.getHoldingCards().size();i++){
                dealerCardValue+=dealer.getHoldingCards().get(i).getValue();
            }

            do{
                giveCardToDealer(dealer, deck);
                dealerCardValue+=dealer.getHoldingCards().get(dealer.getHoldingCards().size()-1).getValue();

            }while(dealerCardValue<17);
            win=win(player1,dealer);
            lost=lost(player1, dealer);
            push=push(player1,dealer);
            if(win){
                winPayout(player1,dealer);
                continue;
            }
            if(lost){
                lostPayout(player1, dealer);
                continue;
            }
            if(push){
                pushPayout(player1,dealer);
                continue;
            }
            if(endGame()){
                player1.setStake(0);
                player1.clearHoldingCards();
                break;
            }

        }while (playing);

        System.out.println("Thank you for Playing");













    }*/





