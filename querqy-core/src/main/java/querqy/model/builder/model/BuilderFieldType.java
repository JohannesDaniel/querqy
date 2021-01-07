package querqy.model.builder.model;

import lombok.Data;
import querqy.model.builder.converter.MapValueConverter;

@Data
public class BuilderFieldType {

    public final BuilderFieldProperties properties;
    public final MapValueConverter mapValueConverter;




    /*
                List<Class>
                List<Interface>

                String, Occur, Boolean, Interface

                Iterfaces: QuerqyQueryBuilder, DisjunctionMaxClauseBuilder
                 */
}
