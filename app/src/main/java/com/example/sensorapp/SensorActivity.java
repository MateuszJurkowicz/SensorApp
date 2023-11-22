    package com.example.sensorapp;

    import androidx.annotation.NonNull;
    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AlertDialog;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.app.Dialog;
    import android.content.Context;
    import android.hardware.Sensor;
    import android.hardware.SensorManager;
    import android.os.Bundle;
    import android.util.AttributeSet;
    import android.view.LayoutInflater;
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
        private SensorManager sensorManager;
        private List<Sensor> sensorList;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.sensor_activity);

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
        @Nullable
        @Override
        public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
            return super.onCreateView(name, context, attrs);
        }


        private class SensorHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
            public ImageView sensorIconImageView;
            public TextView sensorNameTextView;
            public Sensor sensor;

            public SensorHolder(LayoutInflater inflater, ViewGroup parent) {
                super(inflater.inflate(R.layout.sensor_list_item, parent, false));
                itemView.setOnLongClickListener(this);

                sensorIconImageView = itemView.findViewById(R.id.sensor_item);
                sensorNameTextView = itemView.findViewById(R.id.text_item);

            }
            public void bind(Sensor s)
            {
                sensor = s;
                sensorIconImageView.setImageResource(R.drawable.sensor_image);
                sensorNameTextView.setText(s.getName());

            }
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SensorActivity.this);

                builder.setMessage("Producent: " + sensor.getVendor() + "\nMaksymalna wartość: " + sensor.getMaximumRange())
                        .setTitle("Informacja o czujniku");
                AlertDialog dialog = builder.create();
                dialog.show();
                return true;
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