package com.tx.practisesmanagement.model;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ContactWorker {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String email;
	private String telefone;
	private String name;
	
	public ContactWorker(String email, String telefone, String name) {
		super();
		this.email = email;
		this.telefone = telefone;
		this.name = name;
	}

	public ContactWorker() {
		super();
	}

	public ContactWorker(Integer id) {
		super();
		this.id = id;
	}
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContactWorker other = (ContactWorker) obj;
		return Objects.equals(id, other.id);
	}

	@Override
	public String toString() {
		return "ContactWorker [id=" + id + ", email=" + email + ", telefone=" + telefone + ", name=" + name + "]";
	}
	
	
	
	
}
