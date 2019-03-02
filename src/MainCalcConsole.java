import java.util.List;
import java.util.Scanner;

public class MainCalcConsole {
    public static void main (String[] args) {

        Scanner in = new Scanner(System.in);
        String s = in.nextLine();

        Calculations c = new Calculations();
        List<String> postfix = c.parse(s);

        if(Calculations.isError.equals("")) {
            System.out.println(c.calculate(postfix));
        } else {
            System.out.println(Calculations.isError);
        }
    }
}
