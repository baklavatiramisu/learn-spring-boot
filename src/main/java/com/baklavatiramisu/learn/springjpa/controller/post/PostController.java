package com.baklavatiramisu.learn.springjpa.controller.post;

import com.baklavatiramisu.learn.springjpa.controller.user.UserNotFoundException;
import com.baklavatiramisu.learn.springjpa.dto.Post;
import com.baklavatiramisu.learn.springjpa.dto.User;
import com.baklavatiramisu.learn.springjpa.repository.PostsRepository;
import com.baklavatiramisu.learn.springjpa.repository.UsersRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class PostController {
    private final UsersRepository usersRepository;
    private final PostsRepository postsRepository;

    public PostController(final UsersRepository usersRepository, final PostsRepository postsRepository) {
        this.usersRepository = usersRepository;
        this.postsRepository = postsRepository;
    }

    @GetMapping("/{userId}/posts")
    public List<PostResponse> getAllPosts(@PathVariable("userId") final Long userId) {
        return postsRepository.findAllPostsByUserId(userId).stream().map(PostResponse::fromPost).toList();
    }

    @PostMapping("/{userId}/posts")
    public ResponseEntity<Void> createNewPost(
            @PathVariable("userId") final Long userId,
            @RequestBody @Valid final CreatePostRequest request,
            final UriComponentsBuilder uriComponentsBuilder
    ) {
        final User user = usersRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        Post post = new Post();
        post.setUser(user);
        post.setContent(request.content());
        post.setCreatedOn(OffsetDateTime.now());
        post.setUpdatedOn(OffsetDateTime.now());
        post = postsRepository.save(post);
        final URI location = uriComponentsBuilder.path("/users/{userId}/posts/{postId}")
                .build(userId, post.getId());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{userId}/posts/{postId}")
    public void updatePost(
            @PathVariable("userId") final Long userId,
            @PathVariable("postId") final Long postId,
            @RequestBody final CreatePostRequest request
    ) {
        Post post = postsRepository.findPostByIdAndUserId(postId, userId).orElseThrow(() -> new PostNotFoundException(postId));
        post.setContent(request.content());
        post.setUpdatedOn(OffsetDateTime.now());
        postsRepository.save(post);
    }

    @DeleteMapping("/{userId}/posts/{postId}")
    public void deletePost(
            @PathVariable("userId") final Long userId,
            @PathVariable("postId") final Long postId
    ) {
        Post post = postsRepository.findPostByIdAndUserId(postId, userId).orElseThrow(() -> new PostNotFoundException(postId));
        postsRepository.delete(post);
    }
}
