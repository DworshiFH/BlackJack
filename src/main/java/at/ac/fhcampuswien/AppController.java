package at.ac.fhcampuswien;

import at.ac.fhcampuswien.Objects.*;
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
    public TextArea PlayerValueWindow;
    @FXML
    public TextArea DealerValueWindow;
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
    public Button newRoundButton;
    @FXML
    private Pane mainPane;
    @FXML
    private TextArea terminal;

    Card talon=new Card("Talon",0);

    Player player1;
    Dealer dealer;
    List<Card> deck;

    private boolean lost =false;
    private boolean playing;
    private boolean isBlackJack;
    private boolean push;
    private boolean talonFound;
    private boolean win;
    private Player splitPlayer1;
    private boolean showFullDealerValue;

    private static int numOfCardsDealer=0;
    private double oldStake = 0;

    public void newRound() throws FileNotFoundException {
        if(talonFound){
            deck=Deck.makeDeck();
            talonFound=false;
            GameMethods.resetTalon();
        }
        showTalon();

        //clears PlayingField
        playerPane.getChildren().clear();
        dealerPane.getChildren().clear();

        newRoundButton.setVisible(false);
        setStakeButton.setDisable(false);

        player1.clearHoldingCards();
        dealer.clearHoldingCards();

        terminal.clear();

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

       //isFirstStake=true;

        PlayerValueWindow.setText("Card Value: ");
        DealerValueWindow.setText("Card Value: ");

        DisableButtons();
    }
    public void RoundStart() throws FileNotFoundException {

        String amtStr=stakesFiled.getText();
        oldStake+=player1.getStake();
        int amount = Integer.parseInt(amtStr);
        GameMethods.setStake(player1, amount);
        double printAmount=oldStake+amount;

        terminal.clear();
        terminal.setText(terminal.getText()+"Your Stake: "+printAmount);

        GameMethods.giveCardToPlayer(player1,deck);
        generateCardPlayer();
        GameMethods.giveCardToPlayer(player1,deck);
        generateCardPlayer();

        GameMethods.giveCardToDealer(dealer,deck);
        generateCardDealer();
        GameMethods.giveCardToDealer(dealer,deck);
        generateCardDealer();

        //checks for Blackjack
        if(player1.getCard(0).getValue()==11 && player1.getCard(1).getValue()==10){
            isBlackJack=true;
        }else if(player1.getCard(1).getValue()==11 && player1.getCard(0).getValue()==10) {
            isBlackJack=true;
        }
        if(isBlackJack){
            DisableButtons();
            ShowDealerValue();
            GameMethods.BlackJackPayout(player1);
            terminal.clear();
            terminal.setText("BlackJack!");
            setStakeButton.setDisable(true);
            MakeDealerCardsVisible();
            newRoundButton.setVisible(true);
        }else{
            //enables all Buttons
            hitButton.setDisable(false);
            splitButton.setDisable(false);
            doubleDownButton.setDisable(false);
            StandButton.setDisable(false);
        }

        setStakeButton.setDisable(true);
        showBalance.setText("Your Balance: "+player1.getBalance());
    }
    private final String DealerValueWindowDefault = "Card Value: ";

    public void generateCardDealer() throws FileNotFoundException {
        talonFound=GameMethods.talonFound();
        if(talonFound){
            showTalon();
        }
        int DcardValue=GameMethods.DealerValueCalculator(dealer);

        dealerPane.getChildren().add(dealer.getHoldingCards().get(dealer.getHoldingCards().size()-1).getImageView());

        if(showFullDealerValue){
            DealerValueWindow.setText(DealerValueWindowDefault + DcardValue);
        }else{
            DealerValueWindow.setText(DealerValueWindowDefault + dealer.getHoldingCards().get(0).getValue());
        }

        if(dealer.getHoldingCards().size()==3){
            String path=System.getProperty("user.dir")+"/src/main/java/at/ac/fhcampuswien/textures/cards/"+dealer.getHoldingCards().get(1).getID()+".jpg";
            Image image = new Image(new FileInputStream(path));
            dealer.getHoldingCards().get(1).setImage(image);
        }
    }
    public void MakeDealerCardsVisible() throws FileNotFoundException {
        String path=System.getProperty("user.dir")+"/src/main/java/at/ac/fhcampuswien/textures/cards/"+dealer.getHoldingCards().get(1).getID()+".jpg";
        Image image = new Image(new FileInputStream(path));
        dealer.getHoldingCards().get(1).setImage(image);
    }
    public void generateCardPlayer() throws FileNotFoundException {
        talonFound=GameMethods.talonFound();
        if(talonFound){
            showTalon();
        }
        int PcardValue=GameMethods.PlayerValueCalculator(player1);

        playerPane.getChildren().add(player1.getHoldingCards().get(player1.getHoldingCards().size()-1).getImageView());

        PlayerValueWindow.setText("Card Value: " + PcardValue);
    }
    public void showTalon() throws FileNotFoundException {
        Image talonImage=new Image(new FileInputStream(System.getProperty("user.dir")+"/src/main/java/at/ac/fhcampuswien/textures/cards/Talon.jpg"));
        ImageView talonImageView=new ImageView();
        talonImageView.setId(talon.getID());
        talonImageView.setImage(talonImage);
        talonImageView.setLayoutX(50);
        talonImageView.setLayoutY(50);
        talonImageView.setFitHeight(85);
        talonImageView.setFitWidth(55);
        dealerPane.getChildren().add(talonImageView);
    }


    public void DealerDraws() throws FileNotFoundException {
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
            if(GameMethods.DealerHasOverdrawn(dealer)){
                terminal.appendText("\nDealer has overdrawn.");
            }
            GameMethods.winPayout(player1);
            setStakeButton.setDisable(true);
            ShowDealerValue();
            MakeDealerCardsVisible();
            newRoundButton.setVisible(true);
        }else if(push){
            terminal.clear();
            terminal.setText("Push!");
            GameMethods.pushPayout(player1);
            setStakeButton.setDisable(true);
            ShowDealerValue();
            MakeDealerCardsVisible();
            newRoundButton.setVisible(true);
        }else if(lost){
            terminal.clear();
            terminal.setText("Lost!");
            GameMethods.lostPayout(player1);
            setStakeButton.setDisable(true);
            ShowDealerValue();
            MakeDealerCardsVisible();
            newRoundButton.setVisible(true);
        }
    }

    public void hit() throws FileNotFoundException {
        doubleDownButton.setDisable(true);
        splitButton.setDisable(true);

        GameMethods.Hit(player1,deck);

        generateCardPlayer();

        if(GameMethods.PlayerHasOverdrawn(player1)){
            terminal.clear();
            terminal.setText("Bust!");
            GameMethods.lostPayout(player1);
            setStakeButton.setDisable(true);
            newRoundButton.setVisible(true);
        }
    }

    public void DisableButtons(){
        hitButton.setDisable(true);
        splitButton.setDisable(true);
        doubleDownButton.setDisable(true);
        StandButton.setDisable(true);
    }

    public void Split() {


    }

    public void DoubleDown() throws FileNotFoundException {
        if(player1.getHoldingCards().size()==2){
            GameMethods.DoubleDown(player1,deck);
            generateCardPlayer();
            //rotates the card by 90°
            player1.getHoldingCards().get(player1.getHoldingCards().size()-1).getImageView().setRotate(90);
            lost =GameMethods.lost(player1, dealer);

            if(GameMethods.PlayerHasOverdrawn(player1)){
                terminal.clear();
                terminal.setText("You have Overdrawn!");
                ShowDealerValue();
                GameMethods.lostPayout(player1);
                setStakeButton.setDisable(true);
                newRoundButton.setVisible(true);
            }else{
                doubleDownButton.setDisable(true);
                splitButton.setDisable(true);
                Stand();
            }

            //dealer draws Cards
        }
    }

    public void Stand() throws FileNotFoundException {
        MakeDealerCardsVisible();
        DisableButtons();
        //dealer draws Cards
        showFullDealerValue=true;
        DealerDraws();
        ShowDealerValue();
    }

    public void ShowDealerValue(){
        int DcardValue=0;
        for(int i=0; i<dealer.getHoldingCards().size();i++) {
            DcardValue += dealer.getHoldingCards().get(i).getValue();
        }
        DealerValueWindow.setText(DealerValueWindowDefault+ DcardValue);
    }

    private static final int offset = 110;

    public static ImageView CardTextureAssigner(Card card, int cardnum, boolean isDealer) { //true = Dealer
        int yOffset;
        if(isDealer){
            yOffset=50;
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

    public void exit(){
        Platform.exit();
    }

    public void initialize() throws FileNotFoundException { //is like Main
        player1=new Player("Kurti",1000);
        dealer=new Dealer();
        deck= Deck.makeDeck();
        newRoundButton.setVisible(false);
        talonFound=false;

        newRound();
    }

}