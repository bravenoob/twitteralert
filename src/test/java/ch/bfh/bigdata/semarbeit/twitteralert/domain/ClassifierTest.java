package ch.bfh.bigdata.semarbeit.twitteralert.domain;

import com.aliasi.classify.*;
import com.aliasi.corpus.XValidatingObjectCorpus;
import com.aliasi.lm.NGramProcessLM;
import com.aliasi.util.AbstractExternalizable;
import opennlp.tools.doccat.*;
import opennlp.tools.ml.AbstractTrainer;
import opennlp.tools.ml.maxent.GISTrainer;
import opennlp.tools.ml.maxent.quasinewton.QNTrainer;
import opennlp.tools.ml.naivebayes.NaiveBayesTrainer;
import opennlp.tools.ml.perceptron.PerceptronTrainer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.Random;
import java.util.stream.Stream;

public class ClassifierTest {

    private DoccatModel model;
    private ObjectStream tweetsStream;
    private String file;

    @Before
    public void setUp() throws IOException {
        URL url = Thread.currentThread().getContextClassLoader().getResource("all_tweets.train");
        file = URLDecoder.decode(url.getFile(), "UTF-8");
        InputStreamFactory dataIn = new MarkableFileInputStreamFactory(new File(file));
        ObjectStream lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
        tweetsStream = new DocumentSampleStream(lineStream);

    }

    @Test
    public void naives_cross_validation_default() throws IOException {
        TrainingParameters params = new TrainingParameters();
        params.put(AbstractTrainer.ALGORITHM_PARAM, NaiveBayesTrainer.NAIVE_BAYES_VALUE);
        params.put(AbstractTrainer.VERBOSE_PARAM, false);

        TweetFMeasure fMeasure = new TweetFMeasure();
        DoccatCrossValidator validator = new DoccatCrossValidator("en", params, new DoccatFactory(), fMeasure);
        validator.evaluate(tweetsStream, 10);

        printFMeasure(fMeasure, validator);
    }

    @Test
    @Ignore("keine veränderung gegenüber cross-valdation 10")
    public void naives_cross_validation_1000() throws IOException {
        TrainingParameters params = new TrainingParameters();
        params.put(TrainingParameters.ITERATIONS_PARAM, 10000 + "");
        params.put(AbstractTrainer.ALGORITHM_PARAM, NaiveBayesTrainer.NAIVE_BAYES_VALUE);
        params.put(AbstractTrainer.VERBOSE_PARAM, false);

        TweetFMeasure fMeasure = new TweetFMeasure();
        DoccatCrossValidator validator = new DoccatCrossValidator("en", params, new DoccatFactory(), fMeasure);
        validator.evaluate(tweetsStream, 10);

        printFMeasure(fMeasure, validator);
    }

    @Test
    public void naives_cross_validation_cutoff_5() throws IOException {
        TrainingParameters params = new TrainingParameters();
        params.put(TrainingParameters.CUTOFF_PARAM, 5 + "");
        params.put(AbstractTrainer.ALGORITHM_PARAM, NaiveBayesTrainer.NAIVE_BAYES_VALUE);
        params.put(AbstractTrainer.VERBOSE_PARAM, false);

        TweetFMeasure fMeasure = new TweetFMeasure();
        DoccatCrossValidator validator = new DoccatCrossValidator("en", params, new DoccatFactory(), fMeasure);
        validator.evaluate(tweetsStream, 10);

        printFMeasure(fMeasure, validator);
    }

    @Test
    public void naives_cross_validation_cutoff_0() throws IOException {
        TrainingParameters params = new TrainingParameters();
        params.put(TrainingParameters.CUTOFF_PARAM, 0 + "");
        params.put(AbstractTrainer.ALGORITHM_PARAM, NaiveBayesTrainer.NAIVE_BAYES_VALUE);
        params.put(AbstractTrainer.VERBOSE_PARAM, false);

        TweetFMeasure fMeasure = new TweetFMeasure();
        DoccatCrossValidator validator = new DoccatCrossValidator("en", params, new DoccatFactory(), fMeasure);
        validator.evaluate(tweetsStream, 10);

        printFMeasure(fMeasure, validator);
    }

    @Test
    public void gistrainer_cross_validation() throws IOException {
        TrainingParameters params = new TrainingParameters();
        params.put(AbstractTrainer.ALGORITHM_PARAM, GISTrainer.MAXENT_VALUE);
        params.put(AbstractTrainer.VERBOSE_PARAM, false);

        TweetFMeasure fMeasure = new TweetFMeasure();
        DoccatCrossValidator validator = new DoccatCrossValidator("en", params, new DoccatFactory(), fMeasure);
        validator.evaluate(tweetsStream, 10);

        printFMeasure(fMeasure, validator);
    }

