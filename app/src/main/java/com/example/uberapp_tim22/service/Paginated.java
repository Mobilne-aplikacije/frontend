package com.example.uberapp_tim22.service;

import java.util.ArrayList;
import java.util.List;

public class Paginated<D> {
    private int totalCount;
    private List<Object> results;


    public Paginated(int totalCount) {
        this.totalCount = totalCount;
        this.results = new ArrayList<>();
    }
    public void addResult(Object result){
        this.results.add(result);
    }

    public Paginated(int totalCount, List<Object> results) {
        this.totalCount = totalCount;
        this.results = results;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<Object> getResults() {
        return results;
    }

    public void setResults(List<Object> results) {
        this.results = results;
    }

    public void add(Object t) {
        this.results.add(t);
    }

}
