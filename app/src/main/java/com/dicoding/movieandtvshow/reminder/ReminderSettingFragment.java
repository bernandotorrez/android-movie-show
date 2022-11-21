package com.dicoding.movieandtvshow.reminder;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.dicoding.movieandtvshow.R;
import com.dicoding.movieandtvshow.alarm.AlarmReceiver;
import com.dicoding.movieandtvshow.db.ReminderHelper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReminderSettingFragment extends Fragment {

    private ReminderHelper reminderHelper;
    private Switch sw_release, sw_daily;
    private AlarmReceiver alarmReceiver;


    public ReminderSettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminder_setting, container, false);


    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reminderHelper = ReminderHelper.getInstance(getActivity());
        reminderHelper.open();

        final ReminderSettingItems reminder = new ReminderSettingItems();

        alarmReceiver = new AlarmReceiver();

        sw_release = view.findViewById(R.id.switch_release_reminder);

        String cek_release = ReminderHelper.getDataReminder(1);

        checkedRelease(cek_release);

        final AlarmReceiver alarmReceiver = new AlarmReceiver();

        sw_release.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    reminderHelper.getAllReminder();

                    reminder.setId(1);
                    reminder.setIsReminder("yes");

                    long result = ReminderHelper.updateReminder(reminder);
                    updateReleaseON(result);
                } else {
                    reminderHelper.getAllReminder();

                    reminder.setId(1);
                    reminder.setIsReminder("no");

                    long result = ReminderHelper.updateReminder(reminder);

                    updateReleaseOFF(result);
                }
            }
        });

        sw_daily = view.findViewById(R.id.switch_daily_reminder);

        String cek_daily = ReminderHelper.getDataReminder(2);

        checkedDaily(cek_daily);

        sw_daily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    reminderHelper.getAllReminder();

                    reminder.setId(2);
                    reminder.setIsReminder("yes");

                    long result = ReminderHelper.updateReminder(reminder);

                    updateDailyON(result);
                } else {
                    reminderHelper.getAllReminder();

                    reminder.setId(2);
                    reminder.setIsReminder("no");

                    long result = ReminderHelper.updateReminder(reminder);

                    updateDailyOFF(result);
                }
            }
        });
    }

    private void checkedRelease(String check) {
        if(check.equals("yes")) {
            sw_release.setChecked(true);
        } else {
            sw_release.setChecked(false);
        }
    }

    private void checkedDaily(String check) {
        if(check.equals("yes")) {
            sw_daily.setChecked(true);
        } else {
            sw_daily.setChecked(false);
        }
    }

    private void updateReleaseON(long result) {
        if (result > 0) {
            setReleaseReminder();
            Toast.makeText(getActivity(), R.string.release_reminder_on, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getActivity(), R.string.release_reminder_fail, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateReleaseOFF(long result) {
        if (result > 0) {
            alarmReceiver.cancelAlarmRelease(getContext(), AlarmReceiver.TYPE_RELEASE);
            Toast.makeText(getActivity(), R.string.release_reminder_off, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getActivity(), R.string.release_reminder_fail, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDailyON(long result) {
        if (result > 0) {
            setDailyReminder();
            Toast.makeText(getActivity(), R.string.daily_reminder_on, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getActivity(), R.string.daily_reminder_fail, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDailyOFF(long result) {
        if (result > 0) {
            alarmReceiver.cancelAlarmDaily(getContext(), AlarmReceiver.TYPE_DAILY);
            Toast.makeText(getActivity(), R.string.daily_reminder_off, Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getActivity(), R.string.release_reminder_fail, Toast.LENGTH_SHORT).show();
        }
    }

    private void setDailyReminder() {
        String timeDaily = ReminderHelper.getTimeReminder(2);
        String text_daily_notification = String.format(getResources().getString(R.string.text_daily_notif));

        String cek_release = ReminderHelper.getDataReminder(2);

        alarmReceiver.setRepeatingAlarmDaily(getContext(), AlarmReceiver.TYPE_DAILY,
                    timeDaily, text_daily_notification);


    }

    private void setReleaseReminder() {
        String timeRelease = ReminderHelper.getTimeReminder(1);
        String cek_daily = ReminderHelper.getDataReminder(1);

         alarmReceiver.setRepeatingAlarmRelease(getContext(), AlarmReceiver.TYPE_RELEASE,
                 timeRelease, alarmReceiver.EXTRA_MESSAGE);



    }
}
