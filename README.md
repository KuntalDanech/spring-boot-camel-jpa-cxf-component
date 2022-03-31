# spring-boot-camel-jpa-cxf-component
We can make our DB a queue that processes the data in a FIFO manner. We can use this with the help of the Apache Camel JPA component.

I have created Apache Camel JPA and CXF component with some demo use case for instance, if any row is inserted in DB it will take the inserted record and process it and sends the inserted record to the third party rest API as a request body autonomously.