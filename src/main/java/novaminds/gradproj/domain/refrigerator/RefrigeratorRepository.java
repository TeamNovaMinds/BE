package novaminds.gradproj.domain.refrigerator;

import novaminds.gradproj.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefrigeratorRepository extends JpaRepository<Refrigerator, Long> {
    boolean existsByUser(User user);
}
