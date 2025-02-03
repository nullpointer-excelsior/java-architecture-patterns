# Java architecture patterns

Arquitecturas y patrones de diseño utilizados en aplicaciones empresariales con Java vanilla.

## Eventsourcing

Event Sourcing es un patrón arquitectónico en el que los cambios de estado de una aplicación se almacenan como una secuencia de eventos inmutables, en lugar de almacenar solo el estado actual. Esto permite una trazabilidad completa y la posibilidad de reconstruir el estado de la aplicación en cualquier momento a partir de los eventos.

### Ejemplo basado en la creación de órdenes de compra

Implementación sencilla de event sourcing con los principales conceptos en el siguiente [ejemplo en código](app/src/main/java/com/benjamin/eventsourcing).
## CQRS (Command Query Responsibility Segregation)

CQRS es un patrón arquitectónico que separa las operaciones de lectura (Query) de las operaciones de escritura (Command) dentro de una aplicación. Esta separación permite optimizar cada una de estas operaciones de forma independiente, mejorando la escalabilidad y el rendimiento del sistema.
En aplicaciones con alta concurrencia o grandes volúmenes de datos, CQRS ayuda a evitar bloqueos en la base de datos y facilita la sincronización entre modelos de lectura y escritura a través de mecanismos como **CDC, eventos de integración, réplicas de lectura y vistas materializadas**.

### Ejemplo basado en la creación de reseña de productos

La implementación de un sistema de reseñas utilizando CQRS incluye una implementación concurrente de `CommandBus` y `QueryBus`. [ejemplo](app/src/main/java/com/benjamin/cqrs)

## Ejecución de test unitarios

```shell
#!/bin/bash

gradle test 

```

### Autor:
**Benjamín**