    @Test
    public void PerceptronTrainer_cross_validation() throws IOException {
        TrainingParameters params = new TrainingParameters();
        params.put(AbstractTrainer.ALGORITHM_PARAM, PerceptronTrainer.PERCEPTRON_VALUE);
        params.put(AbstractTrainer.VERBOSE_PARAM, false);

        TweetFMeasure fMeasure = new TweetFMeasure();
        DoccatCrossValidator validator = new DoccatCrossValidator("en", params, new DoccatFactory(), fMeasure);
        validator.evaluate(tweetsStream, 10);

        printFMeasure(fMeasure, validator);
    }

    @Test
    public void QNTrainer_cross_validation() throws IOException {
        TrainingParameters params = new TrainingParameters();
        params.put(AbstractTrainer.ALGORITHM_PARAM, QNTrainer.MAXENT_QN_VALUE);
        params.put(AbstractTrainer.VERBOSE_PARAM, false);

        TweetFMeasure fMeasure = new TweetFMeasure();
        DoccatCrossValidator validator = new DoccatCrossValidator("en", params, new DoccatFactory(), fMeasure);
        validator.evaluate(tweetsStream, 10);

        printFMeasure(fMeasure, validator);
    }

    @Test
    public void lingpipe_classifier_ngram_1() throws IOException, ClassNotFoundException {
        String[] CATEGORIES = {"WAHR", "FALSCH"};
        int NGRAM_SIZE = 1;
        int NUM_FOLDS = 10;
        PrecisionRecallEvaluation eval = runClassifier(CATEGORIES, NGRAM_SIZE, NUM_FOLDS);
        printEval(eval);
    }

    @Test
    public void lingpipe_classifier_ngram_2() throws IOException, ClassNotFoundException {
        String[] CATEGORIES = {"WAHR", "FALSCH"};
        int NGRAM_SIZE = 2;
        int NUM_FOLDS = 10;
        PrecisionRecallEvaluation eval = runClassifier(CATEGORIES, NGRAM_SIZE, NUM_FOLDS);
        printEval(eval);
    }

    @Test
    public void lingpipe_classifier_ngram_4() throws IOException, ClassNotFoundException {
        String[] CATEGORIES = {"WAHR", "FALSCH"};
        int NGRAM_SIZE = 4;
        int NUM_FOLDS = 10;
        PrecisionRecallEvaluation eval = runClassifier(CATEGORIES, NGRAM_SIZE, NUM_FOLDS);
        printEval(eval);
    }

    @Test
    public void lingpipe_classifier_ngram_6() throws IOException, ClassNotFoundException {
        String[] CATEGORIES = {"WAHR", "FALSCH"};
        int NGRAM_SIZE = 6;
        int NUM_FOLDS = 10;
        PrecisionRecallEvaluation eval = runClassifier(CATEGORIES, NGRAM_SIZE, NUM_FOLDS);
        printEval(eval);
    }

    @Test
    public void lingpipe_classifier_ngram_10() throws IOException, ClassNotFoundException {
        String[] CATEGORIES = {"WAHR", "FALSCH"};
        int NGRAM_SIZE = 10;
        int NUM_FOLDS = 10;
        PrecisionRecallEvaluation eval = runClassifier(CATEGORIES, NGRAM_SIZE, NUM_FOLDS);
        printEval(eval);
    }

    @Test
    @Ignore ("takes 6 minutes to finish, not better than with 10.")
    public void lingpipe_classifier_ngram_25() throws IOException, ClassNotFoundException {
        String[] CATEGORIES = {"WAHR", "FALSCH"};
        int NGRAM_SIZE = 25;
        int NUM_FOLDS = 10;
        PrecisionRecallEvaluation eval = runClassifier(CATEGORIES, NGRAM_SIZE, NUM_FOLDS);
        printEval(eval);
    }


    private PrecisionRecallEvaluation runClassifier(String[] CATEGORIES, int NGRAM_SIZE, int NUM_FOLDS) throws ClassNotFoundException, IOException {
        XValidatingObjectCorpus<Classified<CharSequence>> corpus = new XValidatingObjectCorpus<>(NUM_FOLDS);
        try (Stream<String> stream = Files.lines(new File(file).toPath())) {
            stream.forEach(line -> {
                    String parts[] = line.split(" ", 2);
                    Classification classification = new Classification(parts[0]);
                    Classified<CharSequence> classified
                        = new Classified<>(parts[1], classification);
                    corpus.handle(classified);
                }
            );
        }
        long seed = 42L;
        corpus.permuteCorpus(new Random(seed));
        PrecisionRecallEvaluation eval = new PrecisionRecallEvaluation();
        for (
            int fold = 0;
            fold < NUM_FOLDS; ++fold) {
            corpus.setFold(fold);
            DynamicLMClassifier<NGramProcessLM> classifier = DynamicLMClassifier.createNGramProcess(CATEGORIES, NGRAM_SIZE);
            corpus.visitTrain(classifier);
            JointClassifier<CharSequence> compiledClassifier = (JointClassifier<CharSequence>) AbstractExternalizable.compile(classifier);
            JointClassifierEvaluator<CharSequence> evaluator = new JointClassifierEvaluator<>(compiledClassifier, CATEGORIES, true);
            corpus.visitTest(evaluator);
            extractCases(eval, evaluator);
        }
        return eval;
    }

