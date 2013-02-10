package com.hascode.tutorial
import org.apache.camel.scala.dsl.builder.RouteBuilder
import org.apache.camel.CamelContext
import org.apache.camel.impl.DefaultCamelContext
import javax.jms.ConnectionFactory
import org.apache.camel.component.jms.JmsComponent
import org.apache.activemq.ActiveMQConnectionFactory
import org.apache.camel.Processor
import org.apache.camel.scala.dsl.builder.RouteBuilderSupport

object LoadBalancerExample extends App with RouteBuilderSupport {
  val context: CamelContext = new DefaultCamelContext
  val connectionFactory: ConnectionFactory = new ActiveMQConnectionFactory("vm://localhost")
  context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory))

  val routeBuilder = new RouteBuilder {
    from("jms:incoming").loadBalance().
      roundRobin().to("jms:worker-queue-1", "jms:worker-queue-2", "jms:worker-queue-3");

    from("jms:worker-queue-1")
      .process(exchange => println("Handler 1 recevied a message with number flag: " + exchange.getIn().getHeader("my-number")))
    from("jms:worker-queue-2")
      .process(exchange => println("Handler 2 recevied a message with number flag: " + exchange.getIn().getHeader("my-number")))
    from("jms:worker-queue-3")
      .process(exchange => println("Handler 3 recevied a message with number flag: " + exchange.getIn().getHeader("my-number")))
  }
  context.addRoutes(routeBuilder)
  context.start

  Thread.sleep(2000)
  val tpl = context.createProducerTemplate
  tpl.setDefaultEndpointUri("jms:incoming")
  tpl.sendBodyAndHeader("foo1", "my-number", 1)
  tpl.sendBodyAndHeader("foo2", "my-number", 2)
  tpl.sendBodyAndHeader("foo3", "my-number", 3)
  tpl.sendBodyAndHeader("foo4", "my-number", 4)
  tpl.sendBodyAndHeader("foo5", "my-number", 5)
  tpl.sendBodyAndHeader("foo6", "my-number", 6)
  while (true) {}
}

