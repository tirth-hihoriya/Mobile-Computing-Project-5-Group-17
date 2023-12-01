package com.example.mobilecomp1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class RemediesActivity extends AppCompatActivity {
    private TextView RemRes;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remediesactivity);
        RemRes = findViewById(R.id.remediesResult);
        RemRes.setText(" ");
        PrintRemedies();
    }
    public void onReportSymptomsMailPROF(View view){
        Intent intent = new Intent(RemediesActivity.this, MessageActivity.class);
        startActivity(intent);
    }
    private void PrintRemedies(){
        HashMap<String, Double> symOps = ReportSymptomsActivity.getSymOps();
        for (HashMap.Entry<String,Double> entry : symOps.entrySet()){
            String a = entry.getKey();
            Double value = entry.getValue();
            if(value != 0.0){
                Log.d("EntryKey",entry.getKey());

                Log.d("a",a);
                Log.d("value", String.valueOf(value));
                if(a.equals("Headache")) {
                    if (value <= 3.0 && value >= 0.0) {
                        RemRes.append("Headache: \nFor a mild headache (below 3 out of 5), you can try drinking plenty of water, taking a short break to rest your eyes, or doing some gentle neck and shoulder stretches.\n Ensure you're in a well-ventilated space and consider dimming lights to reduce strain.\n");
                    } else if (entry.getValue() <= 5.0 && entry.getValue() > 3.0) {
                        RemRes.append("Headache: \nIf your headache is more intense (above 3 out of 5), try resting in a quiet, dark room, applying a cold or warm compress to your forehead or neck, and staying hydrated.\n Over-the-counter pain relievers like acetaminophen, ibuprofen, or aspirin might also be considered, but consult with a healthcare professional if you have concerns or if the headache persists.\n");
                    } else
                        break;
                }
                 if(a.equals("ShortnessofBreath")) {
                    if (value <= 3.0 && value >= 0.0) {
                        RemRes.append("Shortness of Breath:\nFor mild shortness of breath (below 3 out of 5), practice slow, deep breathing exercises, maintain good posture, and ensure your environment is well-ventilated.\n Stay calm and avoid triggers like smoke or strong odors.\n If shortness of breath continues, consult with a healthcare professional for a thorough evaluation.\n");
                    } else if (value <= 5.0 && value > 3.0) {
                        RemRes.append("Shortness of Breath:\nIf shortness of breath is more intense (above 3 out of 5), it's crucial to seek immediate medical attention.\n Call emergency services or go to the nearest emergency room.\n Severe shortness of breath can be a sign of a serious medical condition, and professional evaluation is necessary to determine the underlying cause and appropriate treatment.\n");
                    } else
                        break;
                }
                 if(a.equals("FeelingTired")) {
                    if (value <= 3.0 && value >= 0.0) {
                        RemRes.append("Feeling Tired :\nFor mild fatigue (below 3 out of 5), consider taking short breaks to stretch and move around, stay hydrated, and ensure you're getting adequate sleep.\n A healthy snack with a mix of protein and carbohydrates can provide a quick energy boost.\n If fatigue persists, it may be beneficial to evaluate your sleep patterns, stress levels, and overall lifestyle for potential improvements.\n");
                    } else if (value <= 5.0 && value > 3.0) {
                        RemRes.append("Feeling Tired :\nIf fatigue is more intense (above 3 out of 5), prioritize getting ample rest by ensuring you have a good sleep routine.\n Consider short power naps if possible. Stay hydrated, and incorporate light physical activity into your day to boost energy.\n Evaluate your diet for nutritional balance and consider consulting with a healthcare professional to rule out underlying health issues contributing to fatigue.\n");
                    } else
                        break;
                }
                 if(a.equals("LossofSmellorTaste")) {
                    if (value <= 3.0 && value >= 0.0) {
                        RemRes.append("For mild loss of taste or smell (below 3 out of 5), consider aromatic remedies like sniffing strong scents such as essential oils. Stay well-hydrated, and include flavorful foods in your diet to stimulate taste. Good oral hygiene can also contribute to a heightened sense of taste. If the issue persists or worsens, consult with a healthcare professional for further evaluation.\n");
                    } else if (value <= 5.0 && value > 3.0) {
                        RemRes.append("f the loss of taste or smell is more intense (above 3 out of 5), it's crucial to consult with a healthcare professional for proper evaluation. In the meantime, maintain good oral hygiene, stay hydrated, and consider using strong flavors in your meals. Avoid irritants like smoke, and be cautious with hot foods to prevent burns if your sense of temperature is affected. Early medical attention is essential for persistent or severe cases.\n");
                    } else
                        break;
                }
                 if(a.equals("MuscleAche")) {
                    if (value <= 3.0 && value >= 0.0) {
                        RemRes.append("Loss of Smell or Taste:\nFor mild muscle aches (below 3 out of 5), consider gentle stretching, applying a warm compress, or taking a warm bath.\n Over-the-counter pain relievers like acetaminophen or ibuprofen may provide relief.\n Ensure you're well-hydrated and get adequate rest to support muscle recovery.\n");
                    } else if (value <= 5.0 && value > 3.0) {
                        RemRes.append("Loss of Smell or Taste:\nIf muscle aches are more intense (above 3 out of 5), prioritize rest and avoid activities that worsen the pain.\n Consider using ice or a cold compress for the first 48 hours, then switch to heat to promote blood flow and ease muscle tension.\n Over-the-counter pain relievers like ibuprofen or acetaminophen can help, but consult with a healthcare professional if the pain persists or is severe.\n Stretch gently and maintain good hydration to support muscle recovery.\n");
                    } else
                        break;
                }
                 if(a.equals("Diarrhea")) {
                    if (value <= 3.0 && value >= 0.0) {
                        RemRes.append("Diarrhea :\nFor mild diarrhea (below 3 out of 5), focus on staying hydrated with clear fluids like water, herbal teas, or electrolyte solutions.\n Eating bland, easily digestible foods such as bananas, rice, applesauce, and toast (BRAT diet) may help.\n Consider avoiding dairy, caffeine, and high-fat or spicy foods.\n If symptoms persist or worsen, consult a healthcare professional.\n");
                    } else if (value <= 5.0 && value > 3.0) {
                        RemRes.append("Diarrhea :\nIf diarrhea is more intense (above 3 out of 5), prioritize fluid intake to prevent dehydration, and consider oral rehydration solutions.\n Stick to the BRAT diet (bananas, rice, applesauce, and toast) to ease digestion.\n Avoid dairy, caffeine, and fatty or spicy foods.\n If diarrhea persists or is accompanied by severe symptoms, consult with a healthcare professional for appropriate advice and possible medication.\n");
                    } else
                        break;
                }
                 if(a.equals("Nausea")) {
                    if (value <= 3.0 && value >= 0.0) {
                        RemRes.append("Nausea :\nFor mild nausea (below 3 out of 5), you can try ginger tea, peppermint, deep breathing exercises, or eating small, bland snacks like crackers.\n Staying hydrated and avoiding strong odors may also help alleviate symptoms.\n");
                    } else if (value <= 5 && value > 3.0) {
                        RemRes.append("Nausea :\nIf nausea is more intense (above 3 out of 5), consider sipping ginger ale or ginger tea, taking slow deep breaths, and resting in a comfortable position.\n Over-the-counter anti-nausea medications like dimenhydrinate or meclizine may be helpful.\n Consult with a healthcare professional for personalized advice based on your specific situation.\n");
                    } else
                        break;
                }
                 if(a.equals("SoarThroat" ) ){
                    if (value <= 3.0 && value >= 0.0) {
                        RemRes.append("Sore Throat: \nFor a mild sore throat (below 3 out of 5), try soothing remedies like drinking warm tea with honey, gargling with salt water, or using throat lozenges.\n Stay hydrated, and consider using a humidifier to add moisture to the air.\n Resting your voice and avoiding irritants like smoke may also help.\n");
                    } else if (value <= 5.0 && value > 3.0) {
                        RemRes.append("Sore Throat: \nIf your sore throat is more intense (above 3 out of 5), try warm saltwater gargles, throat lozenges with numbing agents, and staying well-hydrated.\n Over-the-counter pain relievers like acetaminophen or ibuprofen can help reduce pain and inflammation.\n Consider using throat sprays or throat coat teas for additional relief.\n If the sore throat persists or is accompanied by severe symptoms, consult with a healthcare professional.\n");
                    } else
                        break;
                }
                 if(a.equals( "Fever")) {
                    if (value <= 3.0 && value >= 0.0) {
                        RemRes.append("Fever: \nFor a mild fever (below 3 out of 5), focus on staying hydrated with water, and get plenty of rest.\n You can use a damp cloth to cool your forehead and consider taking over-the-counter medications like acetaminophen or ibuprofen according to the recommended dosage.\n If the fever persists or worsens, consult with a healthcare professional.\n");
                    } else if (value <= 5.0 && value > 3.0) {
                        RemRes.append("Fever: \nIf the fever is more intense (above 3 out of 5), prioritize rest and stay well-hydrated with water or electrolyte solutions.\n Use a cool compress or take a tepid bath to help bring down the temperature.\n Over-the-counter fever-reducing medications like acetaminophen or ibuprofen can be considered, but adhere to recommended dosages.\n If the fever persists or is accompanied by severe symptoms, seek medical advice promptly.\n");
                    } else
                        break;
                }
                if(a.equals( "Cough")) {
                    if (value <= 3.0 && value >= 0.0) {
                        RemRes.append("Cough: \nFor a mild cough (below 3 out of 5), consider home remedies such as drinking warm tea with honey, staying hydrated, and using throat lozenges.\n Ensure the air is humidified, and consider inhaling steam.\n Rest and avoid irritants like smoke.\n If the cough persists or worsens, consult with a healthcare professional for further guidance.\n");
                    } else if (value <= 5.0 && value > 3.0) {
                        RemRes.append("Cough: \nFor a more intense cough (above 3 out of 5), consider using over-the-counter cough medications that contain ingredients like dextromethorphan or guaifenesin.\n Stay hydrated, and use a humidifier to add moisture to the air.\n Warm saltwater gargles or throat lozenges may provide relief.\n If the cough persists or is accompanied by other concerning symptoms, consult with a healthcare professional for personalized advice and possible further evaluation.\n");
                    } else
                        break;
                }
                if(a.isEmpty()) {
                    RemRes.append("\nNo Symptoms have been selected.\n");
                    break;
                }
                }

        }
    }
}
