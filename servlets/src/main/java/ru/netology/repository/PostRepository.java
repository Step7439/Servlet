package ru.netology.repository;

import org.springframework.stereotype.Repository;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {
  //List<Post> posts = new CopyOnWriteArrayList<>();
  ConcurrentLinkedQueue<Post> posts = new ConcurrentLinkedQueue<Post>();
  private final AtomicLong atomicLong = new AtomicLong(1L);
  public ConcurrentLinkedQueue<Post> all() {
    return posts;
  }

  public Optional<Post> getById(long id) {
    return posts.stream()
            .filter(post -> post.getId() == id)
            .findFirst();
  }

  public Post save(Post post) {
      if (post.getId() == 0L){
        post.setId(atomicLong.getAndIncrement());
        posts.add(post);
        return post;
      }else {
        final Post endPost = getById(post.getId()).orElseThrow(NotFoundException::new);
        endPost.setContent(post.getContent());
        return endPost;
      }
  }
  public Post removeById(long id) {
    final Post oldPost = getById(id).orElseThrow(NotFoundException::new);
    posts.removeIf(post -> post.getId() == id);
    return oldPost;
  }
}
