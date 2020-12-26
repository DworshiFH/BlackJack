package HideThePain;


public class Card {

    private String ID;
    private int value;
    private int texPosX;
    private int texPosY;

    public Card(String ID, int value){
        this.ID = ID;
        this.value = value;
        this.texPosX=50;
        this.texPosY=50;
    }


    public int getTexPosX() {
        return texPosX;
    }

    public void setTexPosX(int texPosX) {
        this.texPosX = texPosX;
    }

    public int getTexPosY() {
        return texPosY;
    }

    public void setTexPosY(int texPosY) {
        this.texPosY = texPosY;
    }

    public int getValue(){
        return value;
    }

    public String getID() {
        return ID;
    }
}
