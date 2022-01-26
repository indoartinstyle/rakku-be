package in.as.sixtynine.rakku.repositories;

import com.azure.spring.data.cosmos.repository.CosmosRepository;
import com.azure.spring.data.cosmos.repository.Query;
import in.as.sixtynine.rakku.entities.Post;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface PostsRepository extends CosmosRepository<Post, String> {

    @Query("select * from c order by c.createdTime offset @offset limit @limit")
    List<Post> getPosts(@Param("offset") int offset, @Param("limit") int limit);

}
