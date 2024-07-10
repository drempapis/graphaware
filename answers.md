
-------------------------------------------------------------------
What do you think about the overall architecture of this app?
-------------------------------------------------------------------

The service acts as a facade to DB operations to synchronize the customer-worker flow. It provides an API that could be used by another service to orchestrate the calls, although it would be dangerous to expose it to end clients because of the DB exposure through public calls. The structure of the code follows the Spring separation of Concerns by annotating the different layers with proper annotations.

It follows the typical two-tier architecture, consisting of the rest and data tiers, where the business logic is limited. 
The service is stateful, making it impossible to scale. The PurchaseService has the ongoingPurchases map, which invalidates between restarts.
The scalability of the service is restricted also by the DB capacity. 

The use of JPA speeds up development, connecting the DTO model to the DB model, which is fine for most cases. A drawback is that it carries additional hidden complexity. It is difficult to "tweak" DB queries, and in some cases, it creates and executes more operations than necessary, resulting in performance degradation. This always depends on the case and the implementation.

Regarding the data model, at first glance, it is strange; when updating the purchase_table, the pizza_table is updated in cascade. That makes it impossible for the pizza_table to be used for a menu display. The method PurchaseService:addPizzaToPurchase propagates a Pizza object where no validation is applied, e.g., for values of the ingredients, the price, etc. When finalizing a purchase, at the computeAmount method, there are hardcoded rules, including types and prices.



-------------------------------------------------------------------
What would be the most valuable improvements? (prioritised)
-------------------------------------------------------------------

Revise and sanitize the domain model and relationships between the objects as described in the previous section.

Complete the unit tests and implement integration tests, validating all the possible execution flows. In the current solution, the docker compose-up is required to run the unit tests, which is unsuitable for local development and requires fast execution without initializing external components. This would be suitable when creating integration tests and smoke-testing the interaction between deployed components.

The public rest API does not properly handle exceptions. All Controllers should return the status, preferably encapsulated in a ResponseEntity. When an exception is thrown, the exception is returned to the caller in some cases or nothing in other cases—the HTTP.Status(es) can propagate the state for all instances instead of the exception details.

Use a non-volatile distrib-cache for the PurchaseService:ongoingPurchases. The use of map in the current implementation it is not thread safe.

Update the Transactional using the spring framework equivalent, where the isolation level can be clearly defined. Add the transactional on the service level to apply to all methods (it comes with an extra overhead), or add it only to the methods, including get/save operations using the same method.

Fix code smells, which are identified even in the IDE. 
	For example, I prefer the constructor injection to the field injection, e.g., in PizzaController. It looks minor, but mock testing the class is more challenging. We should depend on abstractions to inject mocks carefully, which could fail without being visible to the user.

	PizzaRepositoryImpl: SQL construction without a builder

Swagger API "documentation" can be easily integrated into the api calls.


-------------------------------------------------------------------
What needs to be added to have the application production ready?
-------------------------------------------------------------------

Work on the improvement section bullets.

There is a configuration that should be externalized but not delivered in the bundled application (jar). Part of the application.properties, e.g., maximum-pool-size, db-connection details, email.properties, etc, should be delivered at deployment time. That depends on the deployment mechanism and can be achieved in various ways,
e.g., a configuration server, ansible gathering, Kubernetes config, etc.

If DB scheme initialization is needed, e.g., running the data.sql, this should be part of the deployment process in the app bootstrap phase.

The authentication role-based system connecting roles to authorities is hardcoded to the application. This should also be externalized, and data should be fetched at runtime.

The discount rules should also be able to be registered through an external configuration.


-------------------------------------------------------------------
Is there any framework, library, or tools that you know would greatly benefit this app
and development?
-------------------------------------------------------------------

The app is designed in a way that requires an orchestrator to define the steps and flows of execution. This makes the service tightly coupled to the database and challenging to scale. 

An alternative approach is using async messages by using a distrib message queue to interchange messages between the involved parties. The customers and workers will be decoupled, where resources can be added on demand, increasing the scalability. There can also be a stateless proxy service to validate the authentication, maintain database updates, and provide a minimal rest API for the end clients. The use of the DB will be minimal, containing primarily static content, e.g., pizzas, users, the rules, and discount rules. Instead of synchronizing in the DB, the available messages will mark the progress, whereas an append-only ledger will store all. 








