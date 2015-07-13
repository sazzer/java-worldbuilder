package uk.co.grahamcox.worldbuilder.worlds.dal;

import uk.co.grahamcox.worldbuilder.model.HitList;
import uk.co.grahamcox.worldbuilder.worlds.model.World;
import uk.co.grahamcox.worldbuilder.worlds.model.WorldId;

/**
 * Interface describing the Searching of Worlds
 */
public interface WorldSearcher {
    /**
     * Perform a search of worlds
     * @param keyword the keyword search to perform
     * @param offset the page offset to retrieve
     * @param count the number of hits to request
     * @return the results
     */
    HitList<WorldId> search(String keyword, int offset, int count);

    /**
     * Index a world in the searcher
     * @param world the world to index
     */
    void index(final World world);

    /**
     * Delete a world from the index
     * @param worldId the world to delete
     */
    void delete(final WorldId worldId);
}
