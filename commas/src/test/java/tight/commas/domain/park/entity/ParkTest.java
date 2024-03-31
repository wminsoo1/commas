package tight.commas.domain.park.entity;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import tight.commas.domain.Address;
import tight.commas.domain.park.Tag;
import tight.commas.domain.park.repository.ParkRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class ParkTest {

    @Autowired
    ParkRepository parkRepository;
    @Autowired
    EntityManager em;

    @Test
    void 공원에_태그_추가하기() {
        //given
        Park park = new Park();
        park.saveParkInfo(
                "Example Park",
                "This is a test park",
                new Address("123 Street", 100, 100),
                "Playground, Picnic Area",
                "Oak, Maple",
                "example.com/image.jpg"
        );

        //when
        em.persist(park);
        //then

        ParkTag walkTag = new ParkTag();
        walkTag.setTag(Tag.WALK);
        walkTag.addPark(park);

        ParkTag prettyTag = new ParkTag();
        prettyTag.setTag(Tag.PRETTY);
        prettyTag.addPark(park);

        List<ParkTag> parkTags = new ArrayList<>();
        parkTags.add(walkTag);
        parkTags.add(prettyTag);


        Park foundPark = parkRepository.findById(park.getId()).orElseThrow();
        assertNotNull(foundPark);
        assertEquals(2, foundPark.getParkTags().size());

    }

}