
import com.example.remotes.ChangeServiceRemote;
import jakarta.ejb.Stateless;

@Stateless
public class ChangeService implements ChangeServiceRemote {

    @Override
    public String hello(String name) {
        return "Hello " + name + " from change-ejb!";
    }
}
