package co.edu.umb.guide1mobileengineering.domain.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "`user`")
public class User {

  @Id
  @SequenceGenerator(
    name = "user_id_sequence",
    sequenceName = "user_id_sequence"
  )
  @GeneratedValue(
    strategy = GenerationType.SEQUENCE,
    generator = "user_id_sequence"
  )
  private Integer id;

  @Column(length = 40, unique = true, nullable = false)
  private String email;

  @Column(length = 120, nullable = false)
  private String fullName;

  @Column(length = 200, nullable = false)
  private String password;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
    User user = (User) o;
    return id != null && Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
