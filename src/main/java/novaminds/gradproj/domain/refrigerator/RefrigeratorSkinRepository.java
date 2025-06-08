package novaminds.gradproj.domain.refrigerator;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefrigeratorSkinRepository extends JpaRepository<RefrigeratorSkin, Long> {
    Optional<RefrigeratorSkin> findByIsDefaultTrue();
}