package com.soul.math;

import java.util.Stack;
import java.util.regex.Pattern;

public class EvalExpr {
	static boolean rad_f;

	/*
	 * Preprocessing the string (removing unary operators)
	 */

	private static String preProcessString(String expr) {
		// removing unary minus operator if at all
		int length = expr.length();
		for (int i = 0; i < length; i++) {
			if (expr.charAt(i) == '-' || expr.charAt(i) == '+') {
				if (expr.charAt(i - 1) == '(') {
					expr = "" + expr.substring(0, i) + "0"
							+ expr.substring(i, expr.length());
					length++;
					i++;
				}
			} else if (expr.charAt(i) == '!') {
				expr = "" + expr.substring(0, i + 1) + "0 "
						+ expr.substring(i + 1, expr.length());
				length++;
				i++;
			}
		}
		return expr;
	}

	/*
	 * Tokenization of the given expression begins here! Possible tokens in the
	 * expression can be- 1)id (can be a function call or a constant)
	 * 2)operators (+, -, /, *, !(factorial), ^(power)) 3)brackets (small
	 * brackets, square brackets, braces) 4)Numbers Note that after tokenization
	 * the first token will always be an empty string
	 */

	// returns true if s is an operator
	private static boolean isOperator(String s) {
		return Pattern.matches("[-+/*!\\^]", s);
	}

	// returns true if s is a bracket
	private static boolean isBracket(String s) {
		return Pattern.matches("[()]", s);
	}

	// returns true if s can be a part of number
	private static boolean isNumberT(String s) {
		return Pattern.matches("[0-9]|[.]", s);
	}

	// returns true if s is a function/constant
	private static boolean isId(String s) {
		return Pattern.matches("[a-z]+|[π√]", s);
	}

	// tokenize the expression into tokens
	private static String[] tokenize(String expr) {
		String result = " ";
		boolean number = false, id = false;
		for (int i = 0; i < expr.length(); i++) {
			char c = expr.charAt(i);
			if (isOperator(Character.toString(c))
					|| isBracket(Character.toString(c))) {
				result = result + " " + c + " ";
				number = false;
				id = false;
			} else if (isNumberT(Character.toString(c))) {
				if (number) {
					result = result + c;
				} else {
					result = result + " " + c;
					number = true;
				}
				id = false;
			} else if (isId(Character.toString(c))) {
				if (id) {
					result = result + c;
				} else {
					result = result + " " + c;
					id = true;
				}
				number = false;
			} else {
				result = result + c;
			}
		}
		String[] tokens = result.split("\\s+");
		return tokens;
	}

	/*
	 * Preprocessing tokens (removing constants)
	 */
	// returns the value of constant
	private static double evaluateConstant(String cons) {
		if (cons.equals("π")) {
			return Math.PI;
		} else if (cons.equals("e")) {
			return Math.E;
		} else {
			throw new Error();
		}
	}

	private static String[] preProcessTokens(String[] tokens) {
		// removing constants with their values
		for (int i = 0; i < tokens.length; i++) {
			if (isConstant(tokens, i)) {
				tokens[i] = Double.toString(evaluateConstant(tokens[i]));
			}
		}
		return tokens;
	}

	/*
	 * Parsing phase begins! The tokens will be converted into a postfix
	 * expression. Before this phase all the function calls has to be removed
	 * from the expression. This is done by evaluating the function call. So the
	 * parsing is done only for the expression which don't have function call.
	 * Constants has also been removed already from the expression.
	 */

	// takes care of preference
	private static int convertTOnumber(String s) {
		if (s.equals("^") || s.equals("!")) {
			return 4;
		} else if (s.equals("/")) {
			return 3;
		} else if (s.equals("*")) {
			return 2;
		} else
			return 1;
	}

	// checks which one has higher preference
	private static boolean isLower(String s1, String s2) {
		int i1 = convertTOnumber(s1);
		int i2 = convertTOnumber(s2);
		return i1 < i2;
	}

