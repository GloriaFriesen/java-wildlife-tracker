import org.junit.*;
import static org.junit.Assert.*;
import org.sql2o.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class AnimalTest {
  @Rule
  public DatabaseRule database = new DatabaseRule();

  @Test
  public void animal_instantiatesCorrectly_false() {
    Animal testAnimal = new Animal("Deer");
    assertEquals(true, testAnimal instanceof Animal);
  }

  @Test
  public void getName_animalInstantiatesWithName_Deer() {
    Animal testAnimal = new Animal("Deer");
    assertEquals("Deer", testAnimal.getName());
  }

  @Test
  public void equals_returnsTrueIfNameIsTheSame_false() {
    Animal firstAnimal = new Animal("Deer");
    Animal anotherAnimal = new Animal("Deer");
    assertTrue(firstAnimal.equals(anotherAnimal));
  }

  @Test
  public void save_assignsIdToObjectAndSavesObjectToDatabase() {
    Animal testAnimal = new Animal("Deer");
    testAnimal.save();
    Animal savedAnimal = Animal.all().get(0);
    assertEquals(testAnimal.getId(), savedAnimal.getId());
  }

  @Test
  public void all_returnsAllInstancesOfAnimal_false() {
    Animal firstAnimal = new Animal("Deer");
    firstAnimal.save();
    Animal secondAnimal = new Animal("Black Bear");
    secondAnimal.save();
    assertEquals(true, Animal.all().get(0).equals(firstAnimal));
    assertEquals(true, Animal.all().get(1).equals(secondAnimal));
  }

  @Test
  public void find_returnsAnimalWithSameId_secondAnimal() {
    Animal firstAnimal = new Animal("Deer");
    firstAnimal.save();
    Animal secondAnimal = new Animal("Black Bear");
    secondAnimal.save();
    assertEquals(Animal.find(secondAnimal.getId()), secondAnimal);
  }

  @Test
  public void delete_deletesAnimalFromDatabase_0() {
    Animal testAnimal = new Animal("Deer");
    testAnimal.save();
    testAnimal.delete();
    assertEquals(0, Animal.all().size());
  }

  public void updateName_updatesAnimalNameInDatabase_String() {
    Animal testAnimal = new Animal("Deer");
    testAnimal.save();
    testAnimal.updateName("Buck");
    assertEquals("Buck", testAnimal.getName());
  }

  @Test
  public void find_returnsNullWhenNoAnimalFound_null() {
    assertTrue(Animal.find(999) == null);
  }

  @Test
  public void getSightings_returnsAllSightings_List() {
    Animal testAnimal = new Animal("Fox");
    testAnimal.save();
    Sighting firstSighting = new Sighting(testAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    firstSighting.save();
    Sighting secondSighting = new Sighting(testAnimal.getId(), "45.472428, -121.946466", "Ranger Avery");
    secondSighting.save();
    Sighting[] sightings = new Sighting[] { firstSighting, secondSighting };
    assertTrue(testAnimal.getSightings().containsAll(Arrays.asList(sightings)));
  }

  @Test
  public void save_returnsTrueIfAnimalsAreTheSame() {
    Animal testAnimal = new Animal("Fox");
    testAnimal.save();
    assertTrue(Animal.all().get(0).equals(testAnimal));
  }

  @Test(expected = IllegalArgumentException.class)
  public void animal_throwsExceptionIfAnimalHasEmptyParameters() {
    Animal testAnimal = new Animal("");
  }
}
