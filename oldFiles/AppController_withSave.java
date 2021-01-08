package at.ac.fhcampuswien.alteVersionen;

import at.ac.fhcampuswien.GameMethods;
import at.ac.fhcampuswien.Objects.Card;
import at.ac.fhcampuswien.Objects.Dealer;
import at.ac.fhcampuswien.Objects.Deck;
import at.ac.fhcampuswien.Objects.Player;
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

public class AppController_withSave {

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


    @FXML
    public TextArea showSlotsOld;
    @FXML
    public TextField chooseSaveSlotNew;
    @FXML
    public TextField enterPlayerBalance;
    @FXML
    public Button startGameButton;
    @FXML
    public TextField enterPlayerName;
    @FXML
    public Button newGame;
    @FXML
    public Button resumeOld;
    @FXML
    public Pane newGamePane;
    @FXML
    public Pane chooseGamePane;
    @FXML
    public Pane resumeOldPane;
    @FXML
    public TextArea showSlotsNew;
    @FXML
    public TextField chooseSaveSlotOld;


    //SaveSlots playerList=new SaveSlots();

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

    //boolean isFirstStake;

    private static int numOfCardsDealer=0;
    double oldStake = 0;

    public void newRound() {
        clearPlayingField();
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

        if(true){
            terminal.clear();
            terminal.setText(terminal.getText()+"Your Stake: "+printAmount);

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
                isBlackJack=true;
            }else if(player1.getCard(1).getValue()==11 && player1.getCard(0).getValue()==10) {
                isBlackJack=true;
            }
            if(isBlackJack){
                DisableButtons();
                GameMethods.BlackJackPayout(player1, dealer);
                terminal.clear();
                terminal.setText("BlackJack!");
                setStakeButton.setDisable(true);
                MakeDealerCardsVisible();
                newRoundButton.setVisible(true);
            }

            //isFirstStake=false;
        }else{
            terminal.setText(terminal.getText()+"\n"+"Your Stake: "+printAmount);
        }
        //enables all Buttons
        hitButton.setDisable(false);
        splitButton.setDisable(false);
        doubleDownButton.setDisable(false);
        StandButton.setDisable(false);

        setStakeButton.setDisable(true);

        showBalance.setText("Your Balance: "+player1.getBalance());
    }
    private final String DealerValueWindowDefault = "Card Value: ";

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
        PlayerValueWindow.setText("Card Value: " + PcardValue);
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
            GameMethods.winPayout(player1,dealer);
            setStakeButton.setDisable(true);
            ShowDealerValue();
            MakeDealerCardsVisible();
            //newRound();
            newRoundButton.setVisible(true);
        }else if(push){
            terminal.clear();
            terminal.setText("Push!");
            GameMethods.pushPayout(player1,dealer);
            setStakeButton.setDisable(true);
            ShowDealerValue();
            MakeDealerCardsVisible();
            //newRound();
            newRoundButton.setVisible(true);
        }else if(lost){
            terminal.clear();
            terminal.setText("Lost!");
            GameMethods.lostPayout(player1,dealer);
            setStakeButton.setDisable(true);
            ShowDealerValue();
            MakeDealerCardsVisible();
            //newRound();
            newRoundButton.setVisible(true);
        }
    }

    public void clearPlayingField() {
        playerPane.getChildren().clear();
        dealerPane.getChildren().clear();
    }

    public void hit() throws FileNotFoundException {
        MakeDealerCardsVisible();
        ShowDealerValue();
        GameMethods.Hit(player1,deck);
        generateCardPlayer();
        lost =GameMethods.lost(player1, dealer);
        if(lost){
            terminal.clear();
            if(GameMethods.PlayerHasOverdrawn(player1)){
                terminal.setText("You have Overdrawn!");
            }else{
                terminal.setText("You have Lost!\nDealer Value > Player Value.");
            }
            ShowDealerValue();
            GameMethods.lostPayout(player1, dealer);
            setStakeButton.setDisable(true);
            //newRound();
            newRoundButton.setVisible(true);
        }
        doubleDownButton.setDisable(true);
        splitButton.setDisable(true);
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
            player1.getHoldingCards().get(player1.getHoldingCards().size()-1).getTexture().setRotate(90);
            lost =GameMethods.lost(player1, dealer);

            if(GameMethods.PlayerHasOverdrawn(player1)){
                terminal.clear();
                terminal.setText("You have Overdrawn!");
                ShowDealerValue();
                GameMethods.lostPayout(player1, dealer);
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

    public static ImageView CardTextureAssigner(Card card, int cardnum, boolean isDealer) { //false = Dealer
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

    public void initialize(){ //is like Main
        player1=new Player("Kurti",1000);
        dealer=new Dealer();
        deck= Deck.makeDeck();
        newRoundButton.setVisible(false);

        //initPane.setVisible(true);
        //initPane.setVisible(false);


        newRound();
    }



    //save and initialize Methods

    //private final int maxSlots=10;
    //private final int minSlots=1;


    public void resumeOld(){
        /*chooseGamePane.setVisible(false);
        chooseGamePane.setDisable(true);

        for(int i=0;i<maxSlots;i++){
            showSlotsOld.appendText("Slot "+ (i+1)+": "+ playerList.getPlayer(i)+"\n");
        }


        playerList.getPlayer(Integer.parseInt(chooseSaveSlotOld.getText()));
*/
    }



    public void newGame(){
        /*chooseGamePane.setVisible(false);
        chooseGamePane.setDisable(true);

        //print out Slots
        for(int i=0;i<maxSlots;i++){
            showSlotsNew.appendText("Slot "+ (i+1)+": "+ playerList.getPlayer(i)+"\n");
        }

        playerList.setPlayerList(enterPlayerName.getText(),
                Double.parseDouble(enterPlayerBalance.getText()),
                Integer.parseInt(chooseSaveSlotNew.getText()));

        player1=playerList.getPlayer(Integer.parseInt(chooseSaveSlotNew.getText()));
*/



    }


}