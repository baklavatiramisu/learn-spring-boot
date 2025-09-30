package com.baklavatiramisu.learn.springjpa.controller.post;

import com.baklavatiramisu.learn.springjpa.dto.Post;

public record PostResponse(Long id, String content) {
    public static PostResponse fromPost(Post post) {
        return new PostResponse(post.getId(), post.getContent());
    }

    @Override
    public String toString() {
        return "PostResponse{" +
                "id=" + id +
                ", content='" + content + '\'' +
                '}';
    }
}
