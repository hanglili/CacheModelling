package com.cachemodelling;

public class CacheModellingCT {

  private Cache cache;
  private double[] cdf;
  private int population;

    // The warm-up is executed when the cache is set up.
    public CacheModellingCT(int warmUp, int population, int cacheSize, double[] cdf) {
      this.cache = new Cache(cacheSize);
      this.population = population;
      this.cdf = cdf;
      for (int i = 0; i < warmUp; i++) {
        makeRequest();
      }
    }

    public int getItemIndex(double p) {
      for (int i = 0; i < population; i++) {
        if (cdf[i] > p) {
          return i + 1;
        }
      }
      return population;
    }

    private boolean makeRequest() {
      double p = Math.random();
      int index = getItemIndex(p);
      return cache.getElement(index);
    }

    // Reset the measures, but not the state - this is useful when
    // dividing a single run into batches.
    // runLength is the run length of each batch.
    public boolean run() {
      return makeRequest();
    }

}
