package org.mitre.android.tutorial;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Adapter;

public class MedicationAdapter extends BaseAdapter {
	private Context context = null;
	private List<Medication> medications = null;
	
	public MedicationAdapter(Context context, List<Medication> medications){
		this.context = context;
		this.medications = medications;
	}
	
	public int getCount() {
		return medications.size();
	}

	public Object getItem(int position) {
		return medications.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		// A ViewHolder keeps references to children views to avoid unneccessary calls
        // to findViewById() on each row.
        ViewHolder holder;

        // When convertView is not null, we can reuse it directly, there is no need
        // to reinflate it. We only inflate a new View when the convertView supplied
        // by ListView is null.
        if (convertView == null) {
            convertView = new MedicationView(context);

            // Creates a ViewHolder and store references to the two children views
            // we want to bind data to.
            holder = new ViewHolder();
            holder.item = (MedicationView)convertView;
            convertView.setTag(holder);
            
        } else {
            // Get the ViewHolder back to get fast access to the TextView
            // and the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }
        Medication medication = medications.get(position);
      

        holder.item.setNarrative(medication.getNarrative());
     
        holder.item.setEffectiveTime(medication.getEffectiveTime());
        holder.item.setDelivery(medication.getDeliveryMethod());
        holder.item.setDose(medication.getDose());
        holder.item.setMedication(medication.getMedication());
        holder.item.setInstructions(medication.getPatientInstructions());
        holder.item.adjustView();
        return convertView;
	}
	
	 static class ViewHolder {
         MedicationView item;
     }
}
