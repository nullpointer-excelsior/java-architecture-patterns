# Java architecture patterns

Arquitecturas y patrones de diseño utilizados en aplicaciones empresariales con Java vanilla.

## Eventsourcing

Event Sourcing es un patrón arquitectónico en el que los cambios de estado de una aplicación se almacenan como una secuencia de eventos inmutables, en lugar de almacenar solo el estado actual. Esto permite una trazabilidad completa y la posibilidad de reconstruir el estado de la aplicación en cualquier momento a partir de los eventos.

### Ejemplo basado en la creación de órdenes de compra

Implementación sencilla de event sourcing con los principales conceptos en el siguiente [ejemplo en código](app/src/main/java/com/benjamin/eventsourcing).

### Autor:
**Benjamín**