package org.apache.commons.text;

/**
 * Takes a collection of String tokens and combines them into a single String.
 * <p>
 * This class functions as the inverse of {@link org.apache.commons.text.StringTokenizer}. All tokens are formatted
 * by a {@link TokenFormatter} which allows fine grained control over the final output.
 * </p>
 */
public class TokenStringifier {

    /**
     * The formatter for the delimiter.
     */
    private TokenFormatter delimiterFormatter;

    /**
     * The formatter for the tokens.
     */
    private TokenFormatter tokenFormatter;

    /**
     * Builder used to hold formatted tokens.
     */
    private StringBuilder builder;

    /**
     * The final string.
     */
    private String string;

    /**
     * The tokens to turn into a String.
     */
    private Iterable<String> tokens;

    public TokenStringifier(TokenFormatter delimiterFormatter, TokenFormatter tokenFormatter) {
        super();
        this.delimiterFormatter = delimiterFormatter;
        this.tokenFormatter = tokenFormatter;
    }

    public void reset(Iterable<String> tokens) {
        this.tokens = tokens;
        this.string = null;
        this.builder = null;
    }

    public TokenStringifier() {
        tokenFormatter = TokenFormatterFactory.noOpFormatter();
        delimiterFormatter = TokenFormatterFactory.noOpFormatter();
    }

    private void stringify() {
        builder = new StringBuilder();
        char[] priorToken = null;
        int i = 0;
        for (String token : tokens) {
            char[] tokenChars = token.toCharArray();
            if (i > 0) {
                String delimiter = delimiterFormatter.format(priorToken, i, tokenChars);
                if (delimiter != null) {
                    builder.append(delimiter);
                }
            }
            String formatted = tokenFormatter.format(priorToken, i, tokenChars);
            if (formatted != null) {
                builder.append(formatted);
            }
            i++;
        }
        string = builder.toString();
    }

    public String getString() {
        if (string == null) {
            stringify();
        }
        return string;
    }
}
