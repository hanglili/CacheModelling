package com.cachemodelling;

public class Measure {
  private double measureSum = 0.0;
  private double measureSumSq = 0.0;
  private int n = 0;

  public void addObservation(double obs) {
    measureSum   += obs;
    measureSumSq += Math.pow(obs, 2);
    n += 1;
  }

  public int getNumObservations() {
    return n;
  }

  public double sampleMean() {
    return measureSum / n;
  }

  public double sampleVariance() {
    return (measureSumSq - (Math.pow(measureSum, 2) / n)) / (n - 1);
  }

  public double ciHalfWidth(double alpha) {
    var s = Math.sqrt(sampleVariance());
    var z = StudentsTable.getPercentile(n - 1, alpha);
    return z * s / Math.sqrt(n);
  }

}