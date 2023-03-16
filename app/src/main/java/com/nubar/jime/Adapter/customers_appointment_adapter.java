package com.nubar.jime.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nubar.jime.Classes.userAppointmentSchedule;
import com.nubar.jime.Classes.userAvailablitySchedule;
import com.nubar.jime.R;

import java.util.List;

public class customers_appointment_adapter extends RecyclerView.Adapter<customers_appointment_adapter.MyViewHolder> {
    Context context;
    List<userAppointmentSchedule> list;
    SharedPreferences preferences;
    private int lastPosition = -1;

    public customers_appointment_adapter(Context context, List<userAppointmentSchedule> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listViewItem = inflater.inflate(R.layout.layout_customers_appointment_ticket, null, true);
        return new customers_appointment_adapter.MyViewHolder(listViewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.day_title.setText(list.get(position).getDayofweek().toUpperCase());
        holder.sevenToeight.setText(list.get(position).getSevenToeight());
        holder.eightTonine.setText(list.get(position).getEightTonine());
        holder.nineToten.setText(list.get(position).getNineToten());
        holder.tenToeleven.setText(list.get(position).getTenToeleven());
        holder.elevenTotwelve.setText(list.get(position).getElevenTotwelve());
        holder.twelveToone.setText(list.get(position).getTwelveToone());
        holder.oneTotwo.setText(list.get(position).getOneTotwo());
        holder.twoTothree.setText(list.get(position).getTwoTothree());
        holder.threeTofour.setText(list.get(position).getThreeTofour());
        holder.fourTofive.setText(list.get(position).getFourTofive());
        holder.fiveTosix.setText(list.get(position).getFiveTosix());
        holder.sixToseven.setText(list.get(position).getSixToseven());

        if (holder.sevenToeight.getText().equals("")) {
            holder.sevenToeight.setText("N/A");
        }
        if (holder.eightTonine.getText().equals("")) {
            holder.eightTonine.setText("N/A");
        }
        if (holder.nineToten.getText().equals("")) {
            holder.nineToten.setText("N/A");
        }
        if (holder.tenToeleven.getText().equals("")) {
            holder.tenToeleven.setText("N/A");
        }
        if (holder.elevenTotwelve.getText().equals("")) {
            holder.elevenTotwelve.setText("N/A");
        }
        if (holder.twelveToone.getText().equals("")) {
            holder.twelveToone.setText("N/A");
        }
        if (holder.oneTotwo.getText().equals("")) {
            holder.oneTotwo.setText("N/A");
        }
        if (holder.twoTothree.getText().equals("")) {
            holder.twoTothree.setText("N/A");
        }
        if (holder.threeTofour.getText().equals("")) {
            holder.threeTofour.setText("N/A");
        }
        if (holder.fourTofive.getText().equals("")) {
            holder.fourTofive.setText("N/A");
        }
        if (holder.fiveTosix.getText().equals("")) {
            holder.fiveTosix.setText("N/A");
        }
        if (holder.sixToseven.getText().equals("")) {
            holder.sixToseven.setText("N/A");
        }
        setAnimation(holder.itemView, position);
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView sevenToeight, eightTonine, nineToten, tenToeleven, elevenTotwelve, twelveToone,
                oneTotwo, twoTothree, threeTofour, fourTofive, fiveTosix, sixToseven;
        TextView day_title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            day_title = itemView.findViewById(R.id.dob1);
            sevenToeight = itemView.findViewById(R.id.sevenToeight);
            eightTonine = itemView.findViewById(R.id.eightTonine);
            nineToten = itemView.findViewById(R.id.nineToten);
            tenToeleven = itemView.findViewById(R.id.tenToeleven);
            elevenTotwelve = itemView.findViewById(R.id.elevenTotweleve);
            twelveToone = itemView.findViewById(R.id.twelveToone);
            oneTotwo = itemView.findViewById(R.id.oneTotwo);
            twoTothree = itemView.findViewById(R.id.twoTothree);
            threeTofour = itemView.findViewById(R.id.threeTofour);
            fourTofive = itemView.findViewById(R.id.fourTofive);
            fiveTosix = itemView.findViewById(R.id.fiveTosix);
            sixToseven = itemView.findViewById(R.id.sixeToseven);
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            //TranslateAnimation anim = new TranslateAnimation(0,-1000,0,-1000);
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            //anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
            anim.setDuration(550);//to make duration random number between [0,501)
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }
}
