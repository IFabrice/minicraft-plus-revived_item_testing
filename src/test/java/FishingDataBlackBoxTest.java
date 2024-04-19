import minicraft.item.ClothingItem;
import minicraft.item.FishingData;
import minicraft.item.Item;
import minicraft.item.Items;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FishingDataBlackBoxTest {

	List<List<String>> data = new ArrayList<>();
	String[] keys = {"fish", "tool", "junk", "rare"};

	@BeforeEach
	public void setUp() {
        for (String key : keys) {
            data.add(FishingData.getData(key));
        }
	}

	@Test
	public void testDataDoesNotOverlap() {
        assertEquals(4, data.stream().distinct().count());
	}
}
