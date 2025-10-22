import java.util.*;

// Menu Item (Linked List Node)
class MenuItem {
    String name;
    double price;
    MenuItem next;

    MenuItem(String name, double price) {
        this.name = name;
        this.price = price;
        this.next = null;
    }
}

// Order Class
class Order {
    String restaurant;
    List<String> items;
    double totalAmount;
    String orderId;
    int deliveryTime; // in minutes

    Order(String restaurant, List<String> items, double totalAmount, String orderId, int deliveryTime) {
        this.restaurant = restaurant;
        this.items = items;
        this.totalAmount = totalAmount;
        this.orderId = orderId;
        this.deliveryTime = deliveryTime;
    }
}

public class OnlineFoodOrderingSystem {
    static HashMap<String, MenuItem> restaurantMenus = new HashMap<>();
    static Queue<Order> orderQueue = new LinkedList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        initializeRestaurants();

        int choice;
        do {
            System.out.println("\n=== ONLINE FOOD ORDERING SYSTEM ===");
            System.out.println("1. View Restaurants");
            System.out.println("2. Place an Order");
            System.out.println("3. Process Next Order");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> viewRestaurants();
                case 2 -> placeOrder();
                case 3 -> processNextOrder();
                case 4 -> System.out.println("Thank you for using Mini Swiggy!");
                default -> System.out.println("Invalid choice! Please try again.");
            }
        } while (choice != 4);
    }

    // Initialize restaurant menus using Linked Lists
    static void initializeRestaurants() {
        addRestaurant("KFC", new String[]{"Zinger Burger", "Popcorn Chicken", "French Fries"},
                new double[]{150.0, 120.0, 80.0});

        addRestaurant("Dominos", new String[]{"Margherita Pizza", "Farmhouse Pizza", "Garlic Bread"},
                new double[]{200.0, 250.0, 100.0});

        addRestaurant("Starbucks", new String[]{"Cold Coffee", "Cappuccino", "Brownie"},
                new double[]{180.0, 160.0, 120.0});
    }

    static void addRestaurant(String name, String[] items, double[] prices) {
        MenuItem head = new MenuItem(items[0], prices[0]);
        MenuItem current = head;
        for (int i = 1; i < items.length; i++) {
            current.next = new MenuItem(items[i], prices[i]);
            current = current.next;
        }
        restaurantMenus.put(name, head);
    }

    // View Restaurants
    static void viewRestaurants() {
        System.out.println("\nAvailable Restaurants:");
        for (String name : restaurantMenus.keySet()) {
            System.out.println("- " + name);
        }
    }

    // Place an Order
    static void placeOrder() {
        System.out.print("\nEnter restaurant name: ");
        String restaurant = sc.nextLine();

        if (!restaurantMenus.containsKey(restaurant)) {
            System.out.println("Restaurant not found!");
            return;
        }

        MenuItem menu = restaurantMenus.get(restaurant);
        System.out.println("\n--- " + restaurant + " Menu ---");
        while (menu != null) {
            System.out.println(menu.name + " - Rs." + menu.price);
            menu = menu.next;
        }

        List<String> items = new ArrayList<>();
        double total = 0;
        while (true) {
            System.out.print("Enter item name to add (or 'done' to finish): ");
            String item = sc.nextLine();
            if (item.equalsIgnoreCase("done")) break;

            double price = findPrice(restaurant, item);
            if (price > 0) {
                items.add(item);
                total += price;
                System.out.println(item + " added to cart.");
            } else {
                System.out.println("Item not found in menu!");
            }
        }

        if (items.isEmpty()) {
            System.out.println("No items selected. Order cancelled.");
            return;
        }

        String orderId = generateOrderId();
        int deliveryTime = new Random().nextInt(15) + 15; // 15–30 mins

        orderQueue.add(new Order(restaurant, items, total, orderId, deliveryTime));
        System.out.println("\nOrder placed successfully!");
        System.out.println("Order ID: " + orderId);
        System.out.println("Total Amount: Rs." + total);
        System.out.println("Estimated Delivery Time: " + deliveryTime + " minutes");
    }

    // Find item price
    static double findPrice(String restaurant, String itemName) {
        MenuItem menu = restaurantMenus.get(restaurant);
        while (menu != null) {
            if (menu.name.equalsIgnoreCase(itemName)) return menu.price;
            menu = menu.next;
        }
        return -1;
    }

    // Process next order
    static void processNextOrder() {
        if (orderQueue.isEmpty()) {
            System.out.println("No pending orders!");
            return;
        }

        Order order = orderQueue.poll();
        System.out.println("\nProcessing Order from " + order.restaurant + "...");
        System.out.println("Order ID: " + order.orderId);
        System.out.println("Items: " + order.items);
        System.out.println("Total Amount: Rs." + order.totalAmount);
        System.out.println("Estimated Delivery Time: " + order.deliveryTime + " minutes");

        // Simulate delivery progress
        try {
            System.out.print("Status: Preparing");
            Thread.sleep(1000);
            System.out.print(" -> Out for Delivery");
            Thread.sleep(1000);
            System.out.print(" -> Delivered (Success)\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Generate random order ID
    static String generateOrderId() {
        return "ORD" + (new Random().nextInt(9000) + 1000);
    }
}
