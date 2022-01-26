package in.as.sixtynine.rakku.controllers;

import in.as.sixtynine.rakku.dtos.PostDto;
import in.as.sixtynine.rakku.services.PostService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.Principal;

/**
 * @Author Sanjay Das (s0d062y), Created on 23/01/22
 */


@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class DemoController {

    private static final Logger log = LogManager.getLogger(DemoController.class);

    private final RestTemplate restTemplate;
    private final PostService postService;

    @GetMapping("/users")
    public Object fetchUsers() {
        log.info("fetching users....");
        return restTemplate.getForObject("https://jsonplaceholder.typicode.com/users", Object.class);
    }

    @GetMapping("/post")
    public Object getPosts(@RequestParam(value = "page", defaultValue = "0") Integer page,
                           @RequestParam(value = "size", defaultValue = "10") Integer size,
                           Principal principal) {
        log.info("fetching post...., page={}, size={}", page, size);
        return postService.getScrollingPost(page, size, principal.getName());
    }

    @GetMapping("/relevant/post")
    public Object getRelevantPosts(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                   @RequestParam(value = "size", defaultValue = "10") Integer size,
                                   Principal principal) {
        log.info("fetching post...., page={}, size={}", page, size);
        return postService.getRelevantPost(page, size, principal.getName());
    }

    @PostMapping("/post")
    public PostDto post(@RequestBody PostDto postDto, Principal principal) {
        return postService.post(postDto, principal.getName());
    }
}
