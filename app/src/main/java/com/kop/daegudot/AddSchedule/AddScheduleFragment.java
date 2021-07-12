package com.kop.daegudot.AddSchedule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.applikeysolutions.cosmocalendar.utils.*;
import com.applikeysolutions.cosmocalendar.selection.OnDaySelectedListener;
import com.applikeysolutions.cosmocalendar.selection.RangeSelectionManager;
//import com.applikeysolutions.cosmocalendar.utils.SelectionType;
import com.applikeysolutions.cosmocalendar.view.CalendarView;
import com.kop.daegudot.KakaoMap.MapMainActivity;
import com.kop.daegudot.Network.RestApiService;
import com.kop.daegudot.Network.RestfulAdapter;
import com.kop.daegudot.Network.Schedule.MainSchedule;
import com.kop.daegudot.Network.Schedule.MainScheduleRegister;
import com.kop.daegudot.R;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 *  Second Fragment' 일정추가
 *
 *  select two dates to make main schedule
 *  and add to database
 */

public class AddScheduleFragment extends Fragment implements View.OnClickListener {
    final static private String TAG = "AddScheduleFragment";
    View view;
    CalendarView mCalendar;
    Button mCalendarBtn;
    String mStartDate, mEndDate;
    String mBtnDay1, mBtnDay2;
    LocalDate localStart, localEnd;
    int flag = 1;
    CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public AddScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_schedule, container, false);

        // change top title
        TextView title = view.findViewById(R.id.title);
        title.setText("일정 추가");

        mCalendar = view.findViewById(R.id.calendar);
        mCalendarBtn = view.findViewById(R.id.calendarBtn);

        mCalendarBtn.setOnClickListener(this);

        mCalendar.setSelectionType(SelectionType.RANGE);

        mCalendar.setSelectionManager(new RangeSelectionManager(new OnDaySelectedListener() {
            @Override
            public void onDaySelected() {
                List<Calendar> days = mCalendar.getSelectedDates();

                Calendar startCal = days.get(0);
                localStart = LocalDateTime.ofInstant(
                        startCal.toInstant(), startCal.getTimeZone().toZoneId()).toLocalDate();
                        
                mBtnDay1 = localStart.format(DateTimeFormatter.ofPattern("M월d일"));
                mStartDate = localStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                Log.i(TAG, "mStartDate: " + mStartDate + "mbtnday1: " + mBtnDay1);
                
                Calendar endCal = days.get(days.size() - 1);
                localEnd = LocalDateTime.ofInstant(
                        endCal.toInstant(), endCal.getTimeZone().toZoneId()).toLocalDate();

                mBtnDay2 = localEnd.format(DateTimeFormatter.ofPattern("M월d일"));
                mEndDate = localEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                String text = null;
                if (mBtnDay1.equals(mBtnDay2)) {
                    text = mBtnDay1 + " - ";
                } else {
                    text = mBtnDay1 + " - " + mBtnDay2;
                }
                mCalendarBtn.setText(text);
                
                if (text.length() > 10)
                    flag = 1;
                else flag = 0;
    
             }
        }));

         return view;
    }

    
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.calendarBtn:
                if (flag == 1) {
                    // TODO: user id 수정 필요
                    // save main schedule to DB
                    // complete: change to Kakao Map Activity
                    // failure: none
//                    UserResponse user = new UserResponse;
//                    user.id = (long)1;
                    MainScheduleRegister mainScheduleRegister = new MainScheduleRegister();
                    mainScheduleRegister.startDate = localStart.toString();
                    mainScheduleRegister.endDate = localEnd.toString();
                    mainScheduleRegister.userId = 1;
                    registerMainSchedule(mainScheduleRegister);
                } else {
                    Toast.makeText(getContext(), "날짜를 선택해주세요", Toast.LENGTH_SHORT).show();
                }
            //    ((MainActivity)getActivity()).changeFragment(1, 0);
        }
    }
    
   private void registerMainSchedule(MainScheduleRegister mainSchedule) {
       RestApiService service = RestfulAdapter.getInstance().getServiceApi(null);
       
       Observable<Long> observable = service.saveMainSchedule(mainSchedule);
    
       final long[] mainId = new long[1];
       
       mCompositeDisposable.add(observable.subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribeWith(new DisposableObserver<Long>() {
                   @Override
                   public void onNext(Long response) {
                       Log.d("RX", "Next" + " Response id:: " + response);
                       mainId[0] = response;
                   }
                
                   @Override
                   public void onError(Throwable e) {
                       Log.d("RX", e.getMessage());
                       Toast.makeText(getContext(), "다시 시도해주세요", Toast.LENGTH_SHORT).show();
                   }
                
                   @Override
                   public void onComplete() {
                       Log.d("RX", "complete");
    
                       Intent intent = new Intent(getContext(), MapMainActivity.class);
                       intent.putExtra("startDay", mStartDate);
                       intent.putExtra("endDay", mEndDate);
                       intent.putExtra("mainId", mainId);
                       startActivity(intent);
                       flag = 0;
                    
                   }
               })
       );
   }
}