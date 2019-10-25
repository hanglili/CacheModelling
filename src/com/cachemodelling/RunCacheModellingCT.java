package com.cachemodelling;

import java.io.PrintStream;
//
// Generates <numObservations> observations from a board simulation and
// computes a point estimate and confidence interval for each board location
// probability in the steady state (equilibrium). A warm-up allows the model
// to reach equilibrium before measurements are made.
//
// Supports independent replications (runNtimes) and a single run with multiple
// batches (runOnce).
//
// The system is ergodic, so the two approaches yield the same expected values
// for the steady-state probabilities.
//

public class RunCacheModellingCT {

    private int numObservations, warmUp, populationSize, cacheSize;
    private Measure hitMeasure;
    private double[] cdf;

    public RunCacheModellingCT(int numObservations, int warmUp, int populationSize, int cacheSize) {
        this.numObservations = numObservations;
        this.populationSize = populationSize;
        this.warmUp = warmUp;
        this.cacheSize = cacheSize;
        this.hitMeasure = new Measure();
        this.cdf = constructCdf(populationSize);
    }

    private double[] constructCdf(int populationSize) {
        double[] cdf = new double[populationSize];
        double cumulative = 0;
        double sumOfLambdas = 0;
        for (int i = 0; i < cdf.length; i++) {
            double k = i + 1;
            sumOfLambdas += (1.0 / k);
        }

        for (int i = 0; i < cdf.length; i++) {
            double k = i + 1;
            cdf[i] = ((1.0 / k) / sumOfLambdas) + cumulative;
            cumulative += (1.0 / k) / sumOfLambdas;
        }
        return cdf;
    }

    private void addToMeasures(int numHits) {
        hitMeasure.addObservation(numHits);
    }

    public void displayResults(PrintStream out) {
        double hitRatio = hitMeasure.sampleMean();
        if (numObservations == 1) {
            out.printf(": %.4f\n", hitRatio);
        } else {
            double hw = hitMeasure.ciHalfWidth(95);
            out.printf("%.4f, 95%% CI = (%.4f, %.4f)\n", hitRatio, hitRatio - hw, hitRatio + hw);
        }
    }

    // Each run sets up a fresh board.
    // There is a separate warm-up for each.
    public void runNtimes() {
        for (int i = 0; i < numObservations; i++) {
            var cacheModelling = new CacheModellingCT(warmUp, populationSize, cacheSize, cdf);
            int isHit = cacheModelling.run()? 1 : 0;
            addToMeasures(isHit);
        }
    }

    // Each run resets the measures but doesn't reset the state.
    // There is a single warm-up when the board is built.
    public void runOnce() {
        var cacheModelling = new CacheModellingCT(warmUp, populationSize, cacheSize, cdf);
        for (int i = 0; i < numObservations; i++) {
            cacheModelling.run();
            int isHit = cacheModelling.run()? 1 : 0;
            addToMeasures(isHit);
        }
    }

    // Usage: java ...
    //   RunCacheModellingCT <numObservations> <warmup> <populationSize> <cacheSize>
    public static void main(String[] args) {
        var numObservations = Integer.parseInt(args[0]);
        var warmUp = Integer.parseInt(args[1]);
        var populationSize = Integer.parseInt(args[2]);
        var cacheSize = Integer.parseInt(args[3]);
        var sim = new RunCacheModellingCT(numObservations, warmUp, populationSize, cacheSize);

//        sim.runOnce();
        sim.runNtimes();
        sim.displayResults(System.out);
    }
}
