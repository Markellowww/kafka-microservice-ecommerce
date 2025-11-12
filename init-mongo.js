db.createUser({
    user: "mongo",
    pwd: "mongo",
    roles: [
        { role: "readWrite", db: "raw_orders" },
        { role: "dbAdmin", db: "raw_orders" }
    ]
});

db.getSiblingDB("raw_orders").createCollection("initial");
db.getSiblingDB("raw_orders").initial.insertOne({ status: "created" });