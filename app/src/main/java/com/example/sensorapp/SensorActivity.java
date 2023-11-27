/*TYPE_STEP_COUNTER(18) śledzi ogólną liczbę kroków od momentu uruchomienia,
podczas gdy TYPE_STEP_DETECTOR(19) reaguje na pojedyncze kroki. .
 */

package com.example.sensorapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class SensorActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public SensorAdapter adapter;
    public boolean subtitleVisible;
    private SensorManager sensorManager;
    private List<Sensor> sensorList;
    private Sensor sensorLight;
    private int colorChosenSensor;
    private int colorDefaultSensor;
    private int colorMagnetometerSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_activity);

        colorChosenSensor = ContextCompat.getColor(this, R.color.show_chosen_sensor);
        colorDefaultSensor = ContextCompat.getColor(this, R.color.show_default_sensor);
        colorMagnetometerSensor = ContextCompat.getColor(this, R.color.show_magnetometer_sensor);

        recyclerView = findViewById(R.id.sensor_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);

        if (adapter == null) {
            adapter = new SensorAdapter(sensorList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sensorapp_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.show_subtitle) {
            subtitleVisible = !subtitleVisible;
            this.invalidateOptionsMenu();
            showSubtitle();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public void showSubtitle() {
        String subtitle = getString(R.string.sensors_count, sensorList.size());
        if (!subtitleVisible) {
            subtitle = null;
        }
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(subtitle);
        }
    }

    private class SensorHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        public ImageView sensorIconImageView;
        public TextView sensorNameTextView;
        public Sensor sensor;

        public SensorHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.sensor_list_item, parent, false));
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);

            sensorIconImageView = itemView.findViewById(R.id.sensor_item);
            sensorNameTextView = itemView.findViewById(R.id.text_item);
        }

        public void bind(Sensor s) {
            sensor = s;
            sensorIconImageView.setImageResource(R.drawable.sensor_image);
            sensorNameTextView.setText(s.getName());
            if (sensor.getType() == Sensor.TYPE_LIGHT || sensor.getType() == Sensor.TYPE_STEP_DETECTOR || sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
                sensorIconImageView.setBackgroundColor(colorChosenSensor);
                sensorNameTextView.setBackgroundColor(colorChosenSensor);
            } else if (sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                sensorIconImageView.setBackgroundColor(colorMagnetometerSensor);
                sensorNameTextView.setBackgroundColor(colorMagnetometerSensor);
            } else {
                sensorIconImageView.setBackgroundColor(colorDefaultSensor);
                sensorNameTextView.setBackgroundColor(colorDefaultSensor);
            }

        }

        @Override
        public boolean onLongClick(View view) {
            AlertDialog.Builder builder = new AlertDialog.Builder(SensorActivity.this);

            builder.setMessage("Producent: " + sensor.getVendor() + "\nTyp czujnika: " + sensor.getType() + "\nMaksymalna wartość: " + sensor.getMaximumRange()).setTitle("Informacja o czujniku");
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        }

        @Override
        public void onClick(View v) {
            if (sensor.getType() == sensor.TYPE_MAGNETIC_FIELD) {
                Intent intent = new Intent(SensorActivity.this, LocationActivity.class);
                intent.putExtra("sensorType", sensor.getType());
                startActivity(intent);
            } else {
                Intent intent = new Intent(SensorActivity.this, SensorDetailsActivity.class);
                intent.putExtra("sensorType", sensor.getType());
                startActivity(intent);
            }
        }
    }

    private class SensorAdapter extends RecyclerView.Adapter<SensorHolder> {

        public SensorAdapter(List<Sensor> sList) {
            sensorList = sList;
        }

        @NonNull
        @Override
        public SensorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(SensorActivity.this);
            return new SensorHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull SensorHolder holder, int position) {
            Sensor sensor = sensorList.get(position);
            holder.bind(sensor);
        }

        @Override
        public int getItemCount() {
            return sensorList.size();
        }
    }
}