import java.util.Scanner;
import java.util.ArrayList;

public class FinancialHealth {

    // storing history here for now, maybe ill add a file later
    static ArrayList<String> history = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("Welcome to the Financial Health Calculator");

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("  1. Calculate my financial health");
            System.out.println("  2. See past results");
            System.out.println("  3. Exit");
            System.out.print("\nPick an option: ");

            String option = sc.nextLine().trim();

            if (option.equals("1")) {
                runCalc();
            } else if (option.equals("2")) {
                showHistory();
            } else if (option.equals("3")) {
                System.out.println("Goodbye!");
                break;
            } else {
                System.out.println("Pick 1, 2 or 3");
            }
        }
    }

    static void runCalc() {
        System.out.println("\n-- Financial Health Calculator --\n");
        System.out.print("Your name: ");
        String name = sc.nextLine().trim();

        System.out.println("\n-- Monthly Figures --\n");
        double income = getNum("Monthly income: R");
        double expenses = getNum("Monthly expenses (rent, food, bills): R");
        double savings = getNum("Monthly savings: R");
        double repayment = getNum("Monthly debt repayment: R");

        System.out.println("\n-- Debt and Assets --\n");
        double totalDebt = getNum("Total overall debt (home loan, car, cards etc): R");
        double totalAssets = getNum("Total assets (property, car, investments, cash): R");

        // net worth is just what you own minus what you owe
        double netWorth = totalAssets - totalDebt;

        String rating = getRating(income, expenses, savings, repayment, totalDebt, totalAssets);

        System.out.println("\nFinancial Health Rating for " + name + ": " + rating);

        showBreakdown(income, expenses, savings, repayment, totalDebt, totalAssets, netWorth);
        showTips(income, expenses, savings, repayment, netWorth);

        history.add(name + " | Income: R" + (int)income + " | Net Worth: R" + (int)netWorth + " | Rating: " + rating);
    }

    // keep asking until we get a valid number
    static double getNum(String msg) {
        while (true) {
            System.out.print(msg);
            try {
                double val = Double.parseDouble(sc.nextLine().trim());
                if (val < 0) {
                    System.out.println("Please enter a positive number");
                } else {
                    return val;
                }
            } catch (NumberFormatException e) {
                System.out.println("Thats not a number, try again");
            }
        }
    }

    static String getRating(double income, double expenses, double savings, double repayment, double totalDebt, double totalAssets) {
        int score = 0;

        // savings check
        double sRate = income > 0 ? (savings / income) * 100 : 0;
        if (sRate >= 20) score += 3;
        else if (sRate >= 10) score += 2;
        else if (sRate >= 5) score += 1;

        // expense check
        double eRate = income > 0 ? (expenses / income) * 100 : 100;
        if (eRate <= 50) score += 3;
        else if (eRate <= 70) score += 2;
        else if (eRate <= 90) score += 1;

        // debt repayment check
        double rRate = income > 0 ? (repayment / income) * 100 : 100;
        if (repayment == 0) score += 2;
        else if (rRate <= 20) score += 2;
        else if (rRate <= 40) score += 1;

        // net worth check
        double nw = totalAssets - totalDebt;
        if (nw > 0) score += 2;
        else if (nw == 0) score += 1;

        // do they have anything left over
        if (income - expenses > 0) score += 1;

        if (score >= 9) return "Excellent";
        else if (score >= 6) return "Good";
        else if (score >= 3) return "Fair";
        else return "Poor";
    }

    static void showBreakdown(double income, double expenses, double savings, double repayment, double totalDebt, double totalAssets, double netWorth) {

        System.out.println("\n-- Where Your Money Goes --\n");

        if (income > 0) {
            int ePct = (int) Math.round((expenses / income) * 100);
            int sPct = (int) Math.round((savings / income) * 100);
            int rPct = (int) Math.round((repayment / income) * 100);
            double left = income - expenses;
            int lPct = (int) Math.round((left / income) * 100);

            System.out.println("  Monthly:");
            System.out.printf("    Income:          R%,.2f%n", income);
            System.out.printf("    Expenses:        R%,.2f (%d%% of income)%n", expenses, ePct);
            System.out.printf("    Savings:         R%,.2f (%d%% of income)%n", savings, sPct);
            System.out.printf("    Debt repayment:  R%,.2f (%d%% of income)%n", repayment, rPct);

            if (left >= 0) {
                System.out.printf("    Left over:       R%,.2f (%d%% of income)%n", left, lPct);
            } else {
                System.out.printf("    Shortfall:       R%,.2f (spending more than you earn!)%n", Math.abs(left));
            }
        }

        System.out.println();
        System.out.println("  Overall:");
        System.out.printf("    Total assets:    R%,.2f%n", totalAssets);
        System.out.printf("    Total debt:      R%,.2f%n", totalDebt);

        if (netWorth >= 0) {
            System.out.printf("    Net worth:       R%,.2f (positive)%n", netWorth);
        } else {
            System.out.printf("    Net worth:       -R%,.2f (negative, debt is more than assets)%n", Math.abs(netWorth));
        }
    }

    static void showTips(double income, double expenses, double savings, double repayment, double netWorth) {
        System.out.println("\n-- Tips For You --\n");

        if (income > 0) {
            double sRate = (savings / income) * 100;
            double eRate = (expenses / income) * 100;
            double rRate = (repayment / income) * 100;
            double left = income - expenses;

            if (sRate < 10) System.out.println("  - Try to save at least 10% of your income every month");
            if (sRate >= 20) System.out.println("  - Great savings rate, keep it up");
            if (eRate > 70) System.out.println("  - Your expenses are quite high, look for areas to cut back");
            if (eRate <= 50) System.out.println("  - Good job keeping expenses low");
            if (rRate > 40) System.out.println("  - Over 40% of income goes to debt repayment, try pay it off faster");
            if (repayment == 0) System.out.println("  - No debt repayments is great, keep it that way");
            if (left < 0) System.out.println("  - You are spending more than you earn, this needs urgent attention");
            else if (left < income * 0.1) System.out.println("  - Not much left over at end of month, try reduce expenses");
        }

        if (netWorth < 0) {
            System.out.println("  - Net worth is negative, focus on paying down debt and building assets");
        } else if (netWorth == 0) {
            System.out.println("  - Net worth is zero, start building assets to get ahead");
        } else {
            System.out.println("  - Positive net worth is good, keep growing your assets");
        }
    }

    static void showHistory() {
        if (history.isEmpty()) {
            System.out.println("No records yet");
            return;
        }
        System.out.println("\n-- Past Results --\n");
        for (String r : history) {
            System.out.println("  " + r);
        }
    }
}
