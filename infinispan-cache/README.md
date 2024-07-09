# Riviera Dev 2024 - Demo

# Montrer Cluster

```properties
# LON
quarkus.infinispan-client.dev-services.enabled=false
quarkus.infinispan-client.hosts=localhost:11222
quarkus.infinispan-client.username=admin
quarkus.infinispan-client.password=password
quarkus.infinispan-client.client-intelligence=BASIC
## NYC Backup
quarkus.infinispan-client.backup-cluster.nyc.hosts=localhost:31222
```

```bash
podman compose up 
```

```bash
./loopPutData.sh
```

* Montrer Console
* Montrer Surveillance
* 
## Surveillance

Console Jaeger

http://localhost:16686/


