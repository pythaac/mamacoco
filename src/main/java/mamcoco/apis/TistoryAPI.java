package mamcoco.apis;

import mamcoco.database.data.TistoryInfo;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class TistoryAPI {
    private final String tistory_domain;
    private TistoryInfo tistory_info;
    private final RestTemplate restTemplate;

    public TistoryAPI(){
        this.tistory_domain = "https://www.tistory.com/apis/";

        RestTemplateBuilder builder = new RestTemplateBuilder();
        this.restTemplate = builder.build();
    }

    public void setTistory_info(TistoryInfo tistory_info) {
        this.tistory_info = tistory_info;
    }

    public String info(){
        String url = this.tistory_domain + "blog/info?" +
                "access_token=" + this.tistory_info.getTistoryAccessToken();

        return this.restTemplate.getForObject(url, String.class);
    }

    public String categoryList(){
        String url = this.tistory_domain + "category/list?" +
                "access_token=" + this.tistory_info.getTistoryAccessToken() +
                "&blogName=" + this.tistory_info.getTistoryBlogName();

        return this.restTemplate.getForObject(url, String.class);
    }

    public String postList(Long page){
        String url = this.tistory_domain + "post/list?" +
                "access_token=" + this.tistory_info.getTistoryAccessToken() +
                "&blogName=" + this.tistory_info.getTistoryBlogName() +
                "&page=" + page;

        return this.restTemplate.getForObject(url, String.class);
    }

    public String post(Long postId){
        String url = this.tistory_domain + "post/read?" +
                "access_token=" + this.tistory_info.getTistoryAccessToken() +
                "&blogName=" + this.tistory_info.getTistoryBlogName() +
                "&postId=" + postId.toString();

        return this.restTemplate.getForObject(url, String.class);
    }
}