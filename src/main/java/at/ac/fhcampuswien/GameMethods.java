package at.ac.fhcampuswien;


import at.ac.fhcampuswien.Objects.Card;
import at.ac.fhcampuswien.Objects.Dealer;
import at.ac.fhcampuswien.Objects.Player;

import java.util.List;

public class GameMethods {
    private static boolean hasTalon=false;

    public static void GiveCardToPlayer(Player player, List<Card> deck) {
        Card card=deck.get(0);
        if(card.getID().equals("Talon")){ //Talon Checker
            hasTalon=true;
            deck.remove(0);
            card=deck.get(0);
        }
        card.setImageView(AppController.CardTextureAssigner(card, player.getHoldingCards().size(),false));
        player.addCard(card);
        deck.remove(0);
    }

    public static boolean TalonFound(){
        return hasTalon;
    }
    public static void ResetTalon(){
        hasTalon=false;
    }

    public static void GiveCardToDealer(Dealer dealer, List<Card> deck) {
        Card card=deck.get(0);
        if(card.getID().equals("Talon")){ //Talon Checker
            hasTalon=true;
            deck.remove(0);
            card=deck.get(0);
        }
        card.setImageView(AppController.CardTextureAssigner(card, dealer.getHoldingCards().size(),true));
        dealer.addCard(card);
        deck.remove(0);
    }

    public static void SetBet(Player player, int amount){
        player.setStake(amount);
        player.removeBalance(amount);
    }


    //implement Split
    public static Player Split(Player player, List<Card> deck) {

        Player ret = new Player(player.getPlayerName() + "Split", player.getStake());
        ret.setStake(player.getStake());

        ret.addCard(new Card(player.getCard(1).getID(),player.getCard(1).getValue()));

        player.removeCard(1);

        return ret;
    }

    public static void DoubleDown(Player player, List<Card> deck) {
        player.removeBalance(player.getStake());
        player.setStake(player.getStake()*2);
        GiveCardToPlayer(player,deck);
    }
        public static void BlackJackPayout(Player player){
        player.addBalance(player.getStake()*2.5);
        player.setStake(0);
    }
    public static void WinPayout(Player player){
        player.addBalance(player.getStake()*2);
        player.setStake(0);
    }
    public static void PushPayout(Player player){
        player.addBalance(player.getStake()*1);
        player.setStake(0);
    }
    public static void LostPayout(Player player){
        player.setStake(0);
    }
    public static void SurrenderPayout(Player player){
        player.addBalance(player.getStake()*0.5);
        player.setStake(0);
    }
    public static void SplitPayout(Player split){

    }

    public static boolean Win(Player player, Dealer dealer){
        boolean ret;

        if(DealerHasOverdrawn(dealer)){
            ret=true;
        }else{
            int PcardValue=0;
            for(int i=0; i<player.getHoldingCards().size();i++){
                PcardValue+=player.getHoldingCards().get(i).getValue();
            }

            int DcardValue=0;
            for(int i=0; i<dealer.getHoldingCards().size();i++){
                DcardValue+=dealer.getHoldingCards().get(i).getValue();
            }

            ret= PcardValue > DcardValue;
        }

        return ret;
    }
    public static boolean Push(Player player, Dealer dealer){

        boolean ret;
        int PcardValue=0;
        for(int i=0; i<player.getHoldingCards().size();i++){
            PcardValue+=player.getHoldingCards().get(i).getValue();
        }
        int DcardValue=0;
        for(int i=0; i<dealer.getHoldingCards().size();i++){
            DcardValue+=dealer.getHoldingCards().get(i).getValue();
        }

        ret= PcardValue == DcardValue;
        return ret;
    }

    public static boolean Lost(Player player, Dealer dealer){
        boolean ret;
        if(PlayerHasOverdrawn(player)){
            ret=true;
        }else{

            int PcardValue=PlayerValueCalculator(player);
            int DcardValue=DealerValueCalculator(dealer);

            ret= PcardValue < DcardValue;
        }
        return ret;
    }

    public static boolean PlayerHasOverdrawn(Player player){
        boolean ret;
        int PcardValue=0;
        for(int i=0; i<player.getHoldingCards().size();i++){
            PcardValue+=player.getHoldingCards().get(i).getValue();
        }
        if(PcardValue>21){
            ret=true;
            for(int i=0; i<player.getHoldingCards().size();i++){
                if(player.getHoldingCards().get(i).getValue()==11){
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
    public static boolean DealerHasOverdrawn(Dealer dealer){
        boolean ret;
        int DcardValue=0;
        for(int i=0; i<dealer.getHoldingCards().size();i++){
            DcardValue+=dealer.getHoldingCards().get(i).getValue();
        }
        //ret= DcardValue > 21;
        if(DcardValue > 21){
            ret = true;
            for(int i=0; i<dealer.getHoldingCards().size();i++){
                if(dealer.getHoldingCards().get(i).getValue()==11){
                    DcardValue-=10;
                    ret=false;
                }
            }
        }else{
            ret = false;
        }
        if(DcardValue > 21){
            ret = true;
        }
        return ret;
    }


    public static int PlayerValueCalculator(Player player){
        int PcardValue=0;
        for(int i=0; i<player.getHoldingCards().size();i++){
            PcardValue+=player.getHoldingCards().get(i).getValue();
        }
        if(PcardValue>21){
            for(int i=0; i<player.getHoldingCards().size();i++){ //checks for As
                if(player.getHoldingCards().get(i).getValue()==11){
                    PcardValue-=10;
                    break;
                }
            }
        }
        return PcardValue;
    }
    public static int DealerValueCalculator(Dealer dealer){
        int PcardValue=0;
        for(int i=0; i<dealer.getHoldingCards().size();i++){
            PcardValue+=dealer.getHoldingCards().get(i).getValue();
        }
        if(PcardValue>21){
            for(int i=0; i<dealer.getHoldingCards().size();i++){ //checks for As
                if(dealer.getHoldingCards().get(i).getValue()==11){
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





