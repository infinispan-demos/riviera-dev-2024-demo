# Riviera Dev 2024 - Demo

## Caching Use Case

```properties
quarkus.infinispan-client.dev-services.enabled=false
quarkus.infinispan-client.hosts=localhost:11222
quarkus.infinispan-client.username=admin
quarkus.infinispan-client.password=password
quarkus.infinispan-client.client-intelligence=BASIC
quarkus.cache.infinispan.expiration-cache.lifespan=15s
```

```bash
podman compose up 
```

## Surveillance

Console Jaeger

http://localhost:16686/