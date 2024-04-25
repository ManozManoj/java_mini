    import java.util.ArrayList;
    import java.util.HashMap;
    import java.util.InputMismatchException;
    import java.util.Scanner;

    abstract class LibraryItem {
        protected String title;
        protected String author;

        public LibraryItem(String title, String author) {
            this.title = title;
            this.author = author;
        }

        public String getTitle() {
            return title;
        }

        public String getAuthor() {
            return author;
        }

        public abstract void displayDetails();
    }

    class Book extends LibraryItem {
        private int availableCopies;

        public Book(String title, String author, int availableCopies) {
            super(title, author);
            this.availableCopies = availableCopies;
        }

        public int getAvailableCopies() {
            return availableCopies;
        }

        public void decreaseAvailableCopies() {
            availableCopies--;
        }

        public void increaseAvailableCopies() {
            availableCopies++;
        }

        @Override
        public void displayDetails() {
            System.out.println("Book Title: " + title);
            System.out.println("Author: " + author);
            System.out.println("Available Copies: " + availableCopies);
        }
    }

    class Member {
        private String name;
        private String memberId;

        public Member(String name, String memberId) {
            this.name = name;
            this.memberId = memberId;
        }

        public String getName() {
            return name;
        }

        public String getMemberId() {
            return memberId;
        }

        public void displayDetails() {
            System.out.println("Member Name: " + name);
            System.out.println("Member ID: " + memberId);
        }
    }

    class Transaction {
        private LibraryItem item;
        private Member member;
        private String issueDate;
        private String dueDate;

        public Transaction(LibraryItem item, Member member, String issueDate, String dueDate) {
            this.item = item;
            this.member = member;
            this.issueDate = issueDate;
            this.dueDate = dueDate;
        }

        public void displayTransactionDetails() {
            System.out.println("Transaction Details:");
            item.displayDetails();
            member.displayDetails();
            System.out.println("Issue Date: " + issueDate);
            System.out.println("Due Date: " + dueDate);
        }
    }

    public class Main {
        private ArrayList<LibraryItem> items = new ArrayList<>();
        private HashMap<String, Member> members = new HashMap<>();
        private ArrayList<Transaction> transactions = new ArrayList<>();

        public static void main(String[] args) {
            Main library = new Main();
            library.start();
        }

        public void start() {
            Scanner scanner = new Scanner(System.in);
            int choice;
            do {
                System.out.println("Library Management System");
                System.out.println("1. Add Book");
                System.out.println("2. Add Member");
                System.out.println("3. Borrow Book");
                System.out.println("4. Return Book");
                System.out.println("5. Print All Details");
                System.out.println("6. Print All Transactions");
                System.out.println("7. Exit");
                System.out.print("Enter your choice: ");
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline

                    switch (choice) {
                        case 1:
                            addBook(scanner);
                            break;
                        case 2:
                            addMember(scanner);
                            break;
                        case 3:
                            borrowItem(scanner);
                            break;
                        case 4:
                            returnItem(scanner);
                            break;
                        case 5:
                            printAllDetails();
                            break;
                        case 6:
                            printAllTransactions();
                            break;
                        case 7:
                            System.out.println("Exiting...");
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.nextLine();
                    choice = -1;
                }
            } while (choice != 7);

            scanner.close();
        }

        private void addBook(Scanner scanner) {
            System.out.println("Enter Book Details:");
            System.out.print("Title: ");
            String title = scanner.nextLine();
            System.out.print("Author: ");
            String author = scanner.nextLine();
            System.out.print("Available Copies: ");
            int availableCopies = scanner.nextInt();
            scanner.nextLine();
            Book book = new Book(title, author, availableCopies);
            items.add(book);
            System.out.println("Book added successfully!");
        }

        private void addMember(Scanner scanner) {
            System.out.println("Enter Member Details:");
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Member ID: ");
            String memberId = scanner.nextLine();
            Member member = new Member(name, memberId);
            members.put(memberId, member);
            System.out.println("Member added successfully!");
        }

        private void borrowItem(Scanner scanner) {
            System.out.print("Enter Member ID: ");
            String memberId = scanner.nextLine();
            Member member = members.get(memberId);
            if (member == null) {
                System.out.println("Member not found!");
                return;
            }

            System.out.println("Available Items:");
            for (int i = 0; i < items.size(); i++) {
                LibraryItem item = items.get(i);
                System.out.print((i + 1) + ". ");
                item.displayDetails();
            }

            System.out.print("Enter Item Number to Borrow: ");
            try {
                int itemIndex = scanner.nextInt() - 1;
                scanner.nextLine();
                if (itemIndex < 0 || itemIndex >= items.size()) {
                    System.out.println("Invalid item number!");
                    return;
                }

                LibraryItem selectedItem = items.get(itemIndex);
                if (selectedItem instanceof Book) {
                    Book selectedBook = (Book) selectedItem;
                    if (selectedBook.getAvailableCopies() == 0) {
                        System.out.println("Sorry, the book is not available.");
                        return;
                    }
                    selectedBook.decreaseAvailableCopies();
                }

                String issueDate = "Today"; // You can use the actual date here
                String dueDate = "One week from today"; // You can calculate the due date based on the issue date
                Transaction transaction = new Transaction(selectedItem, member, issueDate, dueDate);
                transactions.add(transaction); // Add transaction to the list
                transaction.displayTransactionDetails();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input from the scanner
            }
        }

        private void returnItem(Scanner scanner) {
            System.out.print("Enter Member ID: ");
            String memberId = scanner.nextLine();
            Member member = members.get(memberId);
            if (member == null) {
                System.out.println("Member not found!");
                return;
            }

            System.out.print("Enter Item Title to Return: ");
            String itemTitle = scanner.nextLine();
            for (LibraryItem item : items) {
                if (item.getTitle().equalsIgnoreCase(itemTitle)) {
                    if (item instanceof Book) {
                        Book book = (Book) item;
                        book.increaseAvailableCopies();
                    }
                    System.out.println("Item returned successfully!");
                    return;
                }
            }

            System.out.println("Item not found!");
        }

        private void printAllDetails() {
            System.out.println("Items:");
            for (LibraryItem item : items) {
                item.displayDetails();
                System.out.println();
            }

            System.out.println("Members:");
            for (Member member : members.values()) {
                member.displayDetails();
                System.out.println();
            }
        }

        private void printAllTransactions() {
            System.out.println("All Transactions:");
            for (Transaction transaction : transactions) {
                transaction.displayTransactionDetails();
                System.out.println();
            }
        }
    }
