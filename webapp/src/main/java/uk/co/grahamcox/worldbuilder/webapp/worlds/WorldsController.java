package uk.co.grahamcox.worldbuilder.webapp.worlds;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.co.grahamcox.worldbuilder.webapp.users.UserLink;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Controller for accessing Worlds
 */
@Controller
@RequestMapping("/api/worlds")
public class WorldsController {
    /**
     * Get a single world by it's ID
     * @param id the ID
     * @return the world
     */
    @RequestMapping("/{id}")
    @ResponseBody
    public WorldModel get(@PathVariable("id") final String id) {
        WorldModel worldModel = new WorldModel();
        worldModel.setId(id);
        worldModel.setName("Middle Earth");
        worldModel.setDescription("The world of The Hobbit and The Lord of the Rings");
        worldModel.setCreationDate(OffsetDateTime.now());
        worldModel.setOwner(new UserLink());
        worldModel.getOwner().setId(UUID.randomUUID().toString());
        worldModel.getOwner().setName("Tolkien");
        return worldModel;
    }
}
