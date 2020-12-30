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
    public Button doubleDownButton;
    @FXML
    public Button StandButton;
    @FXML
    public TextField showBalance;
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
    Player splitPlayer1;
    boolean showFullDealerValue;

    boolean isFirstStake;

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
        clearPlayingField();
        player1.clearHoldingCards();
        dealer.clearHoldingCards();

        showBalance.setText("Your Balance: "+player1.getBalance());

        numOfCardsDealer=0;
        oldStake=0;
        splitPlayer1=null;

        playing=true;
        isBlackJack=false;
        push=false;
        talonFound=false;
        win=false;
        lost=false;
        showFullDealerValue=false;

        isFirstStake=true;

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
            terminal.clear();
            terminal.setText("BlackJack!");
            newRound();
        }else if(player1.getCard(1).getValue()==11 && player1.getCard(0).getValue()==10) {
            GameMethods.BlackJack(player1, dealer);
            terminal.clear();
            terminal.setText("BlackJack!");
            newRound();
        }

        hitButton.setDisable(true);
        splitButton.setDisable(true);
        doubleDownButton.setDisable(true);
        StandButton.setDisable(true);


        //dealer pulls cards until 17 or more has been reached


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
        if(showFullDealerValue){
            showDealerValue.setText(showDealerValueDefault+ DcardValue);
        }else{
            showDealerValue.setText(showDealerValueDefault+ dealer.getHoldingCards().get(0).getValue());
        }

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

    public void DealerDraws() throws FileNotFoundException, InterruptedException {
        int dealerCardValue=0;
        showFullDealerValue =true;
        for(int i=0;i<dealer.getHoldingCards().size();i++){
            dealerCardValue+=dealer.getHoldingCards().get(i).getValue();
        }
        if(dealerCardValue<17){
            int i=dealer.getHoldingCards().size();
            do{
                GameMethods.giveCardToDealer(dealer,deck);
                generateCardDealer();
                dealerCardValue+=dealer.getHoldingCards().get(i).getValue();
                i++;
            }while(dealerCardValue<17);
        }
        win=GameMethods.win(player1,dealer);
        push=GameMethods.push(player1,dealer);
        lost=GameMethods.lost(player1,dealer);
        if(win){
            terminal.clear();
            terminal.setText("Won!");
            GameMethods.winPayout(player1,dealer);
            newRound();
        }else if(push){
            terminal.clear();
            terminal.setText("Push!");
            GameMethods.pushPayout(player1,dealer);
            newRound();
        }else if(lost){
            terminal.clear();
            terminal.setText("Lost!");
            GameMethods.lostPayout(player1,dealer);
            newRound();
        }
    }

    public void clearPlayingField() {
        playerPane.getChildren().clear();
        dealerPane.getChildren().clear();
    }

    public void hit() throws FileNotFoundException, InterruptedException {

        GameMethods.Hit(player1,deck);
        generateCardPlayer();
        lost =GameMethods.lost(player1, dealer);
        if(lost){
            terminal.clear();
            terminal.setText("You have Overdrawn!");
            GameMethods.lostPayout(player1, dealer);
            newRound();
        }
        doubleDownButton.setDisable(true);
        splitButton.setDisable(true);
    }

    public void stake(){
        String amtStr=stakesFiled.getText();
        oldStake+=player1.getStake();
        int amount = Integer.parseInt(amtStr);
        GameMethods.setStake(player1, amount);
        double printAmount=oldStake+amount;

        if(isFirstStake){
            terminal.clear();
            terminal.setText(terminal.getText()+"Your Stake: "+printAmount);
            isFirstStake=false;
        }else{
            terminal.setText(terminal.getText()+"\n"+"Your Stake: "+printAmount);
        }
        hitButton.setDisable(false);
        splitButton.setDisable(false);
        doubleDownButton.setDisable(false);
        StandButton.setDisable(false);

        showBalance.setText("Your Balance: "+player1.getBalance());
    }

    public void Split() throws FileNotFoundException {


    }

    public void DoubleDown() throws FileNotFoundException, InterruptedException {
        if(player1.getHoldingCards().size()==2){
            GameMethods.DoubleDown(player1,deck);
            generateCardPlayer();
            //rotates the card by 90°
            player1.getHoldingCards().get(player1.getHoldingCards().size()-1).getTexture().setRotate(90);
            Stand();

            //dealer draws Cards
        }
    }

    public void Stand() throws FileNotFoundException, InterruptedException {
        hitButton.setDisable(true);
        splitButton.setDisable(true);
        doubleDownButton.setDisable(true);
        StandButton.setDisable(true);
        //dealer draws Cards
        showFullDealerValue=true;
        DealerDraws();

        int DcardValue=0;
        for(int i=0; i<dealer.getHoldingCards().size();i++) {
            DcardValue += dealer.getHoldingCards().get(i).getValue();
        }
        showDealerValue.setText(showDealerValueDefault+ DcardValue);

        String path=System.getProperty("user.dir")+"/src/main/java/at/ac/fhcampuswien/textures/cards/"+dealer.getHoldingCards().get(1).getID()+".jpg";
        Image image = new Image(new FileInputStream(path));
        dealer.getHoldingCards().get(1).setImage(image);
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