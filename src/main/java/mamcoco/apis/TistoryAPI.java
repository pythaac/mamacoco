package mamcoco.apis;

import mamcoco.dao.TistoryInfo;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

public class TistoryAPI {
    private final String tistory_domain;
    private final TistoryInfo tistory_info;
    private final RestTemplate restTemplate;

    public TistoryAPI(TistoryInfo tistory_info){
        this.tistory_domain = "https://www.tistory.com/apis/blog/";

        RestTemplateBuilder builder = new RestTemplateBuilder();
        this.restTemplate = builder.build();

        this.tistory_info = tistory_info;
    }

    public String info(){
        String url = this.tistory_domain + "info?" +
                "access_token=" + this.tistory_info.getTistoryAccessToken();

        return this.restTemplate.getForObject(url, String.class);
    }
}
