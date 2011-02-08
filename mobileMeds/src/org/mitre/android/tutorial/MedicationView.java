package org.mitre.android.tutorial;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MedicationView extends LinearLayout {
	

	
	private Medication medication = null; 
	 
 
	 TextView narrativeView = null; 
	 TextView medicationView = null; 
	 TextView instructionsView = null; 
	 TextView deliveryView = null; 
	 TextView effectiveTimeView = null; 
	 TextView doseView = null; 

    public MedicationView(Context context) { 
	 	 super(context); 
	 	 LayoutInflater.from(context).inflate(R.layout.medication, this); 
	 	 


	 	narrativeView = (TextView) findViewById(R.id.narrative); 
	 	medicationView = (TextView) findViewById(R.id.medication); 
	 	instructionsView = (TextView) findViewById(R.id.patientInstructions); 
	 	deliveryView = (TextView) findViewById(R.id.deliveryMethod); 
	 	effectiveTimeView = (TextView) findViewById(R.id.effectiveTime); 
	 	doseView = (TextView) findViewById(R.id.dose); 

	        //repeat with other views	 	 
	 } 
	 
	 public MedicationView(Context context, Medication medication) { 
	 	 this(context); 
	 	 this.medication = medication; 

	 	 this.narrativeView.setText(medication.getNarrative()); 
	 	 this.medicationView.setText(medication.getMedication()); 
	 	 this.instructionsView.setText(medication.getPatientInstructions()); 
	 	 this.deliveryView.setText(medication.getDeliveryMethod()); 
	 	 this.effectiveTimeView.setText(medication.getEffectiveTime()); 
	 	 this.doseView.setText(medication.getDose()); 
		 	 //repeat with other views... 
	 } 
	 

	 
	  public void setNarrative(String narrative){
		  narrativeView.setText(narrative);
		}
	
	  public void setMedication(String medication){
		  medicationView.setText(medication);
		}
	  
	  public void setDelivery(String delivery){
		  deliveryView.setText(delivery);
		}
	  
	  public void setEffectiveTime(String effectiveTime){
		  effectiveTimeView.setText(effectiveTime);
		}
	  
	  public void setDose(String dose){
		  doseView.setText(dose);
		}
	  
	  public void setInstructions(String instructions){
		  instructionsView.setText(instructions);
		}
	  public void adjustView()
	  {

			 changeHeightAndWidth(narrativeView);
			 changeHeightAndWidth(medicationView);
			 changeHeightAndWidth(instructionsView);
			 changeHeightAndWidth(deliveryView);
			 changeHeightAndWidth(effectiveTimeView);
			 changeHeightAndWidth(doseView);

	  }
	  public void changeHeightAndWidth(TextView current)
	  {
		  if (current != null)
		  {
			  if (current.getText() == null || current.getText().equals(""))
			  {
				  current.setVisibility(GONE);

			  }
			  else
			  {
				  current.setVisibility(VISIBLE);

			  }
		  }
	  }
	  
}