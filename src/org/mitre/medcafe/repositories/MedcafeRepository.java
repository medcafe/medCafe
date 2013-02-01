package org.mitre.medcafe.repositories;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hl7.greencda.c32.Allergy;
import org.hl7.greencda.c32.Condition;
import org.hl7.greencda.c32.Encounter;
import org.hl7.greencda.c32.Immunization;
import org.hl7.greencda.c32.Medication;
import org.hl7.greencda.c32.Procedure;
import org.hl7.greencda.c32.Result;
import org.hl7.greencda.c32.SocialHistory;
import org.hl7.greencda.c32.Support;
import org.mitre.medcafe.model.Patient;
import org.mitre.medcafe.util.NotImplementedException;
import org.mitre.medcafe.util.Repository;

import com.medsphere.fileman.FMRecord;

public class MedcafeRepository extends Repository {

	public MedcafeRepository(HashMap<String, String> credMap) {
		super(credMap);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Patient getPatient(String patientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getPatientByName(String family, String given,
			String middle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, String> getPatients() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Allergy> getAllergies(String patientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertAllergies(String patientId,
			Collection<Allergy> allergies) throws NotImplementedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Medication> getMedications(String patientId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Condition> getProblems(String patientId)
			throws NotImplementedException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public List<SocialHistory> getSocialHistory(String patientId)
			throws NotImplementedException {
		// TODO Auto-generated method stub
		return null;
	}
 
	@Override
	public List<Support> getSupportInfo(String patientId)
			throws NotImplementedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Immunization> getImmunizations(String id)
			throws NotImplementedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FMRecord> getTimeLineInfo(String ien)
			throws NotImplementedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Encounter> getPatientEncounters(String id)
			throws NotImplementedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Result> getLatestVitals(String id)
			throws NotImplementedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Result> getAllVitals(String id) throws NotImplementedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Procedure> getProcedures(String patientId)
			throws NotImplementedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPatientID(String family, String given)
			throws NotImplementedException {
		// TODO Auto-generated method stub
		return null;
	}

	

}
