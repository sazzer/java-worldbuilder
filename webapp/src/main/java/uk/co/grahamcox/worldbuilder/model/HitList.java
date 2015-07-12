package uk.co.grahamcox.worldbuilder.model;

import java.util.List;

/**
 * Hitlist representing a paged results list
 * @param <T> The type of result in the hitlist
 */
public class HitList<T> {
    /** The actual list of results */
    private final List<T> results;

    /** The total number of results that were available */
    private final long totalResults;

    /**
     * Construct the hitlist
     * @param results the actual list of results
     * @param totalResults the total number of results
     */
    public HitList(final List<T> results, final long totalResults) {
        this.results = results;
        this.totalResults = totalResults;
    }

    /**
     * Get the list of results
     * @return the list of results
     */
    public List<T> getResults() {
        return results;
    }

    /**
     * Get the total number of results
     * @return the total number of results
     */
    public long getTotalResults() {
        return totalResults;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "HitList{" +
            "results=" + results +
            ", totalResults=" + totalResults +
            '}';
    }
}
