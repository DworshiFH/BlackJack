package at.ac.fhcampuswien;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

public class AppController {

    @FXML
    public Button setStakeButton;
    @FXML
    public TextField stakesFiled;
    @FXML
    public Pane playerPane;
    @FXML
    public Pane dealerPane;
    @FXML
    public TextArea showPlayerValue;
    @FXML
    public TextArea showDealerValue;
    @FXML
    public Button hitButton;
    @FXML
    public Button splitButton;
    @FXML
    private Pane mainPane;
    @FXML
    private TextArea terminal;

    Player player1;
    Dealer dealer;
    List<Card> deck;

    boolean lost =false;
    boolean playing;
    boolean isBlackJack;
    boolean push;
    boolean talonFound;
    boolean win;

    private static int numOfCardsDealer=0;
    double oldStake = 0;

    public void initialize() throws FileNotFoundException, InterruptedException { //is like Main

        player1=new Player("Kurti",1000);
        dealer=new Dealer();
        deck=Deck.makeDeck();

        newRound();

        /*
        //check for possibility of Hit Split Double Down
        int dealerCardValue=0;
        for(int i=0; i<dealer.getHoldingCards().size();i++){
            dealerCardValue+=dealer.getHoldingCards().get(i).getValue();
        }

        do{
            GameMethods.giveCardToDealer(dealer, deck);
            dealerCardValue+=dealer.getHoldingCards().get(dealer.getHoldingCards().size()-1).getValue();

        }while(dealerCardValue<17);*/



        //terminal.setText("Test");
    }
    public void newRound() throws FileNotFoundException, InterruptedException {

        numOfCardsDealer=0;
        oldStake=0;

        playing=true;
        isBlackJack=false;
        push=false;
        talonFound=false;
        win=false;
        lost=false;

        GameMethods.giveCardToDealer(dealer,deck);
        generateCardDealer();
        GameMethods.giveCardToDealer(dealer,deck);
        generateCardDealer();

        GameMethods.giveCardToPlayer(player1,deck);
        generateCardPlayer();
        GameMethods.giveCardToPlayer(player1,deck);
        generateCardPlayer();

        //checks for Blackjack
        if(player1.getCard(0).getValue()==11 && player1.getCard(1).getValue()==10){
            GameMethods.BlackJack(player1, dealer);
            clearPlayingField();
            newRound();
        }else if(player1.getCard(1).getValue()==11 && player1.getCard(0).getValue()==10) {
            GameMethods.BlackJack(player1, dealer);
            clearPlayingField();
            newRound();
        }
    }

    private final String showDealerValueDefault= "Card Value: ";
    public void generateCardDealer() throws FileNotFoundException {
        int DcardValue=0;
        dealerPane.getChildren().add(dealer.getHoldingCards().get(dealer.getHoldingCards().size()-1).getTexture());
        //calculates PlayerCardValues
        for(int i=0; i<dealer.getHoldingCards().size();i++){
            DcardValue+=dealer.getHoldingCards().get(i).getValue();
        }
        if(DcardValue>21){
            for(int i=0; i<dealer.getHoldingCards().size();i++){
                if(dealer.getHoldingCards().get(i).getValue()==11){
                    DcardValue-=10;
                    break;
                }
            }
        }
        showDealerValue.setText(showDealerValueDefault+ DcardValue);

        if(dealer.getHoldingCards().size()==3){
            String path=System.getProperty("user.dir")+"/src/main/java/at/ac/fhcampuswien/textures/cards/"+dealer.getHoldingCards().get(1).getID()+".jpg";
            Image image = new Image(new FileInputStream(path));
            dealer.getHoldingCards().get(1).setImage(image);
        }
    }
    private final String showPlayerValueDefault= "Card Value: ";
    public void generateCardPlayer(){
        int PcardValue=0;
        playerPane.getChildren().add(player1.getHoldingCards().get(player1.getHoldingCards().size()-1).getTexture());
        //calculates PlayerCardValues
        for(int i=0; i<player1.getHoldingCards().size();i++){
            PcardValue+=player1.getHoldingCards().get(i).getValue();
        }
        if(PcardValue>21){
            for(int i=0; i<player1.getHoldingCards().size();i++){
                if(player1.getHoldingCards().get(i).getValue()==11){
                    PcardValue-=10;
                    break;
                }
            }
        }
        showPlayerValue.setText(showPlayerValueDefault+ PcardValue);
    }

    public void clearPlayingField() throws InterruptedException {
        playerPane.getChildren().clear();
        dealerPane.getChildren().clear();
    }

    public void hit() throws FileNotFoundException, InterruptedException {
        GameMethods.Hit(player1,deck);
        generateCardPlayer();
        lost =GameMethods.lost(player1, dealer);
        if(lost){
            terminal.setText("You have Overdrawn!");
            GameMethods.lostPayout(player1, dealer);
            clearPlayingField();
            newRound();
        }
    }

    public void stake(){
        String amtStr=stakesFiled.getText();
        oldStake+=player1.getStake();
        int amount = Integer.parseInt(amtStr);
        GameMethods.setStake(player1, amount);
        double printAmount=oldStake+amount;


        //make so that first systemlineseperator isnt shown
        terminal.setText(terminal.getText()+"\n"+"Your Stake: "+printAmount);
    }

    public void exit(){
        Platform.exit();
    }

    private static final int offset = 50;
    public static ImageView CardTextureAssigner(Card card, int cardnum, boolean isDealer) throws FileNotFoundException { //false = Dealer
        int yOffset;
        if(isDealer){
            yOffset=80;
            numOfCardsDealer++;
        }else{
            yOffset=50;
        }
        ImageView imageView = new ImageView();
        try{
            String path;

            if(numOfCardsDealer==2 && isDealer){
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
            imageView.setLayoutY(yOffset);
        }catch (FileNotFoundException e){
            e.printStackTrace();
            //use Logger for future builds
        }
        return imageView;
    }


}
