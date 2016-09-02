package com.company;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.List;

/**
 * Created by 52947 on 26-08-2016.
 */
public class OrderLoader {
    public List<Order> load() throws JAXBException {
        JAXBContext orderContext = JAXBContext.newInstance(OrderCollection.class);
        Unmarshaller orderUnmarshaller = orderContext.createUnmarshaller();
        OrderCollection orderCollection = (OrderCollection) orderUnmarshaller.unmarshal( new File(".\\file.xml") );

        return orderCollection.getOrders();
    }
}
