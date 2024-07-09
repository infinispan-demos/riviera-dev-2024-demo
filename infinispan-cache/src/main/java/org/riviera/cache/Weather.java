package org.riviera.cache;

import org.infinispan.protostream.annotations.Proto;

@Proto
public record Weather(String weather, String day, String city) {
}
