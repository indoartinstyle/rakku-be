package in.as.sixtynine.rakku.mappers;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import in.as.sixtynine.rakku.dtos.PostDto;
import in.as.sixtynine.rakku.entities.Post;
import org.springframework.stereotype.Service;

/**
 * @Author Sanjay Das (s0d062y), Created on 02/01/22
 */


@Service
public class PostMapper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public PostDto toDto(Post entry) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.convertValue(entry, PostDto.class);
    }

    public Post toEntity(PostDto dto) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper.convertValue(dto, Post.class);
    }

}
