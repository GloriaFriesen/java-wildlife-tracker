import org.junit.*;
import org.sql2o.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.text.DateFormat;
import java.util.Date;
import java.sql.Timestamp;

public class SightingTest {

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void sighting_instantiatesCorrectly_true() {
    Animal testAnimal = new Animal("Deer");
    testAnimal.save();
    Sighting testSighting = new Sighting(testAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    assertEquals(true, testSighting instanceof Sighting);
  }

  @Test
  public void equals_returnsTrueIfLocationAndDescriptionAreSame_true() {
    Animal testAnimal = new Animal("Deer");
    testAnimal.save();
    Sighting testSighting = new Sighting(testAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    Sighting anotherSighting = new Sighting(testAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    assertTrue(testSighting.equals(anotherSighting));
  }

  @Test
  public void save_insertsObjectIntoDatabase_Sighting() {
    Animal testAnimal = new Animal("Deer");
    testAnimal.save();
    Sighting testSighting = new Sighting (testAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    testSighting.save();
    assertEquals(true, Sighting.all().get(0).equals(testSighting));
  }

  @Test
  public void all_returnsAllInstancesOfSighting_true() {
    Animal testAnimal = new Animal("Deer");
    testAnimal.save();
    Sighting testSighting = new Sighting (testAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    testSighting.save();
    Animal secondTestAnimal = new Animal("Badger");
    secondTestAnimal.save();
    Sighting secondTestSighting = new Sighting (testAnimal.getId(), "45.472428, -121.946466", "Ranger Reese");
    secondTestSighting.save();
    assertEquals(true, Sighting.all().get(0).equals(testSighting));
    assertEquals(true, Sighting.all().get(1).equals(secondTestSighting));
  }

  @Test
  public void find_returnsSightingWithSameId_secondSighting() {
    Animal testAnimal = new Animal("Deer");
    testAnimal.save();
    Sighting testSighting = new Sighting (testAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    testSighting.save();
    Animal secondTestAnimal = new Animal("Badger");
    secondTestAnimal.save();
    Sighting secondTestSighting = new Sighting (testAnimal.getId(), "45.472428, -121.946466", "Ranger Reese");
    secondTestSighting.save();
    assertEquals(Sighting.find(secondTestSighting.getId()), secondTestSighting);
  }

  @Test
  public void find_returnsNullWhenNoAnimalFound_null() {
    assertTrue(Animal.find(999) == null);
  }

  @Test
  public void save_assignsIdToSighting() {
    Animal testAnimal = new Animal("Deer");
    testAnimal.save();
    Sighting testSighting = new Sighting (testAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    testSighting.save();
    Sighting savedSighting = Sighting.all().get(0);
    assertEquals(savedSighting.getId(), testSighting.getId());
  }

  @Test
  public void Sighting_instantiatesWithLocation_String() {
    Animal testAnimal = new Animal("Deer");
    Sighting testSighting = new Sighting (testAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    assertEquals("45.472428, -121.946466", testSighting.getLocation());
  }

  @Test
  public void Sighting_instantiatesWithRangerName_String() {
    Animal testAnimal = new Animal("Deer");
    Sighting testSighting = new Sighting (testAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    assertEquals("Ranger Avery", testSighting.getRangerName());
  }

  @Test
  public void Sighting_instantiatesWithAnimalId_int() {
    Animal testAnimal = new Animal("Deer");
    Sighting testSighting = new Sighting (testAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    assertEquals(testAnimal.getId(), testSighting.getAnimalId());
  }

  @Test
  public void delete_deletesSighting_true() {
    Animal testAnimal = new Animal("Deer");
    Sighting testSighting = new Sighting (testAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    testSighting.save();
    testSighting.delete();
    assertEquals(null,  Sighting.find(testSighting.getId()));
  }

  @Test
  public void save_recordsTimeOfSightingInDatabase() {
    Animal testAnimal = new Animal("Deer");
    Sighting testSighting = new Sighting (testAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    testSighting.save();
    Timestamp savedTimeSeen = Sighting.find(testSighting.getId()).getTimeSeen();
    Timestamp rightNow = new Timestamp(new Date().getTime());
    assertEquals(rightNow.getDay(), savedTimeSeen.getDay());
  }

}
