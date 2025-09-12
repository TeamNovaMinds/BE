package novaminds.gradproj.global.search;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EsIndexBootstrap {
    private final ElasticsearchOperations operations;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        var idx = operations.indexOps(IngredientDocument.class);
        if (!idx.exists()) {
            idx.create(); // @Setting(nori-setting.json) 반영
            idx.putMapping(idx.createMapping(IngredientDocument.class)); // @MultiField 매핑 반영
        }
    }
}