	// returns true if s is a Number
	private static boolean isNumber(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// converts an infix expression into postfix expression
	// an extra ")" must have been inserted at the end before caling this
	// function. Also the first token has to be an empty string which will be
	// ignored by the function.
	private static String[] inTOpost(String[] tokens) {
		String[] post_tokens = new String[tokens.length];
		int index = 0;
		Stack<String> S = new Stack<String>();
		S.push("(");
		for (int i = 0; i < tokens.length; i++) {
			String token = tokens[i];
			if (isNumber(token)) {
				post_tokens[index] = token;
				index++;
			} else if (token.equals("(")) {
				S.push(token);
			} else if (isOperator(token)) {
				while (!S.isEmpty() && !isLower(S.peek(), token)
						&& isOperator(S.peek())) {
					post_tokens[index] = S.pop();
					index++;
				}
				S.push(token);
			} else if (token.equals(")")) {
				while (!S.isEmpty() && !S.peek().equals("(")
						&& isOperator(S.peek())) {
					post_tokens[index] = S.pop();
					index++;
				}
				if (!S.isEmpty()) {
					S.pop();
				}
			} else {
				throw new Error();
			}
		}
		while (!S.isEmpty()) {
			post_tokens[index] = S.pop();
			index++;
		}
		return post_tokens;
	}

	/*
	 * Evaluation phase begins! No function calls and functions will be present
	 * in the expression.
	 */

	// evaluate the binary operator expression
	private static double evalHelperBin(double a, String op, double b) {
		if (op.equals("+")) {
			return (a + b);
		} else if (op.equals("-")) {
			return (a - b);
		} else if (op.equals("*")) {
			return (a * b);
		} else if (op.equals("/")) {
			return (a / b);
		} else if (op.equals("^")) {
			return (double) Math.pow(a, b);
		} else if (op.equals("!")) {
			return (double) factorial((int) a);
		} else {

			throw new Error();
		}
	}

	// evaluates factorial
	private static int factorial(int num) {
		if (num == 0) {
			return 1;
		} else {
			return (factorial(num - 1) * num);
		}
	}

	// evaluating the expression
	private static double calculate(String[] tokens) {
		Stack<String> stak = new Stack<String>();
		for (String token : tokens) {
			if (!isNumber(token)) {
				double sec = Double.parseDouble(stak.pop());
				double first;
				if (isOperator(stak.firstElement())) {
					first = 0;
				} else {
					first = Double.parseDouble(stak.pop());
				}
				stak.push("" + evalHelperBin(first, token, sec));
			} else {
				stak.push(token);
			}
		}
		double res = Double.parseDouble(stak.pop());
		if (!stak.empty()) {
			throw new Error();
		} else {
			return res;
		}
	}

	// returns true if the given id is a constant not a function
	// Note that (i+1) cannot be more that length because there is one closing
	// bracket at the end appended manually.
	private static boolean isConstant(String[] tokens, int i) {
		if (isId(tokens[i])) {
			return (!tokens[i + 1].equals("("));
		} else {
			return false;
		}
	}

	// evaluates functions
	private static double evaluateFunction(String func, double arg) {
		if (func.equals("log")) {
			return Math.log10(Math.abs(arg));
		} else if (func.equals("sin")) {
			if (!rad_f) {
				arg = arg * Math.PI / 180;
			}
			return Math.sin(arg);
		} else if (func.equals("cos")) {
			if (!rad_f) {
				arg = arg * Math.PI / 180;
			}
			return Math.cos(arg);
		} else if (func.equals("tan")) {
			if (!rad_f) {
				arg = arg * Math.PI / 180;
			}
			return Math.tan(arg);
		} else if (func.equals("asin")) {
			if (rad_f) {
				return Math.asin(arg);
			} else {
				return Math.asin(arg) * 180 / Math.PI;
			}

		} else if (func.equals("acos")) {
			if (rad_f) {
				return Math.acos(arg);
			} else {
				return Math.acos(arg) * 180 / Math.PI;
			}

		} else if (func.equals("atan")) {
			if (rad_f) {
				return Math.atan(arg);
			} else {
				return Math.atan(arg) * 180 / Math.PI;
			}

		} else if (func.equals("√")) {
			return Math.sqrt(arg);
		} else if (func.equals("abs")) {
			return Math.abs(arg);
		} else if (func.equals("ln")) {
			return Math.log10(Math.abs(arg)) / Math.log10(Math.E);
		} else {
			throw new Error();
		}
	}

	// evaluates after tokens has been made
	private static double evaluateTokens(String[] tokens) {
		String[] arg_tokens;
		// evaluates function arguments
		for (int i = 0; i < tokens.length; i++) {
			if (isId(tokens[i]) && !isConstant(tokens, i)) {
				tokens[i + 1] = null;
				int first = i + 2;
				i = i + 2;
				int count = 1;
				while (count != 0) {
					if (tokens[i].equals(")")) {
						count--;
					} else if (tokens[i].equals("(")) {
						count++;
					}
					i++;
				}
				i--;
				arg_tokens = new String[i - first + 1];
				for (int j = first; j <= i; j++) {
					arg_tokens[j - first] = tokens[j];
					tokens[j] = null;
				}
				tokens[first - 2] = Double.toString(evaluateFunction(
						tokens[first - 2], evaluateTokens(arg_tokens)));
			}
		}

		// finding the number of non-null tokens
		int count = 0;
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i] != null) {
				count++;
			}
		}

		// copying the tokens into a new string
		String[] result_tokens = new String[count];
		int index = 0;
		for (int i = 0; i < tokens.length; i++) {
			if (tokens[i] != null) {
				result_tokens[index] = tokens[i];
				index++;
			}
		}

		String[] toks = inTOpost(result_tokens);

		// removing null tokens at the end
		index = 0;
		while (toks[index] != null) {
			index++;
		}
		String[] post_tokens = new String[index];
		for (int i = 0; i < index; i++) {
			post_tokens[i] = toks[i];
		}
		return calculate(post_tokens);
	}

	/*
	 * Main function which is public. This function should be called on an
	 * expression
	 */

	// main public function to be called from outside
	public static double evaluate(String expr, boolean rad_flag) {
		rad_f = rad_flag;
		expr = preProcessString(expr);

		// tokenization
		String[] toks = tokenize(expr + ")");

		// removing the first empty string
		String[] tokens = new String[toks.length - 1];
		for (int i = 1; i < toks.length; i++) {
			tokens[i - 1] = toks[i];
		}

		tokens = preProcessTokens(tokens);

		// evaluating the tokens
		return evaluateTokens(tokens);
	}
}