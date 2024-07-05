package org.riviera.cache;

import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.ProtoSchema;


@ProtoSchema(includeClasses = Weather.class,
        schemaPackageName = "riviera")
public interface WeatherSchema extends GeneratedSchema {
}
