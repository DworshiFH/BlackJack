package HideThePain;

import HideThePain.People.Dealer;
import HideThePain.People.Player;
import com.sun.javafx.scene.text.TextLayout;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.input.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static HideThePain.GameMethods.*;

public class Game extends Application {
    private static boolean hasTalon=false;

    public static void main(String[] args){
        launch(args);
    }

    public static void startGame() throws FileNotFoundException {
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
    }

    @Override
    public void start(Stage primaryStage){
        //Group ui = FXMLLoader.load(getClass().getResource("UI.fxml"));

        Player player1=new Player("Kurti",1000);
        player1.setStake(500);
        primaryStage.setTitle("Blackjack");
       /* Button btn = new Button();
        btn.setText("Hello JavaFX!");
        btn.setOnAction( (event) -> Platform.exit() );*/
        Pane root = new StackPane();
        //root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 150));
        primaryStage.show();
    }

    @FXML
    private static void hit(Player player, List<Card> deck) throws FileNotFoundException {
        Hit(player, deck);
    }
}
