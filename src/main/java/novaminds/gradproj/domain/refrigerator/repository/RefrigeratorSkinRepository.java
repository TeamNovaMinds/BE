package novaminds.gradproj.domain.refrigerator.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import novaminds.gradproj.domain.refrigerator.entity.RefrigeratorSkin;

public interface RefrigeratorSkinRepository extends JpaRepository<RefrigeratorSkin, Long>, RefrigeratorSkinRepositoryCustom {
    Optional<RefrigeratorSkin> findByIsDefaultTrue();
}