package novaminds.gradproj.global.search.repository;

import novaminds.gradproj.global.search.IngredientDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface IngredientSearchRepository extends ElasticsearchRepository<IngredientDocument, Long> {
}