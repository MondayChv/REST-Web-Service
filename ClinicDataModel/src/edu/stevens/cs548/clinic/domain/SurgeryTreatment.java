package edu.stevens.cs548.clinic.domain;

import edu.stevens.cs548.clinic.domain.Treatment;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: SurgeryTreatment
 *
 */
@Entity
@DiscriminatorValue("S")
public class SurgeryTreatment extends Treatment implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.DATE)
	private Date s_date;
	
	
	public Date getDate() {
		return s_date;
	}
	public void setDate(Date s_date) {
		this.s_date = s_date;
	}
	
	
	private Provider Surgeon;
	
	
	public Provider getSurgeon() {
		return Surgeon;
	}

	public void setSurgeon() {
		this.Surgeon = this.getProvider();
		
	}
	public void visit(ITreatmentVisitor visitor) {
		visitor.visitSurgery(this.getId(), this.getDiagnosis(),
				this.s_date,this.getProvider());
	}
	public SurgeryTreatment() {
		super();
		this.setTreatmentType("S");
	}
   
}
