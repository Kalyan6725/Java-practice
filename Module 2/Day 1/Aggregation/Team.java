import java.util.ArrayList;
import java.util.List;
class Team {
    String Tname;
    List<Player> players;

    Team(String Tname) {
        this.Tname = Tname;
        this.players = new ArrayList<>();
    }

    public String getTname() {
        return Tname;
    }
    public List<Player> getPlayers() {
        return players;
    }
    public void addPlayer(Player player) {
        players.add(player);
    }
    public void removePlayer(Player player) {
        players.remove(player);
    }
}