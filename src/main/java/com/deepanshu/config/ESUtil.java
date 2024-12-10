package com.deepanshu.config;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import java.util.function.Supplier;

public class ESUtil {

    public static Supplier<Query> createSupplierAutoSuggest(String partialProductName){
        Supplier<Query> supplier = () -> Query.of(q -> q
                .bool(b -> b
                        .should(sh -> sh
                                .prefix(p -> p
                                        .field("name")
                                        .value(partialProductName)
                                )
                        )
                )
        );
        return supplier;
    }
}