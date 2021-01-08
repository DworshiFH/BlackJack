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

    private boolean lost;
    private boolean isBlackJackPlayer;
    private boolean push;
    private boolean talonFound;
    private boolean win;
    private boolean leftHandSelected;
    private boolean talonShow;
    private boolean splitActive;
    private boolean leftHandHasHit;
    private boolean rightHandHasHit;
    private boolean rightHandSelected;
    private boolean rightHandAlreadyDone;
    private boolean leftHandAlreadyDone;
    private boolean showFullDealerValue;
    private boolean isBlackJackDealer;

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

        player.clearHoldingCards();
        dealer.clearHoldingCards();

        terminal.clear();

        showBalance.setText("Your Balance: "+ player.getBalance());

        splitPlayer=null;

        isBlackJackPlayer =false;
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
        leftHandAlreadyDone =false;
        rightHandAlreadyDone =false;
        isBlackJackDealer=false;

        hitButton.setVisible(true);
        surrenderButton.setVisible(true);
        doubleDownButton.setVisible(true);

        handSelectButton.setVisible(false);
        showSelectedHand.setVisible(false);

        MakeSplitButtonsInvisible();
        DisableButtons();
        setBetButton.setDisable(false);

        playerValueWindow.setText("Card Value: ");
        dealerValueWindow.setText("Card Value: ");

        //Does Player have enough money?
        if(player.getBalance()<=minBet){
            terminal.setText("Insufficient funds.\nThank you for playing!");
            setBetButton.setDisable(true);
        }
    }

    private static final int minBet =10;
    public void placeBet() throws FileNotFoundException {
        newRoundButton.setDisable(true);

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
            isBlackJackPlayer =true;
        }else if(player.getCard(1).getValue()==11 && player.getCard(0).getValue()==10) {
            isBlackJackPlayer =true;
        }
        //checks for dealer Blackjack
        if(dealer.getCard(0).getValue()==10 && dealer.getCard(1).getValue()==11){
            isBlackJackDealer=true;
        }else if(dealer.getCard(1).getValue()==10 && dealer.getCard(0).getValue()==11) {
            isBlackJackDealer=true;
        }

        if(isBlackJackDealer && !isBlackJackPlayer){
            ShowDealerValue();
            MakeDealerCardsVisible();
            DisableButtons();
            newRoundButton.setVisible(true);
            terminal.setText("Dealer has Blackjack!\nLost!");
            splitButton.setDisable(true);
            GameMethods.LostPayout(player);
        }

        if(isBlackJackPlayer && isBlackJackDealer){
            ShowDealerValue();
            MakeDealerCardsVisible();
            DisableButtons();
            newRoundButton.setVisible(true);
            terminal.setText("Player and dealer have Blackjack!\nPush!");
            GameMethods.PushPayout(player);
        }

        if(isBlackJackPlayer && !isBlackJackDealer){
            DisableButtons();
            ShowDealerValue();
            GameMethods.BlackJackPayout(player);
            terminal.clear();
            terminal.setText("Blackjack!");
            setBetButton.setDisable(true);
            MakeDealerCardsVisible();
            newRoundButton.setVisible(true);
        }

        if(!isBlackJackPlayer && !isBlackJackDealer){
            //enables all Buttons, except Split Button
            splitButton.setDisable(true);
            hitButton.setDisable(false);
            standButton.setDisable(false);
            doubleDownButton.setDisable(false);
            surrenderButton.setDisable(false);

        }

        //Checks for possibility Split (Split is only possible if both cards are of same value)
        if(player.getCard(0).getValue() == player.getCard(1).getValue() && !isBlackJackDealer){
            splitButton.setDisable(false);
        }
        if(player.getCard(0).getIsAce() && !isBlackJackDealer){
            if(player.getCard(1).getIsAce()){
                splitButton.setDisable(false);
            }
        }

        setBetButton.setDisable(true);
        showBalance.setText("Your Balance: "+ player.getBalance());
    }

    private void GenerateCardDealer() throws FileNotFoundException {
        talonFound=GameMethods.TalonFound();
        if(talonFound){
            if(talonShow)
            ShowTalon();
            talonShow=false;
        }
        int DcardValue=GameMethods.CardsValueCalculator(dealer);

        dealerPane.getChildren().add(dealer.getCard(dealer.getHoldingCards().size()-1).getImageView());

        if(showFullDealerValue){
            dealerValueWindow.setText("Card Value: " + DcardValue);
        }else{
            dealerValueWindow.setText("Card Value: " + dealer.getCard(0).getValue());
        }

        if(dealer.getHoldingCards().size()==3){
            String path=System.getProperty("user.dir")+"/src/main/java/at/ac/fhcampuswien/textures/cards/"+dealer.getHoldingCards().get(1).getID()+".jpg";
            Image image = new Image(new FileInputStream(path));
            dealer.getCard(1).setImage(image);
        }
    }

    private void MakeDealerCardsVisible() throws FileNotFoundException {
        String path=System.getProperty("user.dir")+"/src/main/java/at/ac/fhcampuswien/textures/cards/"+dealer.getHoldingCards().get(1).getID()+".jpg";
        Image image = new Image(new FileInputStream(path));
        dealer.getCard(1).setImage(image);
    }

    private void GenerateCardPlayer() throws FileNotFoundException {
        talonFound=GameMethods.TalonFound();
        if(talonFound){
            if(talonShow)
                ShowTalon();
            talonShow=false;
        }

        int PlayerCardValue=GameMethods.CardsValueCalculator(player);

        ImageView show=player.getCard(player.getHoldingCards().size()-1).getImageView();

        if(splitActive){
            show.setLayoutX(show.getLayoutX()-(player.getHoldingCards().size()-1)*60);
        }

        playerPane.getChildren().add(show);

        playerValueWindow.setText("Card Value: " + PlayerCardValue);
    }

    //boolean isFistSplitCard=true;
    private void GenerateCardSplit() throws FileNotFoundException {
        talonFound=GameMethods.TalonFound();
        if(talonFound){
            if(talonShow)
                ShowTalon();
            talonShow=false;
        }
        int SplitCardValue=GameMethods.CardsValueCalculator(splitPlayer);

        ImageView show = splitPlayer.getCard(splitPlayer.getHoldingCards().size()-1).getImageView();

        show.setLayoutX(500+55*(splitPlayer.getHoldingCards().size()-2)); //560

        playerPane.getChildren().add(show);

        splitValueWindow.setText("Card Value Split: " + SplitCardValue);
    }

    private void ShowTalon() throws FileNotFoundException {
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

    private void DealerDraws() throws FileNotFoundException {
        showFullDealerValue =true;
        terminal.clear();
        ShowDealerValue();
        MakeDealerCardsVisible();

        int dealerCardValue=GameMethods.CardsValueCalculator(dealer);
        if(dealerCardValue<17){
            while(dealerCardValue<17){
                GameMethods.GiveCardToDealer(dealer,deck);
                GenerateCardDealer();
                dealerCardValue=GameMethods.CardsValueCalculator(dealer);
                ShowDealerValue();
            }
        }
        if(GameMethods.BustCalculator(dealer)){
            terminal.setText("Dealer Bust.");
        }

        if(!leftHandAlreadyDone){
            win=GameMethods.Win(player,dealer);
            push=GameMethods.Push(player,dealer);
            lost=GameMethods.Lost(player,dealer);
            if(win){
                if(splitActive && !rightHandAlreadyDone){
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
        }


        //for Split
        if(!rightHandAlreadyDone){
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
        }
        if(rightHandAlreadyDone && leftHandAlreadyDone) {
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

        if (GameMethods.BustCalculator(player) && !splitActive) {
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
        }else if(GameMethods.BustCalculator(player) && splitActive){
            terminal.clear();
            terminal.setText("Left Hand Bust!");
            GameMethods.LostPayout(player);
            HandSelector();
            handSelectButton.setDisable(true);
            player.clearHoldingCards();
            leftHandAlreadyDone =true;
        }
        if(leftHandAlreadyDone && rightHandAlreadyDone){
            standButton.setDisable(true);
            DisableButtons();
            MakeSplitButtonsInvisible();
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

        if (GameMethods.BustCalculator(splitPlayer)) {
            terminal.clear();
            terminal.setText("Right Hand Bust!");
            GameMethods.LostPayout(splitPlayer);
            setBetButton.setDisable(true);
            newRoundButton.setVisible(false);
            HandSelector();
            handSelectButton.setDisable(true);
            splitPlayer.clearHoldingCards();
            splitValueWindow.setText("Card Value: ");
            rightHandAlreadyDone =true;
        }
        if(leftHandAlreadyDone && rightHandAlreadyDone){
            standButton.setDisable(true);
            DisableButtons();
            MakeSplitButtonsInvisible();
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

    public void DisableButtonsSplit(){
        hitButtonSplit.setDisable(true);
        doubleDownButtonSplit.setDisable(true);
        surrenderButtonSplit.setDisable(true);
    }

    public void MakeSplitButtonsInvisible(){
        doubleDownButtonSplit.setVisible(false);
        hitButtonSplit.setVisible(false);
        surrenderButtonSplit.setVisible(false);
    }

    public void MakeButtonsInvisible(){
        doubleDownButton.setVisible(false);
        hitButton.setVisible(false);
        surrenderButton.setVisible(false);
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
            MakeSplitButtonsInvisible();

            doubleDownButton.setVisible(true);
            hitButton.setVisible(true);
            surrenderButton.setVisible(true);

            if(leftHandHasHit){
                doubleDownButton.setDisable(true);
                surrenderButton.setDisable(true);
            }
        }

        if(rightHandSelected){
            MakeButtonsInvisible();

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

        player.getCard(1).setImageViewX(50+110+290);

        splitPlayer=GameMethods.Split(player);
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
        int splitCardValue=GameMethods.CardsValueCalculator(splitPlayer);

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
            rightHandAlreadyDone =true;
        }

        //checks for Blackjack
        if(player.getCard(0).getValue()==11 && player.getCard(1).getValue()==10){
            isBlackJackPlayer =true;
        }else if(player.getCard(1).getValue()==11 && player.getCard(0).getValue()==10) {
            isBlackJackPlayer =true;
        }
        if(isBlackJackPlayer){
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

        if(isBlackjackSplit&& isBlackJackPlayer){
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
        player.getCard(player.getHoldingCards().size() - 1).getImageView().setRotate(90);
        lost = GameMethods.Lost(player, dealer);
        showBalance.setText("Your Balance: " + player.getBalance());

        if (GameMethods.BustCalculator(player)) {
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

        DisableButtonsSplit();

        GameMethods.DoubleDown(splitPlayer, deck);
        GenerateCardSplit();
        //rotates the card by 90°
        splitPlayer.getCard(splitPlayer.getHoldingCards().size() - 1).getImageView().setRotate(90);
        lost = GameMethods.Lost(splitPlayer, dealer);
        showBalance.setText("Your Balance: " + player.getBalance());

        if (GameMethods.BustCalculator(splitPlayer)) {
            terminal.clear();
            terminal.setText("Right Hand Bust!");
            GameMethods.LostPayout(player);
            setBetButton.setDisable(true);
            newRoundButton.setVisible(false);
            rightHandAlreadyDone =true;
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
            DisableButtonsSplit();
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
        if(rightHandAlreadyDone) newRoundButton.setVisible(true);
    }

    public void SplitSurrender() {
        terminal.appendText("\nSurrendered!");
        player.addBalance(splitPlayer.getStake()*0.5);
        splitPlayer.setStake(0);
        splitPlayer.clearHoldingCards();
        HandSelector();
        handSelectButton.setDisable(true);
        rightHandAlreadyDone =true;
        if(leftHandAlreadyDone) newRoundButton.setVisible(true);
    }

    private void ShowDealerValue(){
        int DcardValue=GameMethods.CardsValueCalculator(dealer);
        dealerValueWindow.setText("Card Value: "+ DcardValue);
    }

    public void exit(){
        Platform.exit();
    }
}