package novaminds.gradproj.domain.refrigerator.repository;

import novaminds.gradproj.domain.refrigerator.entity.Refrigerator;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefrigeratorRepository extends JpaRepository<Refrigerator, Long> {
}
