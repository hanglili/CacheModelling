package com.cachemodelling;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Cache {

  private Set<Integer> cache;

  public Cache(int maxEntries) {
    this.cache = Collections.newSetFromMap(new LinkedHashMap<Integer, Boolean>(){
      protected boolean removeEldestEntry(Map.Entry<Integer, Boolean> eldest) {
        return size() > maxEntries;
      }
    });
  }

  public boolean getElement(int index) {
    if (cache.contains(index)) {
      return true;
    } else {
      cache.add(index);
      return false;
    }
  }

}
