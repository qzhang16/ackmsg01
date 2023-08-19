package com.asg.ackmsg01;

import java.util.concurrent.CountDownLatch;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class AppCons implements MessageListener {
    private static final CountDownLatch latch = new CountDownLatch(1);

    @Override
    public void onMessage(Message message) {
        try {
            TextMessage msg = (TextMessage) message;
            
            System.out.println(msg.getText());
            msg.acknowledge();
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            latch.countDown();
        }


    }

    public static void main(String[] args) {
        try {
            InitialContext initContext = new InitialContext();
            Queue ackmsgQ = (Queue) initContext.lookup("queue/ackmsgQueue");

            try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://localhost:61616", "admin",
                    "admin");
                    JMSContext jmsContext = cf.createContext(JMSContext.CLIENT_ACKNOWLEDGE)) {

                // JMSProducer producer = jmsContext.createProducer();
                // Random rand01 = new Random(100);
                // producer.send(ackmsgQ, "number " + rand01.nextInt(10));
                JMSConsumer consumer = jmsContext.createConsumer(ackmsgQ);
                consumer.setMessageListener(new AppCons());

                latch.await();
                
            } 

            

        } catch (NamingException | InterruptedException e) {
            e.printStackTrace();

        }
    }

}
