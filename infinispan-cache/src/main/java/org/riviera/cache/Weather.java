package org.riviera.cache;

import org.infinispan.api.annotations.indexing.Indexed;
import org.infinispan.api.annotations.indexing.Text;
import org.infinispan.protostream.annotations.Proto;

@Proto
@Indexed
public record Weather(@Text String weather, String day, String city) {
}
