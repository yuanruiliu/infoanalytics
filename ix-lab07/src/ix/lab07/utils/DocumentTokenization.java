package ix.lab07.utils;

import java.text.Normalizer;
import java.util.Iterator;
import java.util.regex.Pattern;

import com.aliasi.tokenizer.EnglishStopTokenizerFactory;
import com.aliasi.tokenizer.IndoEuropeanTokenizerFactory;
import com.aliasi.tokenizer.LowerCaseTokenizerFactory;
import com.aliasi.tokenizer.ModifyTokenTokenizerFactory;
import com.aliasi.tokenizer.PorterStemmerTokenizerFactory;
import com.aliasi.tokenizer.TokenizerFactory;

/**
 * Tokenization of text documents. This class provides utilities to transform
 * the raw character string representing a document into a stream of terms.
 *
 * More concretely, the following processing steps are applied:
 * - tokens are split around whitespace, hyphens and punctuation
 * - diacritics and non-alphanumeric symbols are removed
 * - tokens are converted to lowercase
 * - common English stop words are removed (e.g. but, and, ...)
 * - tokens are stemmed using a Porter stemmer
 */
public class DocumentTokenization {

    private static final Pattern NON_ASCII = Pattern.compile("[^\\p{ASCII}]");
    private static final TokenizerFactory TOKENIZER_FACTORY = getFactory();


    /**
     * TokenizerFactory that removes diacritics (accents) and all non-alphanumeric
     * characters.
     */
    @SuppressWarnings("serial")
    private static class NormalizationTokenizerFactory extends ModifyTokenTokenizerFactory {

        private static final Pattern DISALLOWED = Pattern.compile("[^a-zA-Z0-9]");

        public NormalizationTokenizerFactory(TokenizerFactory factory) {
            super(factory);
        }

        @Override
        public String modifyToken(String token) {
            // Unicode canonical decomposition separates diacritics from base char.
            String normalized =  Normalizer.normalize(token, Normalizer.Form.NFD);
            // Replace non-alphanumeric chars (also strips out diacritics).
            String modified = DISALLOWED.matcher(normalized).replaceAll("");
            // If resulting string is empty, return null.
            return modified.length() > 0 ? modified : null;
        }
    }


    /** Builds a TokenizerFactory used for the IMDB plot dataset. */
    private static TokenizerFactory getFactory() {
        TokenizerFactory factory = IndoEuropeanTokenizerFactory.INSTANCE;
        // Convert everything to ASCII and remove symbols we're not interested in.
        factory = new NormalizationTokenizerFactory(factory);
        // Lower case everything.
        factory = new LowerCaseTokenizerFactory(factory);
        // Remove common English stop words.
        factory = new EnglishStopTokenizerFactory(factory);
        // Finally, stem the words with a simple Porter stemmer.
        factory = new PorterStemmerTokenizerFactory(factory);

        return factory;
    }


    /**
     * Given a string, returns a stream of processed terms.
     *
     * @param data the original character string from which to extract the terms
     * @return an Iterable containing the terms resulting from the processing steps
     */
    public static Iterable<String> stream(String data) {
        final char[] chars = data.toCharArray();
        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return TOKENIZER_FACTORY.tokenizer(chars, 0, chars.length).iterator();
            }
        };
    }


    /**
     * Returns the "best" ASCII-characters-only version of a string. Especially useful to
     * remove diacritics.
     *
     * @param str the string to process
     * @return a string containing only ASCII characters
     */
    public static String toASCII(String str) {
        return NON_ASCII.matcher(
                Normalizer.normalize(str, Normalizer.Form.NFD)).replaceAll("");
    }

}
