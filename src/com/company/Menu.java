package com.company;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by 52947 on 29-08-2016.
 * Credit to Michael Nielsen. Seen on codeblog.dk
 */
public class Menu {

    private ArrayList<MenuItem> items = new ArrayList<>();

    private class MenuItem {
        private Callback callback;
        private String text;


        public MenuItem(String text, Callback callback) {
            this.callback = callback;
            this.text = text;
        }

        public Callback getCallback() {
            return callback;
        }

        public String getText() {
            return text;
        }
    }

    public boolean add(String text, Callback callback) {
        return items.add(new MenuItem(text, callback));
    }

    public void show() throws Exception {
        int chosen = 0;
        Scanner in = new Scanner(System.in);

        for (int i = 0; i < items.size(); ++i) {
            MenuItem menuItem = items.get(i);
            System.out.printf("[%d] %s \n", i + 1, menuItem.getText());
        }

        System.out.println();

        try {
            chosen = in.nextInt();
        } catch (Exception ex) { }

        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
            {
                Runtime.getRuntime().exec("cls");
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (final Exception e) { }

        if (chosen > items.size() || chosen < 1) {
            System.out.println("Invalid option.\nPress enter to continue...");
            in.nextLine();
            in.nextLine();
        } else {
            MenuItem menuItem = items.get(chosen - 1);
            Callback callback = menuItem.getCallback();
            callback.invoke();
        }
    }
}
