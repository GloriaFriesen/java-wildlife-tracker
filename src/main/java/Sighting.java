import org.sql2o.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.sql.Timestamp;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Sighting implements DatabaseManagement {
  private int animal_id;
  private String location;
  private String ranger_name;
  private int id;
  private Timestamp time_seen;

  public Sighting(int animal_id, String location, String ranger_name) {
    if (location.equals("") || ranger_name.equals("")) {
      throw new IllegalArgumentException("Please complete the form to submit a sighting.");
    }
    this.animal_id = animal_id;
    this.location = location;
    this.ranger_name = ranger_name;
    this.id = id;
  }

  @Override
  public int getId() {
    return id;
  }

  public int getAnimalId() {
    return animal_id;
  }

  public String getLocation() {
    return location;
  }

  public String getRangerName() {
    return ranger_name;
  }

  @Override
  public boolean equals(Object otherSighting) {
    if(!(otherSighting instanceof Sighting)) {
      return false;
    } else {
      Sighting newSighting = (Sighting) otherSighting;
      return this.getAnimalId() == (newSighting.getAnimalId()) && this.getLocation().equals(newSighting.getLocation()) && this.getRangerName().equals(newSighting.getRangerName());
    }
  }

  @Override
  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO sightings (animal_id, location, ranger_name, time_seen) VALUES (:animal_id, :location, :ranger_name, now());";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("animal_id", this.animal_id)
        .addParameter("location", this.location)
        .addParameter("ranger_name", this.ranger_name)
        .throwOnMappingFailure(false)
        .executeUpdate()
        .getKey();
    }
  }

  public static List<Sighting> all() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM sightings;";
      return con.createQuery(sql)
        .throwOnMappingFailure(false)
        .executeAndFetch(Sighting.class);
    }
  }

  public static Sighting find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM sightings WHERE id=:id;";
      Sighting sighting = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Sighting.class);
      return sighting;
    } catch (IndexOutOfBoundsException exception) {
      return null;
    }
  }

  @Override
  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "DELETE FROM sightings WHERE id=:id";
      con.createQuery(sql)
        .addParameter("id", this.id)
        .executeUpdate();
    }
  }

  public Timestamp getTimeSeen() {
    return time_seen;
  }

  public String getFormattedTime() {
    Date date = new Date();
    date.setTime(time_seen.getTime());
    String formattedDate = new SimpleDateFormat("EEE, d MMM yyyy hh:mm aaa").format(date);
    return formattedDate;
  }

  public void update(int animal_id, String location, String ranger_name) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE sightings SET animal_id=:animal_id, location=:location, ranger_name=:ranger_name WHERE id=:id";
      con.createQuery(sql)
        .addParameter("animal_id", animal_id)
        .addParameter("location", location)
        .addParameter("ranger_name", ranger_name)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

}
