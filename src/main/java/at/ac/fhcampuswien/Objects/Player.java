package at.ac.fhcampuswien.Objects;

public class Player extends Person{

    private double balance;
    private double stake;

    public Player(String playerName, double balance) {
        super(playerName);
        this.balance=balance;
        this.stake=0;
    }

    public double getStake() {
        return this.stake;
    }

    public void setStake(double stake) {
        if(stake < 0) {
            stake *= -1;
        }
        this.stake = stake;
    }


    public double getBalance() {
        return balance;
    }

    public void addBalance(double add){
        this.balance+=add;
    }

    public void removeBalance(double rem){
        this.balance-=rem;
    }

}
