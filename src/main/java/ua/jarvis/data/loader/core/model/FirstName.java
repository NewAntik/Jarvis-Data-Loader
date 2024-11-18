package ua.jarvis.data.loader.core.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_first_names")
public class FirstName extends BaseNameEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	public FirstName() {
	}

	public FirstName(final String firstName){
		super.setValue(firstName);
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

}
