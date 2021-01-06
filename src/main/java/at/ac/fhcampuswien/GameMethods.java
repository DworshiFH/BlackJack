package at.ac.fhcampuswien;

import at.ac.fhcampuswien.Objects.Card;
import at.ac.fhcampuswien.Objects.Dealer;
import at.ac.fhcampuswien.Objects.Player;

import java.util.List;

public class GameMethods {
    private static boolean hasTalon=false;
    public static boolean TalonFound(){
        return hasTalon;
    }
    public static void ResetTalon(){
        hasTalon=false;
    }

    public static void GiveCardToPlayer(Player player, List<Card> deck) {
        Card card=deck.get(0);
        if(card.getID().equals("Talon")){ //Talon Checker
            hasTalon=true;
            deck.remove(0);
            card=deck.get(0);
        }

        Card addCard=new Card(card);
        addCard.setImageView(AppController.CardTextureAssigner(addCard, player.getHoldingCards().size(),false));
        player.addCard(addCard); //make new Card object, to be able to change card values, without affecting the whole deck.
        deck.remove(0);
    }

    public static void GiveCardToDealer(Dealer dealer, List<Card> deck) {
        Card card=deck.get(0);
        if(card.getID().equals("Talon")){ //Talon Checker
            hasTalon=true;
            deck.remove(0);
            card=deck.get(0);
        }
        Card addCard=new Card(card);
        addCard.setImageView(AppController.CardTextureAssigner(addCard, dealer.getHoldingCards().size(),true));
        dealer.addCard(addCard);
        deck.remove(0);
    }

    public static void SetBet(Player player, int amount){
        player.setStake(amount);
        player.removeBalance(amount);
    }

    public static Player Split(Player player, List<Card> deck) {

        Player ret = new Player("playerSplit", player.getStake());
        ret.setStake(player.getStake());
        player.removeBalance(player.getStake());

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

    public static boolean Win(Player player, Dealer dealer){
        boolean ret;
        int PcardValue=0;

        if(DealerBust(dealer)){
            ret=true;
        }else{
            PcardValue=PlayerValueCalculator(player);
            int DcardValue=DealerValueCalculator(dealer);
            ret= PcardValue > DcardValue;
        }
        if(PcardValue>21){
            ret=false;
        }

        return ret;
    }

    public static boolean Push(Player player, Dealer dealer){
        boolean ret;
        int PcardValue=PlayerValueCalculator(player);
        int DcardValue=DealerValueCalculator(dealer);
        ret= PcardValue == DcardValue;
        return ret;
    }

    public static boolean Lost(Player player, Dealer dealer){
        boolean ret;
        int PcardValue=0;
        if(PlayerBust(player)){
            ret=true;
        }else{
            PcardValue=PlayerValueCalculator(player);
            int DcardValue=DealerValueCalculator(dealer);
            ret= PcardValue < DcardValue;
        }
        if(PcardValue>21) ret = true;

        return ret;
    }

    public static boolean PlayerBust(Player player){
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

    public static boolean DealerBust(Dealer dealer){
        boolean ret = false;
        int DcardValue=DealerValueCalculator(dealer);
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
            for(int i=0; i<player.getHoldingCards().size();i++){ //checks for Ace
                if(player.getHoldingCards().get(i).getValue()==11){
                    player.getHoldingCards().get(i).setValue(1);
                    break;
                }
            }
            PcardValue=0;
            for(int j=0; j<player.getHoldingCards().size();j++){
                PcardValue+=player.getHoldingCards().get(j).getValue();
            }
        }

        return PcardValue;
    }

    public static int DealerValueCalculator(Dealer dealer){
        int DcardValue=0;
        for(int i=0; i<dealer.getHoldingCards().size();i++){
            DcardValue+=dealer.getHoldingCards().get(i).getValue();
        }
        if(DcardValue>21){
            for(int i=0; i<dealer.getHoldingCards().size();i++){ //checks for Ace
                if(dealer.getHoldingCards().get(i).getValue()==11){
                    dealer.getHoldingCards().get(i).setValue(1);
                    break;
                }
            }
            DcardValue=0;
            for(int j=0; j<dealer.getHoldingCards().size();j++){
                DcardValue+=dealer.getHoldingCards().get(j).getValue();
            }
        }
        return DcardValue;
    }
}