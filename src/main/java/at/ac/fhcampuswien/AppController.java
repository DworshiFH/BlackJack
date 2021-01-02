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
    public Button setBetButton;
    @FXML
    public TextField stakesFiled;
    @FXML
    public Pane playerPane;
    @FXML
    public Pane dealerPane;
    @FXML
    public TextField playerValueWindow;
    @FXML
    public TextField dealerValueWindow;
    @FXML
    public Button hitButton;
    @FXML
    public Button splitButton;
    @FXML
    public Button doubleDownButton;
    @FXML
    public Button standButton;
    @FXML
    public TextField showBalance;
    @FXML
    public Button newRoundButton;
    @FXML
    public TextField splitValueWindow;
    @FXML
    public Button handSelectButton;
    @FXML
    public TextField showSelectedHand;
    @FXML
    public Button surrenderButton;
    @FXML
    private Pane mainPane;
    @FXML
    private TextArea terminal;

    Card talon=new Card("Talon",0);

    Player player;
    Dealer dealer;
    Player splitPlayer;
    List<Card> deck;

    private boolean lost =false;
    private boolean isBlackJack;
    private boolean push;
    private boolean talonFound;
    private boolean win;
    private boolean rightHandSelected;

    private boolean showFullDealerValue;

    private static int numOfCardsDealer=0;

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
        rightHandSelected =false;
        handSelectButton.setVisible(false);
        showSelectedHand.setVisible(false);

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
            hitButton.setDisable(false);
            splitButton.setDisable(true);
            doubleDownButton.setDisable(false);
            standButton.setDisable(false);
        }

        //Split is only possible if both cards are of same value
        if(player.getCard(0).getValue()==player.getCard(1).getValue()){
            splitButton.setDisable(false);
        }

        setBetButton.setDisable(true);
        showBalance.setText("Your Balance: "+ player.getBalance());
    }
    private final String DealerValueWindowDefault = "Card Value: ";

    public void GenerateCardDealer() throws FileNotFoundException {
        talonFound=GameMethods.TalonFound();
        if(talonFound){
            ShowTalon();
        }
        int DcardValue=GameMethods.DealerValueCalculator(dealer);

        dealerPane.getChildren().add(dealer.getHoldingCards().get(dealer.getHoldingCards().size()-1).getImageView());

        if(showFullDealerValue){
            dealerValueWindow.setText(DealerValueWindowDefault + DcardValue);
        }else{
            dealerValueWindow.setText(DealerValueWindowDefault + dealer.getHoldingCards().get(0).getValue());
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
            ShowTalon();
        }
        int PlayerCardValue=GameMethods.PlayerValueCalculator(player);

        playerPane.getChildren().add(player.getHoldingCards().get(player.getHoldingCards().size()-1).getImageView());

        playerValueWindow.setText("Card Value: " + PlayerCardValue);
    }

    public void GenerateCardSplit() throws FileNotFoundException {
        talonFound=GameMethods.TalonFound();
        if(talonFound){
            ShowTalon();
        }
        int PlayerCardValue=GameMethods.PlayerValueCalculator(splitPlayer);

        ImageView show = splitPlayer.getHoldingCards().get(splitPlayer.getHoldingCards().size()-1).getImageView();

        show.setLayoutX(560+offset);

        playerPane.getChildren().add(show);

        splitValueWindow.setText("Card Value Split: " + PlayerCardValue);
    }


    public void ShowTalon() throws FileNotFoundException {
        terminal.appendText("Talon drawn!\nDeck will be reshuffled.");

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
        int dealerCardValue=0;
        showFullDealerValue =true;
        for(int i=0;i<dealer.getHoldingCards().size();i++){
            dealerCardValue+=dealer.getHoldingCards().get(i).getValue();
        }
        if(dealerCardValue<17){
            int i=dealer.getHoldingCards().size();
            do{
                GameMethods.GiveCardToDealer(dealer,deck);
                GenerateCardDealer();
                dealerCardValue+=dealer.getHoldingCards().get(i).getValue();
                i++;
            }while(dealerCardValue<17);
        }
        win=GameMethods.Win(player,dealer);
        push=GameMethods.Push(player,dealer);
        lost=GameMethods.Lost(player,dealer);
        if(win){
            terminal.clear();
            terminal.setText("Won!");
            if(GameMethods.DealerHasOverdrawn(dealer)){
                terminal.appendText("\nDealer has overdrawn.");
            }
            GameMethods.WinPayout(player);
            setBetButton.setDisable(true);
            ShowDealerValue();
            MakeDealerCardsVisible();
            newRoundButton.setVisible(true);
        }else if(push){
            terminal.clear();
            terminal.setText("Push!");
            GameMethods.PushPayout(player);
            setBetButton.setDisable(true);
            ShowDealerValue();
            MakeDealerCardsVisible();
            newRoundButton.setVisible(true);
        }else if(lost){
            terminal.clear();
            terminal.setText("Lost!");
            GameMethods.LostPayout(player);
            setBetButton.setDisable(true);
            ShowDealerValue();
            MakeDealerCardsVisible();
            newRoundButton.setVisible(true);
        }
    }

    public void Hit() throws FileNotFoundException {
        doubleDownButton.setDisable(true);
        splitButton.setDisable(true);

        GameMethods.GiveCardToPlayer(player,deck);

        GenerateCardPlayer();

        if(GameMethods.PlayerHasOverdrawn(player)){
            terminal.clear();
            terminal.setText("Bust!");
            GameMethods.LostPayout(player);
            setBetButton.setDisable(true);
            newRoundButton.setVisible(true);
            DisableButtons();
        }
    }

    public void DisableButtons(){
        hitButton.setDisable(true);
        splitButton.setDisable(true);
        doubleDownButton.setDisable(true);
        standButton.setDisable(true);
    }

    public void HandSelector(){ //false = left hand selected
        if(rightHandSelected){
            showSelectedHand.setText("       Selected Hand     ->");
        }else{
            showSelectedHand.setText("<-     Selected Hand");
        }
        rightHandSelected =!rightHandSelected;
    }

    private final int splitOffset=400;
    public void Split() throws FileNotFoundException {

        talonFound=GameMethods.TalonFound();
        if(talonFound){
            ShowTalon();
        }

        splitValueWindow.setVisible(true);
        handSelectButton.setVisible(true);
        showSelectedHand.setVisible(true);

        player.getCard(1).setImageViewX(player.getCard(1).getImageView().getLayoutX()+splitOffset);

        splitPlayer=GameMethods.Split(player, deck);

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
        GenerateCardSplit();

        GenerateCardPlayer();
        splitButton.setDisable(true);
    }

    public void DoubleDown() throws FileNotFoundException {
        if(player.getHoldingCards().size()==2){
            GameMethods.DoubleDown(player,deck);
            GenerateCardPlayer();
            //rotates the card by 90°
            player.getHoldingCards().get(player.getHoldingCards().size()-1).getImageView().setRotate(90);
            lost =GameMethods.Lost(player, dealer);
            showBalance.setText("Your Balance: "+ player.getBalance());

            if(GameMethods.PlayerHasOverdrawn(player)){
                terminal.clear();
                terminal.setText("You have Overdrawn!");
                ShowDealerValue();
                GameMethods.LostPayout(player);
                setBetButton.setDisable(true);
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
        dealerValueWindow.setText(DealerValueWindowDefault+ DcardValue);
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
}