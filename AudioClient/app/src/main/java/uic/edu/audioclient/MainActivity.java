package uic.edu.audioclient;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import uic.edu.clipserver.player;

public class MainActivity extends AppCompatActivity {
    Button StartService, StopService, Play, Stop;
    RadioGroup rg;
    int counterPlay=0;
    int legnth=-1;
    player play;

int bind=0;
    private ServiceConnection Scon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            play = player.Stub.asInterface(service);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            play=null;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(Scon);
        bind=0;
        legnth=-1;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StartService= findViewById(R.id.StartS);
        StopService= findViewById(R.id.StopS);
        Play= findViewById(R.id.Play);
        Stop= findViewById(R.id.Stop);
        rg= findViewById(R.id.rg);

        StartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Play.setEnabled(true);
                StopService.setEnabled(true);
                StartService.setEnabled(false);
                Intent i = new Intent();
                i.setComponent(new ComponentName("uic.edu.clipserver", "uic.edu.clipserver.MainActivity"));
                startForegroundService(i);
                bindService(i, Scon, BIND_AUTO_CREATE);
                bind=1;
            }
        });

        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int radioButtonId= rg.getCheckedRadioButtonId();
                if (radioButtonId==-1){
                    Toast.makeText(getApplicationContext(),"Please select an Option",Toast.LENGTH_SHORT).show();
                    return;
                }

                View radioButton = rg.findViewById(radioButtonId);
                int idx = rg.indexOfChild(radioButton)+1;
                //Toast.makeText(getApplicationContext(),String.valueOf(idx),Toast.LENGTH_SHORT).show();

                Stop.setEnabled(true);

                if(bind==0){
                    Intent i = new Intent();
                    i.setComponent(new ComponentName("uic.edu.clipserver", "uic.edu.clipserver.MainActivity"));
                    bindService(i, Scon, BIND_AUTO_CREATE);
                    bind=1;
                }

                if (counterPlay==1){
                    Play.setText("PLAY");
                    counterPlay=0;
                    try {
                        legnth= play.pause();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                else if (counterPlay==0){
                    Play.setText("PAUSE");
                    counterPlay=1;
                    if(legnth==-1){
                    try {
                        play.play(idx);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }}
                    else {
                        try {
                            play.resume(legnth);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        legnth=-1;
                    }
                }
            }
        });

        Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stop.setEnabled(false);
                Play.setText("PLAY");
                counterPlay= 0;
                try {
                    play.stop();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if(bind!=0)
                unbindService(Scon);
                bind=0;
                legnth=-1;
            }
        });

        StopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Play.setEnabled(false);
                StopService.setEnabled(false);
                StartService.setEnabled(true);
                Stop.setEnabled(false);
                counterPlay= 0;
                Play.setText("PLAY");
                Intent i = new Intent();
                i.setComponent(new ComponentName("uic.edu.clipserver", "uic.edu.clipserver.MainActivity"));
                stopService(i);
                if(bind!=0)
                unbindService(Scon);
                bind=0;
                legnth=-1;
            }
        });
    }
}
