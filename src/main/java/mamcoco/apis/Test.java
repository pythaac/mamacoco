package mamcoco.apis;

import mamcoco.dao.TistoryInfo;
import mamcoco.data.AuthRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static mamcoco.MamacocoApplication.client_id;

@RestController
public class Test {
    private final AuthRepo repo;

    @Autowired
    public Test(AuthRepo repo)
    {
        this.repo = repo;
    }

    @GetMapping(value="/test")
    public String test(String s)
    {
        TistoryInfo info = repo.findByTistoryBlogId(client_id);
        try
        {
            return "test : " + info.getTistoryAccessToken();
        }
        catch(NullPointerException e)
        {
            return "test : e";
        }
        finally
        {
            System.out.println("test executed");
        }
    }
}
