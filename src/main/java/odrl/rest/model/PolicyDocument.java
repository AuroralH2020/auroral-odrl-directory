package odrl.rest.model;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.google.gson.JsonObject;

@Entity
public class PolicyDocument {
	
	@Id
	public String id;
	@Column(name = "policy_doc", columnDefinition = "BLOB")
	@Lob
	public String policyDocument;

	public PolicyDocument(String id, String policyDocument) {
		super();
		this.policyDocument = policyDocument;
		this.id = id;
	}

	public PolicyDocument(String policyDocument) {
		super();
		this.policyDocument = policyDocument;
		this.id = UUID.randomUUID().toString();
	}

	public PolicyDocument() {
		super();
	}

	public String getPolicyDocument() {
		return policyDocument;
	}

	public void setPolicyDocument(String policyDocument) {
		this.policyDocument = policyDocument;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, policyDocument);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if ((obj == null) || (getClass() != obj.getClass()))
			return false;
		PolicyDocument other = (PolicyDocument) obj;
		return Objects.equals(id, other.id) && Objects.equals(policyDocument, other.policyDocument);
	}

	@Override
	public String toString() {
		JsonObject obj = new JsonObject();
		obj.addProperty("id", id);
		obj.addProperty("odrl", policyDocument);
		return obj.toString();
	}

}
