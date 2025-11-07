# kafka-microservice-ecommerce

# TODO:

## Распределение технологий по микросервисам (посл-ое выполнение)

1. Order Ingestion Service
- Kafka: Consumer (слушает orders.incoming)
- MongoDB: Сохраняет сырые заказы
- REST: Вызывает Order Processing Service

2. Order Processing Service
- REST: Принимает запросы от Ingestion Service
- Redis: Кеширует срочные заказы (TTL = 5 мин)
- REST: Вызывает Order Fulfillment Service

3. Order Fulfillment Service
- REST: Принимает запросы от Processing Service
- PostgreSQL: Сохраняет готовые заказы
- Kafka: Producer (публикует в orders.processed)

## Компоненты

1. Kafka Topics:
- orders.incoming <- UI/внешние системы
- orders.processed -> UI/email-service/analytics

2. Базы данных:
- MongoDB: Все входящие заказы (сырые данные)
- Redis: Кеш срочных заказов + кеш цен/акций
- PostgreSQL: Обработанные заказы (готовые к отгрузке)
