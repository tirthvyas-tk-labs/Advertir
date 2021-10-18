package com.project.adverstir.workmanager.workers;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.adverstir.R;
import com.instacart.library.truetime.TrueTime;
import com.instacart.library.truetime.TrueTimeRx;

import java.util.Calendar;
import java.util.Date;

import com.project.adverstir.ble.BleDbRecordRepository;
import com.project.adverstir.gps.GpsDbRecordRepository;
import com.project.adverstir.seed_uuid.SeedUUIDDbRecordRepository;
import com.project.adverstir.ui.symptoms.SymptomsDbRecordRepository;
import com.project.adverstir.utils.Constants;
import com.project.adverstir.utils.TimeUtils;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

public class LogPurgerWorker extends Worker {

    private Context context;
    private WorkerParameters workerParameters;
    private Data.Builder resultData;
    public static final String STATUS = "status";

    public LogPurgerWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
        this.workerParameters = workerParams;
        resultData = new Data.Builder();
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e("uuid", "PURGE LOGS");
        try {
            Log.e("truetime", "truetime init");
            RxJavaPlugins.setErrorHandler(e -> {
                Log.e("truetime", e.getMessage());
            });
            TrueTimeRx.build()
                    .initializeRx("time.apple.com")
                    .subscribeOn(Schedulers.io())
                    .subscribe(date -> {
                        Log.e("truetime", "TrueTime was initialized and we have a time: " + TrueTime.now());
                    }, throwable -> {
                        throwable.printStackTrace();
                    });
            SharedPreferences prefs = context.getSharedPreferences(Constants.SHARED_PREFENCE_NAME, Context.MODE_PRIVATE);
            int daysOfLogsToKeep = prefs.getInt(context.getString(R.string.infection_window_in_days_pkeys), Constants.DefaultDaysOfLogsToKeep);

            Date date = new Date(TimeUtils.getTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, -daysOfLogsToKeep);
            long thresh = calendar.getTime().getTime();
            Log.e("ble", "THRESH " + thresh);

            BleDbRecordRepository bleRepo = new BleDbRecordRepository(context);
            GpsDbRecordRepository gpsRepo = new GpsDbRecordRepository(context);
            SymptomsDbRecordRepository symptomsRepo = new SymptomsDbRecordRepository(context);
            SeedUUIDDbRecordRepository seedUUIDRepo = new SeedUUIDDbRecordRepository(context);

            bleRepo.deleteEarlierThan(thresh);
            gpsRepo.deleteEarlierThan(thresh);
            symptomsRepo.deleteEarlierThan(thresh);
            seedUUIDRepo.deleteEarlierThan(thresh);
        }
            catch(Exception e) {
                Log.e("truetime",e.getMessage());
                resultData.putString(STATUS, "Failed with exception" + e.getMessage());
                return Result.failure(resultData.build());
            }
            Log.e("truetime","truetime build ");
        return Result.success(resultData.putString(STATUS, "success").build());
    }
}
