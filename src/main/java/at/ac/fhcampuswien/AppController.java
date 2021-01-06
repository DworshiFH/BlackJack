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
    @FXML public Button setBetButton;
    @FXML public TextField stakesFiled;
    @FXML public Pane playerPane;
    @FXML public Pane dealerPane;
    @FXML public TextField playerValueWindow;
    @FXML public TextField dealerValueWindow;
    @FXML public Button hitButton;
    @FXML public Button splitButton;
    @FXML public Button standButton;
    @FXML public TextField showBalance;
    @FXML public Button newRoundButton;
    @FXML public TextField splitValueWindow;
    @FXML public Button handSelectButton;
    @FXML public TextField showSelectedHand;
    @FXML public Button surrenderButton;
    @FXML public Button doubleDownButton;
    @FXML public Button hitButtonSplit;
    @FXML public Button doubleDownButtonSplit;
    @FXML public Button surrenderButtonSplit;
    @FXML public Pane mainPane;
    @FXML public TextArea terminal;

    Card talon=new Card("Talon",0);

    Player player;
    Dealer dealer;
    Player splitPlayer;
    List<Card> deck;

    private boolean lost = false;
    private boolean isBlackJack;
    private boolean push;
    private boolean talonFound;
    private boolean win;
    private boolean leftHandSelected;
    private boolean talonShow;
    private boolean splitActive;
    private boolean leftHandHasHit;
    private boolean rightHandHasHit;
    private boolean rightHandSelected;
    private boolean rightHandAlreadyLost;
    private boolean leftHandAlreadyDone;
    private boolean showFullDealerValue;

    public void initialize() { //is like Main
        player =new Player("Kurti",1000);
        dealer=new Dealer();
        deck=Deck.makeDeck();
        newRoundButton.setVisible(false);
        talonFound=false;

        newRound();
    }

    public void newRound() {

        if(talonFound){
            deck=Deck.makeDeck();
            talonFound=false;
            GameMethods.ResetTalon();
        }

        //clears PlayingField
        playerPane.getChildren().clear();
        dealerPane.getChildren().clear();

        //Deck.resetAces();

        newRoundButton.setVisible(false);
        setBetButton.setDisable(false);
        splitValueWindow.setVisible(false);

        player.clearHoldingCards();
        dealer.clearHoldingCards();

        terminal.clear();

        showBalance.setText("Your Balance: "+ player.getBalance());

        numOfCardsDealer=0;

        splitPlayer=null;

        isBlackJack=false;
        push=false;
        talonFound=false;
        win=false;
        lost=false;
        showFullDealerValue=false;
        leftHandSelected = true;
        rightHandSelected = false;
        splitActive=false;
        leftHandHasHit=false;
        rightHandHasHit=false;
        isFistSplitCard=true;
        leftHandAlreadyDone =false;
        rightHandAlreadyLost =false;

        hitButton.setVisible(true);
        surrenderButton.setVisible(true);
        doubleDownButton.setVisible(true);

        handSelectButton.setVisible(false);
        showSelectedHand.setVisible(false);

        hitButtonSplit.setVisible(false);
        surrenderButtonSplit.setVisible(false);
        doubleDownButtonSplit.setVisible(false);

        playerValueWindow.setText("Card Value: ");
        dealerValueWindow.setText("Card Value: ");

        DisableButtons();

        //Does Player have enough money?
        if(player.getBalance()<=minBet){
            terminal.setText("Insufficient funds.\nThank you for playing!");
            setBetButton.setDisable(true);
        }
    }

    public static final int minBet =10;
    public void placeBet() throws FileNotFoundException {

        int amount;
        do{
            amount = Integer.parseInt(stakesFiled.getText());
            if(amount<minBet){
                terminal.setText("Minimal bet is: "+ minBet);
                placeBet();
            }
        }while(amount<minBet);

        GameMethods.SetBet(player, amount);

        terminal.clear();
        terminal.setText("Your Bet: "+ player.getStake());

        GameMethods.GiveCardToPlayer(player,deck);
        GenerateCardPlayer();
        GameMethods.GiveCardToPlayer(player,deck);
        GenerateCardPlayer();

        GameMethods.GiveCardToDealer(dealer,deck);
        GenerateCardDealer();
        GameMethods.GiveCardToDealer(dealer,deck);
        GenerateCardDealer();

        //checks for Blackjack
        if(player.getCard(0).getValue()==11 && player.getCard(1).getValue()==10){
            isBlackJack=true;
        }else if(player.getCard(1).getValue()==11 && player.getCard(0).getValue()==10) {
            isBlackJack=true;
        }
        if(isBlackJack){
            DisableButtons();
            ShowDealerValue();
            GameMethods.BlackJackPayout(player);
            terminal.clear();
            terminal.setText("BlackJack!");
            setBetButton.setDisable(true);
            MakeDealerCardsVisible();
            newRoundButton.setVisible(true);
        }else{
            //enables all Buttons, except Split Button
            doubleDownButton.setDisable(false);
            hitButton.setDisable(false);
            splitButton.setDisable(true);
            standButton.setDisable(false);
            surrenderButton.setDisable(false);
        }

        //Checks for possibility Split (Split is only possible if both cards are of same value)
        if(player.getCard(0).getValue()==player.getCard(1).getValue()){
            splitButton.setDisable(false);
        }
        if(player.getCard(0).getIsAce()){
            if(player.getCard(1).getIsAce()){
                splitButton.setDisable(false);
            }
        }

        setBetButton.setDisable(true);
        showBalance.setText("Your Balance: "+ player.getBalance());
    }

    private static int numOfCardsDealer=0;
    public void GenerateCardDealer() throws FileNotFoundException {
        talonFound=GameMethods.TalonFound();
        if(talonFound){
            if(talonShow)
            ShowTalon();
            talonShow=false;
        }
        int DcardValue=GameMethods.DealerValueCalculator(dealer);

        dealerPane.getChildren().add(dealer.getHoldingCards().get(dealer.getHoldingCards().size()-1).getImageView());

        if(showFullDealerValue){
            dealerValueWindow.setText("Card Value: " + DcardValue);
        }else{
            dealerValueWindow.setText("Card Value: " + dealer.getHoldingCards().get(0).getValue());
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

    public void GenerateCardPlayer() throws FileNotFoundException {
        talonFound=GameMethods.TalonFound();
        if(talonFound){
            if(talonShow)
                ShowTalon();
            talonShow=false;
        }

        int PlayerCardValue=GameMethods.PlayerValueCalculator(player);

        ImageView show=player.getHoldingCards().get(player.getHoldingCards().size()-1).getImageView();

        if(splitActive){
            show.setLayoutX(show.getLayoutX()-(player.getHoldingCards().size()-1)*60);
        }

        playerPane.getChildren().add(show);

        playerValueWindow.setText("Card Value: " + PlayerCardValue);
    }

    boolean isFistSplitCard=true;
    public void GenerateCardSplit() throws FileNotFoundException {
        talonFound=GameMethods.TalonFound();
        if(talonFound){
            if(talonShow)
                ShowTalon();
            talonShow=false;
        }
        int SplitCardValue=GameMethods.PlayerValueCalculator(splitPlayer);

        ImageView show = splitPlayer.getHoldingCards().get(splitPlayer.getHoldingCards().size()-1).getImageView();

        show.setLayoutX(500+55*(splitPlayer.getHoldingCards().size()-2)); //560

        playerPane.getChildren().add(show);

        splitValueWindow.setText("Card Value Split: " + SplitCardValue);
    }

    public void ShowTalon() throws FileNotFoundException {
        terminal.appendText("\nTalon drawn!\nDeck will be reshuffled.");

        Image talonImage=new Image(new FileInputStream(System.getProperty("user.dir")+"/src/main/java/at/ac/fhcampuswien/textures/cards/Talon.jpg"));
        ImageView talonImageView=new ImageView();
        talonImageView.setId(talon.getID());
        talonImageView.setImage(talonImage);
        talonImageView.setLayoutX(797);
        talonImageView.setLayoutY(164);
        talonImageView.setFitHeight(85);
        talonImageView.setFitWidth(55);
        dealerPane.getChildren().add(talonImageView);
    }

    public void DealerDraws() throws FileNotFoundException {
        showFullDealerValue =true;
        terminal.clear();
        ShowDealerValue();
        MakeDealerCardsVisible();

        int dealerCardValue=GameMethods.DealerValueCalculator(dealer);
        if(dealerCardValue<17){
            while(dealerCardValue<17){
                GameMethods.GiveCardToDealer(dealer,deck);
                GenerateCardDealer();
                dealerCardValue=GameMethods.DealerValueCalculator(dealer);
                ShowDealerValue();
            }
        }
        win=GameMethods.Win(player,dealer);
        push=GameMethods.Push(player,dealer);
        lost=GameMethods.Lost(player,dealer);

        if(win){
            if(GameMethods.DealerBust(dealer)){
                terminal.setText("Dealer Bust.");
            }
            if(splitActive && !rightHandAlreadyLost){
                terminal.appendText("\nLeft Hand Won!");
            }else{
                terminal.appendText("\nWon!");
            }

            GameMethods.WinPayout(player);
            setBetButton.setDisable(true);
            newRoundButton.setVisible(true);
        }else if(push){
            if(splitActive){
                terminal.setText("Left Hand Push!");
            }else{
                terminal.setText("Push!");
            }
            GameMethods.PushPayout(player);
            setBetButton.setDisable(true);
            newRoundButton.setVisible(true);
        }else if(lost){
            if(splitActive){
                terminal.setText("Left Hand Lost!");
            }else{
                terminal.setText("Lost!");
            }
            GameMethods.LostPayout(player);
            setBetButton.setDisable(true);
            newRoundButton.setVisible(true);
        }

        //for Split
        if(!rightHandAlreadyLost && !leftHandAlreadyDone){
            win=GameMethods.Win(splitPlayer,dealer);
            push=GameMethods.Push(splitPlayer,dealer);
            lost=GameMethods.Lost(splitPlayer,dealer);
            if(win){
                terminal.appendText("\nRight Hand Won!");
                player.addBalance(splitPlayer.getStake()*2);
                //setBetButton.setDisable(true);
                newRoundButton.setVisible(true);
            }else if(push){
                terminal.appendText("\nRight Hand Push!");
                player.addBalance(splitPlayer.getBalance());
                //setBetButton.setDisable(true);
                newRoundButton.setVisible(true);
            }else if(lost){
                terminal.appendText("\nRight Hand Lost!");
                //setBetButton.setDisable(true);

                newRoundButton.setVisible(true);
            }
        }else if(rightHandAlreadyLost && leftHandAlreadyDone) {
            terminal.setText("Lost!");
            setBetButton.setDisable(true);
            newRoundButton.setVisible(true);
        }
    }

    public void Hit() throws FileNotFoundException {
        splitButton.setDisable(true);
        surrenderButton.setDisable(true);
        doubleDownButton.setDisable(true);

        leftHandHasHit=true;

        GameMethods.GiveCardToPlayer(player, deck);
        GenerateCardPlayer();

        if (GameMethods.PlayerBust(player) && !splitActive) {
            terminal.clear();
            terminal.setText("Bust!");

            GameMethods.LostPayout(player);
            setBetButton.setDisable(true);
            newRoundButton.setVisible(true);
            if(splitActive){
                HandSelector();
                handSelectButton.setDisable(true);
            }else{
                DisableButtons();
            }
        }else if(GameMethods.PlayerBust(player) && splitActive){
            terminal.clear();
            terminal.setText("Left Hand Bust!");
            GameMethods.LostPayout(player);
            HandSelector();
            handSelectButton.setDisable(true);
            player.clearHoldingCards();
            leftHandAlreadyDone =true;
        }
        if(leftHandAlreadyDone && rightHandAlreadyLost){
            standButton.setDisable(true);
            DisableButtons();
            hitButtonSplit.setVisible(false);
            doubleDownButtonSplit.setVisible(false);
            surrenderButtonSplit.setVisible(false);
            newRoundButton.setVisible(true);
            terminal.appendText("\nLost!");
        }
    }

    public void SplitHit() throws FileNotFoundException {
        surrenderButtonSplit.setDisable(true);
        doubleDownButtonSplit.setDisable(true);

        GameMethods.GiveCardToPlayer(splitPlayer, deck);
        GenerateCardSplit();

        rightHandHasHit=true;

        if (GameMethods.PlayerBust(splitPlayer)) {
            terminal.clear();
            terminal.setText("Right Hand Bust!");
            GameMethods.LostPayout(splitPlayer);
            setBetButton.setDisable(true);
            newRoundButton.setVisible(false);
            HandSelector();
            handSelectButton.setDisable(true);
            splitPlayer.clearHoldingCards();
            splitValueWindow.setText("Card Value: ");
            rightHandAlreadyLost =true;
        }
        if(leftHandAlreadyDone && rightHandAlreadyLost){
            standButton.setDisable(true);
            DisableButtons();
            hitButtonSplit.setVisible(false);
            doubleDownButtonSplit.setVisible(false);
            surrenderButtonSplit.setVisible(false);
            newRoundButton.setVisible(true);
            hitButton.setVisible(true);
            doubleDownButton.setVisible(true);
            surrenderButton.setVisible(true);
            terminal.appendText("\nLost!");
        }
    }

    public void DisableButtons(){
        hitButton.setDisable(true);
        splitButton.setDisable(true);
        standButton.setDisable(true);
        doubleDownButton.setDisable(true);
        surrenderButton.setDisable(true);
    }


    public void HandSelector(){
        leftHandSelected = !leftHandSelected;
        rightHandSelected = !rightHandSelected;

        if(leftHandSelected){
            showSelectedHand.setText("<-     Selected Hand");
        }
        if(rightHandSelected){
            showSelectedHand.setText("       Selected Hand     ->");
        }

        if(leftHandSelected){
            doubleDownButtonSplit.setVisible(false);
            hitButtonSplit.setVisible(false);
            surrenderButtonSplit.setVisible(false);

            doubleDownButton.setVisible(true);
            hitButton.setVisible(true);
            surrenderButton.setVisible(true);

            if(leftHandHasHit){
                doubleDownButton.setDisable(true);
                surrenderButton.setDisable(true);
            }
        }

        if(rightHandSelected){
            doubleDownButton.setVisible(false);
            hitButton.setVisible(false);
            surrenderButton.setVisible(false);

            doubleDownButtonSplit.setVisible(true);
            hitButtonSplit.setVisible(true);
            surrenderButtonSplit.setVisible(true);

            if(rightHandHasHit){
                doubleDownButtonSplit.setDisable(true);
                surrenderButtonSplit.setDisable(true);
            }
        }
    }

    public void Split() throws FileNotFoundException {
        boolean isBlackjackSplit=false;
        splitActive=true;

        splitButton.setDisable(true);

        talonFound=GameMethods.TalonFound();
        if(talonFound){
            if(talonShow)
                ShowTalon();
            talonShow=false;
        }

        splitValueWindow.setVisible(true);
        handSelectButton.setVisible(true);
        showSelectedHand.setVisible(true);

        hitButtonSplit.setDisable(false);
        doubleDownButtonSplit.setDisable(false);
        surrenderButtonSplit.setDisable(false);
        handSelectButton.setDisable(false);

        player.getCard(1).setImageViewX(50+offset+290);

        splitPlayer=GameMethods.Split(player, deck);
        showBalance.setText("Your Balance: "+player.getBalance());
        terminal.appendText("\nSplit Bet: "+splitPlayer.getStake());

        //sets Value of Aces back to original
        if(player.getCard(0).getIsAce()) player.getCard(0).setValue(11);
        if(splitPlayer.getCard(0).getIsAce()) splitPlayer.getCard(0).setValue(11);

        GameMethods.GiveCardToPlayer(player,deck);
        GenerateCardPlayer();

        GameMethods.GiveCardToPlayer(splitPlayer,deck);
        GenerateCardSplit();


        //generate Card for Split
        int splitCardValue=GameMethods.PlayerValueCalculator(splitPlayer);

        /*playerPane.getChildren().add(splitPlayer.getHoldingCards().get(splitPlayer.getHoldingCards().size()-1).getImageView());
        splitPlayer.getCard(
                splitPlayer.getHoldingCards().size()-1).getImageView().setLayoutX(
                splitPlayer.getCard(splitPlayer.getHoldingCards().size()-1).getImageView().getLayoutX()+  splitOffset);*/

        splitValueWindow.setText("Card Value: " + splitCardValue);

        //checks for Blackjack for Split
        if(splitPlayer.getCard(0).getValue()==11 && splitPlayer.getCard(1).getValue()==10){
            isBlackjackSplit=true;
        }else if(splitPlayer.getCard(1).getValue()==11 && splitPlayer.getCard(0).getValue()==10) {
            isBlackjackSplit=true;
        }
        if(isBlackjackSplit){
            terminal.clear();
            terminal.setText("Right Hand BlackJack!");
            hitButtonSplit.setDisable(true);
            doubleDownButtonSplit.setDisable(true);
            surrenderButtonSplit.setDisable(true);
            player.addBalance(splitPlayer.getStake()*2.5);
            splitPlayer.clearHoldingCards();
            showBalance.setText("Your Blance: "+player.getBalance());
            rightHandAlreadyLost =true;
        }

        //checks for Blackjack
        if(player.getCard(0).getValue()==11 && player.getCard(1).getValue()==10){
            isBlackJack=true;
        }else if(player.getCard(1).getValue()==11 && player.getCard(0).getValue()==10) {
            isBlackJack=true;
        }
        if(isBlackJack){
            DisableButtons();
            standButton.setDisable(false);
            terminal.clear();
            terminal.setText("Left Hand BlackJack!");
            setBetButton.setDisable(true);
            GameMethods.BlackJackPayout(player);
            showBalance.setText("Your Blance: "+player.getBalance());
            leftHandAlreadyDone =true;
            player.clearHoldingCards();
        }

        if(isBlackjackSplit&&isBlackJack){
            terminal.setText("Both Hands BlackJack!!");
            standButton.setDisable(true);
            GameMethods.BlackJackPayout(player);
            newRoundButton.setVisible(true);
            MakeDealerCardsVisible();
            ShowDealerValue();
        }
        showSelectedHand.setText("<-     Selected Hand");

        GenerateCardSplit();

        GenerateCardPlayer();
    }

    public void DoubleDown() throws FileNotFoundException {

        DisableButtons();
        if(splitActive) standButton.setDisable(false);
        GameMethods.DoubleDown(player, deck);
        GenerateCardPlayer();
        //rotates the card by 90°
        player.getHoldingCards().get(player.getHoldingCards().size() - 1).getImageView().setRotate(90);
        lost = GameMethods.Lost(player, dealer);
        showBalance.setText("Your Balance: " + player.getBalance());

        if (GameMethods.PlayerBust(player)) {
            terminal.clear();
            terminal.setText("Bust!");
            ShowDealerValue();
            GameMethods.LostPayout(player);
            setBetButton.setDisable(true);
            if(!splitActive) newRoundButton.setVisible(true);
            if(splitActive) leftHandAlreadyDone =true;
        } else {
            if(!splitActive) Stand();
        }

    }

    public void SplitDoubleDown() throws FileNotFoundException {

        doubleDownButtonSplit.setDisable(true);
        hitButtonSplit.setDisable(true);
        surrenderButton.setDisable(true);

        GameMethods.DoubleDown(splitPlayer, deck);
        GenerateCardSplit();
        //rotates the card by 90°
        splitPlayer.getHoldingCards().get(splitPlayer.getHoldingCards().size() - 1).getImageView().setRotate(90);
        lost = GameMethods.Lost(splitPlayer, dealer);
        showBalance.setText("Your Balance: " + player.getBalance());

        if (GameMethods.PlayerBust(splitPlayer)) {
            terminal.clear();
            terminal.setText("Right Hand Bust!");
            GameMethods.LostPayout(player);
            setBetButton.setDisable(true);
            newRoundButton.setVisible(false);
            rightHandAlreadyLost=true;
        }
    }

    public void Stand() throws FileNotFoundException {
            MakeDealerCardsVisible();
            DisableButtons();
            //dealer draws Cards
            showFullDealerValue = true;
            DealerDraws();
            ShowDealerValue();
            handSelectButton.setVisible(false);
            hitButtonSplit.setDisable(true);
            surrenderButtonSplit.setDisable(true);
            doubleDownButtonSplit.setDisable(true);
    }

    public void Surrender(){
        DisableButtons();
        GameMethods.SurrenderPayout(player);
        if(!splitActive){
            terminal.clear();
            newRoundButton.setVisible(true);
            terminal.setText("Surrendered!");
        }else{
            terminal.appendText("\nSurrendered!");
            standButton.setDisable(false);
            player.clearHoldingCards();
        }
        leftHandAlreadyDone =true;
        if(rightHandAlreadyLost) newRoundButton.setVisible(true);
    }

    public void SplitSurrender() throws FileNotFoundException {
        terminal.appendText("\nSurrendered!");
        player.addBalance(splitPlayer.getStake()*0.5);
        splitPlayer.setStake(0);
        splitPlayer.clearHoldingCards();
        HandSelector();
        handSelectButton.setDisable(true);
        rightHandAlreadyLost=true;
        if(leftHandAlreadyDone) newRoundButton.setVisible(true);
    }

    public void ShowDealerValue(){
        int DcardValue=GameMethods.DealerValueCalculator(dealer);
        dealerValueWindow.setText("Card Value: "+ DcardValue);
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

    public void exit(){
        Platform.exit();
    }
}