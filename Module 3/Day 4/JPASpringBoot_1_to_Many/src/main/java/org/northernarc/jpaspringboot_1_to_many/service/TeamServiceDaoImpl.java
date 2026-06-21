import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamServiceDaoImpl implements TeamServiceDao {
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private PlayerRepository playerRepository;
    public Player addPlayer(Player player){
        return playerRepository.save(player);
    }
    public Team addTeam(Team team){
        return teamRepository.save(team);
    }
    public List<Team> getAll() {
        return teamRepository.findAll();
    }
    public List<Player> getallPlayers() {
        return playerRepository.findAll();
    }
}