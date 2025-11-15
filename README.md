# kafka-microservice-ecommerce

# TODO:

1. Ingestion Service
- Kafka: consumer слушает orders.incoming
- MongoDB: сохраняет сырые заказы
- REST: вызывает Order Processing Service
- - - Реализует Dead Letter Queue (Topic "orders.incoming.failed) в случае неудачной обработки
- - - Эндопоинты: получения "сырого" заказа из MongoDB (Redis)

2. Processing Service
- REST: Принимает запросы от Ingestion Service
- PostgreSQL: Сохраняет заказы
- Kafka: producer публикует обработанные заказы в orders.processed
- - - Реализует TransactionalOutbox для обработка REST запроса
- - - Эндпоинты: получение обработанного заказа из PostgreSQL (Redis)

## Компоненты

1. Kafka Topics:
- orders.incoming используется для входящих "сырых" заказов
- orders.incoming.failed используется в качестве очереди в DLQ
- orders.processed используется для выходных данных обработанных заказов

2. Базы данных:
- MongoDB: Хранит все входящие заказы ("сырые" данные)
- PostgreSQL: Хранит все обработанные заказы и outbox
- Redis: Кеширует запросы на получение "сырых" и обработанных заказов

3. Добавить в выходной Order:
- private String trackingNumber;
- private String assignedWarehouse;
- private Instant estimatedDelivery;