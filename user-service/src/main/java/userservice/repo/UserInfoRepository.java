package userservice.repo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import userservice.model.User;

@Repository
public interface UserInfoRepository extends ReactiveMongoRepository<User, String> {

    Mono<User> getUserById(String id);
}

