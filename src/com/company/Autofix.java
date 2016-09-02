package com.company;

import dnl.utils.text.table.TextTable;
import javax.xml.bind.JAXBException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Autofix {

    static List<Order> orderList = new ArrayList<>();
    static Menu mainMenu = new Menu();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws JAXBException {
        OrderSaver saver = new OrderSaver();
        OrderLoader loader = new OrderLoader();
        orderList = loader.load();
        mainMenu.add("List orders", () -> listOrders(orderList));
        mainMenu.add("Search orders by customer name", Autofix::searchOrders);
        mainMenu.add("Finish order", Autofix::finishOrder);
        mainMenu.add("Create order", Autofix::createOrder);
        mainMenu.add("Delete order", Autofix::removeOrder);
        mainMenu.add("Quit", () -> {
            saver.save(orderList);
            System.exit(0);
        });

        try {
            while (true) {
                mainMenu.show();
                System.out.println("\n");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private static void finishOrder() {
        getUserInput(() -> {
            List<Order> filteredList = orderList.stream().filter(order -> !order.isPaid()).collect(Collectors.toList());
            if (filteredList.size() < 1)
                System.out.println("All orders are finished");
            else {
                System.out.println("Please choose the order you want to finish");
                Order orderToFinish = getOrderFromList(filteredList);
                if (!orderToFinish.isPaid()) {
                    orderToFinish.setPaid(true);
                    System.out.println("\nThe order is now finished");
                } else
                    System.out.println("The order has already been paid");
            }
        });
    }

    private static void searchOrders() {
        System.out.println("Please enter the name you want to search for:");
        Scanner scanner = new Scanner(System.in);
        String searchWord = scanner.nextLine().toUpperCase();
        List<Order> searchedOrderList;
        searchedOrderList = orderList.stream().filter(order -> order.getCustomer().getName().toUpperCase().contains(searchWord)).collect(Collectors.toList());
        if(searchedOrderList.size() > 0)
            listOrders(searchedOrderList);
        else
            System.out.println("No orders were found, with the provided customer name");
    }

    private static void listOrders(List<Order> orderList) {
        if(orderList.size() < 1) {
            System.out.println("There are no orders to show");
        }
        else {
            TextTable tt = getTextTable(orderList);
            tt.printTable();
        }
    }

    /**
     * package source: https://code.google.com/archive/p/j-text-utils/wikis/UsingTextTable.wiki
     */
    private static TextTable getTextTable(List<Order> orderList) {
        String[] orderColumnTitles = {
                "Customer Name",
                "Customer Address",
                "Customer Phonenumber",
                "Car Make",
                "Car Model",
                "Car RegNo",
                "Issue",
                "OrderDate",
                "Price",
                "Paid?"
        };
        Object[][] orders = new Object[orderList.size()][];
        for(int i = 0; i < orderList.size(); i++ ){
            Order order = orderList.get(i);
            orders[i] = new Object[]{
                    order.getCustomer().getName(),
                    order.getCustomer().getAddress(),
                    order.getCustomer().getPhoneNo(),
                    order.getCar().getMake(),
                    order.getCar().getModel(),
                    order.getCar().getRegNo(),
                    order.getIssue(),
                    order.getOrderDate(),
                    order.getPrice(),
                    order.isPaid()};
        }
        TextTable tt = new TextTable(orderColumnTitles, orders);
        tt.setAddRowNumbering(true);
        return tt;
    }

    private static void removeOrder() {
        getUserInput(() -> {
            System.out.println("Please choose the order you want to delete");
            Order orderToRemove = getOrderFromList(orderList);
            orderList.remove(orderToRemove);
            System.out.println("\nOrder was successfully removed");
        });
    }

    private static Order getOrderFromList(List<Order> orderList) throws Exception {
        listOrders(orderList);
        Scanner scanner = new Scanner(System.in);
        int input = scanner.nextInt();
        if(input > orderList.size())
            throw new Exception("Please enter a valid order");
        return orderList.get(input - 1);
    }

    private static void createOrder() {
        OrderFactory orderFactory = new OrderFactory();

        Order order = orderFactory.create();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");

        order.setOrderDate(formatter.format(new Date()));
        System.out.println("Customer details:");
        getUserInput(() -> getNameInput(order));

        getUserInput(() -> getPhoneNoInput(order));

        getUserInput(() -> getAddressInput(order));

        System.out.println("----------------------");
        System.out.println("Car details:");

        getUserInput(() -> getMakeInput(order));

        getUserInput(() -> getModelInput(order));

        getUserInput(() -> getRegNoInput(order));

        System.out.println("----------------------");
        System.out.println("Order details:");

        getUserInput(() -> getIssueInput(order));

        getUserInput(() -> getPriceInput(order));

        orderList.add(order);
        System.out.println("\nThe order is now created");
    }

    private static void getPriceInput(Order order) throws Exception {
        System.out.println("Price:");
        double price = tryToParseDouble(scanner.nextLine());
        if(price < 0) {
            throw new Exception("Price can't be a negative number");
        }
        order.setPrice(price);
    }

    private static void getIssueInput(Order order) throws Exception {
        System.out.println("What is the issue?");
        String issue = scanner.nextLine();
        if (issue.length() == 0 || issue.isEmpty())
            throw new Exception("Issue can't be empty");
        order.setIssue(issue);
    }

    private static void getRegNoInput(Order order) throws Exception {
        System.out.println("Registration number:");
        String regNo = scanner.nextLine();
        if (regNo.length() == 0 || regNo.isEmpty())
            throw new Exception("Registration number can't be empty");
        if(!regNo.toUpperCase().matches("^[A-Z]{2}[0-9]{5}$"))
            throw new Exception("The entered value, is not a valid registration number");
        order.getCar().setRegNo(regNo.toUpperCase());
    }

    private static void getModelInput(Order order) throws Exception {
        System.out.println("Model:");
        String model = scanner.nextLine();
        if (model.length() == 0 || model.isEmpty())
            throw new Exception("Model can't be empty");
        order.getCar().setModel(model);
    }

    private static void getMakeInput(Order order) throws Exception {
        System.out.println("Make:");
        String make = scanner.nextLine();
        if (make.length() == 0 || make.isEmpty())
            throw new Exception("Make can't be empty");
        order.getCar().setMake(make);
    }

    private static void getAddressInput(Order order) throws Exception {
        System.out.println("Address:");
        String address = scanner.nextLine();
        if (address.length() == 0 || address.isEmpty())
            throw new Exception("Address can't be empty");
        order.getCustomer().setAddress(address);
    }

    private static void getPhoneNoInput(Order order) throws Exception {
        System.out.println("Phone number:");
        String phoneNo = scanner.nextLine();
        if (phoneNo.length() != 8)
            throw new Exception("Phonenumber must be 8 digits");
        if (!phoneNo.matches("[0-9]+"))
            throw new Exception("Phonenumber must only be numbers");
        order.getCustomer().setPhoneNo(phoneNo);
    }

    private static void getNameInput(Order order) throws Exception {
        System.out.println("Name:");
        String name = scanner.nextLine();
        if (name.length() == 0 || name.isEmpty())
            throw new Exception("Name can't be empty");
        order.getCustomer().setName(name);
    }

    private static double tryToParseDouble(String priceToBeParsed) throws Exception {
        try {
            double price = Double.parseDouble(priceToBeParsed);
            return price;
        } catch (Exception e) {
            if(e.getMessage().contains("empty"))
                throw new Exception("Price can't be empty");
            throw new Exception("Price can only be numbers");
        }

    }

    private static void getUserInput(Callback callback){
        while(true) {
            try {
                callback.invoke();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }
            break;
        }
    }
}
