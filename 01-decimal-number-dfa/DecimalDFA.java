/*
 * BMU-325 Programming Languages - Midterm Question 2
 *
 * QUESTION:
 * A Deterministic Finite Automaton (DFA) will be designed for a lexical
 * analyzer that recognizes simple decimal (floating-point) numbers.
 * At least one digit after the decimal point is required.
 * Define the DFA and write a program that takes a decimal number as a
 * string and shows whether it is accepted according to the state diagram.
 *
 *   Valid examples  :  .5   0.25   15.0   123.456
 *   Invalid examples:  .    15.    2..3   5e3.
 *
 * DFA DEFINITION:  M = (Q, Sigma, delta, q0, F)
 *   Q     = {q0, q1, q2, q3, qT}
 *   Sigma = {0-9, .}
 *   q0    = start state
 *   F     = {q3}  (the only accepting state)
 *
 * TRANSITION TABLE:
 *   State  |  0-9  |   .   | other
 *   -------+-------+-------+-------
 *   -> q0  |  q1   |  q2   |  qT
 *      q1  |  q1   |  q2   |  qT
 *      q2  |  q3   |  qT   |  qT
 *    * q3  |  q3   |  qT   |  qT     (* = accepting state)
 *      qT  |  qT   |  qT   |  qT     (trap / error state)
 *
 * Regular expression equivalent:  [0-9]* \. [0-9]+
 */

import java.util.Scanner;

public class DecimalDFA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter a number: ");
        String s = sc.nextLine();

        // States:
        //  0 = start                 (nothing read yet)
        //  1 = integer part          (reading digits before the point)
        //  2 = point read            (point seen, no digit after it yet)
        //  3 = fractional part       (at least one digit after the point) -> ACCEPT
        // -1 = trap / error state    (once here, the input can never be valid)
        int state = 0;

        // Read the input one character at a time and move between states
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (state == 0) {                       // q0: start
                if (c >= '0' && c <= '9') state = 1;    // digit -> integer part
                else if (c == '.')        state = 2;    // point -> point read (e.g. ".5")
                else                      state = -1;   // anything else -> error
            }
            else if (state == 1) {                  // q1: integer part
                if (c >= '0' && c <= '9') state = 1;    // more digits -> stay here
                else if (c == '.')        state = 2;    // point -> point read
                else                      state = -1;   // e.g. 'e' in "5e3." -> error
            }
            else if (state == 2) {                  // q2: point read
                if (c >= '0' && c <= '9') state = 3;    // first fractional digit -> accept state
                else                      state = -1;   // second point (e.g. "2..3") -> error
            }
            else if (state == 3) {                  // q3: fractional part (accepting)
                if (c >= '0' && c <= '9') state = 3;    // more digits -> stay here
                else                      state = -1;   // anything else -> error
            }

            // No way out of the trap state, so stop reading early
            if (state == -1) break;
        }

        // Decide only AFTER the whole string has been read:
        // the input is valid only if we end in the accepting state (q3)
        if (state == 3) System.out.println("\"" + s + "\" -> VALID decimal number");
        else            System.out.println("\"" + s + "\" -> INVALID");
    }
}