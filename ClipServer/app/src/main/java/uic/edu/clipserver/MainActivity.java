package uic.edu.clipserver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class MainActivity extends Service {
    MediaPlayer myPlayer;
    Player play= new Player();

    private static String CHANNEL_ID = "Music player style" ;

    private class Player extends player.Stub{
        @Override
        public void play(int index){
            //int index= intent.getIntExtra("index",0);
            if(index==0){
                Toast.makeText(getApplicationContext(), "Index error", Toast.LENGTH_SHORT).show();
            }
            else {
                if (myPlayer!=null)
                    myPlayer.reset();
                switch (index){
                    case 1: myPlayer = MediaPlayer.create(getApplicationContext(),R.raw.a);
                        break;
                    case 2: myPlayer = MediaPlayer.create(getApplicationContext(),R.raw.b);
                        break;
                    case 3: myPlayer = MediaPlayer.create(getApplicationContext(),R.raw.c);
                        break;
                    case 4: myPlayer = MediaPlayer.create(getApplicationContext(),R.raw.d);
                        break;
                    case 5: myPlayer = MediaPlayer.create(getApplicationContext(),R.raw.e);
                        break;
                    case 6: myPlayer = MediaPlayer.create(getApplicationContext(),R.raw.f);
                        break;
                }
                myPlayer.setLooping(true);
            }

        myPlayer.start();
        }
        @Override
        public void stop(){
            myPlayer.stop();
            myPlayer.reset();
        }

        @Override
        public int pause(){
            myPlayer.pause();
            return myPlayer.getCurrentPosition();
        }

        @Override
        public void resume(int legnth){
            myPlayer.seekTo(legnth);
            myPlayer.start();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        return play;
    }

    @Override
    public void onCreate() {
        //myPlayer = MediaPlayer.create(this,R.raw.a);
        //myPlayer.setLooping(false); // Set looping
    }
    int Startid;
    @Override
    public int onStartCommand(Intent intent,int flags, int startid) {
        Toast.makeText(this, "Service Started and Playing Music", Toast.LENGTH_SHORT).show();
        //myPlayer.start();
        Startid=startid;
        this.createNotificationChannel();

        final Intent notificationIntent = new Intent(getApplicationContext(),
                MainActivity.class);

        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0) ;

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(android.R.drawable.ic_media_play)
                        .setOngoing(true).setContentTitle("Music Playing")
                        .setContentText("Click to Access Music Player")
                        .setTicker("Music is playing!")
                        .setFullScreenIntent(pendingIntent, false)
                        .build();
        startForeground(1, notification);
        return START_STICKY;
    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Music player notification";
            String description = "The channel for music player notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Stopped and Music Stopped", Toast.LENGTH_LONG).show();
        myPlayer.stop();
        stopSelf(Startid);
    }
}
