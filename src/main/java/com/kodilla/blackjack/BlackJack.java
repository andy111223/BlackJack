package com.kodilla.blackjack;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.awt.*;


public class BlackJack extends Application {
    private final Deck deck = new Deck(1);
    private final Hand hand = new Hand();
    private final Hand dealer = new Hand();

    private boolean busted;
    private boolean playerTurn;

    private Image imageback = new Image("file:src/main/resources/blackjack-background.jpeg");
    private Image card = new Image("file:src/main/resources/blackjack-card.png");
    private FlowPane cards = new FlowPane(Orientation.HORIZONTAL);
    private FlowPane dealerCards = new FlowPane(Orientation.HORIZONTAL);
    private Label totalLabel = new Label();
    private Label totalLabelDealer = new Label();
    private Label status = new Label();

    private Label dealerLbl = new Label("Karty krupiera");
    private Label playerLbl = new Label("Twoje karty");

    public void drawCard(Hand hand, FlowPane pane, Label l) {
        try{
            Card card = deck.dealCard();
            ImageView img = new ImageView(card.getCardImage());
            pane.getChildren().add(img);
            hand.addCard(card);

            int handTotal = hand.evaluateHand();

            StringBuilder total = new StringBuilder();
            if (hand.getSoft() > 0) {
                total.append(handTotal - 10).append("/");
            }
            total.append(handTotal);
            l.setText(total.toString());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void newDeck() {
        deck.restoreDeck();
        deck.shuffle();
        System.out.println("Talia przetasowana");
    }
    public void newHand() {
        if (deck.getNumberOfCardsRemaining <= deck.getSizeOfDeck() * 0.2) {

        }
        hand.discardHand();
        dealer.discardHand();
        cards.getChildren().removeAll(cards.getChildren());
        dealer.getChildren().removeAll(dealerCards.getChildren());
        totalLabel.setText("");
        totalLabelDealer.setText("");

        busted = false;
        playerTurn = true;

        drawCard(hand, cards, totalLabel);
        drawCard(dealer, dealerCards, totalLabelDealer);
        drawCard(hand, cards, totalLabel);

        status.setText("Tw??j ruch");
    }



    @Override
    public void start(Stage primaryStage) throws Exception {

        totalLabel.setFont(new Font("Arial", 24));
        totalLabel.setTextFill(Color.web("#FFF"));

        totalLabelDealer.setFont(new Font("Arial", 24));
        totalLabelDealer.setTextFill(Color.web("#FFF"));

        status.setTextFill(Color.web("#FFF"));
        status.setFont(new Font("Arial", 24));

        dealerLbl.setTextFill(Color.web("#FFF"));
        dealerLbl.setFont(new Font("Arial", 24));

        playerLbl.setTextFill(Color.web("#FFF"));
        playerLbl.setFont(new Font("Arial", 24));

        BackgroundSize backgroundSize = new BackgroundSize(100,100,true,true, true, false);
        BackgroundImage backgroundImage = new BackgroundImage(imageback, BackgroundRepeat.REPEAT,BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        Background background = new Background(backgroundImage);

        Button drawbtn = new Button();
        drawbtn.setText("Hit");
        drawbtn.setOnAction((e) -> {
            if (playerTurn && !busted) {
                drawCard(hand, cards, totalLabel);
                if (hand.evaluateHand() > 21) {
                    System.out.println("Przegra??e??");
                    busted = true;
                    playerTurn = false;
                    status.setText("Przegra??e??");
                }
            }
        });

        Button standbtn = new Button();
        standbtn.setText("Stand");
        standbtn.setOnAction((e) -> {
           if (playerTurn && !busted) {
               playerTurn = false;
               while(dealer.evaluateHand() < 17 ) {
                   drawCard(dealer, dealerCards, totalLabelDealer);
               }
               int playerTotal = hand.evaluateHand();
               int dealerTotal = dealer.evaluateHand();
               System.out.println("Karty gracza: " + hand);
               System.out.println("Karty krupiera: " + dealer);

               if (playerTotal == 21) {
                   System.out.println("Wygra??e??");
                   status.setText("Wygra??e??");
               } else if (dealerTotal <= 21 && playerTotal == dealerTotal) {
                   System.out.println("Remis");
                   status.setText("Remis");
               }else if (dealerTotal <= 21 && playerTotal <= dealerTotal) {
                   System.out.println("Przegra??e??");
                   status.setText("Przegra??e??");
               } else {
                   System.out.println("Wygra??e??");
                   status.setText("Wygra??e??");
               }
           }
        });

        Button newbtn = new Button();
        newbtn.setText("Nowe rozdanie");
        newbtn.setOnAction((e) -> {
            newHand();
        });


        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(11.5,12.5,13.5,14.5));
        grid.setHgap(5.5);
        grid.setVgap(5.5);
        grid.setBackground(background);

        grid.add(dealerCards, 0, 0, 3, 1);
        grid.add(dealerLbl, 0, 1);
        grid.add(totalLabelDealer, 1, 1, 2, 1);

        Pane p = new Pane();
        p.setPrefSize(0,100);
        grid.add(p, 0, 2);

        grid.add(cards,0,3,3,1);
        grid.add(playerLbl,0,4);
        grid.add(totalLabel, 1, 4,2,1);
        grid.add(drawbtn, 0, 5);
        grid.add(standbtn, 1, 5);
        grid.add(newbtn, 2, 5);
        grid.add(status, 0, 6, 3, 1);
        grid.setBackground(background);

        grid.add(cards, 0,0,3,1);

        Scene scene = new Scene(grid,1600,900);

        primaryStage.setTitle("BlackJack");
        primaryStage.setScene(scene);
        primaryStage.show();

        newDeck();
        newHand();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
