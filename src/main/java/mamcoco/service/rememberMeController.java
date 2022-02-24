package mamcoco.service;

import mamcoco.database.data.Category;
import mamcoco.database.data.Post;
import mamcoco.database.repository.CategoryRepository;
import mamcoco.database.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

@RestController
@RequestMapping(value = "/rememberMe")
public class rememberMeController
{
    private final CategoryRepository catRepo;
    private final PostRepository postRepo;

    @Autowired
    public rememberMeController(CategoryRepository catRepo,
                                PostRepository postRepo){
        this.catRepo = catRepo;
        this.postRepo = postRepo;
    }

    @GetMapping(value = "/categories")
    public ArrayList<String> categories(){
        ArrayList<String> result = new ArrayList<>();

        ArrayList<Category> catList = this.catRepo.findAll();
        for(Category cat : catList){
            String catName = cat.getCatName();
            if (cat.getCatParent() != null){
                Category parent = catList.stream().
                        filter( c -> cat.getCatParent().equals(c.getCatId()) ).
                        findFirst().orElse(null);

                if (parent != null){
                    catName = parent.getCatName() + "/" + catName;
                }
                else{
                    catName = "???/" + catName;
                }
            }

            result.add(catName);
        }

        Collections.sort(result);
        return result;
    }

    @PostMapping(value = "/randomPostByCategory")
    public HashMap<String, String> randomPostByCategory(@RequestBody String category){
        System.out.println("[before-split] " + category);
        ArrayList<Category> catList = this.catRepo.findAll();
        String[] realCat = category.split("/");
        System.out.println("[after-split] " + realCat[realCat.length-1]);
        Category cat = catList.stream()
                .filter(c -> realCat[realCat.length-1].equals(c.getCatName()))
                .findFirst()
                .orElse(null);
        Assert.notNull(cat, "cat is null");

        ArrayList<Post> posts = postRepo.findAllByCatId(cat.getCatId());
        assert posts.size() != 0;
        fisherYatesSuffle(posts);

        int idx = (int)(Math.random() * posts.size());
        Post post = posts.get(idx);

        HashMap<String, String> result = new HashMap<>();
        result.put("title", post.getPostTitle());
        result.put("id", post.getPostId().toString());
        return result;
    }

    private void fisherYatesSuffle(ArrayList<Post> posts){
        for(int i=posts.size()-1; i>=0; i--){
            int j = (int)(Math.random() * (i+1));
            Collections.swap(posts, i, j);
        }
    }

    @GetMapping(value = "/postById")
    public HashMap<String, String> postById(@RequestParam String postId){
        Long id = Long.parseLong(postId);
        Post post = postRepo.findPostByPostId(id);
        assert post != null;

        HashMap<String, String> result = new HashMap<>();
        result.put("title", post.getPostTitle());
        result.put("content", post.getPostContent());
        return result;
    }
}
