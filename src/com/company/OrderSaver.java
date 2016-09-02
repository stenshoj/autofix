package com.company;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.List;

/**
 * Created by 52947 on 26-08-2016.
 */
public class OrderSaver {
    public void save(List<Order> orderList){
        try {
            File file = new File(".\\file.xml");
            JAXBContext orderContext = JAXBContext.newInstance(OrderCollection.class);
            Marshaller orderMarshaller = orderContext.createMarshaller();
            OrderCollection orderCollection = new OrderCollection();

            orderCollection.setOrders(orderList);

            // output pretty printed
            orderMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            orderMarshaller.marshal(orderCollection, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }

    }
}

