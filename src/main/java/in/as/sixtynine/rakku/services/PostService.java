package in.as.sixtynine.rakku.services;

import com.azure.cosmos.models.CosmosQueryRequestOptions;
import com.azure.cosmos.util.CosmosPagedIterable;
import in.as.sixtynine.rakku.dtos.PostDto;
import in.as.sixtynine.rakku.entities.Post;
import in.as.sixtynine.rakku.mappers.PostMapper;
import in.as.sixtynine.rakku.repositories.PostsRepository;
import in.as.sixtynine.rakku.userservice.entity.User;
import in.as.sixtynine.rakku.userservice.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author Sanjay Das (s0d062y), Created on 23/01/22
 */

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostsRepository postsRepository;
    private final UserManagementService userManagementService;
    private final PostMapper mapper;
    private final DatabaseQueryService databaseQueryService;

    public PostDto post(PostDto dto, String name) {
        final Post entity = mapper.toEntity(dto);
        entity.setUsername(name);
        entity.setId(UUID.randomUUID().toString());
        entity.setCreatedTime(System.currentTimeMillis());
        return mapper.toDto(postsRepository.save(entity));
    }

    public List<PostDto> getScrollingPost(int page, int size, String userId) {
        final List<Post> all = postsRepository.getPosts(page, size);
        return all.stream().map(mapper::toDto).collect(Collectors.toList());
    }

    public List<PostDto> getRelevantPost(int offset, int limit, String userId) {
        final User user = userManagementService.getUserByID(userId);
        String userRegion = "'a'"; // need to implement using location;
        CosmosQueryRequestOptions options = new CosmosQueryRequestOptions();
        final String query = "SELECT * FROM c where c.org IN ('" + String.join("','", user.getFollowing()) + "') OR c.region IN(" + userRegion + ") ORDER BY c._ts DESC OFFSET " + offset + " LIMIT " + limit;
        final CosmosPagedIterable<Post> posts = databaseQueryService.runQuery(options, query, Post.class);
        return posts.stream().map(mapper::toDto).collect(Collectors.toList());
    }
}