    private void printEval(PrecisionRecallEvaluation eval) {
        System.out.println("Precision: " + Double.toString(eval.precision()) + "\n"
            + "Recall: " + Double.toString(eval.recall()) + "\n" + "F-Measure: "
            + Double.toString(eval.fMeasure()));
    }

    private void extractCases(PrecisionRecallEvaluation eval, JointClassifierEvaluator<CharSequence> evaluator) {
        evaluator.truePositives("WAHR").forEach(wahr -> eval.addCase(true, true));
        evaluator.truePositives("FALSCH").forEach(wahr -> eval.addCase(false, false));
        evaluator.falseNegatives("WAHR").forEach(wahr -> eval.addCase(true, false));
        evaluator.falseNegatives("FALSCH").forEach(wahr -> eval.addCase(false, true));
    }

    @Test
    @Ignore
    public void postagger_cross_validation() throws IOException {
        TrainingParameters params = new TrainingParameters();

        URL tokenModelFile = Thread.currentThread().getContextClassLoader().getResource("en-token.bin");
        String tokenModelDecoded = URLDecoder.decode(tokenModelFile.getFile(), "UTF-8");

        FileInputStream tokenModelIn = new FileInputStream(tokenModelDecoded);
        TokenizerModel tokenModel = new TokenizerModel(tokenModelIn);
        Tokenizer tokenizer = new TokenizerME(tokenModel);

        String tokens[] = tokenizer.tokenize("\"RT @snicoll: Spring Framework 10 available now https://t.co/N7uB\"\n");

        // Parts-Of-Speech Tagging
        // reading parts-of-speech model to a stream
        URL maxentUrl = Thread.currentThread().getContextClassLoader().getResource("en-pos-maxent.bin");
        String maxentDecoded = URLDecoder.decode(maxentUrl.getFile(), "UTF-8");

        FileInputStream posModelIn = new FileInputStream(maxentDecoded);
        // loading the parts-of-speech model from stream
        POSModel posModel = new POSModel(posModelIn);
        // initializing the parts-of-speech tagger with model
        POSTaggerME posTagger = new POSTaggerME(posModel);
        // Tagger tagging the tokens
        String tags[] = posTagger.tag(tokens);
        // Getting the probabilities of the tags given to the tokens
        double probs[] = posTagger.probs();

        System.out.println("Token\t:\tTag\t:\tProbability\n---------------------------------------------");
        for (int i = 0; i < tokens.length; i++) {
            System.out.println(tokens[i] + "\t:\t" + tags[i] + "\t:\t" + probs[i]);
        }
    }

    @Test
    @Ignore
    public void naives_apply_model() throws IOException {
        TrainingParameters params = new TrainingParameters();
        params.put(TrainingParameters.ITERATIONS_PARAM, 10 + "");
        params.put(TrainingParameters.CUTOFF_PARAM, 0 + "");
        params.put(AbstractTrainer.ALGORITHM_PARAM, NaiveBayesTrainer.NAIVE_BAYES_VALUE);

        // create a model from traning data
        model = DocumentCategorizerME.train("en", tweetsStream, params, new DoccatFactory());


        // save the model to local
        //  BufferedOutputStream modelOut = new BufferedOutputStream(new FileOutputStream("model" + File.separator + "spring_tweets-classifier-naive-bayes.bin"));
        //  model.serialize(modelOut);
        // System.out.println("\nTrained Model is saved locally at : " + "model" + File.separator + "spring_tweetse-classifier-naive-bayes.bin");

        // test the model file by subjecting it to prediction
        DocumentCategorizer doccat = new DocumentCategorizerME(model);
        String[] docWords = "Afterwards Stuart and Charlie notice Kate in the photos Stuart took at Leopolds ball and realise that her destiny must be to go back and be with Leopold That night while Kate is accepting her promotion at a company banquet he and Charlie race to meet her and show her the pictures Kate initially rejects their overtures and goes on to give her acceptance speech but it is there that she sees Stuarts picture and realises that she truly wants to be with Leopold".replaceAll("[^A-Za-z]", " ").split(" ");
        double[] aProbs = doccat.categorize(docWords);
        printProbability(doccat, aProbs);
    }

    private void printFMeasure(TweetFMeasure fMeasure, DoccatCrossValidator validator) {
        System.out.println("\n---------------------------------\nCategory : Accuracy\n---------------------------------");
        System.out.println(validator.getDocumentAccuracy());
        System.out.println(fMeasure.toString());
        System.out.println("---------------------------------");
    }

    private void printProbability(DocumentCategorizer doccat, double[] aProbs) {
        // print the probabilities of the categories
        System.out.println("\n---------------------------------\nCategory : Probability\n---------------------------------");
        for (int i = 0; i < doccat.getNumberOfCategories(); i++) {
            System.out.println(doccat.getCategory(i) + " : " + aProbs[i]);
        }
        System.out.println("---------------------------------");

        System.out.println("\n" + doccat.getBestCategory(aProbs) + " : is the predicted category for the given sentence.");
    }
}
