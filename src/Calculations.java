import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class Calculations {

    private static String operators = "+-*/";
    private static String delimiters = "() " + operators;

    static String isError = "";

    private boolean isDelimiter(String token) {
        if (token.length() != 1) return false;
        return isDeliOrOper(token, delimiters);
    }

    private boolean isOperator(String token) {
        if (token.equals("u-")) return true;
        return isDeliOrOper(token, operators);
    }

    private boolean isDeliOrOper(String token, String delimiters) {
        for (int i = 0; i < delimiters.length(); i++) {
            if (token.charAt(0) == delimiters.charAt(i)) return true;
        }
        return false;
    }

    private boolean isFunction(String token) {
        return token.equals("sqrt") || token.equals("cube") || token.equals("pow10");
    }

    private int priority(String token) {
        if (token.equals("(")) return 1;
        if (token.equals("+") || token.equals("-")) return 2;
        if (token.equals("*") || token.equals("/")) return 3;
        return 4;
    }

    List<String> parse(String infix) {
        List<String> postfix = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();

        if (infix.equals("") || (infix == null)) {
            isError = "Введено пустое выражение";
            return postfix;
        }

        StringTokenizer tokenizer = new StringTokenizer(infix, delimiters, true);
        String prev = "";
        String curr = "";
        while (tokenizer.hasMoreTokens()) {
            curr = tokenizer.nextToken();
            if (!tokenizer.hasMoreTokens() && isOperator(curr)) {
                isError = "Введено некорректное выражение";
                return postfix;
            }
            if (curr.equals(" ")) continue;
            if (isFunction(curr)) stack.push(curr);
            else if (isDelimiter(curr)) {
                if (curr.equals("(")) stack.push(curr);
                else if (curr.equals(")")) {
                    while (!stack.peek().equals("(")) {
                        postfix.add(stack.pop());
                        if (stack.isEmpty()) {
                            isError = "Скобки не согласованы";
                            return postfix;
                        }
                    }
                    stack.pop();
                    if (!stack.isEmpty() && isFunction(stack.peek())) {
                        postfix.add(stack.pop());
                    }
                }
                else {
                    if (curr.equals("-") && (prev.equals("") || (isDelimiter(prev)  && !prev.equals(")")))) {
                        // унарный минус
                        curr = "u-";
                    }
                    else {
                        while (!stack.isEmpty() && (priority(curr) <= priority(stack.peek()))) {
                            postfix.add(stack.pop());
                        }
                    }
                    stack.push(curr);
                }
            }
            else {
                postfix.add(curr);
            }
            prev = curr;
        }

        while (!stack.isEmpty()) {
            if (isOperator(stack.peek())) postfix.add(stack.pop());
            else {
                isError = "Скобки не согласованы";
                return postfix;
            }
        }
        return postfix;
    }

    String calculate(List<String> postfix) {
        Deque<Double> stack = new ArrayDeque<>();
        for (String x : postfix) {
            try {
                switch (x) {
                    case "sqrt":
                        stack.push(Math.sqrt(stack.pop()));
                        break;
                    case "cube":
                        Double tmp = stack.pop();
                        stack.push(tmp * tmp * tmp);
                        break;
                    case "pow10":
                        stack.push(Math.pow(10, stack.pop()));
                        break;
                    case "+":
                        stack.push(stack.pop() + stack.pop());
                        break;
                    case "-": {
                        Double b = stack.pop(), a = stack.pop();
                        stack.push(a - b);
                        break;
                    }
                    case "*":
                        stack.push(stack.pop() * stack.pop());
                        break;
                    case "/": {
                        Double b = stack.pop(), a = stack.pop();
                        stack.push(a / b);
                        break;
                    }
                    case "u-":
                        stack.push(-stack.pop());
                        break;
                    default:
                        stack.push(Double.valueOf(x));
                        break;
                }
            } catch (Exception e) {
                return "Введено некорректное выражение";
            }
        }
        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        DecimalFormat df = new DecimalFormat("#.####", dfs);
        return df.format(stack.pop());
    }
}
