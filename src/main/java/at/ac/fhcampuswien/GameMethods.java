package at.ac.fhcampuswien;

import at.ac.fhcampuswien.Objects.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
        addCard.setImageView(CardTextureAssigner(addCard, player.getHoldingCards().size(),false));
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
        addCard.setImageView(CardTextureAssigner(addCard, dealer.getHoldingCards().size(),true));
        dealer.addCard(addCard);
        deck.remove(0);
    }

    public static void SetBet(Player player, int amount){
        player.setStake(amount);
        player.removeBalance(amount);
    }

    public static Player Split(Player player) {

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

        if(BustCalculator(dealer)){
            ret=true;
        }else{
            PcardValue= CardsValueCalculator(player);
            int DcardValue=CardsValueCalculator(dealer);
            ret= PcardValue > DcardValue;
        }
        if(PcardValue>21){
            ret=false;
        }

        return ret;
    }

    public static boolean Push(Player player, Dealer dealer){
        boolean ret;
        int PcardValue= CardsValueCalculator(player);
        int DcardValue=CardsValueCalculator(dealer);
        ret= PcardValue == DcardValue;
        return ret;
    }

    public static boolean Lost(Player player, Dealer dealer){
        boolean ret;
        int PcardValue=0;
        if(BustCalculator(player)){
            ret=true;
        }else{
            PcardValue= CardsValueCalculator(player);
            int DcardValue=CardsValueCalculator(dealer);
            ret= PcardValue < DcardValue;
        }
        if(PcardValue>21) ret = true;

        return ret;
    }

    public static boolean BustCalculator(Person person){
        boolean ret = false;
        int CardValue=CardsValueCalculator(person);
        if(CardValue > 21){
            ret = true;
        }
        return ret;
    }

    public static int CardsValueCalculator(Person person){
        int PcardValue=0;
        for(int i=0; i<person.getHoldingCards().size();i++){
            PcardValue+=person.getHoldingCards().get(i).getValue();
        }
        if(PcardValue>21){
            for(int i=0; i<person.getHoldingCards().size();i++){ //checks for Ace
                if(person.getCard(i).getValue()==11){
                    person.getCard(i).setValue(1);
                    break;
                }
            }
            PcardValue=0;
            for(int j=0; j<person.getHoldingCards().size();j++){
                PcardValue+=person.getCard(j).getValue();
            }
        }

        return PcardValue;
    }

    private final static int offset = 110;
    public static ImageView CardTextureAssigner(Card card, int cardnum, boolean isDealer) { //true = Dealer
        ImageView imageView = new ImageView();
        try{
            String path;

            if(cardnum==1 && isDealer){
                path=System.getProperty("user.dir")+"/src/main/java/at/ac/fhcampuswien/textures/cards/Red_back.jpg";
            }else{
                path=System.getProperty("user.dir")+"/src/main/java/at/ac/fhcampuswien/textures/cards/"+card.getID()+".jpg";
            }
            Image image = new Image(new FileInputStream(path));
            imageView.setId(card.getID());
            imageView.setImage(image);
            imageView.setFitHeight(153);
            imageView.setFitWidth(100);
            imageView.setLayoutX(50+offset*cardnum);
            imageView.setLayoutY(50);
        }catch (FileNotFoundException e){
            e.printStackTrace();
            //use Logger for future builds
        }
        return imageView;
    }
}