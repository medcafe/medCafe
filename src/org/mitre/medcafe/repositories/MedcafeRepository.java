package org.mitre.medcafe.repositories;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.mitre.medcafe.hdatabased.encounter.EncounterDetail;
import org.mitre.medcafe.util.NotImplementedException;
import org.mitre.medcafe.util.Repository;
import org.projecthdata.hdata.schemas._2009._06.allergy.Allergy;
import org.projecthdata.hdata.schemas._2009._06.allergy.Product;
import org.projecthdata.hdata.schemas._2009._06.allergy.Reaction;
import org.projecthdata.hdata.schemas._2009._06.condition.Condition;
import org.projecthdata.hdata.schemas._2009._06.immunization.Immunization;
import org.projecthdata.hdata.schemas._2009._06.medication.Medication;
import org.projecthdata.hdata.schemas._2009._06.patient_information.Patient;
import org.projecthdata.hdata.schemas._2009._06.result.Result;
import org.projecthdata.hdata.schemas._2009._06.support.Support;

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
	public Collection<FMRecord> getTimeLineInfo(String ien)
			throws NotImplementedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<EncounterDetail> getPatientEncounters(String id)
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
	public TreeSet<Reaction> generateAllergyReactionList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeSet<Product> generateAllergyReactantList() {
		// TODO Auto-generated method stub
		return null;
	}

}
