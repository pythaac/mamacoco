package mamcoco.web;

import org.springframework.web.bind.annotation.*;

@RestController
public class RestTestController {
    @PostMapping("/restTest")
    public String restTest(){
        return "test 완료";
    }
}
