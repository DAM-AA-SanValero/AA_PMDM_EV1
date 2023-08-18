package com.svalero.cybershopapp;

import static com.svalero.cybershopapp.R.string.client_registered;
import static com.svalero.cybershopapp.R.string.error_registering;
import static com.svalero.cybershopapp.R.string.required_data;
import static com.svalero.cybershopapp.database.Constants.DATABASE_NAME;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.mapbox.android.gestures.MoveGestureDetector;
import com.mapbox.geojson.Point;
import com.mapbox.maps.CameraOptions;
import com.mapbox.maps.MapView;
import com.mapbox.maps.plugin.annotation.AnnotationConfig;
import com.mapbox.maps.plugin.annotation.AnnotationPlugin;
import com.mapbox.maps.plugin.annotation.AnnotationPluginImplKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManagerKt;
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions;
import com.mapbox.maps.plugin.gestures.GesturesPlugin;
import com.mapbox.maps.plugin.gestures.GesturesUtils;
import com.mapbox.maps.plugin.gestures.OnMoveListener;
import com.svalero.cybershopapp.database.AppDatabase;
import com.svalero.cybershopapp.domain.Client;

import java.util.List;

public class RegisterClientActivity extends AppCompatActivity {

    private Client client;
    private EditText etName;
    private EditText etSurname;
    private EditText etNumber;
    private TextInputEditText tilDate;
    private CheckBox cbVIP;
    private MapView clientMap;
    private ScrollView scrollView;
    private Point point;
    private PointAnnotationManager pointAnnotationManager;
    
    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);

        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etNumber = findViewById(R.id.etNumber);
        tilDate = findViewById(R.id.tilDate);
        cbVIP = findViewById(R.id.cbVip);
        clientMap = findViewById(R.id.clientMap);
        scrollView = findViewById(R.id.scrollView);

        GesturesPlugin gesturesPlugin = GesturesUtils.getGestures(clientMap);
        gesturesPlugin.addOnMapClickListener(point -> {
            removeAllMarkers();
            this.point = point;
            addMarker(point);
            return true ;
        });
        gesturesPlugin.setPinchToZoomEnabled(true);
        gesturesPlugin.addOnMoveListener(new OnMoveListener() {
            @Override
            public void onMoveBegin(MoveGestureDetector detector) {
                scrollView.requestDisallowInterceptTouchEvent(true);
            }
            @Override
            public boolean onMove(@NonNull MoveGestureDetector detector) {
                return false;
            }
            @Override
            public void onMoveEnd(MoveGestureDetector detector) {
                scrollView.requestDisallowInterceptTouchEvent(false);
            }
        });
        initializePointManager();

    }

    public void addButton(View view) {

        String name = etName.getText().toString();
        String surname = etSurname.getText().toString();
        String number = etNumber.getText().toString();
        String date = tilDate.getText().toString();
        boolean vip = cbVIP.isActivated();

        if (name.isEmpty() || surname.isEmpty() || number.isEmpty()){
            Snackbar.make(this.getCurrentFocus(), required_data, BaseTransientBottomBar.LENGTH_LONG).show();
            return;
        }

        client = new Client(name, surname, Integer.parseInt(number), date, false, point.latitude(), point.longitude());

        final AppDatabase database = Room.databaseBuilder(this, AppDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries().build();
        try {
            database.clientDao().insert(client);

            Toast.makeText(this, client_registered, Toast.LENGTH_LONG).show();
            etName.setText("");
            etSurname.setText("");
            etNumber.setText("");
            tilDate.setText("");
            onBackPressed();

        } catch (SQLiteConstraintException sce){
            Snackbar.make(etName, R.string.error_registering, BaseTransientBottomBar.LENGTH_LONG).show();
        }

    }

    public void cancelButton(View view){
        onBackPressed();
    }

    private void addMarker(Point point) {
        PointAnnotationOptions pointAnnotationOptions = new PointAnnotationOptions()
                .withPoint(point)
                .withIconImage(BitmapFactory.decodeResource(getResources(), R.mipmap.purple_marker_foreground));
        pointAnnotationManager.create(pointAnnotationOptions);
    }

    private void initializePointManager() {
        AnnotationPlugin annotationPlugin = AnnotationPluginImplKt.getAnnotations(clientMap);
        AnnotationConfig annotationConfig = new AnnotationConfig();
        pointAnnotationManager = PointAnnotationManagerKt.createPointAnnotationManager(annotationPlugin, annotationConfig);
    }

    private void addClientsToMap(List<Client> clients) {
        for(Client client : clients){
            Point point =  Point.fromLngLat(client.getLongitude(), client.getLatitude());
            addMarker(point);
        }

        Client lastClient = clients.get(clients.size() - 1);
        setCameraPosition(Point.fromLngLat(lastClient.getLongitude(),lastClient.getLatitude()));
    }
    private void setCameraPosition(Point point) {
        CameraOptions cameraPosition = new CameraOptions.Builder()
                .center(point)
                .pitch(45.0)
                .zoom(15.5)
                .bearing(-17.6)
                .build();
        clientMap.getMapboxMap().setCamera(cameraPosition);
    }



    private void removeAllMarkers(){
        pointAnnotationManager.deleteAll();
    }

}

