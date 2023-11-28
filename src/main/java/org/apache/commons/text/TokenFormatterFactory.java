package org.apache.commons.text;

import org.apache.commons.lang3.StringUtils;

public class TokenFormatterFactory {

    /**
     * Token formatter that returns the token as is.
     */
    public static class NoOpFormatter implements TokenFormatter {
        @Override
        public String format(char[] prior, int tokenIndex, char[] token) {
            return new String(token);
        }

    }

    /**
     * Token formatter that always returns a constant string, and optionally checks the passed in token
     * for the constant and throws an error when found.
     */
    public static class ConstantTokenFormatter implements TokenFormatter {

        /**
         * The constant to return.
         */
        private char[] constant;

        /**
         * Whether or not to throw an exception if the constant is found.
         */
        private boolean failOnConstantFound = true;

        public ConstantTokenFormatter(char constant) {
            this(new char[] {constant}, true);
        }

        public ConstantTokenFormatter(char constant, boolean failOnConstantFound) {
            this(new char[] {constant}, failOnConstantFound);
        }

        public ConstantTokenFormatter(String constant) {
            this(constant, true);
        }

        public ConstantTokenFormatter(String constant, boolean failOnConstantFound) {
            this(constant.toCharArray(), failOnConstantFound);
        }

        public ConstantTokenFormatter(char[] constant, boolean failOnConstantFound) {
            this.constant = constant;
            this.failOnConstantFound = failOnConstantFound;
        }

        @Override
        public String format(char[] prior, int tokenIndex, char[] token) {
            if (failOnConstantFound) {
                for (int i = 0; i < token.length; i++) {
                    boolean match = false;
                    int t = i;
                    for (int j = 0; j < constant.length; j++) {
                        if (token[t] == constant[j]) {
                            match = true;
                        } else {
                            match = false;
                            break;
                        }
                        t++;
                    }
                    if (match) {
                        throw new IllegalArgumentException("Token " + tokenIndex + " contains illegal character '" + new String(constant) + "' at index " + t);
                    }
                }
            }

            return new String(constant);
        }

        /**
         * Set whether to check the token for the constant.
         * @param checkTokenForConstant whether to check.
         */
        public void setFailOnConstantFound(boolean checkTokenForConstant) {
            this.failOnConstantFound = checkTokenForConstant;
        }

    }

    /**
     * Reuseable NoOpFormatter instance.
     */
    private static final NoOpFormatter NOOP_FORMATTER = new NoOpFormatter();

    /**
     * Reuseable Empty String formatter instance.
     */
    private static final ConstantTokenFormatter EMPTY_STRING_FORMATTER = new ConstantTokenFormatter(StringUtils.EMPTY, false);

    public static NoOpFormatter noOpFormatter() {
        return NOOP_FORMATTER;
    }

    public static ConstantTokenFormatter constantFormatter(char[] constant, boolean failOnConstant) {
        return new ConstantTokenFormatter(constant, failOnConstant);
    }

    public static ConstantTokenFormatter constantFormatter(char constant, boolean failOnConstant) {
        return new ConstantTokenFormatter(constant, failOnConstant);
    }

    public static ConstantTokenFormatter emptyFormatter() {
        return EMPTY_STRING_FORMATTER;
    }
}
