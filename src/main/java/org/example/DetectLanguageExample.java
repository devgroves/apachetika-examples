package org.example;

import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.language.detect.LanguageResult;
import org.apache.tika.langdetect.optimaize.OptimaizeLangDetector;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.translate.DefaultTranslator;
import org.apache.tika.language.translate.Translator;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Locale;

public class DetectLanguageExample {
    public static void main(String[] args) throws IOException {
        DetectLanguageExample detectLanguageExample = new DetectLanguageExample();
        System.out.println("1. Language is " + detectLanguageExample.findDisplayLanguage("testing"));
        System.out.println("2. Language is " + detectLanguageExample.findDisplayLanguage("Je m’appelle Jessica"));
        System.out.println("3. Language is " + detectLanguageExample.findDisplayLanguage("Vacaciones de Navidad en España"));
        System.out.println("4. Language is " + detectLanguageExample.findDisplayLanguage("ஆய்த எழுத்து"));
    }
    public String detectLanguage(String text) throws IOException {
        LanguageDetector detector = new OptimaizeLangDetector().loadModels();
        LanguageResult result = detector.detect(text);
        System.out.println("Language of the given content is : " + result.getLanguage());
        System.out.println("Confidence of the lanuage detection is : " + result.getConfidence());
        System.out.println("Raw score of the language detection is : " + result.getRawScore());
        return result.getLanguage();
    }

//    public String googleTranslateToEnglish(String text) {
////        Translator translator = new MicrosoftTranslator();
//        Translator translator = new DefaultTranslator();
//        String result = null;
//        System.out.println("translator is avilable ?" + translator.isAvailable());
//        if (translator.isAvailable()) {
//
//            try {
//                result = translator.translate(text, "en-US");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return result;
//    }
//
//    /**
//     * Use {@link org.apache.tika.parser.transcribe.aws.AmazonTranscribe} to execute transcription
//     * on input data.
//     * This implementation needs to be configured as explained in the Javadoc.
//     *
//     * @param file the name of the file (which needs to be on the Java Classpath) to transcribe.
//     * @return transcribed text.
//     */
//    public static String amazonTranscribe(Path tikaConfig, Path file) throws Exception {
//        return new Tika(new TikaConfig(tikaConfig)).parseToString(file);
//    }

    public String findDisplayLanguage(String text) throws IOException {
        String locale = this.detectLanguage(text);
        return new Locale(locale).getDisplayLanguage();
    }
}
