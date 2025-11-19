# kafka-microservice-ecommerce

# TODO:

Текущие задачи:
- Сделать DLQ
- Сделать TransactionalOutbox
- Добавить Redis
- Сделать механизм кеширования
- Отрефакторить все конфиги и application.yml

Реализовать:
- API Gateway + Vault + Circuit Breaker (подправить Swagger)
- Spring Eureka
- Spring Cloud Config
- Logger
- JUnit + Mockito

# Что представляет собой проект?

1. Kafgen
- REST: принимает запросы и генерирует рандомные "сырые" заказы
- Kafka: producer отправляет заказы в orders.incoming

2. Ingestion Service
- Kafka: consumer слушает orders.incoming
- MongoDB: сохраняет сырые заказы
- REST: вызывает Order Processing Service
- - Реализует Dead Letter Queue (Topic "orders.incoming.failed) в случае неудачной обработки
- - Эндпоинты: получения "сырого" заказа из MongoDB (Redis)

3. Processing Service
- REST: принимает запросы от Ingestion Service
- PostgresSQL: сохраняет заказы
- Kafka: producer публикует обработанные заказы в orders.processed
- - Реализует TransactionalOutbox для обработки REST запроса
- - Эндпоинты: получение обработанного заказа из PostgresSQL (Redis)

## Компоненты

1. Kafka Topics:
- orders.incoming используется для входящих "сырых" заказов
- orders.incoming.failed используется в качестве очереди в DLQ
- orders.processed используется для выходных данных обработанных заказов

2. Базы данных:
- MongoDB: хранит все входящие заказы ("сырые" данные)
- PostgresSQL: хранит все обработанные заказы и outbox
- Redis: кеширует запросы на получение "сырых" и обработанных заказов
