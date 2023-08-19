package com.asg.ackmsg01;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class Reservation {
    public static void main(String[] args) {
         try {
            InitialContext initContext = new InitialContext();
            Queue ackmsgQ = (Queue) initContext.lookup("queue/ackmsgQueue");

            try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://localhost:61616", "admin",
                    "admin");
                    JMSContext jmsContext = cf.createContext(JMSContext.SESSION_TRANSACTED)) {

                JMSConsumer consumer = jmsContext.createConsumer(ackmsgQ);
                TextMessage msg = (TextMessage) consumer.receive();
                                
                System.out.println("Receiving : " + msg.getText());
                // msg.acknowledge();

                jmsContext.commit();

            } catch (JMSException e) {
                e.printStackTrace();
            }

        } catch (NamingException e) {
            e.printStackTrace();

        }
    }
    
}
