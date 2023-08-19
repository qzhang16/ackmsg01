package com.asg.ackmsg01;

import java.util.Random;

import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Queue;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

public class Checkin {
    public static void main(String[] args) {
         try {
            InitialContext initContext = new InitialContext();
            Queue ackmsgQ = (Queue) initContext.lookup("queue/ackmsgQueue");

            try (ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory("tcp://localhost:61616", "admin",
                    "admin");
                    JMSContext jmsContext = cf.createContext(JMSContext.SESSION_TRANSACTED)) {

                JMSProducer producer = jmsContext.createProducer();
                Random rand01 = new Random(100);
                TextMessage msg = jmsContext.createTextMessage("number " + rand01.nextInt(10));
                producer.send(ackmsgQ, msg);
                System.out.println("Sending : " + msg.getText());

                msg = jmsContext.createTextMessage("number " + rand01.nextInt(10));
                producer.send(ackmsgQ, msg);
                System.out.println("Sending : " + msg.getText());

                jmsContext.commit();

                msg = jmsContext.createTextMessage("number " + rand01.nextInt(10));
                producer.send(ackmsgQ, msg);
                System.out.println("Sending : " + msg.getText());

                jmsContext.rollback();


            } catch (JMSException e) {
                e.printStackTrace();
            }

        } catch (NamingException e) {
            e.printStackTrace();

        }
    }
    
}
