package ch.bfh.bigdata.semarbeit.twitteralert.domain;

import opennlp.tools.doccat.DoccatEvaluationMonitor;
import opennlp.tools.doccat.DocumentSample;

import java.util.concurrent.atomic.AtomicInteger;

public final class TweetFMeasure implements DoccatEvaluationMonitor {

    AtomicInteger tp = new AtomicInteger();
    AtomicInteger fp = new AtomicInteger();
    AtomicInteger fn = new AtomicInteger();
    AtomicInteger tn = new AtomicInteger();


    public double getPrecisionScore() {
        return (double) tp.get() / (tp.get() + fp.get());
    }


    public double getRecallScore() {
        return (double) tp.get() / (tp.get() + fn.get());
    }

    public double getFMeasure() {
        if (getPrecisionScore() + getRecallScore() > 0) {
            return 2 * (getPrecisionScore() * getRecallScore())
                / (getPrecisionScore() + getRecallScore());
        } else {
            // cannot divide by zero, return error code
            return -1;
        }
    }

    @Override
    public String toString() {
        return "Precision: " + Double.toString(getPrecisionScore()) + "\n"
            + "Recall: " + Double.toString(getRecallScore()) + "\n" + "F-Measure: "
            + Double.toString(getFMeasure());
    }

    @Override
    public void correctlyClassified(DocumentSample reference, DocumentSample prediction) {
        if (reference.getCategory().equals("WAHR")) {
            tp.incrementAndGet();
        } else {
            tn.incrementAndGet();
        }
    }

    @Override
    public void missclassified(DocumentSample reference, DocumentSample prediction) {
        if (reference.getCategory().equals("WAHR")) {
            fn.incrementAndGet();
        } else {
            fp.incrementAndGet();
        }
    }
}
