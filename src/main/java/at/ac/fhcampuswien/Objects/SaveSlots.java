package at.ac.fhcampuswien.Objects;

import java.util.List;

public class SaveSlots {
    private List<Player> playerList;

    public SaveSlots(){
        for(int i=0; i<10; i++){
            playerList.add(new Player("empty",0));
        }
    }

    public SaveSlots(String name, double balance, int slot) {
        assert false;
        this.playerList.add(slot,new Player(name, balance));
    }

    public Player getPlayer(int index) {
        return playerList.get(index);
    }

    public void setPlayerList(String name, double balance, int slot) {
        this.playerList.add(slot,new Player(name, balance));    }
}
