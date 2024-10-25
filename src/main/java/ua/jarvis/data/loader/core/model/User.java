package ua.jarvis.data.loader.core.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import ua.jarvis.data.loader.core.model.enums.BooleanType;
import ua.jarvis.data.loader.core.model.enums.Sex;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 50)
	@Column(length = 50, name = "name")
	private String name;

	@Size(max = 50)
	@Column(length = 50, name = "middle_name")
	private String middleName;

	@Size(max = 50)
	@Column(length = 50, name = "sur_name")
	private String surName;

	@Size(max = 10)
	@Column(length = 10, name = "rnokpp")
	private String rnokpp;

	@Enumerated(EnumType.STRING)
	@Column(length = 10, name = "sex")
	private Sex sex;

	@Size(max = 500)
	@Column(length = 500, name = "illegal_actions")
	private String illegalActions;

	@Enumerated(EnumType.STRING)
	@Column(length = 7, name = "individual_entrepreneur")
	private BooleanType individualEntrepreneur;

	@OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private BirthCertificate birthCertificate;

	@OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private Photo photo;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(
		name = "user_siblings",
		joinColumns = @JoinColumn(name = "user_id"),
		inverseJoinColumns = @JoinColumn(name = "sibling_id")
	)
	private Set<User> siblings = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Set<User> children = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Set<User> parents = new HashSet<>();

	@ManyToMany(mappedBy = "drivers", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Set<Car> cars = new HashSet<>();

	@ManyToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Set<JuridicalPerson> juridicalPersons = new HashSet<>();

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Set<Passport> passports = new HashSet<>();

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Set<DriverLicense> driverLicense = new HashSet<>();

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Set<ForeignPassport> foreignPassports = new HashSet<>();

	@ManyToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Set<Address> addresses = new HashSet<>();

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Set<Email> emails = new HashSet<>();

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private Set<Phone> phones = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinTable(
		name = "individual_entrepreneur_addresses",
		joinColumns = @JoinColumn(name = "individual_entrepreneur_id"),
		inverseJoinColumns = @JoinColumn(name = "addresses_id")
	)
	private Set<Address> individualEntrepreneurAddresses = new HashSet<>();

	public User() {}

	public BooleanType getIndividualEntrepreneur() {
		return individualEntrepreneur;
	}

	public void setIndividualEntrepreneur(final BooleanType individualEntrepreneur) {
		this.individualEntrepreneur = individualEntrepreneur;
	}

	public Set<Address> getIndividualEntrepreneurAddresses() {
		return individualEntrepreneurAddresses;
	}

	public void setIndividualEntrepreneurAddresses(final Set<Address> isiIndividualEntrepreneurAddresses) {
		this.individualEntrepreneurAddresses = isiIndividualEntrepreneurAddresses;
	}

	public Set<User> getSiblings() {
		return siblings;
	}

	public void setSiblings(final Set<User> siblings) {
		this.siblings = siblings;
	}

	public Set<User> getChildren() {
		return children;
	}

	public void setChildren(final Set<User> children) {
		this.children = children;
	}

	public Set<User> getParents() {
		return parents;
	}

	public void setParents(final Set<User> parents) {
		this.parents = parents;
	}

	public Set<JuridicalPerson> getJuridicalPersons() {
		return juridicalPersons;
	}

	public void setJuridicalPersons(final Set<JuridicalPerson> juridicalPersons) {
		this.juridicalPersons = juridicalPersons;
	}

	public String getIllegalActions() {
		return illegalActions;
	}

	public void setIllegalActions(final String illegalActions) {
		this.illegalActions = illegalActions;
	}

	public BirthCertificate getBirthCertificate() {
		return birthCertificate;
	}

	public void setBirthCertificate(final BirthCertificate birthCertificate) {
		this.birthCertificate = birthCertificate;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(final String midlName) {
		this.middleName = midlName;
	}

	public String getSurName() {
		return surName;
	}

	public void setSurName(final String surName) {
		this.surName = surName;
	}

	public String getRnokpp() {
		return rnokpp;
	}

	public void setRnokpp(final String rnokpp) {
		this.rnokpp = rnokpp;
	}

	public Sex getSex() {return sex;}

	public void setSex(final Sex sex) {
		this.sex = sex;
	}

	public Set<Email> getEmails() {
		return emails;
	}

	public void setEmails(final Set<Email> emails) {
		this.emails = emails;
	}

	public Set<DriverLicense> getDriverLicense() {
		return driverLicense;
	}

	public void setDriverLicense(final Set<DriverLicense> driverLicense) {
		this.driverLicense = driverLicense;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(final Photo photo) {
		this.photo = photo;
	}

	public Set<Phone> getPhones() {
		return phones;
	}

	public void setPhones(final Set<Phone> phones) {
		this.phones = phones;
	}

	public Set<ForeignPassport> getForeignPassports() {
		return foreignPassports;
	}

	public void setForeignPassports(final Set<ForeignPassport> foreignPassports) {
		this.foreignPassports = foreignPassports;
	}

	public Set<Passport> getPassports() {
		return passports;
	}

	public void setPassports(final Set<Passport> passports) {
		this.passports = passports;
	}

	public Set<Address> getAddresses() {
		return addresses;
	}

	public void setAddresses(final Set<Address> addresses) {
		this.addresses = addresses;
	}

	public Set<Car> getCars() {
		return cars;
	}

	public void setCars(final Set<Car> cars) {
		this.cars = cars;
	}

	public void addEmail(final Email email) {
		this.emails.add(email);
	}

	public void addPhone(final Phone phone) {
		this.phones.add(phone);
	}

	public void addAddress(final Address address) {
		this.addresses.add(address);
	}

	@Override
	public String toString() {
		return "User{" +
			"id=" + id +
			", name='" + name + '\'' +
			", midlName='" + middleName + '\'' +
			", surName='" + surName + '\'' +
			", rnokpp='" + rnokpp + '\'' +
			", sex='" + sex + '\'' +
			'}';
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final User user = (User) o;
		return Objects.equals(rnokpp, user.rnokpp);
	}

	@Override
	public int hashCode() {
		return Objects.hash(rnokpp);
	}
}
