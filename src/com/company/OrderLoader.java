package com.company;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 52947 on 26-08-2016.
 */
public class OrderLoader {
    public List<Order> load() throws JAXBException {
        OrderCollection orderCollection = new OrderCollection();
        JAXBContext orderContext = JAXBContext.newInstance(OrderCollection.class);
        Unmarshaller orderUnmarshaller = orderContext.createUnmarshaller();
        File xmlFile = new File("C:\\Orders\\orders.xml");
        if(xmlFile.exists())
            orderCollection = (OrderCollection) orderUnmarshaller.unmarshal(xmlFile);
        else
            orderCollection.setOrders(new ArrayList<>());
        return orderCollection.getOrders();
    }
}
